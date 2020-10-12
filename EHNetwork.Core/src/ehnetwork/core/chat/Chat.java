package ehnetwork.core.chat;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.achievement.AchievementManager;
import ehnetwork.core.chat.command.BroadcastCommand;
import ehnetwork.core.chat.command.ChatSlowCommand;
import ehnetwork.core.chat.command.SilenceCommand;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilText;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Chat extends MiniPlugin
{
	private CoreClientManager _clientManager;
	private PreferencesManager _preferences;
	private AchievementManager _achievements;
	
	private String[] _hackusations = {"i_am_a_hackusation", "hax0rtest"};
	private String _filterUrl = "http://mplex.endlcdn.site/accounts/filter.php";
	private String _appId = "34018d65-466d-4a91-8e92-29ca49f022c4";
	private String _apiKey = "oUywMpwZcIzZO5AWnfDx";
	private String _serverName;

	private int _chatSlow = 0;
	private long _silenced = 0;
	private boolean _threeSecondDelay = true;

	private HashMap<UUID, MessageData> _playerLastMessage = new HashMap<UUID, MessageData>();

	public Chat(JavaPlugin plugin, CoreClientManager clientManager, PreferencesManager preferences, AchievementManager achievements, String serverName)
	{
		super("Chat", plugin);

		_clientManager = clientManager;
		_serverName = serverName;
		_preferences = preferences;
		_achievements = achievements;
		
		try
		{
			trustCert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void addCommands()
	{
		addCommand(new SilenceCommand(this));
		addCommand(new BroadcastCommand(this));
		addCommand(new ChatSlowCommand(this));
	}

	public void setChatSlow(int seconds, boolean inform)
	{
		if (seconds < 0)
			seconds = 0;

		_chatSlow = seconds;

		if (inform)
		{
			if (seconds == 0)
				UtilServer.broadcast(F.main("Chat", "Chat Slow is now disabled"));
			else
				UtilServer.broadcast(F.main("Chat", "Chat slow is now enabled with a cooldown of " + F.time(seconds + " seconds")));
		}
	}

	public void Silence(long duration, boolean inform)
	{
		// Set Silenced
		if (duration > 0)
			_silenced = System.currentTimeMillis() + duration;
		else
			_silenced = duration;

		if (!inform)
			return;

		// Announce
		if (duration == -1)
			UtilServer.broadcast(F.main("Chat", "Chat has been silenced for " + F.time("Permanent") + "."));
		else if (duration == 0)
			UtilServer.broadcast(F.main("Chat", "Chat is no longer silenced."));
		else
			UtilServer.broadcast(F.main("Chat", "Chat has been silenced for " + F.time(UtilTime.MakeStr(duration, 1))
					+ "."));
	}

	@EventHandler
	public void preventMe(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().toLowerCase().startsWith("/me ")
				|| event.getMessage().toLowerCase().startsWith("/bukkit"))
		{
			event.getPlayer().sendMessage(F.main(getName(), "No, you!"));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void lagTest(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().equals("lag") || event.getMessage().equals("ping"))
		{
			event.getPlayer().sendMessage(F.main(getName(), "PONG!"));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void SilenceUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		SilenceEnd();
	}

	public void SilenceEnd()
	{
		if (_silenced <= 0)
			return;

		if (System.currentTimeMillis() > _silenced)
			Silence(0, true);
	}

	public boolean SilenceCheck(Player player)
	{
		SilenceEnd();

		if (_silenced == 0)
			return false;

		if (_clientManager.Get(player).GetRank().Has(player, Rank.MODERATOR, false))
			return false;

		if (_silenced == -1)
			UtilPlayer.message(player, F.main(getName(), "Chat is silenced permanently."));
		else
			UtilPlayer.message(
					player,
					F.main(getName(),
							"Chat is silenced for "
									+ F.time(UtilTime.MakeStr(_silenced - System.currentTimeMillis(), 1)) + "."));

		return true;
	}

	@EventHandler
	public void removeChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.isAsynchronous())
		{
			for (Iterator<Player> playerIterator = event.getRecipients().iterator(); playerIterator.hasNext();)
			{
				if (!_preferences.Get(playerIterator.next()).ShowChat)
					playerIterator.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event)
	{
		if (_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.ADMIN)) return;

		// Prevent silenced players from using signs
		if (SilenceCheck(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}

		for (int i = 0; i < event.getLines().length; i++)
		{
			String line = event.getLine(i);
			if (line != null && line.length() > 0)
			{
				String filteredLine = getFilteredMessage(event.getPlayer(), line);
				if (filteredLine != null)
					event.setLine(i, filteredLine);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void filterChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.isAsynchronous())
		{
			String filteredMessage = getFilteredMessage(event.getPlayer(), event.getMessage());
			
			for (Player onlinePlayer : event.getRecipients())
			{
				onlinePlayer.sendMessage(String.format(event.getFormat(), event.getPlayer().getDisplayName(), filteredMessage));
			}
			
			event.setCancelled(true);
		}
	}
	
	public String getFilteredMessage(Player player, String originalMessage)
	{
		final String playerName = player.getUniqueId().toString();
		originalMessage = originalMessage.replaceAll("[^\\x00-\\x7F]", "").trim();
		final String filterType = "moderate";
		final String displayName = player.getPlayerListName();

		JSONObject message = buildJsonChatObject(filterType, displayName, playerName, originalMessage, _serverName, 1);
		String response = getResponseFromCleanSpeak(message, filterType);
		
		if (response == null)
		{
			System.out.println("[ERROR] Unable to filter chat message...thanks a lot CleanSpeak.");
			return originalMessage;
		}
		
		/* TESTING OUTPUT - POSSIBLY USEFUL
		System.out.println(message);			
		System.out.println(response);
		System.out.println(JSONValue.parse(response));
		//NullPointerException occasionally happening, JSONValue.parse(String) returns null randomly, why?
		
		for (Object o : ((JSONObject)JSONValue.parse(response)).values())
		{
			System.out.println(o.toString());
		}
		*/
		
		String filteredMsg = "";
					
		filteredMsg = ((JSONObject) JSONValue.parse(response)).get("content").toString();
		if (filteredMsg.contains("parts"))
		{
			filteredMsg = ((JSONObject) JSONValue.parse(filteredMsg)).get("parts").toString();
			filteredMsg = filteredMsg.replace('[', ' ').replace(']', ' ').trim();
			filteredMsg = ((JSONObject) JSONValue.parse(filteredMsg)).get("replacement").toString();
			
			return filteredMsg;
		}
		else
		{
			return originalMessage;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void HandleChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;

		Player sender = event.getPlayer();

		if (SilenceCheck(sender))
		{
			event.setCancelled(true);
			return;
		}
		else if (_threeSecondDelay &&
				_clientManager.Get(sender).GetRank() == Rank.ALL &&
				_achievements.getMineplexLevelNumber(sender, Rank.ALL) < 25 &&
				!Recharge.Instance.use(sender, "All Chat Message", 500, false, false))
		{
			UtilPlayer.message(sender, C.cYellow + "You can only chat once every 0.5 seconds to prevent spam.");
			UtilPlayer.message(sender, C.cYellow + "Request a Rank at " + C.cGreen + "https://discord.gg/FttmSEQ" + C.cYellow + " to remove this limit!");
			event.setCancelled(true);
		}
		else if (!_clientManager.Get(sender).GetRank().Has(Rank.MODERATOR) &&
				!Recharge.Instance.use(sender, "Chat Message", 400, false, false))
		{
			UtilPlayer.message(sender, F.main("Chat", "You are sending messages too fast."));
			event.setCancelled(true);
		}
		else if (!_clientManager.Get(sender).GetRank().Has(Rank.HELPER) &&
				msgContainsHack(event.getMessage()))
		{
			UtilPlayer.message(sender, F.main("Chat", 
					"Accusing players of cheating in-game is against the rules."
					+ "If you think someone is cheating, please gather evidence and report it at "
					+ F.link("https://discord.gg/FttmSEQ")));
			event.setCancelled(true);
		}
		else if (_playerLastMessage.containsKey(sender.getUniqueId()))
		{
			MessageData lastMessage = _playerLastMessage.get(sender.getUniqueId());
			long chatSlowTime = 1000L * _chatSlow;
			long timeDiff = System.currentTimeMillis() - lastMessage.getTimeSent();
			if (timeDiff < chatSlowTime && !_clientManager.Get(sender).GetRank().Has(Rank.HELPER))
			{
				UtilPlayer.message(sender, F.main("Chat", "Chat slow enabled. Please wait " + F.time(UtilTime.convertString(chatSlowTime - timeDiff, 1, UtilTime.TimeUnit.FIT))));
				event.setCancelled(true);
			}
			else if (!_clientManager.Get(sender).GetRank().Has(Rank.MODERATOR) &&
					UtilText.isStringSimilar(event.getMessage(), lastMessage.getMessage(), 0.8f))
			{
				UtilPlayer.message(sender, F.main("Chat", "This message is too similar to your previous message."));
				event.setCancelled(true);
			}
		}

		if (!event.isCancelled())
			_playerLastMessage.put(sender.getUniqueId(), new MessageData(event.getMessage()));
	}

	private boolean msgContainsHack(String msg) 
	{
		msg = " " + msg.toLowerCase().replaceAll("[^a-z ]", "") + " ";
		for (String s : _hackusations) {
			if (msg.contains(" " + s + " ")) {
				return true;
			}
		}
		return false;
	}

	public String hasher(JSONArray hasharray, String message)
	{
		StringBuilder newmsg = new StringBuilder(message);

		for (int i = 0; i < hasharray.size(); i++)
		{
			Long charindex = ((Long) hasharray.get(i));
			int charidx = charindex.intValue();
			newmsg.setCharAt(charidx, '*');
		}

		return newmsg.toString();
	}

	public JSONArray parseHashes(String response)
	{
		JSONObject checkhash = (JSONObject) JSONValue.parse(response);
		JSONArray hasharray;
		hasharray = (JSONArray) checkhash.get("hashes");

		return hasharray;
	}

	@SuppressWarnings("unchecked")
	private JSONObject buildJsonChatObject(String filtertype, String name, String player, String msg, String server, int rule)
	{
		JSONObject message = new JSONObject();
		switch (filtertype)
		{
		case "chat":
			/*
			message.put("player_display_name", name);
			message.put("player", player);
			message.put("text", msg);
			message.put("server", "gamma");
			message.put("room", server);
			message.put("language", "en");
			message.put("rule", rule);
			*/
			message.put("content", msg);
			break;
		case "moderate":
			JSONObject content = new JSONObject();
			content.put("content", msg);
			content.put("type", "text");
			
			JSONArray parts = new JSONArray();
			parts.add(content);
			
			JSONObject mainContent = new JSONObject();
			mainContent.put("applicationId", _appId);
			mainContent.put("createInstant", System.currentTimeMillis());
			mainContent.put("parts", parts);
			mainContent.put("senderDisplayName", name);
			mainContent.put("senderId", player);
			
			message.put("content", mainContent);
			break;
		case "username":
			message.put("player_id", name);
			message.put("username", name);
			message.put("language", "en");
			message.put("rule", rule);
			break;
		}
		return message;
	}

	private String getResponseFromCleanSpeak(JSONObject message, String filtertype)
	{
		/*
		String authString = _authName + ":" + _apiKey;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		String url = _filterUrl + filtertype;
		*/
		String url = _filterUrl;

		StringBuffer response = null;

		HttpsURLConnection connection = null;
		DataOutputStream outputStream = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStream = null;

		try
		{
			URL obj = new URL(url);

			connection = (HttpsURLConnection) obj.openConnection();

			// add request header con.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			//connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.addRequestProperty("Authentication", _apiKey);

			String urlParameters = message.toString();

			// Send post request
			connection.setDoOutput(true);
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(urlParameters);
			outputStream.flush();
			outputStream.close();

			inputStream = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
			bufferedReader = new BufferedReader(inputStream);
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = bufferedReader.readLine()) != null)
			{
				response.append(inputLine);
			}

			bufferedReader.close();
		}
		catch (Exception exception)
		{
			System.out.println("Error getting response from CleanSpeak : " + exception.getMessage());
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}

			if (outputStream != null)
			{
				try
				{
					outputStream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				try
				{
					outputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		String pmresponse = null;
		
		if (response != null)
			pmresponse = response.toString();
				
		return pmresponse;
	}

	public static void trustCert() throws Exception
	{
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
		{
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType)
			{
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType)
			{
			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier()
		{
			public boolean verify(String hostname, SSLSession session)
			{
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	public long Silenced()
	{
		return _silenced;
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		_playerLastMessage.remove(event.getPlayer().getUniqueId());
	}

	public void setThreeSecondDelay(boolean b) 
	{
		_threeSecondDelay = b;
	}
}
