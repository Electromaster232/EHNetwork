package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.WeakHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Sharpshooter extends Skill
{
	private WeakHashMap<Player, Integer> _hitCount = new WeakHashMap<Player, Integer>();
	private HashMap<Entity, Player> _arrows = new HashMap<Entity, Player>();

	public Sharpshooter(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Consecutive arrow hits deal an",
				"additional #1#0.5 damage.",
				"",
				"Stacks up to #1#1 times",
				"",
				"Missing an arrow resets the bonus.",
				});
	}

	@EventHandler
	public void ShootBow(EntityShootBowEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;

		int level = getLevel((Player)event.getEntity());
		if (level == 0)		return;

		//Store
		_arrows.put(event.getProjectile(), (Player)event.getEntity());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.PROJECTILE)
			return;

		Projectile projectile = event.GetProjectile();
		if (projectile == null)	return;

		if (!_arrows.containsKey(projectile))
			return;

		Player player = _arrows.remove(projectile);
		int level = getLevel(player);
	
		if (event.GetDamagerEntity(true) != null && event.GetDamagerEntity(true).equals(event.GetDamageeEntity()))
			return;
		
		if (_hitCount.containsKey(player))
		{
			//Damage
			event.AddMod(player.getName(), GetName(), _hitCount.get(player) * (1 + 0.5 * level), true);

			int limit = Math.min(1 + level, _hitCount.get(player) + 1);

			_hitCount.put(player, limit);
			
			//Inform
			UtilPlayer.message((Entity)projectile.getShooter(), F.main(GetClassType().name(), GetName() + ": " + 
					F.elem(_hitCount.get(player) + " Consecutive Hits") + C.cGray + " (" + F.skill("+"+ (1 + 0.5 * limit) + "Damage" ) + C.cGray + ")" ) );
		}
		else
		{
			_hitCount.put(player, 1);
		}	

		projectile.remove();
	}
	
	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		HashSet<Entity> remove = new HashSet<Entity>();

		for (Entity cur : _arrows.keySet())
			if (cur.isDead() || !cur.isValid() || cur.isOnGround())
				remove.add(cur);

		for (Entity cur : remove)
		{
			Player player = _arrows.remove(cur);

			if (player != null)
				if (_hitCount.remove(player) != null)
					UtilPlayer.message(player, F.main(GetClassType().name(), GetName() + ": " + F.elem("0 Consecutive Hits")));
		}		
	}

	@Override
	public void Reset(Player player) 
	{
		_hitCount.remove(player);
	}
}
