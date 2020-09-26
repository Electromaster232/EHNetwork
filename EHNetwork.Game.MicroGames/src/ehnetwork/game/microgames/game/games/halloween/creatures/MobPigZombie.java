package ehnetwork.game.microgames.game.games.halloween.creatures;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityTargetEvent;

import ehnetwork.core.disguise.disguises.DisguisePigZombie;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.halloween.Halloween;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MobPigZombie extends CreatureBase<Zombie> implements InterfaceMove
{
	public MobPigZombie(Halloween game, Location loc) 
	{
		super(game, null, Zombie.class, loc);
	}

	@Override
	public void SpawnCustom(Zombie ent) 
	{
		DisguisePigZombie disguise = new DisguisePigZombie(ent);
		Host.Manager.GetDisguise().disguise(disguise);
		
		Host.Manager.GetCondition().Factory().Speed("Speed", ent, ent, 99999, 1, false, false, false);
		
		ent.setCustomName("Nether Zombie");
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
