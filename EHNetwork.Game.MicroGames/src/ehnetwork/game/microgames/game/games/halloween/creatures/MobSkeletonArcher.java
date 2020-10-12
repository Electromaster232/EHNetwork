package ehnetwork.game.microgames.game.games.halloween.creatures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.halloween.Halloween;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MobSkeletonArcher extends CreatureBase<Skeleton> implements InterfaceMove
{
	public MobSkeletonArcher(Halloween game, Location loc) 
	{
		super(game, null, Skeleton.class, loc);
	}
	
	@Override
	public void SpawnCustom(Skeleton ent) 
	{
		ent.getEquipment().setItemInHand(new ItemStack(Material.BOW));
		ent.setCustomName("Skeleton Archer");
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
