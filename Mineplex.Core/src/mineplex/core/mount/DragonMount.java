package mineplex.core.mount;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.disguise.disguises.DisguiseChicken;

public class DragonMount extends Mount<DragonData>
{
	public DragonMount(MountManager manager, String name, String[] desc, Material displayMaterial, byte displayData, int cost)
	{
		super (manager, name, displayMaterial, displayData, desc, cost);
		
		KnownPackage = false;
	}
	
	@Override
	public void EnableCustom(final Player player) 
	{
		player.leaveVehicle();
		player.eject();
		
		//Remove other mounts
		Manager.DeregisterAll(player);
		
		//Inform
		UtilPlayer.message(player, F.main("Mount", "You spawned " + F.elem(GetName()) + "."));
		
		//Store
		DragonData dragonData = new DragonData(this, player);
		//Set max health to 1 so player doesn't see a bunch of mount hearts flashing when NewsManager changes the health
		dragonData.Dragon.setMaxHealth(1.0);
		dragonData.Dragon.setHealth(1.0);
		_active.put(player, dragonData);
	}

	@Override
	public void Disable(Player player) 
	{	
		DragonData data = _active.remove(player);
		if (data != null)
		{
			data.Dragon.remove();
			data.Chicken.remove();
			
			//Inform
			UtilPlayer.message(player, F.main("Mount", "You despawned " + F.elem(GetName()) + "."));
			
			Manager.removeActive(player);
		}
	}
}
