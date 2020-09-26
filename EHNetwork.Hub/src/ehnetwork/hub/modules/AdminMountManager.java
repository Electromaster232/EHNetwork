package ehnetwork.hub.modules;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.hub.HubManager;
import ehnetwork.hub.commands.HorseSpawn;

public class AdminMountManager extends MiniPlugin
{
	private HubManager Manager;
	
	private HashMap<Player, Horse> _mounts = new HashMap<Player, Horse>();

	public AdminMountManager(HubManager manager)
	{
		super("Mount Manager", manager.getPlugin());
		
		Manager = manager;
	}

	@Override
	public void addCommands()
	{
		addCommand(new HorseSpawn(this));
	} 

	@EventHandler
	public void HorseInteract(PlayerInteractEntityEvent event)
	{
		if (!(event.getRightClicked() instanceof Horse))
			return;

		Player player = event.getPlayer();
		Horse horse = (Horse)event.getRightClicked();

		//Not Owner
		if (!_mounts.containsKey(player) || !_mounts.get(player).equals(horse))
		{
			UtilPlayer.message(player, F.main("Mount", "This is not your mount!"));
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		Horse horse = _mounts.remove(event.getPlayer());
		if (horse != null)
			horse.remove();
	}
	
	public void HorseCommand(Player caller, String[] args) 
	{
		if (args == null || args.length == 0)
		{
			UtilPlayer.message(caller, F.main("Mount", "Mount Commands;"));
			UtilPlayer.message(caller, "spawn / kill / leash / unleash");
			UtilPlayer.message(caller, "age / color / style / armor");
			return;
		}

		if (args[0].equalsIgnoreCase("spawn"))
		{
			Variant var = Variant.HORSE;
			if (args.length > 1)
			{
				try
				{
					var = Variant.valueOf(args[1].toUpperCase());;
				}
				catch (Exception e)
				{

				}
			}

			Spawn(caller, var);
			return;
		}

		Horse horse = _mounts.get(caller);
		if (horse == null)
		{
			UtilPlayer.message(caller, F.main("Mount", "You do not have a mount."));
			return;
		}

		//Leash
		else if (args[0].equalsIgnoreCase("leash"))
		{
			horse.setLeashHolder(caller);
		}

		//UnLeash
		else if (args[0].equalsIgnoreCase("unleash"))
		{
			horse.setLeashHolder(null);
		}

		//Kill
		else if (args[0].equalsIgnoreCase("kill"))
		{
			horse.remove();
			_mounts.remove(caller);
		}

		//Age
		else if (args[0].equalsIgnoreCase("age"))
		{
			if (args.length >= 2)
			{
				try
				{
					if (args[1].equalsIgnoreCase("adult"))
					{
						horse.setAdult();
					}
					else if (args[1].equalsIgnoreCase("baby"))
					{
						horse.setBaby();
					}
					return;
				}
				catch (Exception e)
				{

				}
			}

			UtilPlayer.message(caller, F.main("Mount", F.value("Age", "baby adult")));
		}

		//Color
		else if (args[0].equalsIgnoreCase("color"))
		{
			if (args.length >= 2)
			{
				Color color = GetColor(caller, args[1]);
				if (color != null)
					horse.setColor(color);
			}
		}

		//Style
		else if (args[0].equalsIgnoreCase("style"))
		{
			if (args.length >= 2)
			{
				Style style = GetStyle(caller, args[1]);
				if (style != null)
					horse.setStyle(style);
			}
		}

		//Variant
		else if (args[0].equalsIgnoreCase("variant") || args[0].equalsIgnoreCase("var"))
		{
			if (args.length >= 2)
			{
				Variant variant = GetVariant(caller, args[1]);
				if (variant != null)
					horse.setVariant(variant);
			}
		}

		//Armor
		else if (args[0].equalsIgnoreCase("armor"))
		{
			if (args.length >= 2)
			{
				try
				{
					if (args[1].equalsIgnoreCase("iron"))
					{
						horse.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
						return;
					}
					if (args[1].equalsIgnoreCase("gold"))
					{
						horse.getInventory().setArmor(new ItemStack(Material.GOLD_BARDING));
						return;
					}
					if (args[1].equalsIgnoreCase("diamond"))
					{
						horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
						return;
					}

				}
				catch (Exception e)
				{

				}
			}

			UtilPlayer.message(caller, F.main("Mount", F.value("Armor", "iron gold diamond")));
		}
	}

	public Style GetStyle(Player caller, String arg)
	{
		ArrayList<Style> match = new ArrayList<Style>();

		for (Style var : Style.values())
		{
			if (var.name().equals(arg.toUpperCase()))
				return var;

			if (var.name().contains(arg.toUpperCase()))
				match.add(var);
		}

		if (match.size() == 1)
			return match.get(0);

		String valids = "";
		for (Style valid : Style.values())
			valids += valid.name() + " ";
		UtilPlayer.message(caller, F.main("Mount", F.value("Styles", valids)));

		return null;
	}

	public Color GetColor(Player caller, String arg)
	{
		ArrayList<Color> match = new ArrayList<Color>();

		for (Color var : Color.values())
		{
			if (var.name().equals(arg.toUpperCase()))
				return var;

			if (var.name().contains(arg.toUpperCase()))
				match.add(var);
		}

		if (match.size() == 1)
			return match.get(0);

		String valids = "";
		for (Color valid : Color.values())
			valids += valid.name() + " ";
		UtilPlayer.message(caller, F.main("Mount", F.value("Colors", valids)));

		return null;
	}

	public Variant GetVariant(Player caller, String arg)
	{
		ArrayList<Variant> match = new ArrayList<Variant>();

		for (Variant var : Variant.values())
		{
			if (var.name().equals(arg.toUpperCase()))
				return var;

			if (var.name().contains(arg.toUpperCase()))
				match.add(var);
		}

		if (match.size() == 1)
			return match.get(0);

		String valids = "";
		for (Variant valid : Variant.values())
			valids += valid.name() + " ";
		UtilPlayer.message(caller, F.main("Mount", F.value("Variants", valids)));

		return null;
	}

	public Horse Spawn(Player caller, Variant var) 
	{
		Horse horse = _mounts.remove(caller);
		if (horse != null)	horse.remove();

		horse = caller.getWorld().spawn(caller.getLocation(), Horse.class);
		horse.setAdult();
		horse.setAgeLock(true);
		horse.setColor(Color.DARK_BROWN);
		horse.setStyle(Style.WHITE_DOTS);
		horse.setVariant(var);
		horse.setOwner(caller);
		horse.setMaxDomestication(1);
		horse.setJumpStrength(1);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

		horse.setCustomName(caller.getName() + "'s Mount");
		horse.setCustomNameVisible(true);

		_mounts.put(caller, horse);
		
		return horse;
	}
	
	@EventHandler
	public void LeashSpawn(ItemSpawnEvent event)
	{
		if (event.getEntity().getItemStack().getType() == Material.LEASH)
			event.setCancelled(true);
	}
}
