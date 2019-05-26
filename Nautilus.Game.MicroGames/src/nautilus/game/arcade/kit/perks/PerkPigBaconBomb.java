package nautilus.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.disguise.disguises.DisguiseBase;
import mineplex.core.disguise.disguises.DisguisePigZombie;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkPigBaconBomb extends SmashPerk
{
	private WeakHashMap<Player, HashSet<Pig>> _pigs = new WeakHashMap<Player, HashSet<Pig>>();
	
	public PerkPigBaconBomb() 
	{
		super("Baby Bacon Bomb", new String[] 
				{ 
				C.cYellow + "Right-Click" + C.cGray + " with Spade to " + C.cGreen + "Baby Bacon Bomb"
				});
	}
	
	
	@EventHandler
	public void Skill(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("_SPADE"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;
		
		float energy = 0.40f;
		
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise != null && disguise instanceof DisguisePigZombie)
			energy = energy * 0.7f;

		//Energy
		if (player.getExp() < energy)
		{
			UtilPlayer.message(player, F.main("Energy", "Not enough Energy to use " + F.skill(GetName()) + "."));
			return;
		}
			
		//Recharge
		if (!Recharge.Instance.use(player, GetName(), 100, false, false))
			return;
	
		//Use Energy
		player.setExp(Math.max(0f, player.getExp() - energy));
		
		//Velocity
		UtilAction.velocity(player, player.getLocation().getDirection(), 0.8, true, 0.9, 0, 1, true);	
		
		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.PIG_IDLE, 2f, 0.75f);
		
		//Pig
		Manager.GetGame().CreatureAllowOverride = true;
		Pig pig = player.getWorld().spawn(player.getLocation(), Pig.class);
		pig.setHealth(5);
		pig.setVelocity(new Vector(0, -0.4, 0));
		Manager.GetGame().CreatureAllowOverride = false;
		
		pig.setBaby();
		UtilEnt.Vegetate(pig);
		UtilEnt.ghost(pig, true, false);
		
		//Store
		if (!_pigs.containsKey(player))
			_pigs.put(player, new HashSet<Pig>());
		
		_pigs.get(player).add(pig);
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void Check(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
	
		for (Player player : _pigs.keySet())
		{
			Iterator<Pig> pigIterator = _pigs.get(player).iterator();
			
			while (pigIterator.hasNext())
			{
				Pig pig = pigIterator.next();
				
				if (!pig.isValid() || pig.getTicksLived() > 80)
				{
					PigExplode(pigIterator, pig, player);
					continue;
				}
				
				Player target = UtilPlayer.getClosest(pig.getLocation(), player);	
				if (target == null)
					continue;
				
				UtilEnt.CreatureMoveFast(pig, target.getLocation(), 1.2f);
				
				if (UtilMath.offset(target, pig) < 2)
					PigExplode(pigIterator, pig, player);
			}
		}
	}	
	
	public void PigExplode(Iterator<Pig> pigIterator, Pig pig, Player owner)
	{
		//Effect
		UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, pig.getLocation().add(0, 0.5, 0), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		//Sound
		pig.getWorld().playSound(pig.getLocation(), Sound.EXPLODE, 0.6f, 2f);
		pig.getWorld().playSound(pig.getLocation(), Sound.PIG_DEATH, 1f, 2f);
		
		//Damage
		HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(pig.getLocation(), 4);
		for (LivingEntity cur : targets.keySet())
		{
			if (cur.equals(owner))
				continue;

			//Damage Event
			Manager.GetDamage().NewDamageEvent(cur, owner, null, 
					DamageCause.CUSTOM, 4 * targets.get(cur) + 2, false, true, false,
					owner.getName(), GetName());	
		}
		
		//Remove
		pigIterator.remove();
		pig.remove();
	}
}
