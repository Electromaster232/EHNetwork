package ehnetwork.game.microgames.game.games.halloween.creatures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.disguise.disguises.DisguiseSkeleton;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.halloween.Halloween;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MobSkeletonWarrior extends CreatureBase<Zombie> implements InterfaceMove
{
	public MobSkeletonWarrior(Halloween game, Location loc) 
	{
		super(game, null, Zombie.class, loc);
	}
	
	@Override
	public void SpawnCustom(Zombie ent) 
	{
		DisguiseSkeleton spider = new DisguiseSkeleton(ent);
		Host.Manager.GetDisguise().disguise(spider);
		ent.setCustomName("Skeleton Warrior");
		ent.getEquipment().setItemInHand(new ItemStack(Material.WOOD_HOE));
		
		Host.Manager.GetCondition().Factory().Speed("Speed", ent, ent, 99999, 0, false, false, false);
	}

	@Override
	public void Damage(CustomDamageEvent event) 
	{
		
	}
	
	@Override
	public void Target(EntityTargetEvent event)
	{
		
	}
	
	@Override
	public void Update(UpdateEvent event) 
	{

	}

	public void Move() 
	{		
		CreatureMove(GetEntity());
	}
}
