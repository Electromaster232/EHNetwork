package ehnetwork.core.punish;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import ehnetwork.core.common.util.Callback;
import ehnetwork.core.punish.Tokens.PunishClientToken;
import ehnetwork.core.punish.Tokens.PunishToken;
import ehnetwork.core.punish.Tokens.RemovePunishToken;
import ehnetwork.core.server.remotecall.AsyncJsonWebCall;
import ehnetwork.core.server.remotecall.JsonWebCall;

public class PunishRepository
{
	private String _webAddress;
	
	public PunishRepository(String webServerAddress)
	{
		_webAddress = webServerAddress;
	}
	
	public void Punish(Callback<String> callback, String target, String category, PunishmentSentence punishment, String reason, double duration, String admin, int severity)
	{
		PunishToken token = new PunishToken();
		token.Target = target;
		token.Category = category;
		token.Sentence = punishment.toString();
		token.Reason = reason;
		token.Duration = duration;
		token.Admin = admin;
		token.Severity = severity;

		new AsyncJsonWebCall(_webAddress + "PlayerAccount/Punish").Execute(String.class, callback, token);
	}

	public void RemovePunishment(Callback<String> callback, int id, String target, String reason, String admin)
	{
		RemovePunishToken token = new RemovePunishToken();
		token.PunishmentId = id;
		token.Target = target;
		token.Reason = reason;
		token.Admin = admin;

		new AsyncJsonWebCall(_webAddress + "PlayerAccount/RemovePunishment").Execute(String.class, callback, token);
	}
	
	public void LoadPunishClient(String target,	Callback<PunishClientToken> callback)
	{
		new AsyncJsonWebCall(_webAddress + "PlayerAccount/GetPunishClient").Execute(PunishClientToken.class, callback, target);
	}
	
	public void MatchPlayerName(final Callback<List<String>> callback, final String userName)
	{
		Thread asyncThread = new Thread(new Runnable()
		{
			public void run()
			{
				List<String> tokenList = new JsonWebCall(_webAddress + "PlayerAccount/GetMatches").Execute(new TypeToken<List<String>>(){}.getType(), userName);
				callback.run(tokenList);
			}
		});

		asyncThread.start();
	}

	public void RemoveBan(String name, String reason)
	{
		RemovePunishToken token = new RemovePunishToken();
		token.Target = name;
		token.Reason = reason;
		
		new JsonWebCall(_webAddress + "PlayerAccount/RemoveBan").Execute(String.class, token);
	}
}
