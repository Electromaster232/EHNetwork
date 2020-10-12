package ehnetwork.minecraft.game.classcombat.Skill.Assassin;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;

public class Recall extends Skill
{
	private HashMap<Player, LinkedList<Location>> _locMap = new HashMap<Player, LinkedList<Location>>();
	
	public Recall(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Drop Axe/Sword to Use.",
				"",
				"Instantly teleport back to where",
				"you were #2#2 seconds ago.",
				});
	}

	@Override
	public String GetRechargeString()
	{
		return "Recharge: #60#-10 Seconds";
	}
	
	@EventHandler
	public void Use(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();

		int level = getLevel(player);
		if (level == 0)		
			return;
		
		if (!UtilGear.isWeapon(event.getItemDrop().getItemStack()))
			return;

		event.setCancelled(true);
		
		//Check Allowed
		SkillTriggerEvent trigger = new SkillTriggerEvent(player, GetName(), GetClassType());
		Bukkit.getServer().getPluginManager().callEvent(trigger);
		
		if (trigger.IsCancelled())
			return;

		if (!Recharge.Instance.use(player, GetName(), GetName(level), 60000 - (level * 10000), true, false))
			return;

		LinkedList<Location> locs = _locMap.remove(player);
		if (locs == null)
			return;
		
		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
		
		Location current = player.getLocation();
		Location target = locs.getLast();
		
		player.teleport(target);
		
		//Inform
		UtilPlayer.message(player, F.main(GetClassType().name(), "You used " + F.skill(GetName(level)) + "."));
		
		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
		
		while (UtilMath.offset(current, target) > 0.5)
		{
			UtilParticle.PlayParticle(ParticleType.WITCH_MAGIC, current, 0, 1f, 0, 0, 1,
					ViewDist.LONGER, UtilServer.getPlayers());
			current = current.add(UtilAlg.getTrajectory(current, target).multiply(0.1));
		}
	}

	@EventHandler
	public void StoreLocation(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : GetUsers())
		{
			if (!_locMap.containsKey(cur))
				_locMap.put(cur, new LinkedList<Location>());
			
			_locMap.get(cur).addFirst(cur.getLocation());
			
			int level = getLevel(cur);
			if (_locMap.get(cur).size() > (2 + 2 * level) * 20)
				_locMap.get(cur).removeLast();
		}
	}

	@Override
	public void Reset(Player player) 
	{
		_locMap.remove(player);
	}
}
