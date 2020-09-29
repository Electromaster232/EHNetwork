package ehnetwork.game.arcade.game.games.amongus;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.EntityArrow;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.disguise.disguises.DisguisePlayer;
import ehnetwork.core.teleport.Teleport;
import ehnetwork.core.teleport.Teleporter;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeFormat;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.SoloGame;
import ehnetwork.game.arcade.game.games.amongus.kits.KitDetective;
import ehnetwork.game.arcade.game.games.amongus.kits.KitMurderer;
import ehnetwork.game.arcade.game.games.amongus.kits.KitPlayer;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.minecraft.game.core.condition.Condition;

public class AmongUs extends SoloGame
{
	// TODO: Write vote timer + timeout skip
	// TODO: Write voter ejection
	private boolean MurdererShot;
	private boolean _freezePlayers = false;
	private Player imposter1;
	private Player imposter2;
	private Kit KitMurderer;
	private ArrayList<Player> ventedPlayers = new ArrayList<>();
	private Kit KitDetective;
	private int playersVoted = 0;
	private NautHashMap<Location, Location> trapdoorLocations = new NautHashMap<>();
	private NautHashMap<Entity, String> deadBodyEntities = new NautHashMap<>();
	private NautHashMap<Player, Player> voteFor = new NautHashMap<>();
	private int skipVotes = 0;
	public AmongUs(ArcadeManager manager)
	{
		super(manager, GameType.AmongUs,
				new Kit[]
						{
								new KitPlayer(manager),
								new KitDetective(manager),
								new KitMurderer(manager)
						},

				new String[]
						{
								"Avoid being killed by the murderer!",
								"If you are the detective, find and kill the murderer!"
						});
		//this.DisableKillCommand = false;
		this.BlockPlace = false;
		this.BlockBreak = false;
		this.PrepareFreeze = true;
		this.DamageFall = false;
		this.DeathOut = true;
		this.HungerSet = 20;
		this.DeathMessages = false;
		this.FirstKill = false;
		this.StrictAntiHack = false;
		this.DamageTeamOther = true;
		this.Damage = true;
		this.DamagePvP = true;
		this.DamageTeamSelf = true;
		this.VersionRequire1_8 = true;
		// whoopsie this did not work lmao this.WorldWaterDamage = 5;

	}

	@EventHandler
	public void SetPlayers(GameStateChangeEvent event){
		if(event.GetState() != GameState.Prepare){
			return;
		}


		for(Kit kit : getArcadeManager().GetGame().GetKits()){
			if(kit.GetFormattedName().contains("Detective")){
				KitDetective = kit;
			}
			else if(kit.GetFormattedName().contains("Murderer")){
				KitMurderer = kit;
			}
		}
	}

	@Override
	public void ParseData()
	{


			for (ArrayList<Location> locs : WorldData.GetAllDataLocs().values())
			{
				try
				{
					Location loc1 = new Location(WorldData.World, locs.get(0).getBlockX(), locs.get(0).getBlockY(), locs.get(0).getBlockZ());
					Location loc2 = new Location(WorldData.World, locs.get(1).getBlockX(), locs.get(1).getBlockY(), locs.get(1).getBlockZ());
					trapdoorLocations.put(loc1, loc2);
					trapdoorLocations.put(loc2, loc1);
				}catch (IndexOutOfBoundsException e){
					continue;
				}
			}
			System.out.println(trapdoorLocations.keySet());


	}

	@EventHandler
	public void GameStartActions(GameStateChangeEvent event){
		if (!IsLive())
			return;

		// Calculate Imposters -> Set Kits
		Random randomizer = new Random();
		imposter1 = getArcadeManager().GetGame().GetPlayers(true).get(randomizer.nextInt(getArcadeManager().GetGame().GetPlayers(true).size()));
		imposter2 = getArcadeManager().GetGame().GetPlayers(true).get(randomizer.nextInt(getArcadeManager().GetGame().GetPlayers(true).size()));
		if (imposter1 == imposter2){
			imposter1 = getArcadeManager().GetGame().GetPlayers(true).get(randomizer.nextInt(getArcadeManager().GetGame().GetPlayers(true).size()));
		}
		MurdererShot = false;
		getArcadeManager().GetGame().SetKit(imposter1, KitMurderer, false);
		UtilTextMiddle.display(C.cRed + "YOU ARE AN IMPOSTER", "Kill everyone!", imposter1);
		getArcadeManager().GetGame().SetKit(imposter2, KitMurderer, false);
		UtilTextMiddle.display(C.cBlue + "YOU ARE AN IMPOSTER", "Find and kill the murderer!", imposter2);
		for(Player player : getArcadeManager().GetGame().GetPlayers(true)){
			if (player == imposter1 || player == imposter2){
				continue;
			}
			else{
				UtilTextMiddle.display(C.cGreen + "YOU ARE INNOCENT", "Survive as long as possible!", player);
			}
		}

		// Silence Chat
		getArcadeManager().GetChat().Silence(-1, true);
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;


		//Wipe Last
		Scoreboard.Reset();

		Scoreboard.WriteBlank();

		Scoreboard.Write(C.cYellow + "Players Alive:");


		Integer num = 0;
		for(Player player : getArcadeManager().GetGame().GetPlayers(true)){
			num = num + 1;
		}
		Scoreboard.Write(C.cGreen + num.toString());
		Scoreboard.Draw();
	}

