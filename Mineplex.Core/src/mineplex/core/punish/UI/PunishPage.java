package mineplex.core.punish.UI;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryCustom;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import mineplex.core.antihack.AntiHack;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilTime;
import mineplex.core.punish.Category;
import mineplex.core.punish.Punish;
import mineplex.core.punish.PunishClient;
import mineplex.core.punish.Punishment;
import mineplex.core.punish.PunishmentResponse;
import mineplex.core.punish.PunishmentSorter;
import mineplex.core.shop.item.IButton;
import mineplex.core.shop.item.ShopItem;

public class PunishPage extends CraftInventoryCustom implements Listener
{
	private Punish _plugin;
	private NautHashMap<Integer, IButton> _buttonMap;
	private Player _player;
	private String _target;
	private String _reason;
	private ShopItem _chatOffenseButton;
	private ShopItem _exploitingButton;
	private ShopItem _hackingButton;
	private ShopItem _warningButton;
	private ShopItem _permMuteButton;
	private ShopItem _permBanButton;

	public PunishPage(Punish plugin, Player player, String target, String reason) 
	{
		super(null, 54, "            Punish");

		_plugin = plugin;
		_buttonMap = new NautHashMap<Integer, IButton>();

		_player = player;
		_target = target;
		_reason = reason;

		BuildPage();

		_player.openInventory(this);
		_plugin.registerEvents(this);
	}

