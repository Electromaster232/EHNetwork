package ehnetwork.game.arcade.game.games.baconbrawl.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.PigDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBaconBlast;
import ehnetwork.game.arcade.kit.perks.PerkSpeed;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

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
		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.PIG);
		PigDisguise d2 = (PigDisguise) d1;
		d2.setCustomNameVisible(true);
		d2.setCustomName(C.cYellow + player.getName());
		Manager.GetDisguise().applyDisguise(d2, player);
		
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
