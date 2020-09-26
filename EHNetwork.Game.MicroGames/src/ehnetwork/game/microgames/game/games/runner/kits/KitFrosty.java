package ehnetwork.game.microgames.game.games.runner.kits;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkConstructor;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KitFrosty extends Kit
{
	public KitFrosty(MicroGamesManager manager)
	{
		super(manager, "Frosty", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Slow enemies to send them to their death!"
						}, 

						new Perk[] 
								{ 
				new PerkConstructor("Frost Balls", 0.5, 16, Material.SNOW_BALL, "Snowball", true)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.SNOW_BALL));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
	
	@EventHandler
	public void SnowballHit(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;
		
		if (!(event.GetProjectile() instanceof Snowball))
			return;
		
		event.GetDamageeEntity().playEffect(EntityEffect.HURT);
		
		Manager.GetCondition().Factory().Slow("Snowball Slow", event.GetDamageeEntity(), (LivingEntity)event.GetProjectile().getShooter(), 2, 1, false, false, true, false);
	}
}
