package mineplex.core.mount.types;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.disguise.disguises.DisguiseBase;
import mineplex.core.disguise.disguises.DisguisePig;
import mineplex.core.disguise.disguises.DisguiseSheep;
import mineplex.core.mount.HorseMount;
import mineplex.core.mount.MountManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class MountSheep extends HorseMount
{
	public MountSheep(MountManager manager) 
	{
		super(manager, "Techno Sheep", new String[]
				{
					ChatColor.RESET + "Muley muley!"
				},
				Material.WOOL,
				(byte)14,
				3000,
				Color.BLACK, Style.BLACK_DOTS, Variant.MULE, 1.0, null);
	}
	
	@Override
	public void EnableCustom(Player player)
	{
		player.leaveVehicle();
		player.eject();
		
		//Remove other mounts
		Manager.DeregisterAll(player);
		
		Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
		
		horse.setOwner(player);
		horse.setMaxDomestication(1);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		
		DisguiseSheep disguise = new DisguiseSheep(horse);
		disguise.setName(player.getName(), Manager.getClientManager().Get(player).GetRank());
		//disguise.setColor(DyeColor.getByColor(org.bukkit.Color.fromRGB(100, 0, 200)));
		Manager.getDisguiseManager().disguise(disguise);

		//Inform
		UtilPlayer.message(player, F.main("Mount", "You spawned " + F.elem(GetName()) + "."));
		
		//Store
		_active.put(player, horse);
	}
	
	@EventHandler
	public void updateColor(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Horse horse : GetActive().values())
		{
			DisguiseBase base = Manager.getDisguiseManager().getDisguise(horse);
			if (base == null || !(base instanceof DisguiseSheep))
				continue;
			
			DisguiseSheep sheep = (DisguiseSheep)base;
			
			if (horse.getTicksLived() % 4 == 0)  		sheep.setColor(DyeColor.RED);
			else if (horse.getTicksLived() % 4 == 1)  	sheep.setColor(DyeColor.YELLOW);
			else if (horse.getTicksLived() % 4 == 2)  	sheep.setColor(DyeColor.GREEN);
			else if (horse.getTicksLived() % 4 == 3)  	sheep.setColor(DyeColor.BLUE);
		}
	}
}