	private void BuildPage()
	{
		// Player head
		getInventory().setItem(4, new ShopItem(Material.SKULL_ITEM, (byte)3, _target, new String[] { ChatColor.RESET + _reason }, 1, false, true).getHandle());

		PunishClient client = _plugin.GetClient(_target);

		HashMap<Category, HashMap<Integer, Integer>> offenseMap = new HashMap<Category, HashMap<Integer, Integer>>();
		for (Category category : Category.values())
		{
			//Initialise Offences
			offenseMap.put(category, new HashMap<Integer, Integer>());

			offenseMap.get(category).put(1, 0);
			offenseMap.get(category).put(2, 0);
			offenseMap.get(category).put(3, 0);
		}

		List<Entry<Category, Punishment>> punishments = new ArrayList<Entry<Category, Punishment>>();

		for (Category category : client.GetPunishments().keySet())
		{	
			for (Punishment punishment : client.GetPunishments().get(category))
			{
				punishments.add(new AbstractMap.SimpleEntry<Category, Punishment>(category, punishment));

				//Count by Severity
				if (!punishment.GetRemoved() && punishment.GetSeverity() > 0 && punishment.GetSeverity() < 4)
					offenseMap.get(category).put(punishment.GetSeverity(), 1 + offenseMap.get(category).get(punishment.GetSeverity()));
			}
		}

		String examplePrefix = ChatColor.RESET + "" + ChatColor.GRAY;
		String examplePrefixEx = ChatColor.RESET + "" + ChatColor.WHITE;
		String examplePrefixNote = ChatColor.RESET + "" + ChatColor.DARK_GREEN;

		_chatOffenseButton = new ShopItem(Material.BOOK_AND_QUILL, (byte)0, "Chat Offense", new String[] { examplePrefix + "Verbal Abuse, Spam, Harassment, Trolling, etc" }, 1, false, true);
		_exploitingButton = new ShopItem(Material.HOPPER, (byte)0, "General Offense", new String[] { examplePrefix + "Command/Map/Class/Skill exploits, etc" }, 1, false, true);
		_hackingButton = new ShopItem(Material.IRON_SWORD, (byte)0, "Client Mod", new String[] { examplePrefix + "X-ray, Forcefield, Speed, Fly, Inventory Hacks, etc" }, 1, false, true);
		_warningButton = new ShopItem(Material.PAPER, (byte)0, "Warning", new String[] {  }, 1, false, true);
		_permMuteButton = new ShopItem(Material.EMERALD_BLOCK, (byte)0, "Permanent Mute", new String[] {  }, 1, false, true);
		_permBanButton = new ShopItem(Material.REDSTONE_BLOCK, (byte)0, "Permanent Ban", new String[] {  }, 1, false, true);

		getInventory().setItem(10, _chatOffenseButton.getHandle());
		getInventory().setItem(12, _exploitingButton.getHandle());
		getInventory().setItem(14, _hackingButton.getHandle());

		//XXX Mute
		{
			AddButton(19, new ShopItem(Material.INK_SACK, (byte)2, "Severity 1", new String[] { 
				ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.ChatOffense).get(1), 
				ChatColor.RESET + "Mute Duration: " + ChatColor.YELLOW + getDurationString(Category.ChatOffense, 1, offenseMap), 
				" ",
				examplePrefix + "Light Spam", 
				examplePrefixEx + "   Sending the same message 2-5 times",
				" ",
				examplePrefix + "Light Advertising;",
				examplePrefixEx + "   'anyone want to play on minecade?'",
				" ",
				examplePrefix + "Light Abuse/Harassment",
				examplePrefixEx + "   'you suck a this game'",
				" ",
				examplePrefix + "Hackusations", 
				examplePrefixEx + "   'you're such a hacker!'",
				" ",
				examplePrefix + "Trolling",
				" ",
				examplePrefixNote + "Give Warning if 0 Past Offences and 0 Warnings.",
			}, 1, false, true), new PunishButton(this, Category.ChatOffense, 1, false, getDuration(Category.ChatOffense, 1, offenseMap)));

			if (_plugin.GetClients().Get(_player).GetRank().Has(Rank.MODERATOR))
			{
				AddButton(28, new ShopItem(Material.INK_SACK, (byte)11, "Severity 2", new String[] { 
					ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.ChatOffense).get(2), 
					ChatColor.RESET + "Mute Duration: " + ChatColor.YELLOW + getDurationString(Category.ChatOffense, 2, offenseMap), 
					" ",
					examplePrefix + "Medium Spam", 
					examplePrefixEx + "   Sending the same message 6-20 times",
					" ",
					examplePrefix + "Medium Advertising;",
					examplePrefixEx + "   'join crap.server.net' - posted once",
					" ",
					examplePrefix + "Medium Abuse/Harassment",
					examplePrefixEx + "   'piss off you stupid newb'", 
					examplePrefixEx + "   'SHIT ADMINS ARE SHIT!!!'",
					examplePrefixEx + "   'you're terrible, learn to play'",
					" ",
					examplePrefix + "Avoiding Chat Filter",
					examplePrefixEx + "   'F|_|<K YOU'", 
				}, 1, false, true), new PunishButton(this, Category.ChatOffense, 2, false, getDuration(Category.ChatOffense, 2, offenseMap)));

				AddButton(37, new ShopItem(Material.INK_SACK, (byte)1, "Severity 3", new String[] { 
					ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.ChatOffense).get(3), 
					ChatColor.RESET + "Mute Duration: " + ChatColor.YELLOW + getDurationString(Category.ChatOffense, 3, offenseMap), 
					" ",
					examplePrefix + "Severe Spam", 
					examplePrefixEx + "   Sending the same message 20+ times",
					examplePrefixEx + "   Only really used for a spam bot",
					" ",
					examplePrefix + "Severe Abuse/Harassment",
					examplePrefixEx + "   'go fucking die in a fire you fucking sack of shit'",
				}, 1, false, true), new PunishButton(this, Category.ChatOffense, 3, false, getDuration(Category.ChatOffense, 3, offenseMap)));
			}
		}

		//XXX General
		{
			AddButton(21, new ShopItem(Material.INK_SACK, (byte)2, "Severity 1", new String[] { 
				ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.Exploiting).get(1), 
				ChatColor.RESET + "Ban Duration: " + ChatColor.YELLOW + getDurationString(Category.Exploiting, 1, offenseMap), 
				" ",
				examplePrefix + "Team Killing", 
				examplePrefixEx + "   Intentionally killing your team mates",
				" ",
				examplePrefix + "Trolling (Gameplay)", 
				examplePrefixEx + "   Using abilities to trap players in spawn",
				" ",
				examplePrefix + "Map/Bug Exploiting", 
				examplePrefixEx + "   Abusing an exploit to gain an advantage",
			}, 1, false, true), new PunishButton(this, Category.Exploiting, 1, true, getDuration(Category.Exploiting, 1, offenseMap)));

			//			if (_plugin.GetClients().Get(_player).GetRank().Has(Rank.MODERATOR))
			//			{
			//
			//				AddButton(30, new ShopItem(Material.INK_SACK, (byte)11, "Severity 2", new String[] { 
			//					ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.Exploiting).get(2), 
			//					ChatColor.RESET + "Ban Duration: " + ChatColor.YELLOW + getDurationString(Category.Exploiting, 2, offenseMap), 
			//					" ",
			//					examplePrefix + "Empty", 
			//					examplePrefixEx + "   Currently no reason to use this :)",
			//				}, 1, false, true),  new PunishButton(this, Category.Exploiting, 2, true, getDuration(Category.Exploiting, 2, offenseMap)));
			//
			//				AddButton(39, new ShopItem(Material.INK_SACK, (byte)1, "Severity 3", new String[] {
			//					ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.Exploiting).get(3), 
			//					ChatColor.RESET + "Ban Duration: " + ChatColor.YELLOW + getDurationString(Category.Exploiting, 3, offenseMap), 
			//					" ",
			//					examplePrefix + "Server Lag/Crash", 
			//					examplePrefixEx + "   Abusing a bug to lag or crash a server",
			//					" ",
			//				}, 1, false, true), new PunishButton(this, Category.Exploiting, 3, true, getDuration(Category.Exploiting, 3, offenseMap)));
			//			}
		}

		//XXX Hacks
		{
			AddButton(23, new ShopItem(Material.INK_SACK, (byte)2, "Severity 1", new String[] { 
				ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.Hacking).get(1), 
				ChatColor.RESET + "Ban Duration: " + ChatColor.YELLOW + getDurationString(Category.Hacking, 1, offenseMap), 
				" ",
				examplePrefix + "Examples;", 
				examplePrefixEx + "   Damage Indicators",
				examplePrefixEx + "   Player Radar",
			}, 1, false, true), new PunishButton(this, Category.Hacking, 1, true, getDuration(Category.Hacking, 1, offenseMap)));

			if (_plugin.GetClients().Get(_player).GetRank().Has(Rank.MODERATOR))
			{

				AddButton(32, new ShopItem(Material.INK_SACK, (byte)11, "Severity 2",new String[]
						{
						ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.Hacking).get(2),
						ChatColor.RESET + "Ban Duration: " + ChatColor.YELLOW + getDurationString(Category.Hacking, 2, offenseMap), 
						" ",
						examplePrefix + "Hacks;", 
						examplePrefixEx + "   Forcefield",
						examplePrefixEx + "   Speed Hack",
						examplePrefixEx + "   Reach Hack",
						examplePrefixEx + "   Other Hack",
						" ",
						examplePrefix + "Hack Reports (SR & FR);", 
						examplePrefixEx + "   Forcefield",
						examplePrefixEx + "   Speed Hack",
						examplePrefixEx + "   Reach Hack",
						examplePrefixEx + "   Other Hack",
						examplePrefixEx + "   Fly Hack",
						}
						, 1, false, true), new PunishButton(this, Category.Hacking, 2, true, getDuration(Category.Hacking, 2, offenseMap)));

		
				int flightSeverity = (AntiHack.Instance.isStrict() ? 3 : 2);
				

				AddButton(41, new ShopItem(Material.INK_SACK, (byte)1, "Severity 3",new String[]
						{
					ChatColor.RESET + "Past Offences: " + ChatColor.YELLOW + offenseMap.get(Category.Hacking).get(flightSeverity),
					ChatColor.RESET + "Ban Duration: " + ChatColor.YELLOW + getDurationString(Category.Hacking, flightSeverity, offenseMap), 
					" ",
					examplePrefix + "Hacks;", 
					examplePrefixEx + "   Fly Hack",
					" ",
					C.cRed + C.Bold + "WARNING;",
					C.cRed + "Use Severity 2 for Forum/Staff Reports"
						}
						, 1, false, true), new PunishButton(this, Category.Hacking, 3, true, getDuration(Category.Hacking, flightSeverity, offenseMap)));
			}
		}

		//XXX Other
		AddButton(25, new ShopItem(Material.PAPER, (byte)0, "Warning", new String[] { 
			" ",
			examplePrefix + "Example Warning Input;", 
			examplePrefixEx + "   Spam - Repeatedly writing MEOW",
			examplePrefixEx + "   Swearing - Saying 'fuck' and 'shit'",
			examplePrefixEx + "   Hack Accusation - Accused Tomp13 of hacking",
			examplePrefixEx + "   Trolling - was trying to make bob angry in chat",

		}, 1, false, true), new PunishButton(this, Category.Warning, 1, false, 0));

		if (_plugin.GetClients().Get(_player).GetRank().Has(Rank.MODERATOR))
		{
			AddButton(34, new ShopItem(Material.REDSTONE_BLOCK, (byte)0, "Permanent Ban", new String[] { 
				ChatColor.RESET + "Ban Duration: " + ChatColor.YELLOW + "Permanent", 
				" ",
				examplePrefixNote + "Must supply detailed reason for Ban." 
			}, 1, false, true), new PunishButton(this, Category.Other, 1, true, -1));

			AddButton(43, new ShopItem(Material.BOOK_AND_QUILL, (byte)0, "Permanent Mute", new String[] { 
				ChatColor.RESET + "Mute Duration: " + ChatColor.YELLOW + "Permanent", 
				" ",
				examplePrefix + "Severe Advertising;",
				examplePrefixEx + "   'JOIN MINECADE! MINEPLEX SUCKS!",
				examplePrefixEx + "   'join crap.server.net! FREE ADMIN!",
				" ",
				examplePrefixNote + "Must supply detailed reason for Mute."
			}, 1, false, true), new PunishButton(this, Category.ChatOffense, 4, false, -1));
		}

		if (_plugin.GetClients().Get(_player).GetRank() == Rank.DEVELOPER || _plugin.GetClients().Get(_player).GetRank() == Rank.JNR_DEV)
		{
			ShopItem devWarning = new ShopItem(Material.YELLOW_FLOWER, "DEV WARNING", new String[] {ChatColor.RESET + "Developers are advised against using the punish system", ChatColor.RESET + "unless permitted by LT"}, 1, true, true);
			devWarning.addGlow();

			AddButton(0, devWarning, new IButton()
			{
				@Override
				public void onClick(Player player, ClickType clickType)
				{
				}
			});
		}

		Collections.sort(punishments, new PunishmentSorter());

		int slot = 45;

		for (Entry<Category, Punishment> punishmentEntry : punishments)
		{
			if (punishmentEntry.getKey() == Category.Advertisement)
				continue;

			if (slot >= 54)
				break;

			ShopItem button = null;

			switch (punishmentEntry.getKey())
			{
			case ChatOffense:
				button = _chatOffenseButton.clone();
				break;
			case Exploiting:
				button = _exploitingButton.clone();
				break;
			case Hacking:
				button = _hackingButton.clone();
				break;
			case Warning:
				button = _warningButton.clone();
				break;
			case PermMute:
				button = _permMuteButton.clone();
				break;
			case Other:
				button = _permBanButton.clone();
				break;	
			default:
				break;				
			}

			Punishment punishment = punishmentEntry.getValue();

			//Reason Lines for LORE
			ArrayList<String> reasonLines = new ArrayList<String>();
			String reason = punishment.GetReason();
			while (reason.length() > 0)
			{
				int index = Math.min(reason.length(), 24);
				
				while (index < reason.length() && reason.charAt(index) != ' ')
					index++;
				
				reasonLines.add(reason.substring(0, index));
				reason = reason.substring(index, reason.length());
			}
			
			//LORE
			ArrayList<String> output = new ArrayList<String>();

			output.add(C.cYellow + "Punishment Type: " + ChatColor.RESET + punishment.GetCategory().toString());

			if (punishmentEntry.getKey() == Category.ChatOffense || 
				punishmentEntry.getKey() == Category.Exploiting || 
				punishmentEntry.getKey() == Category.Hacking)
			{
				output.add(C.cYellow + "Severity: " + ChatColor.RESET + punishment.GetSeverity());
			}

			output.add(" ");
			
			for (int i=0 ; i<reasonLines.size() ; i++)
			{
				if (i == 0)
					output.add(C.cYellow + "Reason: " + ChatColor.RESET + reasonLines.get(i));
				else
					output.add(C.cYellow + "          " + ChatColor.RESET + reasonLines.get(i));
			}
			
			output.add(" ");
			output.add(C.cYellow + "Staff: " + ChatColor.RESET + punishment.GetAdmin());
			output.add(" ");
			output.add(C.cYellow + "Date: " + ChatColor.RESET + UtilTime.when(punishment.GetTime()));


			if (punishment.GetRemoved())
			{
				output.add(" ");
				output.add(C.cYellow + "Removed by: " + (punishment.GetRemoved() ? ChatColor.RESET + punishment.GetRemoveAdmin() : ChatColor.RED + "Not Removed"));
				output.add(C.cYellow + "Remove Reason: " + (punishment.GetRemoved() ? ChatColor.RESET + punishment.GetRemoveReason() : ChatColor.RED + "Not Removed"));	
			}


			String[] loreString = new String[output.size()];

			for (int i=0 ; i<output.size() ; i++)
			{
				loreString[i] = output.get(i);
			}

			button.SetLore(loreString);


			if ((punishment.GetHours() == -1 || punishment.GetRemaining() > 0) && !punishment.GetRemoved() && punishment.GetActive())
			{
				button.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				AddButton(slot, button, new RemovePunishmentButton(this, punishment, button));
			}
			else
			{
				getInventory().setItem(slot, button.getHandle());
			}

			slot++;
		}
	}

	@EventHandler
	public void OnInventoryClick(InventoryClickEvent event)
	{
		if (inventory.getInventoryName().equalsIgnoreCase(event.getInventory().getTitle()) && event.getWhoClicked() == _player)
		{
			if (_buttonMap.containsKey(event.getRawSlot()))
			{
				if (event.getWhoClicked() instanceof Player)
					_buttonMap.get(event.getRawSlot()).onClick(((Player) event.getWhoClicked()), event.getClick());
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public void OnInventoryClose(InventoryCloseEvent event)
	{
		if (inventory.getInventoryName().equalsIgnoreCase(event.getInventory().getTitle()) && event.getPlayer() == _player)
		{
			ClosePunish();
		}
	}

	private void AddButton(int slot, ShopItem item, IButton button)
	{
		getInventory().setItem(slot, item.getHandle());
		_buttonMap.put(slot, button);
	}

	public void AddInfraction(Category category, int severity, boolean ban, long punishTime)
	{
		_plugin.AddPunishment(_target, category, _reason, _player, severity, ban, punishTime);
		_player.closeInventory();
		ClosePunish();
	}

	private void ClosePunish()
	{
		HandlerList.unregisterAll(this);
	}

	public void RemovePunishment(final Punishment punishment, final ItemStack item)
	{
		_plugin.RemovePunishment(punishment.GetPunishmentId(), _target, _player, _reason, new Callback<String>()
				{
			@Override
			public void run(String result)
			{
				PunishmentResponse punishResponse = PunishmentResponse.valueOf(result);

				if (punishResponse != PunishmentResponse.PunishmentRemoved)
				{
					_player.sendMessage(F.main(_plugin.getName(), "There was a problem removing the punishment."));
				}
				else
				{
					punishment.Remove(_player.getName(), _reason);
					_player.closeInventory();
					ClosePunish();
				}
			}
				});
	}
	
	public String getDurationString(Category category, int severity, HashMap<Category, HashMap<Integer, Integer>> pastOffences)
	{
		int hours = getDuration(category, severity, pastOffences);
		
		if (hours == -1)
			return "Permanent";
		
		return UtilTime.MakeStr((long)hours * 3600000L);
	}
	
	public int getDuration(Category category, int severity, HashMap<Category, HashMap<Integer, Integer>> pastOffences)
	{
		if (category == Category.ChatOffense)
		{
			int hours = 0;
			
			if (severity >= 1)
			{
				hours += calculateTime(2, 2, 48, pastOffences.get(category).get(1), severity != 1);
			}
			if (severity >= 2)
			{
				hours += calculateTime(24, 24, 168, pastOffences.get(category).get(2), severity != 2);
			}
			if (severity >= 3)
			{
				hours += calculateTime(720, 720, 720, pastOffences.get(category).get(3), severity != 3);
			}
			
			return hours;	
		}
		
		if (category == Category.Exploiting)
		{
			int hours = 0;
			
			if (severity >= 1)
			{
				hours += calculateTime(4, 4, 96, pastOffences.get(category).get(1), severity != 1);
			}
			if (severity >= 2)
			{
				hours += calculateTime(48, 48, 336, pastOffences.get(category).get(2), severity != 2);
			}
			if (severity >= 3)
			{
				return -1;
			}
			
			return hours;	
		}
		
		if (category == Category.Hacking)
		{
			int hours = 0;
			
			if (severity >= 1)
			{
				hours += calculateTime(24, 24, 168, pastOffences.get(category).get(1), severity != 1);
			}
			if (severity >= 2)
			{
				//Permanent!
				if (pastOffences.get(category).get(2) > 0)
					return -1;
					
				hours = 720;
			}
			if (severity >= 3)
			{
				return -1;
			}
			
			return hours;	
		}
	
		return 0;
	}

	private int calculateTime(int baseAmount, int addAmount, int pastLimit, int offenses, boolean zeroBase)
	{
		int amount = 0;
		
		if (zeroBase)
			baseAmount = 0;
		
		// At what point does Bonus > pastLimit
		int breakLimitCount = 0; 
		while (baseAmount + addAmount * breakLimitCount * breakLimitCount < pastLimit)
			breakLimitCount++;
		
		amount += Math.min(baseAmount + addAmount * offenses * offenses, pastLimit);
		amount += Math.max(0, (offenses - breakLimitCount) * pastLimit);
		
		return amount;
	}
}
