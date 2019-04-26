package nautilus.game.arcade.game.games.baconbrawl.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.disguise.disguises.DisguisePig;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkBaconBlast;
import nautilus.game.arcade.kit.perks.PerkSpeed;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;

public class KitMamaPig extends Kit
{
	public KitMamaPig(ArcadeManager manager)
	{
		super(manager, "Mama Piggles", KitAvailability.Gem, 

				new String[] 
						{
				"Mama & Baby Piggles fight together!"
						}, 

						new Perk[] 
								{
				new PerkBaconBlast(),
				new PerkSpeed(1),
								}, 
								EntityType.PIG,
								new ItemStack(Material.PORK));
	}

	@Override
	public void GiveItems(final Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		
		//Disguise
		DisguisePig disguise = new DisguisePig(player);
		disguise.setName(C.cYellow + player.getName());
		disguise.setCustomNameVisible(false);
		Manager.GetDisguise().disguise(disguise);
		
		Manager.GetGame().CreatureAllowOverride = true;
		final Pig pig = player.getWorld().spawn(player.getEyeLocation(), Pig.class);
		pig.setBaby();
		pig.setAgeLock(true);
		pig.setCustomName(C.cYellow + player.getName());
		pig.setCustomNameVisible(false);
		Manager.GetGame().CreatureAllowOverride = false;
		
		player.setPassenger(pig);

		Bukkit.getScheduler().runTaskLater(Manager.getPlugin(), new Runnable() 
		{
			@Override 
			public void run() 
			{
				UtilPlayer.sendPacket(player, new PacketPlayOutEntityDestroy(new int[] { pig.getEntityId() }));
			}
		}, 2);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void damageTransfer(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (!(event.GetDamageeEntity() instanceof Pig))
			return;
		
		Pig pig = (Pig)event.GetDamageeEntity();
		
		if (pig.getVehicle() == null || !(pig.getVehicle() instanceof LivingEntity))
			return;
		
		event.setDamagee((LivingEntity)pig.getVehicle());
	}
}