	@EventHandler
	public void reportBody(PlayerInteractEntityEvent event){
		//Check if what was r-clicked was actually a dead body
		//We do this by checking if it is in the list of dead body entities
		//Which is a list of the arrow entities created when someone is killed
		if(Manager.isSpectator(event.getPlayer()))
			return;
		if(!deadBodyEntities.keySet().contains(event.getRightClicked()))
			return;
		if(_freezePlayers)
			return;

		String playerName = deadBodyEntities.get(event.getRightClicked());

		UtilTextMiddle.display("Dead Body Reported!", "Reported by " + event.getPlayer().getName());
		for (Player r : getArcadeManager().GetGame().GetPlayers(false))
			r.teleport(Manager.GetGame().GetTeam(r).GetSpawn());
		_freezePlayers = true;

		UtilServer.broadcast(F.main("Game", C.cGreen + C.Bold + "The body of " + C.cRed + playerName + C.cGreen + C.Bold + " was found dead!"));
		UtilServer.broadcast(F.main("Game", C.cGreen + C.Bold + "DISCUSS!"));
		getArcadeManager().GetChat().SilenceEnd();
		UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				OpenVoting();
			}}, 120);
	}

	private void OpenVoting(){
		voteFor.clear();
		skipVotes = 0;
		UtilServer.broadcast(F.main("Game", "Voting has begun!"));
		for(Player r : getArcadeManager().GetGame().GetPlayers(true))
		{
			r.sendMessage(F.main("Voting", "Select a player to vote out, or select skip to skip your vote!"));
			// Send a voting choice for each player
			for (Player x : getArcadeManager().GetGame().GetPlayers(true))
			{
				// Base message object
				JsonObject textObject = new JsonObject();
				textObject.addProperty("text", C.cYellow + "[" + C.cWhite + x.getName() + C.cYellow + "]");

				// Object for click event
				JsonObject clickObject = new JsonObject();
				clickObject.addProperty("action", "run_command");
				clickObject.addProperty("value", "/gamevote " + x.getName());

				// Object for hover event

				// Add the event objects to the base message
				textObject.add("clickEvent", clickObject);

				PacketPlayOutChat chatPacket = new PacketPlayOutChat(ChatSerializer.a(textObject.toString()), true);
				((CraftPlayer) r).getHandle().playerConnection.sendPacket(chatPacket);
			}
			// Send a "skip" voting choice
			// Base message object
			JsonObject textObject = new JsonObject();
			textObject.addProperty("text", C.cYellow + "[" + C.cWhite + C.Bold + "SKIP" + C.cYellow + "]");

			// Object for click event
			JsonObject clickObject = new JsonObject();
			clickObject.addProperty("action", "run_command");
			clickObject.addProperty("value", "/gamevote skip");

			// Object for hover event

			// Add the event objects to the base message
			textObject.add("clickEvent", clickObject);

			PacketPlayOutChat chatPacket = new PacketPlayOutChat(ChatSerializer.a(textObject.toString()), true);
			((CraftPlayer) r).getHandle().playerConnection.sendPacket(chatPacket);

		}
		// Now we start a timer to close voting. Everyone who didnt vote will be counted as skipped.
		UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				if(playersVoted < Manager.GetGame().GetPlayers(true).size()){
					CloseVotes();
				}
			}}, 300);
	}

	@EventHandler
	public void MoveCancel(PlayerMoveEvent event)
	{
		if (!_freezePlayers)
			return;

		if (UtilMath.offset2d(event.getFrom(), event.getTo()) == 0)
			return;

		//if (!IsAlive(event.getPlayer()))
		//	return;

		event.setTo(event.getFrom());
	}

	@EventHandler
	public void ProcessVotes(PlayerCommandPreprocessEvent event){
		if(!event.getMessage().startsWith("/gamevote"))
			return;
		event.setCancelled(true);
		if(voteFor.keySet().contains(event.getPlayer())){
			event.getPlayer().sendMessage(F.main("Voting", "You already voted this round!"));
			return;
		}
		if(event.getMessage().equals("/gamevote skip")){
			event.getPlayer().sendMessage(F.main("Voting", "Your vote to skip was submitted successfully"));
			skipVotes++;
			playersVoted++;
			if(playersVoted == Manager.GetGame().GetPlayers(true).size()){
				CloseVotes();
			}
			return;
		}
		voteFor.put(event.getPlayer(), Manager.GetClients().Get(event.getMessage().replace("/gamevote ", "")).GetPlayer());
		event.getPlayer().sendMessage(F.main("Voting", "Your vote was submitted successfully"));
		playersVoted++;

		// After every vote we want to check if all alive players have voted
		if(playersVoted == Manager.GetGame().GetPlayers(true).size()){
			CloseVotes();
		}
	}


	private void CloseVotes(){
		UtilServer.broadcast(F.main("Voting", "Voting has closed!"));
		Manager.GetChat().Silence(-1, true);
		UtilServer.broadcast(F.main("Voting", "The player with the most votes was..."));
		Player highestPlayer = null;
		int highestVotes = 0;
		for (Player r : Manager.GetGame().GetPlayers(true)){
			int playervotes = 0;
			for (Player x : voteFor.values()){
				if(x == r){
					playervotes++;
				}
			}
			if(playervotes > highestVotes)
				highestPlayer = r;
		}
		if (skipVotes > highestVotes){
			UtilServer.broadcast("SKIP! No player will be ejected.");
		}
		else
		{
			UtilServer.broadcast(C.cYellow + C.Bold + highestPlayer.getName());
			highestPlayer.damage(21);
		}
		_freezePlayers = false;

	}

	@EventHandler
	public void ImposterInVent(PlayerToggleSneakEvent event){
		if(getArcadeManager().isSpectator(event.getPlayer()))
			return;
		if(!(event.getPlayer() == imposter1 || event.getPlayer() == imposter2) )
			return;
		if(ventedPlayers.contains(event.getPlayer()))
			return;
		// Check if they're standing on "set 1" of vents
		// Have to assign a dummy location to ventToGo and check if its the same later, since you can't check
		// initialization without a try
		Location ventToGo = new Location(event.getPlayer().getWorld(), 0,0,0);
		for (Location r : trapdoorLocations.keySet()){
			Location loc1 = new Location(event.getPlayer().getWorld(), event.getPlayer().getLocation().getBlockX(), 0, event.getPlayer().getLocation().getBlockZ());
			if(r.getBlockX() == loc1.getBlockX() && r.getBlockZ() == loc1.getBlockZ())
				ventToGo = trapdoorLocations.get(r);
		}

		// If there isn't a valid match, they must not be standing on a vent.
		if(ventToGo.equals(new Location(event.getPlayer().getWorld(), 0, 0, 0))){
			//System.out.println("Not standing on vent!");
			return;
		}

		// Make the player invisible, we dont want people seeing them or other imposters in the vents
		// But if they're already invisible, that means they're inside a vent, so we want to remove that condition
		if(!getArcadeManager().GetCondition().HasCondition(event.getPlayer(), Condition.ConditionType.CLOAK, "Vanish")){
			//event.getPlayer().sendMessage(F.main("Condition", "You are now invisible"));
			getArcadeManager().GetCondition().Factory().Cloak("Vanish", event.getPlayer(), event.getPlayer(), 7777, false, false);
		}
		else{
			getArcadeManager().GetCondition().EndCondition(event.getPlayer(), Condition.ConditionType.CLOAK, "Vanish");
			//event.getPlayer().sendMessage(F.main("Condition", "You are no longer invisible"));
		}

		event.getPlayer().teleport(ventToGo, new PlayerTeleportEvent(event.getPlayer(), event.getPlayer().getLocation(), ventToGo).getCause());
		event.getPlayer().sendMessage(F.main("Game", "You entered a vent"));
		ventedPlayers.add(event.getPlayer());
		final Player p1 = event.getPlayer();
		UtilServer.getServer().getScheduler().runTaskLater(Manager.getPlugin(), new Runnable() { public void run() { ventedPlayers.remove(p1.getPlayer()); }}, 100);
	}

	@EventHandler
	public void cancelVentDamage(EntityDamageByBlockEvent event){
		if(ventedPlayers.contains(event.getEntity()))
			event.setCancelled(true);
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		if (MurdererShot)
		{
			//Set Places
			for(Player player : getArcadeManager().GetGame().GetPlayers(false)){
				AddGems(player, 20, "Winning", false, false);
			}



			//Participation
			for (Player player : GetPlayers(false))
				if (player.isOnline())
				{
					AddGems(player, 10, "Participation", false, false);
				}
			SetState(GameState.End);
			AnnounceEnd(ChatColor.GREEN + "Players");
			return;
		}

		if (GetPlayers(true).size() <= 1 && MurdererShot == false)
		{
			//Set Places
			for(Player player : getArcadeManager().GetGame().GetPlayers(true)){
				AddGems(player, 20, "Winning", false, false);
			}



			//Participation
			for (Player player : GetPlayers(false))
				if (player.isOnline())
				{
					AddGems(player, 10, "Participation", false, false);
				}
			SetState(GameState.End);
			AnnounceEnd(ChatColor.RED + "Murderer");
		}
	}

	public void AnnounceEnd(String WinningTeam){

		String winnerText = WinningTeam;
		ChatColor subColor = ChatColor.WHITE;

		for (Player player : UtilServer.getPlayers())
		{
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);

			UtilPlayer.message(player, "");
			UtilPlayer.message(player, ArcadeFormat.Line);

			UtilPlayer.message(player, "§aGame - §f§l" + this.GetName());
			UtilPlayer.message(player, "");
			UtilPlayer.message(player, "");


			UtilPlayer.message(player, winnerText + " won the game!");
			UtilPlayer.message(player, C.cRed + C.Bold + "The murderer was " + imposter1.getDisplayName());
			UtilPlayer.message(player, C.cBlue + C.Bold + "The other murderer was  " + imposter2.getDisplayName());


			UtilPlayer.message(player, "");
			UtilPlayer.message(player, "§aMap - §f§l" + WorldData.MapName + C.cGray + " created by " + "§f§l" + WorldData.MapAuthor);

			UtilPlayer.message(player, ArcadeFormat.Line);
		}

		UtilTextMiddle.display(winnerText, subColor + "won the game", 20, 120, 20);

		if (AnnounceSilence)
			Manager.GetChat().Silence(5000, false);
	}

	private BlockFace getFace(Location loc)
	{
		Block block = loc.getBlock();

		while (block.getY() > 0 && !UtilBlock.fullSolid(block.getRelative(BlockFace.DOWN))
				&& !UtilBlock.solid(block.getRelative(BlockFace.DOWN)))
		{
			block = block.getRelative(BlockFace.DOWN);
		}

		BlockFace proper = BlockFace.values()[Math.round(loc.getYaw() / 90F) & 0x3].getOppositeFace();

		// A complicated way to get the face the dead body should be towards.
		for (HashSet<Byte> validBlocks : new HashSet[]
				{
						UtilBlock.blockAirFoliageSet, UtilBlock.blockPassSet
				})
		{

			if (validBlocks.contains((byte) block.getRelative(proper).getTypeId()))
			{
				return proper;
			}

			for (BlockFace face : new BlockFace[]
					{
							BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.WEST
					})
			{
				if (validBlocks.contains((byte) block.getRelative(face).getTypeId()))
				{
					return face;
				}
			}
		}

		return proper;
	}

	@EventHandler
	public void Damage(EntityDamageByEntityEvent event)
	{
		if (!IsLive())
			return;

		Player damager = (Player) event.getDamager();
		Player entity = (Player) event.getEntity();

		/*
		if (damager != detective || damager != murderer){
			System.out.println("W R O N G");
			System.out.println(damager.getName());
			event.setCancelled(true);
			return;
		}*/

		if(damager == imposter1 || damager == imposter2){
			if(entity == imposter1 || entity == imposter2){
				event.setCancelled(true);
				return;
			}

			getArcadeManager().GetCondition().AddCondition(getArcadeManager().GetCondition().Factory().Blind("blind", entity, damager, 3, 1000, false, true, true));
			GameProfile newProfile = new GameProfile(UUID.randomUUID(), entity.getName());

			newProfile.getProperties().putAll(((CraftPlayer) entity).getHandle().getProfile().getProperties());

			DisguisePlayer disguise = new DisguisePlayer(null, newProfile);

			disguise.setSleeping(getFace(entity.getLocation()));

			getArcadeManager().GetDisguise().addFutureDisguise(disguise);

			Entity entity1 = entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ARROW);
			deadBodyEntities.put(entity1, entity.getName());
			try
			{
				EntityArrow entityArrow = ((CraftArrow) entity1).getHandle();

				Field at = EntityArrow.class.getDeclaredField("at");
				at.setAccessible(true);
				at.set(entityArrow, Integer.MIN_VALUE); // Despawn time
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			entity.damage(9001);
		}
		else{
			event.setCancelled(true);
			return;
		}

		EndCheck();


	}



}
