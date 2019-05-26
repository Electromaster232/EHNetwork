package nautilus.game.arcade.game.games.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import mineplex.core.MiniPlugin;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilText;
import mineplex.core.creature.event.CreatureKillEntitiesEvent;
import mineplex.core.gadget.types.Gadget;
import mineplex.core.gadget.types.GadgetType;
import mineplex.core.give.Give;
import mineplex.core.mount.Mount;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.ArcadeManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EventModule extends MiniPlugin
{

	public ArcadeManager Manager;
	
	private NautHashMap<PotionEffectType, Long> _potionEffectsDuration = new NautHashMap<>();
	private NautHashMap<PotionEffectType, Integer> _potionEffectsMult = new NautHashMap<>();
	
	private boolean _mobGriefing = true;
	
	public EventModule(ArcadeManager manager, JavaPlugin plugin)
	{
		super("EventModule", plugin);
		Manager = manager;
	}
	
	@EventHandler
	public void mobGriefing(EntityChangeBlockEvent event)
	{
		if(!_mobGriefing)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void setHostDebug(PlayerCommandPreprocessEvent event)
	{
		if (!event.getPlayer().isOp())
			return;
		
		if (!event.getMessage().toLowerCase().startsWith("/sethost "))
			return;
		
		Manager.GetServerConfig().HostName = event.getMessage().split(" ")[1];

		event.getPlayer().sendMessage("Set host to: " + event.getMessage().split(" ")[1]);

		Manager.GetGameHostManager().setHost(Bukkit.getPlayerExact(Manager.GetServerConfig().HostName));
		if (Manager.GetGameHostManager().getHost() != null)
			Manager.GetGameHostManager().setHostRank(Manager.GetClients().Get(Manager.GetGameHostManager().getHost()).GetRank());
		
		Manager.GetGameHostManager().setDefaultConfig();
		
		event.setCancelled(true);
	}
	
	private void commandHelp(Player player) 
	{
		UtilPlayer.message(player, F.main("Event", "Displaying Commands;"));

		UtilPlayer.message(player, F.value("/e settings", "View Settings Help"));

		UtilPlayer.message(player, F.value("/e tp <Player>", "Teleport to Target"));
		UtilPlayer.message(player, F.value("/e tp here <Player>", "Teleport Target to Self"));
		UtilPlayer.message(player, F.value("/e tp here all", "Teleport Everyone to Self"));

		UtilPlayer.message(player, F.value("/e gadget", "Toggle Gadgets"));
		UtilPlayer.message(player, F.value("/e gadget list", "Lists Gadgets (Shows Whitelist)"));
		UtilPlayer.message(player, F.value("/e gadget <Gadget>", "Toggles Whitelist for Gadget"));
		UtilPlayer.message(player, F.value("/e gadget clear", "Clears Gadget Whitelist"));

		UtilPlayer.message(player, F.value("/e silence [Time]", "Silence Chat"));

		UtilPlayer.message(player, F.value("/e admin [Player]", "Toggle Event Admin"));

		UtilPlayer.message(player, F.value("/e gm [Player]", "Toggle Creative Mode"));

		UtilPlayer.message(player, F.value("/e radius [Radius]", "Set Forcefield Radius"));

		UtilPlayer.message(player, F.value("/e give <item> <amount>", "Give Item"));
		UtilPlayer.message(player, F.value("/e give <player> <item> <amount> [e:#,e:#...]", "Give Item"));
		
		UtilPlayer.message(player, F.value("/e doublejump", "Toggles Double Jump"));

		UtilPlayer.message(player, F.value("/e scoreboard <Line #> [Text]", "Sets Scoreboard Text"));

		UtilPlayer.message(player, F.value("/e mob <type> [#Amount] n[Name] s[Size] [angry] [baby]", ""));
		UtilPlayer.message(player, F.value("/e mob kill <type>", "Kill Mobs"));

		UtilPlayer.message(player, F.value("/e kit set", "Sets Player Kit to your Hotbar"));
		UtilPlayer.message(player, F.value("/e kit apply", "Gives Kit to Players"));
		UtilPlayer.message(player, F.value("/e kit clear", "Gives Kit to Players"));

		UtilPlayer.message(player, F.value("/e effect <player> <type> <mult> <seconds>", ""));
		UtilPlayer.message(player, F.value("/e effect <player> clear", ""));
	}

	private void commandHelpSettings(Player player) 
	{
		UtilPlayer.message(player, F.main("Event", "Displaying Settings Commands;"));
		UtilPlayer.message(player, F.value("/e damage all", "Toggles All Damage"));
		UtilPlayer.message(player, F.value("/e damage pvp", "Toggles PvP Damage"));
		UtilPlayer.message(player, F.value("/e damage pve", "Toggles PvE Damage"));
		UtilPlayer.message(player, F.value("/e damage pve", "Toggles EvP Damage"));
		UtilPlayer.message(player, F.value("/e damage fall", "Toggles Fall Damage"));
		UtilPlayer.message(player, F.value("/e health <-1 to 20>", "Locks Players Health"));
		UtilPlayer.message(player, F.value("/e hunger <-1 to 20>", "Locks Players Hunger"));
		UtilPlayer.message(player, F.value("/e item drop", "Toggles Item Drop"));
		UtilPlayer.message(player, F.value("/e item pickup", "Toggles Item Pickup"));
		UtilPlayer.message(player, F.value("/e blockplacecreative", "Toggles Block Placing in Creative (On/Off)"));
		UtilPlayer.message(player, F.value("/e blockbreakcreative", "Toggles Block Breaking in Creative (On/Off)"));
		UtilPlayer.message(player, F.value("/e blockplace", "Toggles Block Placing (On/Off)"));
		UtilPlayer.message(player, F.value("/e blockplace", "Toggles Block Placing (On/Off)"));
		UtilPlayer.message(player, F.value("/e blockplace whitelist <add/remove/list/clear> <id>", ""));
		UtilPlayer.message(player, F.value("/e blockplace blacklist <add/remove/list/clear> <id>", ""));
		UtilPlayer.message(player, F.value("/e blockbreak", "Toggles Block Breaking (On/Off)"));
		UtilPlayer.message(player, F.value("/e blockbreak whitelist <add/remove/list/clear> <id>", ""));
		UtilPlayer.message(player, F.value("/e blockbreak blacklist <add/remove/list/clear> <id>", ""));
		UtilPlayer.message(player, F.value("/e time <-1 to 24000>", "Sets World Time"));
		//UtilPlayer.message(player, F.value("/e joiningame", "toggles Join In Process for games"));
		//UtilPlayer.message(player, F.value("/e deathout", "toggles Deathout in games"));
		//UtilPlayer.message(player, F.value("/e quitout", "toggles Quitout in games"));
		UtilPlayer.message(player, F.value("/e mobgriefing", "toggles mobgriefing in games"));
	}

	//Command Handler
	@EventHandler(priority = EventPriority.LOWEST)
	private void commandHandler(PlayerCommandPreprocessEvent event)
	{	
		if (!Manager.GetGame().InProgress())
			return;

		if (!event.getMessage().toLowerCase().startsWith("/e "))
			return;
		
		if(!Manager.GetGameHostManager().isEventServer())
			return;

		event.setCancelled(true);

		if (!Manager.GetGameHostManager().isAdmin(event.getPlayer(), false))
			return;

		//Trim off /e and split to args
		String[] args = event.getMessage().substring(3, event.getMessage().length()).split(" ");

		if (args.length == 0 || args[0].equalsIgnoreCase("help"))
		{
			commandHelp(event.getPlayer());
		}
		else if (args[0].equalsIgnoreCase("settings"))
		{
			if (args.length >= 2 && args[1].equalsIgnoreCase("list"))
				listSettings(event.getPlayer());
			else
				commandHelpSettings(event.getPlayer());
		}

		//XXX Commands
		else if (args[0].equalsIgnoreCase("tp"))
		{
			commandTeleport(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("gadget"))
		{
			commandGadget(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("silence"))
		{
			commandSilence(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("admin"))
		{
			commandAdmin(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("gm"))
		{
			commandGamemode(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("radius"))
		{
			commandForcefieldRadius(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("doublejump"))
		{
			commandDoubleJump(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("scoreboard"))
		{
			commandScoreboard(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("whitelist"))
		{
			commandWhitelist(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("give"))
		{
			commandGive(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("effect"))
		{
			commandEffect(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("kit"))
		{
			commandKit(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("mob"))
		{
			if (args.length >= 2 && args[1].equalsIgnoreCase("kill"))
				commandMobKill(event.getPlayer(), args);
			else
				commandMob(event.getPlayer(), args);
		}
		

		//XXX Settings
		else if (event.getMessage().toLowerCase().equals("/e damage all"))
		{
			Manager.GetGame().Damage = !Manager.GetGame().Damage;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Damage All", F.tf(Manager.GetGame().Damage))));
		}
		else if (event.getMessage().toLowerCase().equals("/e damage pvp"))
		{
			Manager.GetGame().DamagePvP = !Manager.GetGame().DamagePvP;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Damage PvP", F.tf(Manager.GetGame().DamagePvP))));
		}
		else if (event.getMessage().toLowerCase().equals("/e damage pve"))
		{
			Manager.GetGame().DamagePvE = !Manager.GetGame().DamagePvE;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Damage PvE", F.tf(Manager.GetGame().DamagePvE))));
		}
		else if (event.getMessage().toLowerCase().equals("/e damage evp"))
		{
			Manager.GetGame().DamageEvP = !Manager.GetGame().DamageEvP;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Damage EvP", F.tf(Manager.GetGame().DamageEvP))));
		}
		else if (event.getMessage().toLowerCase().equals("/e damage fall"))
		{
			Manager.GetGame().DamageFall = !Manager.GetGame().DamageFall;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Damage Fall", F.tf(Manager.GetGame().DamageFall))));
		}
		else if (args[0].equalsIgnoreCase("health"))
		{
			commandHealth(event.getPlayer(), args);
		}
		else if (args[0].equalsIgnoreCase("hunger"))
		{
			commandHunger(event.getPlayer(), args);
		}
		else if (event.getMessage().toLowerCase().equals("/e item drop"))
		{
			Manager.GetGame().ItemDrop = !Manager.GetGame().ItemDrop;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Item Drop", F.tf(Manager.GetGame().ItemDrop))));
		}
		else if (event.getMessage().toLowerCase().equals("/e item pickup"))
		{
			Manager.GetGame().ItemPickup = !Manager.GetGame().ItemPickup;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Item Pickup", F.tf(Manager.GetGame().ItemPickup))));
		}
		else if (event.getMessage().toLowerCase().equals("/e blockplace"))
		{
			Manager.GetGame().BlockPlace = !Manager.GetGame().BlockPlace;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Block Place", F.tf(Manager.GetGame().BlockPlace))));
		}
		else if (args.length >= 4 && args[0].equalsIgnoreCase("blockplace") 
				&& (args[1].equalsIgnoreCase("whitelist") || args[1].equalsIgnoreCase("blacklist")))
		{
			commandBlockPlace(event.getPlayer(), args, args[1].equalsIgnoreCase("whitelist"), args[2]);
		}
		else if (event.getMessage().toLowerCase().equals("/e blockbreak"))
		{
			Manager.GetGame().BlockBreak = !Manager.GetGame().BlockBreak;
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Block Break", F.tf(Manager.GetGame().BlockBreak))));
		}
		else if (args.length >= 4 && args[0].equalsIgnoreCase("blockbreak") 
				&& (args[1].equalsIgnoreCase("whitelist") || args[1].equalsIgnoreCase("blacklist")))
		{
			commandBlockBreak(event.getPlayer(), args, args[1].equalsIgnoreCase("whitelist"), args[2]);
		}
		else if (args[0].equalsIgnoreCase("time"))
		{
			commandTime(event.getPlayer(), args);
		}
		/*else if(args[0].equalsIgnoreCase("joiningame"))
		{
			commandSpectators(event.getPlayer(), args);
		}
		else if(args[0].equalsIgnoreCase("deathout"))
		{
			commandDeathout(event.getPlayer(), args);
		}
		else if(args[0].equalsIgnoreCase("quitout"))
		{
			commandQuitOut(event.getPlayer(), args);
		}*/
		else if(args[0].equalsIgnoreCase("blockplacecreative"))
		{
			commandBlockPlaceInCreative(event.getPlayer(), args);
		}
		else if(args[0].equalsIgnoreCase("blockbreakcreative"))
		{
			commandBlockBreakInCreative(event.getPlayer(), args);
		}
		else if(args[0].equalsIgnoreCase("mobgriefing"))
		{
			commandMobGriefing(event.getPlayer(), args);
		}
	}

	private void listSettings(Player player) 
	{
		UtilPlayer.message(player, F.value("Damage All", F.tf(Manager.GetGame().Damage)));
		UtilPlayer.message(player, F.value("Damage PvP", F.tf(Manager.GetGame().DamagePvP)));
		UtilPlayer.message(player, F.value("Damage PvE", F.tf(Manager.GetGame().DamagePvE)));
		UtilPlayer.message(player, F.value("Damage EvP", F.tf(Manager.GetGame().DamageEvP)));
		UtilPlayer.message(player, F.value("Damage Fall", F.tf(Manager.GetGame().DamageFall)));
		UtilPlayer.message(player, F.value("Health Set", Manager.GetGame().HealthSet+""));
		UtilPlayer.message(player, F.value("Hunger Set", Manager.GetGame().HungerSet+""));
		UtilPlayer.message(player, F.value("Item Pickup", F.tf(Manager.GetGame().ItemPickup)));
		UtilPlayer.message(player, F.value("Item Drop", F.tf(Manager.GetGame().ItemDrop)));
		UtilPlayer.message(player, F.value("Block Place Creative", F.tf(Manager.GetGame().BlockPlaceCreative)));
		UtilPlayer.message(player, F.value("Block Break Creative", F.tf(Manager.GetGame().BlockBreakCreative)));
		UtilPlayer.message(player, F.value("Block Place", F.tf(Manager.GetGame().BlockPlace)));
		UtilPlayer.message(player, F.value("Block Place Whitelist", UtilText.listToString(Manager.GetGame().BlockPlaceAllow, true)));
		UtilPlayer.message(player, F.value("Block Place Blacklist", UtilText.listToString(Manager.GetGame().BlockPlaceDeny, true)));
		UtilPlayer.message(player, F.value("Block Break", F.tf(Manager.GetGame().BlockPlace)));
		UtilPlayer.message(player, F.value("Block Break Whitelist", UtilText.listToString(Manager.GetGame().BlockBreakAllow, true)));
		UtilPlayer.message(player, F.value("Block Break Blacklist", UtilText.listToString(Manager.GetGame().BlockBreakDeny, true)));
		UtilPlayer.message(player, F.value("Time Set", Manager.GetGame().WorldTimeSet+""));
		UtilPlayer.message(player, F.value("Mob griefing", F.tf(_mobGriefing)));
	}
	
	private void commandBlockBreakInCreative(Player player, String[] args)
	{
		Manager.GetGame().BlockBreakCreative = !Manager.GetGame().BlockBreakCreative;
		
		UtilPlayer.message(player, F.main("Settings", "BlockBreakCreative: " + F.tf(Manager.GetGame().BlockBreakCreative)));
	}
	
	private void commandBlockPlaceInCreative(Player player, String[] args)
	{
		Manager.GetGame().BlockPlaceCreative = !Manager.GetGame().BlockPlaceCreative;
		
		UtilPlayer.message(player, F.main("Settings", "BlockPlaceCreative: " + F.tf(Manager.GetGame().BlockPlaceCreative)));
	}
	
	private void commandMobGriefing(Player player, String[] args)
	{
		_mobGriefing = !_mobGriefing;
		
		UtilPlayer.message(player, F.main("Settings", "Mob Griefing: " + F.tf(_mobGriefing)));
	}

	private void commandBlockPlace(Player player, String[] args, boolean whitelist, String command)
	{
		try
		{
			int blockId = Integer.parseInt(args[3]);

			if (whitelist)
			{
				if (command.equalsIgnoreCase("add"))
				{
					Manager.GetGame().BlockPlaceAllow.add(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Whitelist", "Added " + blockId)));
				}
				else if (command.equalsIgnoreCase("remove"))
				{
					Manager.GetGame().BlockPlaceAllow.remove(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Whitelist", "Removed " + blockId)));
				}
				else if (command.equalsIgnoreCase("clear"))
				{
					Manager.GetGame().BlockPlaceAllow.clear();
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Whitelist", "Cleared")));
				}
				else if (command.equalsIgnoreCase("list"))
				{
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Whitelist", UtilText.listToString(Manager.GetGame().BlockPlaceAllow, true))));
				}
			}
			else
			{
				if (command.equalsIgnoreCase("add"))
				{
					Manager.GetGame().BlockPlaceDeny.add(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Blacklist", "Added " + blockId)));
				}
				else if (command.equalsIgnoreCase("remove"))
				{
					Manager.GetGame().BlockPlaceDeny.remove(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Blacklist", "Removed " + blockId)));
				}
				else if (command.equalsIgnoreCase("clear"))
				{
					Manager.GetGame().BlockPlaceDeny.clear();
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Blacklist", "Cleared")));
				}
				else if (command.equalsIgnoreCase("list"))
				{
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Place Blacklist", UtilText.listToString(Manager.GetGame().BlockPlaceDeny, true))));
				}
			}

			return;
		}
		catch (Exception e)
		{

		}

		commandHelpSettings(player);
	}

	private void commandBlockBreak(Player player, String[] args, boolean whitelist, String command)
	{
		try
		{
			int blockId = Integer.parseInt(args[3]);

			if (whitelist)
			{
				if (command.equalsIgnoreCase("add"))
				{
					Manager.GetGame().BlockBreakAllow.add(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Whitelist", "Added " + blockId)));
				}
				else if (command.equalsIgnoreCase("remove"))
				{
					Manager.GetGame().BlockBreakAllow.remove(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Whitelist", "Removed " + blockId)));
				}
				else if (command.equalsIgnoreCase("clear"))
				{
					Manager.GetGame().BlockBreakAllow.clear();
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Whitelist", "Cleared")));
				}
				else if (command.equalsIgnoreCase("list"))
				{
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Whitelist", UtilText.listToString(Manager.GetGame().BlockBreakAllow, true))));
				}
			}
			else
			{
				if (command.equalsIgnoreCase("add"))
				{
					Manager.GetGame().BlockBreakDeny.add(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Blacklist", "Added " + blockId)));
				}
				else if (command.equalsIgnoreCase("remove"))
				{
					Manager.GetGame().BlockBreakDeny.remove(blockId);
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Blacklist", "Removed " + blockId)));
				}
				else if (command.equalsIgnoreCase("clear"))
				{
					Manager.GetGame().BlockBreakDeny.clear();
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Blacklist", "Cleared")));
				}
				else if (command.equalsIgnoreCase("list"))
				{
					UtilPlayer.message(player, F.main("Event Settings", F.value("Block Break Blacklist", UtilText.listToString(Manager.GetGame().BlockBreakDeny, true))));
				}
			}

			return;
		}
		catch (Exception e)
		{

		}

		commandHelpSettings(player);
	}

	private void commandHealth(Player player, String[] args) 
	{
		try
		{
			if (args.length >= 2)
			{
				int health = Integer.parseInt(args[1]);

				if (health <= 0)
					health = -1;
				if (health > 20)
					health = 20;

				Manager.GetGame().HealthSet = health;

				if (Manager.GetGame().HealthSet == -1)
					Manager.GetGame().Announce(F.main("Event Settings", F.value("Health Set", "Disabled")));
				else
					Manager.GetGame().Announce(F.main("Event Settings", F.value("Health Set", Manager.GetGame().HealthSet + "")));

				return;
			}
		}
		catch (Exception e)
		{

		}

		commandHelpSettings(player);
	}

	private void commandHunger(Player player, String[] args) 
	{
		try
		{
			if (args.length >= 2)
			{
				int hunger = Integer.parseInt(args[1]);

				if (hunger <= 0)
					hunger = -1;
				if (hunger > 20)
					hunger = 20;

				Manager.GetGame().HungerSet = hunger;

				if (Manager.GetGame().HungerSet == -1)
					Manager.GetGame().Announce(F.main("Event Settings", F.value("Hunger Set", "Disabled")));
				else
					Manager.GetGame().Announce(F.main("Event Settings", F.value("Hunger Set", Manager.GetGame().HungerSet + "")));

				return;
			}
		}
		catch (Exception e)
		{

		}

		commandHelpSettings(player);
	}

	private void commandTime(Player player, String[] args) 
	{
		try
		{
			if (args.length >= 2)
			{
				int time = Integer.parseInt(args[1]);

				if (time <= -1)
					time = -1;
				if (time > 24000)
					time = 24000;

				Manager.GetGame().WorldTimeSet = time;

				if (Manager.GetGame().WorldTimeSet == -1)
					Manager.GetGame().Announce(F.main("Event Settings", F.value("Time Set", "Disabled")));
				else
					Manager.GetGame().Announce(F.main("Event Settings", F.value("Time Set", Manager.GetGame().WorldTimeSet + "")));

				return;
			}
		}
		catch (Exception e)
		{

		}

		commandHelpSettings(player);
	}

	//Teleport Command (To, Here, All)
	private void commandTeleport(Player player, String[] args)
	{
		if (args.length >= 3 && args[1].equalsIgnoreCase("here"))
		{
			if (args[2].equalsIgnoreCase("all"))
			{
				for (Player other : UtilServer.getPlayers())
				{
					UtilPlayer.message(other, F.main("Event TP", player.getName() + " teleported everyone to self."));
					other.teleport(player);
				}

				return;
			}

			Player target = UtilPlayer.searchOnline(player, args[2], true);
			if (target != null)
			{
				target.teleport(player);
				UtilPlayer.message(target, F.main("Event TP", player.getName() + " teleported you to self."));
				UtilPlayer.message(player, F.main("Event TP", "Teleported " + target.getName() + " to you."));
			}

			return;
		}

		if (args.length >= 2)
		{
			Player target = UtilPlayer.searchOnline(player, args[1], true);
			if (target != null)
			{
				player.teleport(target);
				UtilPlayer.message(player, F.main("Event TP", "Teleported to " + target.getName() + "."));
			}

			return;
		}

		commandHelp(player);
	}

	//Gadget Commands (Global & Individual)
	private void commandGadget(Player player, String[] args)
	{
		if(!(Manager.GetGame() instanceof EventGame)) {
			UtilPlayer.message(player, F.main("Inventory", "You can only enable/disable gadgets in the Event game!"));
			return;
		}
			
		if (args.length < 2)
		{
			((EventGame) Manager.GetGame()).setAllowGadget(!((EventGame) Manager.GetGame()).isAllowGadget());

			if (!((EventGame) Manager.GetGame()).isAllowGadget())
			{
				Manager.getCosmeticManager().getMountManager().DisableAll();
				Manager.getCosmeticManager().getGadgetManager().DisableAll();
			}

			Manager.GetGame().Announce(F.main("Inventory", F.value("Allow All Gadgets", F.ed(((EventGame) Manager.GetGame()).isAllowGadget()))));
			return;
		}

		if (args.length >= 2 && args[1].equalsIgnoreCase("clear"))
		{
			((EventGame) Manager.GetGame()).getGadgetWhitelist().clear();
			Manager.GetGame().Announce(F.main("Inventory", F.value("Gadget Whitelist", "Cleared.")));
			return;
		}

		if (args.length >= 2 && args[1].equalsIgnoreCase("list"))
		{
			ChatColor color = ChatColor.AQUA;

			//Gadgets
			for (GadgetType type : GadgetType.values())
			{
				String items = C.Bold + type + " Gadgets> ";

				for (Gadget gadget : Manager.getCosmeticManager().getGadgetManager().getGadgets(type))
				{
					items += color + gadget.GetName().replaceAll(" ", "") + " ";
					color = (color == ChatColor.AQUA ? ChatColor.DARK_AQUA : ChatColor.AQUA);
				}

				UtilPlayer.message(player, items);
			}

			//Mounts
			String mounts = C.Bold + "Mount Gadgets> ";
			for (Mount<?> mount : Manager.getCosmeticManager().getMountManager().getMounts())
			{
				mounts += color + mount.GetName().replaceAll(" ", "") + " ";
				color = (color == ChatColor.AQUA ? ChatColor.DARK_AQUA : ChatColor.AQUA);
			}
			UtilPlayer.message(player, mounts);

			return;
		}

		if (args.length >= 2)
		{
			//Gadgets
			for (GadgetType type : GadgetType.values())
			{
				for (Gadget gadget : Manager.getCosmeticManager().getGadgetManager().getGadgets(type))
				{
					if (gadget.GetName().replaceAll(" ", "").equalsIgnoreCase(args[1]))
					{
						if (((EventGame) Manager.GetGame()).getGadgetWhitelist().remove(gadget))
						{
							Manager.GetGame().Announce(F.main("Inventory", F.value(gadget.GetName() + " Gadget", F.ed(false))));
							gadget.DisableForAll();
						}
						else
						{
							Manager.GetGame().Announce(F.main("Inventory", F.value(gadget.GetName() + " Gadget", F.ed(true))));
							((EventGame) Manager.GetGame()).getGadgetWhitelist().add(gadget);
						}

						return;
					}
				}
			}

			//Mounts
			for (Mount mount : Manager.getCosmeticManager().getMountManager().getMounts())
			{
				if (mount.GetName().replaceAll(" ", "").equalsIgnoreCase(args[1]))
				{
					if (((EventGame) Manager.GetGame()).getGadgetWhitelist().remove(mount))
					{
						Manager.GetGame().Announce(F.main("Inventory", F.value(mount.GetName() + " Gadget", F.ed(false))));
						mount.DisableForAll();
					}
					else
					{
						Manager.GetGame().Announce(F.main("Inventory", F.value(mount.GetName() + " Gadget", F.ed(true))));
						((EventGame) Manager.GetGame()).getGadgetWhitelist().add(mount);
					}

					return;
				}
			}

			UtilPlayer.message(player, F.main("Inventory", args[1] + " is not a valid gadget."));

			return;
		}

		commandHelp(player);
	}

	//Silence
	private void commandSilence(Player player, String[] args)
	{	
		try
		{	
			//Toggle
			if (args.length == 1)
			{
				//Disable
				if (Manager.GetChat().Silenced() != 0)
				{
					Manager.GetChat().Silence(0, true);
				}
				//Enable
				else
				{
					Manager.GetChat().Silence(-1, true);
				}
			}
			//Timer
			else
			{
				long time = (long) (Double.valueOf(args[1]) * 3600000);

				Manager.GetChat().Silence(time, true);
			}
		}
		catch (Exception e)
		{
			UtilPlayer.message(player, F.main("Chat", "Invalid Time Parameter."));
		}
	}

	//Gamemode (Self and Others)
	private void commandAdmin(Player player, String[] args)
	{
		Player target = player;

		if (args.length >= 2)
		{
			Player newTarget = UtilPlayer.searchOnline(player, args[1], true);
			if (newTarget != null)
				target = newTarget;
			else
				return;
		}

		if (!Manager.GetGameHostManager().isAdmin(target, false))
			Manager.GetGameHostManager().giveAdmin(target);
		else
			Manager.GetGameHostManager().removeAdmin(target.getName());

		UtilPlayer.message(player, F.main("Event Admin", target.getName() + " Admin: " + F.tf(Manager.GetGameHostManager().isAdmin(target, false))));
	}

	//Gamemode (Self and Others)
	private void commandGamemode(Player player, String[] args)
	{
		Player target = player;

		if (args.length >= 2)
		{
			Player newTarget = UtilPlayer.searchOnline(player, args[1], true);
			if (newTarget != null)
				target = newTarget;
			else
				return;
		}

		if (target.getGameMode() == GameMode.CREATIVE)
			target.setGameMode(GameMode.SURVIVAL);
		else
			target.setGameMode(GameMode.CREATIVE);

		UtilPlayer.message(player, F.main("Event GM", target.getName() + " Creative: " + F.tf(target.getGameMode() == GameMode.CREATIVE)));
	}

	//Forcefield
	private void commandForcefieldRadius(Player player, String[] args)
	{
		
		if(!(Manager.GetGame() instanceof EventGame)) {
			UtilPlayer.message(player, F.main("Inventory", "You can only enable/disable the forcefield in the Event game!"));
			return;
		}
		
		//Toggle
		if (args.length >= 2)
		{
			try
			{
				int range = Integer.parseInt(args[1]);

				((EventGame) Manager.GetGame()).getForcefieldList().put(player.getName(), range);

				UtilPlayer.message(player, F.main("Forcefield", "Enabled with  " + F.elem(range + "") + " radius."));
			}
			catch (Exception e)
			{
				UtilPlayer.message(player, F.main("Forcefield", "Invalid Input."));
			}
		}
		else
		{
			((EventGame) Manager.GetGame()).getForcefieldList().remove(player.getName());
			UtilPlayer.message(player, F.main("Forcefield", "Disabled."));
		}
	}

	//Give 
	private void commandGive(Player player, String[] args)
	{
		String[] newArgs = new String[args.length-1];

		for (int i=0 ; i<newArgs.length ; i++)
			newArgs[i] = args[i+1];

		Give.Instance.parseInput(player, newArgs);  
	}
	
	//Spec 
	private void commandSpectators(Player player, String[] args)
	{
		Manager.GetGame().JoinInProgress = !Manager.GetGame().JoinInProgress;
		
		UtilPlayer.message(player, F.main("Settings", "Spectator Join: " + F.tf(Manager.GetGame().JoinInProgress)));
	}
	
	//Deathout 
	private void commandDeathout(Player player, String[] args)
	{
		Manager.GetGame().DeathOut = !Manager.GetGame().DeathOut;
	
		UtilPlayer.message(player, F.main("Settings", "Deathout: " + F.tf(Manager.GetGame().DeathOut)));
	}
	
	//QuitOut 
	private void commandQuitOut(Player player, String[] args)
	{
		Manager.GetGame().QuitOut = !Manager.GetGame().QuitOut;
		
		UtilPlayer.message(player, F.main("Settings", "QuitOut: " + F.tf(Manager.GetGame().QuitOut)));
	}
	
	//Double Jump
	private void commandDoubleJump(Player player, String[] args)
	{
		
		if(!(Manager.GetGame() instanceof EventGame)) {
			UtilPlayer.message(player, F.main("Settings", "You can only enable/disable the Doublejump in the Event game!"));
			return;
		}
		
		((EventGame) Manager.GetGame()).setDoubleJump(!((EventGame) Manager.GetGame()).isDoubleJump());

		UtilPlayer.message(player, F.main("Settings", "Double Jump: " + F.tf(((EventGame) Manager.GetGame()).isDoubleJump())));

		if (!((EventGame) Manager.GetGame()).isDoubleJump())
			for (Player other : UtilServer.getPlayers())
				other.setAllowFlight(false);
	}

	//Scoreboard
	private void commandScoreboard(Player player, String[] args)
	{
		
		if(!(Manager.GetGame() instanceof EventGame)) {
			UtilPlayer.message(player, F.main("Scoreboard", "You can only edit the Scoreboard in the Event game!"));
			return;
		}
		
		if (args.length >= 2)
		{
			//Line
			int line = 0;
			try
			{
				line = Integer.parseInt(args[1]);
			}
			catch (Exception e)
			{
				UtilPlayer.message(player, F.main("Scoreboard", "Invalid Line Number."));
				return;
			}

			if (line < 1 || line > 14)
			{
				UtilPlayer.message(player, F.main("Scoreboard", "Invalid Line Number."));
				return;
			}

			//Text
			String lineText = "";

			//Reset String
			if (args.length <= 2)
				for (int i=0 ; i<line ; i++)
					lineText += " ";

			//New String
			for (int i=2 ; i<args.length ; i++)
			{
				lineText += args[i];

				if (i < args.length -1)
					lineText += " ";
			}

			((EventGame) Manager.GetGame()).getSideText()[line] = lineText;	

			UtilPlayer.message(player, F.main("Scoreboard", "Set Line " + F.elem(line+"") + " to " + F.elem(lineText) + "."));

			return;
		}

		//Clear
		if (args.length >= 2 && args[1].equalsIgnoreCase("clear"))
		{
			for (int i=0 ; i<((EventGame) Manager.GetGame()).getSideText().length ; i++)
			{
				String lineText = "";
				for (int j=0 ; j<i ; j++)
					lineText += " ";

				((EventGame) Manager.GetGame()).getSideText()[i] = lineText;
			}

			return;
		}

		for (int i=0 ; i<((EventGame) Manager.GetGame()).getSideText().length ; i++)
		{
			UtilPlayer.message(player, F.value("Line " + i, ((EventGame) Manager.GetGame()).getSideText()[i]));
		}
	}

	//Whitelist
	private void commandWhitelist(Player player, String[] args)
	{
		//On and Off
		if (args.length >= 2)
		{
			if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off"))
			{
				UtilServer.getServer().setWhitelist(args[1].equalsIgnoreCase("on"));

				Manager.GetGame().Announce(F.main("Event Settings", F.value("Whitelist", F.tf(args[1].equalsIgnoreCase("on")))));
				return;
			}
		}

		//Add and Remove
		if (args.length >= 3)
		{
			if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))
			{
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);

				if (args[1].equalsIgnoreCase("add"))
				{
					UtilServer.getServer().getWhitelistedPlayers().add(target);
					UtilPlayer.message(player, F.main("Whitelist", "Added " + args[2] + " to the whitelist."));
				}
				else
				{
					UtilServer.getServer().getWhitelistedPlayers().remove(target);
					UtilPlayer.message(player, F.main("Whitelist", "Removed " + args[2] + " to the whitelist."));
				}

				return;
			}
		}

		commandHelp(player);
	}

	//Mob
	private void commandMob(Player caller, String[] args)
	{
		if (args.length == 1)
		{
			HashMap<EntityType, Integer> entMap = new HashMap<EntityType, Integer>();

			int count = 0;
			for (World world : UtilServer.getServer().getWorlds())
			{
				for (Entity ent : world.getEntities())
				{
					if (!entMap.containsKey(ent.getType()))
						entMap.put(ent.getType(), 0);

					entMap.put(ent.getType(), 1 + entMap.get(ent.getType()));
					count++;
				}
			}

			UtilPlayer.message(caller, F.main("Creature", "Listing Entities:"));
			for (EntityType cur : entMap.keySet())
			{
				UtilPlayer.message(caller, F.desc(UtilEnt.getName(cur), entMap.get(cur)+""));
			}

			UtilPlayer.message(caller, F.desc("Total", count+""));
		}
		else
		{
			EntityType type = UtilEnt.searchEntity(caller, args[1], true);

			if (type == null)
				return;

			UtilPlayer.message(caller, F.main("Creature", "Spawning Creature(s);"));

			//Store Args
			HashSet<String> argSet = new HashSet<String>();
			for (int i = 2 ; i < args.length ; i++)
				if (args[i].length() > 0)
					argSet.add(args[i]);


			//Search Count
			int count = 1;
			HashSet<String> argHandle = new HashSet<String>();
			for (String arg : argSet)
			{
				try
				{
					int newCount = Integer.parseInt(arg);

					if (newCount <= 0)
						continue;

					//Set Count
					count = newCount;
					UtilPlayer.message(caller, F.desc("Amount", count+""));

					//Flag Arg
					argHandle.add(arg);
					break;
				}
				catch (Exception e)
				{
					//None
				}
			}
			for (String arg : argHandle)
				argSet.remove(arg);

			//Spawn
			HashSet<Entity> entSet = new HashSet<Entity>();
			for (int i = 0 ; i < count ; i++)
			{
				Manager.GetGame().CreatureAllowOverride = true;
				entSet.add(Manager.GetCreature().SpawnEntity(caller.getTargetBlock(null, 0).getLocation().add(0.5, 1, 0.5), type));
				Manager.GetGame().CreatureAllowOverride = false;
			}

			//Search Vars
			for (String arg : argSet)
			{
				if (arg.length() == 0)
					continue;

				//Baby
				else if (arg.equalsIgnoreCase("baby") || arg.equalsIgnoreCase("b"))
				{
					for (Entity ent : entSet)
					{
						if (ent instanceof Ageable)
							((Ageable)ent).setBaby();
						else if (ent instanceof Zombie)
							((Zombie)ent).setBaby(true);
					}

					UtilPlayer.message(caller, F.desc("Baby", "True"));
					argHandle.add(arg);
				}

				//Lock
				else if (arg.equalsIgnoreCase("age") || arg.equalsIgnoreCase("lock"))
				{
					for (Entity ent : entSet)
						if (ent instanceof Ageable)
						{
							((Ageable)ent).setAgeLock(true);
							UtilPlayer.message(caller, F.desc("Age", "False"));
						}					

					argHandle.add(arg);
				}

				//Angry
				else if (arg.equalsIgnoreCase("angry") || arg.equalsIgnoreCase("a"))
				{
					for (Entity ent : entSet)
						if (ent instanceof Wolf)
							((Wolf)ent).setAngry(true);

					for (Entity ent : entSet)
						if (ent instanceof Skeleton)
							((Skeleton)ent).setSkeletonType(SkeletonType.WITHER);

					UtilPlayer.message(caller, F.desc("Angry", "True"));
					argHandle.add(arg);
				}

				//Profession
				else if (arg.toLowerCase().charAt(0) == 'p')
				{
					try
					{
						String prof = arg.substring(1, arg.length());

						Profession profession = null;
						for (Profession cur : Profession.values())
							if (cur.name().toLowerCase().contains(prof.toLowerCase()))
								profession = cur;

						UtilPlayer.message(caller, F.desc("Profession", profession.name()));

						for (Entity ent : entSet)
							if (ent instanceof Villager)
								((Villager)ent).setProfession(profession);			
					}
					catch (Exception e)
					{
						UtilPlayer.message(caller, F.desc("Profession", "Invalid [" + arg + "] on " + type.name()));
					}
					argHandle.add(arg);
				}

				//Size
				else if (arg.toLowerCase().charAt(0) == 's')
				{
					try
					{
						String size = arg.substring(1, arg.length());

						UtilPlayer.message(caller, F.desc("Size", Integer.parseInt(size)+""));

						for (Entity ent : entSet)
							if (ent instanceof Slime)
								((Slime)ent).setSize(Integer.parseInt(size));
					}
					catch (Exception e)
					{
						UtilPlayer.message(caller, F.desc("Size", "Invalid [" + arg + "] on " + type.name()));
					}
					argHandle.add(arg);
				}

				else if (arg.toLowerCase().charAt(0) == 'n' && arg.length() > 1)
				{
					try
					{
						String name = "";

						for (char c : arg.substring(1, arg.length()).toCharArray())
						{
							if (c != '_')
								name += c;
							else
								name += " ";
						}

						for (Entity ent : entSet)
						{
							if (ent instanceof CraftLivingEntity)
							{
								CraftLivingEntity cEnt = (CraftLivingEntity)ent;
								cEnt.setCustomName(name); 
								cEnt.setCustomNameVisible(true);
							}
						}
					}
					catch (Exception e)
					{
						UtilPlayer.message(caller, F.desc("Size", "Invalid [" + arg + "] on " + type.name()));
					}
					argHandle.add(arg);	
				}
				else if (arg.toLowerCase().charAt(0) == 'h' && arg.length() > 1)
				{
					try
					{
						String health = "";

						for (char c : arg.substring(1, arg.length()).toCharArray())
						{
							if (c != '_')
								health += c;
							else
								health += " ";
						}
						
						double healthint = Double.parseDouble(health);
						
						for (Entity ent : entSet)
						{
							if (ent instanceof CraftLivingEntity)
							{
								CraftLivingEntity cEnt = (CraftLivingEntity)ent;
								cEnt.setMaxHealth(healthint);
								cEnt.setHealth(healthint);
							}
						}
					}
					catch (Exception e)
					{
						UtilPlayer.message(caller, F.desc("Health", "Invalid [" + arg + "] on " + type.name()));
					}
					argHandle.add(arg);	
				}
				else if (arg.toLowerCase().charAt(0) == 'e' && arg.length() > 1)
				{
					try
					{
						String effect = "";

						for (char c : arg.substring(1, arg.length()).toCharArray())
						{
							if (c != '_')
								effect += c;
							else
								effect += " ";
						}
						
						PotionEffectType potionType = PotionEffectType.getByName(effect);
						
						if (potionType == null)
						{
							UtilPlayer.message(caller, F.main("Effect", "Invalid Effect Type: " + args[2]));
							UtilPlayer.message(caller, F.value("Valid Types", "http://minecraft.gamepedia.com/Status_effect"));
							return;
						}	
						
						for (Entity ent : entSet)
						{
							if (ent instanceof CraftLivingEntity)
							{
								CraftLivingEntity cEnt = (CraftLivingEntity)ent;
								cEnt.addPotionEffect(new PotionEffect(potionType, Integer.MAX_VALUE, 0));
							}
						}
					}
					catch (Exception e)
					{
						UtilPlayer.message(caller, F.desc("PotionEffect", "Invalid [" + arg + "] on " + type.name()));
					}
					argHandle.add(arg);	
				}
				else if (arg.toLowerCase().charAt(0) == 'i' && arg.length() > 1)
				{
					try
					{
						String item = "";

						for (char c : arg.substring(1, arg.length()).toCharArray())
						{
							item += c;
						}
						
						Material mat = Material.getMaterial(item);
						
						for (Entity ent : entSet)
						{
							if (ent instanceof CraftLivingEntity)
							{
								CraftLivingEntity cEnt = (CraftLivingEntity)ent;
								cEnt.getEquipment().setItemInHand(new ItemStack(mat));
							}
						}
					}
					catch (Exception e)
					{
						UtilPlayer.message(caller, F.desc("Item", "Invalid [" + arg + "] on " + type.name()));
					}
					argHandle.add(arg);	
				}
				else if (arg.toLowerCase().charAt(0) == 'a' && arg.length() > 1)
				{
					try
					{
						String armor = "";

						for (char c : arg.substring(1, arg.length()).toCharArray())
						{
							if (c != '_')
								armor += c;
							else
								armor += " ";
						}
						
						ItemStack head = null;
						ItemStack chest = null;
						ItemStack leggings = null;
						ItemStack boots = null;
						
						
						if(armor.equalsIgnoreCase("leather"))
						{
							head = new ItemStack(Material.LEATHER_HELMET);
							chest = new ItemStack(Material.LEATHER_CHESTPLATE);
							leggings = new ItemStack(Material.LEATHER_LEGGINGS);
							boots = new ItemStack(Material.LEATHER_BOOTS);
						}
						if(armor.equalsIgnoreCase("gold"))
						{
							head = new ItemStack(Material.GOLD_HELMET);
							chest = new ItemStack(Material.GOLD_CHESTPLATE);
							leggings = new ItemStack(Material.GOLD_LEGGINGS);
							boots = new ItemStack(Material.GOLD_BOOTS);
						}
						if(armor.equalsIgnoreCase("chain"))
						{
							head = new ItemStack(Material.CHAINMAIL_HELMET);
							chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
							leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
							boots = new ItemStack(Material.CHAINMAIL_BOOTS);
						}
						if(armor.equalsIgnoreCase("iron"))
						{
							head = new ItemStack(Material.IRON_HELMET);
							chest = new ItemStack(Material.IRON_CHESTPLATE);
							leggings = new ItemStack(Material.IRON_LEGGINGS);
							boots = new ItemStack(Material.IRON_BOOTS);
						}
						if(armor.equalsIgnoreCase("diamond"))
						{
							head = new ItemStack(Material.DIAMOND_HELMET);
							chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
							leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
							boots = new ItemStack(Material.DIAMOND_BOOTS);
						}
						
						for (Entity ent : entSet)
						{
							if (ent instanceof CraftLivingEntity)
							{
								CraftLivingEntity cEnt = (CraftLivingEntity)ent;
								cEnt.getEquipment().setHelmet(head);
								cEnt.getEquipment().setChestplate(chest);
								cEnt.getEquipment().setLeggings(leggings);
								cEnt.getEquipment().setBoots(boots);
							}
						}
					}
					catch (Exception e)
					{
						UtilPlayer.message(caller, F.desc("Armor", "Invalid [" + arg + "] on " + type.name()));
					}
					argHandle.add(arg);	
				}
			}
			for (String arg : argHandle)
				argSet.remove(arg);

			for (String arg : argSet)
				UtilPlayer.message(caller, F.desc("Unhandled", arg));

			//Inform
			UtilPlayer.message(caller, F.main("Creature", "Spawned " + count + " " + UtilEnt.getName(type) + "."));
		}
	}

	private void commandMobKill(Player caller, String[] args)
	{
		if (args.length < 3)
		{
			UtilPlayer.message(caller, F.main("Creature", "Missing Entity Type Parameter."));
			return;
		}

		EntityType type = UtilEnt.searchEntity(caller, args[2], true);

		if (type == null && !args[2].equalsIgnoreCase("all"))
			return;

		int count = 0;
		List<Entity> killList = new ArrayList<Entity>();

		for (World world : UtilServer.getServer().getWorlds())
		{
			for (Entity ent : world.getEntities())
			{
				if (ent.getType() == EntityType.PLAYER)
					continue;

				if (type == null || ent.getType() == type)
				{
					killList.add(ent);
				}
			}
		}

		CreatureKillEntitiesEvent event = new CreatureKillEntitiesEvent(killList);
		UtilServer.getServer().getPluginManager().callEvent(event);

		for (Entity entity : event.GetEntities())
		{
			entity.remove();
			count++;
		}

		String target = "ALL";
		if (type != null)
			target = UtilEnt.getName(type);

		UtilPlayer.message(caller, F.main("Creature", "Killed " + target + ". " + count + " Removed."));
	}

	private void commandKit(Player caller, String[] args)
	{
		
		if(!(Manager.GetGame() instanceof EventGame)) {
			UtilPlayer.message(caller, F.main("Inventory", "You can only enable/disable a Kit in the Event game!"));
			return;
		}
		
		if (args.length >= 2 && args[1].equalsIgnoreCase("apply"))
		{
			for (Player player : UtilServer.getPlayers())
				((EventGame) Manager.GetGame()).giveItems(player);

			Manager.GetGame().Announce(F.main("Event Settings", F.value("Player Kit", "Applied to Players")));
			return;
		}

		if (args.length >= 2 && args[1].equalsIgnoreCase("clear"))
		{
			((EventGame) Manager.GetGame()).setKitItems(new ItemStack[6]);
			Manager.GetGame().Announce(F.main("Event Settings", F.value("Player Kit", "Cleared Kit")));
			return;
		}

		if (args.length >= 2 && args[1].equalsIgnoreCase("set"))
		{
			((EventGame) Manager.GetGame()).setKitItems(new ItemStack[6]);

			for (int i=0 ; i<6 ; i++)
			{
				if (caller.getInventory().getItem(i) != null)
					((EventGame) Manager.GetGame()).getKitItems()[i] = caller.getInventory().getItem(i).clone();
				else
					((EventGame) Manager.GetGame()).getKitItems()[i] = null;
			}

			Manager.GetGame().Announce(F.main("Event Settings", F.value("Player Kit", "Updated Items")));
			return;
		}

		commandHelp(caller);
	}

	private void commandEffect(Player caller, String[] args)
	{
		//Clear
		if (args.length >= 3 && args[2].equalsIgnoreCase("clear"))
		{
			//Get Targets
			LinkedList<Player> targets = new LinkedList<Player>();

			if (args[1].equalsIgnoreCase("all"))
			{
				for (Player cur : UtilServer.getPlayers())
					targets.add(cur);
				
				_potionEffectsDuration.clear();
				_potionEffectsMult.clear();
			}
			else
			{
				targets = UtilPlayer.matchOnline(caller, args[1], true);
				if (targets.isEmpty())			
					return;
			}

			for (Player player : targets)
			{
				//Remove all conditions
				Manager.GetCondition().EndCondition(player, null, null);

				//Remove all effects
				player.removePotionEffect(PotionEffectType.ABSORPTION);
				player.removePotionEffect(PotionEffectType.BLINDNESS);
				player.removePotionEffect(PotionEffectType.CONFUSION);
				player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				player.removePotionEffect(PotionEffectType.FAST_DIGGING);
				player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
				player.removePotionEffect(PotionEffectType.HARM);
				player.removePotionEffect(PotionEffectType.HEAL);
				player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
				player.removePotionEffect(PotionEffectType.HUNGER);
				player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.removePotionEffect(PotionEffectType.JUMP);
				player.removePotionEffect(PotionEffectType.NIGHT_VISION);
				player.removePotionEffect(PotionEffectType.POISON);
				player.removePotionEffect(PotionEffectType.REGENERATION);
				player.removePotionEffect(PotionEffectType.SATURATION);
				player.removePotionEffect(PotionEffectType.SLOW);
				player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
				player.removePotionEffect(PotionEffectType.SPEED);
				player.removePotionEffect(PotionEffectType.WATER_BREATHING);
				player.removePotionEffect(PotionEffectType.WEAKNESS);
				player.removePotionEffect(PotionEffectType.WITHER);	
			}

			return;
		}

		//Apply
		if (args.length >= 5)
		{

			//Get Type
			PotionEffectType type = PotionEffectType.getByName(args[2]);
			if (type == null)
			{
				UtilPlayer.message(caller, F.main("Effect", "Invalid Effect Type: " + args[2]));
				UtilPlayer.message(caller, F.value("Valid Types", "http://minecraft.gamepedia.com/Status_effect"));
				return;
			}		

			//Get Multiplier
			int mult = 0;
			try
			{
				mult = Integer.parseInt(args[3]);

				if (mult <= 0)
					mult = 0;
				if (mult > 255)
					mult = 255;				
			}
			catch (Exception e)
			{
				UtilPlayer.message(caller, F.main("Effect", "Invalid Effect Level: " + args[3]));
				return;
			}

			//Get Duration
			int dur = 0;
			try
			{
				dur = Integer.parseInt(args[4]);

				if (dur <= 0)
					dur = 0;			
			}
			catch (Exception e)
			{
				UtilPlayer.message(caller, F.main("Effect", "Invalid Effect Duration: " + args[4]));
				return;
			}
			
			//Get Targets
			LinkedList<Player> targets = new LinkedList<Player>();

			if (args[1].equalsIgnoreCase("all"))
			{
				for (Player cur : UtilServer.getPlayers())
					targets.add(cur);
				
				_potionEffectsDuration.put(type, (long) (System.currentTimeMillis() + (dur * 1000)));
				_potionEffectsMult.put(type, mult);
			}
			else
			{
				targets = UtilPlayer.matchOnline(caller, args[1], true);
				if (targets.isEmpty())			
					return;
			}

			//Apply
			PotionEffect effect = new PotionEffect(type, dur*20, mult);
			for (Player cur : targets)
			{
				cur.addPotionEffect(effect);
			}

			if (args[1].equalsIgnoreCase("all"))
				Manager.GetGame().Announce(F.main("Effect", F.value("Applied Effect", type.getName() + " " + (mult+1) + " for " + dur + "s")));
			else
				UtilPlayer.message(caller, F.main("Effect", "Applied " + type.getName() + " " + (mult+1) + " for " + dur + "s for Targets."));

			return;
		}

		commandHelp(caller);
	}
	
	@EventHandler
	public void updatePotionEffects(UpdateEvent event)
	{
		if(event.getType() != UpdateType.SEC)
			return;
		
		for(Player player : UtilServer.getPlayers()) 
		{
			for(PotionEffectType effect : _potionEffectsDuration.keySet())
			{
				player.addPotionEffect(new PotionEffect(effect, (int) (((_potionEffectsDuration.get(effect) - System.currentTimeMillis()) / 1000) * 20), _potionEffectsMult.get(effect)));
			}
		}
	}

}
