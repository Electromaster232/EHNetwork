package ehnetwork.game.microgames.game.games.halloween.creatures;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityTargetEvent;

import ehnetwork.core.disguise.disguises.DisguiseSpider;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.game.games.halloween.Halloween;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MobSpiderSmasher extends CreatureBase<Zombie> implements InterfaceMove
{
	public MobSpiderSmasher(Halloween game, Location loc) 
	{
		super(game, null, Zombie.class, loc);
	}

	@Override
	public void SpawnCustom(Zombie ent) 
	{
		DisguiseSpider spider = new DisguiseSpider(ent);
		Host.Manager.GetDisguise().disguise(spider);
		ent.setCustomName("Smashing Spider");
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
		if (event.getType() == UpdateType.SLOW)
			Speed();
	}

	public void Move()
	{
		CreatureMove(GetEntity());
	}
	
	public void Speed()
	{
		if (GetEntity().getTicksLived() > 3600)
			Host.Manager.GetCondition().Factory().Speed("Speed", GetEntity(), GetEntity(), 10, 2, false, false, false);
		else if (GetEntity().getTicksLived() > 2400)
			Host.Manager.GetCondition().Factory().Speed("Speed", GetEntity(), GetEntity(), 10, 1, false, false, false);
		else if (GetEntity().getTicksLived() > 1200)
			Host.Manager.GetCondition().Factory().Speed("Speed", GetEntity(), GetEntity(), 10, 0, false, false, false);
	}
}
