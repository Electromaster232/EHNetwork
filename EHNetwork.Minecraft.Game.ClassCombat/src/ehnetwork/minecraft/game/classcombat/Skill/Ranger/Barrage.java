package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillChargeBow;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;

public class Barrage extends SkillChargeBow
{
	private WeakHashMap<Player, Integer> _chargeArrows = new WeakHashMap<Player, Integer>();
	private HashSet<Projectile> _arrows = new HashSet<Projectile>();

	public Barrage(SkillFactory skills, String name, IPvpClass.ClassType classType,
			SkillType skillType, int cost, int maxLevel)
	{
		super(skills, name, classType, skillType, cost, maxLevel, 
				0.012f, 0.006f, false, true);
	
		SetDesc(new String[] 
				{
				"Charge your bow fire bonus arrows.",
				"",
				GetChargeString(),
				"",
				"Fires up to #2#2 additional arrows."
				});
	}
	
	@Override
	public void DoSkillCustom(Player player, float charge, Arrow arrow)
	{
		_chargeArrows.put(player, (int)(charge * (2 + 2* getLevel(player))));
	}

	@EventHandler
	public void Skill(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player cur : GetUsers())
		{
			if (!_chargeArrows.containsKey(cur))
				continue;

			if (!UtilGear.isBow(cur.getItemInHand()))
			{
				_chargeArrows.remove(cur);
				continue;
			}

			int arrows = _chargeArrows.get(cur);
			if (arrows <= 0)
			{
				_chargeArrows.remove(cur);
				continue;
			}

			_chargeArrows.put(cur, arrows-1);

			//Fire Arrow
			Vector random = new Vector((Math.random()-0.5)/10, (Math.random()-0.5)/10, (Math.random()-0.5)/10);
			Projectile arrow = cur.launchProjectile(Arrow.class);
			arrow.setVelocity(cur.getLocation().getDirection().add(random).multiply(3));
			_arrows.add(arrow);
			cur.getWorld().playSound(cur.getLocation(), Sound.SHOOT_ARROW, 1f, 1f);
			
			UtilParticle.PlayParticle(ParticleType.CRIT, arrow.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
		}
	}

	@EventHandler
	public void ProjectileHit(ProjectileHitEvent event)
	{
		if (_arrows.remove(event.getEntity()))
			event.getEntity().remove();
	}

	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;
		
		for (Iterator<Projectile> arrowIterator = _arrows.iterator(); arrowIterator.hasNext();) 
		{
			Projectile arrow = arrowIterator.next();
			
			if (arrow.isDead() || !arrow.isValid())
				arrowIterator.remove();
		}
	}
	
	@Override
	public void Reset(Player player) 
	{
		_charge.remove(player);
		_chargeArrows.remove(player);
	}
}
