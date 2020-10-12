package ehnetwork.core.mount;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_8_R3.EntityCreature;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class HorseMount extends Mount<Horse>
{
	protected Color _color;
	protected Style _style;
	protected Variant _variant;
	protected double _jump;
	protected Material _armor;
	
	public HorseMount(MountManager manager, String name, String[] desc, Material displayMaterial, byte displayData, int cost, Color color, Style style, Variant variant, double jump, Material armor)
	{
		super (manager, name, displayMaterial, displayData, desc, cost);
		KnownPackage = false;
		
		_color = color;
		_style = style;
		_variant = variant;
		_jump = jump;
		_armor = armor;
	}
	
	@EventHandler
	public void UpdateHorse(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		Iterator<Player> activeIterator = _active.keySet().iterator();
		
		while (activeIterator.hasNext())
		{
			Player player = activeIterator.next();
			Horse horse = _active.get(player);
			
			//Invalid (dead)
			if (!horse.isValid())
			{
				horse.remove();
				activeIterator.remove();
				continue;
			}
			
			//Move
			EntityCreature ec = ((CraftCreature)horse).getHandle();
			//Navigation nav = ec.getNavigation();
			
			Location target = player.getLocation().add(UtilAlg.getTrajectory(player, horse).multiply(2));

			if (UtilMath.offset(horse.getLocation(), target) > 12)
			{
				target = horse.getLocation();
				target.add(UtilAlg.getTrajectory(horse, player).multiply(12));
				//nav.a(target.getX(), target.getY(), target.getZ(), 1.4f);
			}
			else if (UtilMath.offset(horse, player) > 4)
			{
				//nav.a(target.getX(), target.getY(), target.getZ(), 1.4f);
			}
		}
	}
	
	public void EnableCustom(Player player)
	{
		player.leaveVehicle();
		player.eject();
		
		//Remove other mounts
		Manager.DeregisterAll(player);
		
		Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
		horse.setAdult();
		horse.setAgeLock(true);
		horse.setColor(_color);
		horse.setStyle(_style);
		horse.setVariant(_variant);
		horse.setOwner(player);
		horse.setMaxDomestication(1);
		horse.setJumpStrength(_jump);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

		if (horse.getVariant() == Variant.MULE)
			horse.setCarryingChest(true);
		
		if (_armor != null)
			horse.getInventory().setArmor(new ItemStack(_armor));

		horse.setCustomName(player.getName() + "'s " + GetName());
		
		//Inform
		UtilPlayer.message(player, F.main("Mount", "You spawned " + F.elem(GetName()) + "."));
		
		//Store
		_active.put(player, horse);
	}
	
	public void Disable(Player player)
	{
		Horse horse = _active.remove(player);
		if (horse != null)
		{
			horse.remove();
			
			//Inform
			UtilPlayer.message(player, F.main("Mount", "You despawned " + F.elem(GetName()) + "."));
			
			Manager.removeActive(player);
		}	
	}
}
