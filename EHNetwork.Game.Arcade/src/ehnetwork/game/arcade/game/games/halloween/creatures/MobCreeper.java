package ehnetwork.game.arcade.game.games.halloween.creatures;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.EntityTargetEvent;

import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.game.games.halloween.Halloween;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MobCreeper extends CreatureBase<Creeper> implements InterfaceMove
{
	public MobCreeper(Halloween game, Location loc) 
	{
		super(game, null, Creeper.class, loc);
	}
	
	@Override
	public void SpawnCustom(Creeper ent) 
	{
		ent.setCustomName("Creeper");
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

	@Override
	public void Move() 
	{		
		CreatureMove(GetEntity());
	}
}
