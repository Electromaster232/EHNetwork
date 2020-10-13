package ehnetwork.game.arcade.game.games.halloween.creatures;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityTargetEvent;

import de.robingrether.idisguise.disguise.Disguise;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.game.games.halloween.Halloween;
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
		Disguise d1 = Host.Manager.GetDisguise().createDisguise(EntityType.PIG_ZOMBIE);
		Host.Manager.GetDisguise().getApi().disguise(ent, d1);
		
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
