package ehnetwork.game.arcade.game.games.tug;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.game.arcade.game.Game;

public class TugCreature 
{
	public Game Host;
	
	public Creature Entity;
	public LivingEntity Target;
	public Block Destination;
	
	public long lastAttack = 0;
	
	public TugCreature(Game host, Creature ent, ArrayList<Block> lives)
	{
		Host = host;
		
		Entity = ent;
		Move(lives);
	}

	public void Move(ArrayList<Block> lives) 
	{
		if (Destination == null || !lives.contains(Destination))
		{
			Destination = UtilAlg.Random(lives);
		}
		
		if (Target != null)
		{
			if (UtilMath.offset(Entity, Target) < 2 && UtilTime.elapsed(lastAttack, 400))
			{
				//Damage Event
				Host.Manager.GetDamage().NewDamageEvent(Target, Entity, null, 
						DamageCause.ENTITY_ATTACK, 3, true, false, false,
						UtilEnt.getName(Entity), "Headbutt");
				
				lastAttack = System.currentTimeMillis();
			}
			
			if (UtilMath.offset(Entity, Target) > 1)
			{
				UtilEnt.CreatureMoveFast(Entity, Target.getLocation().add(UtilAlg.getTrajectory2d(Target, Entity).multiply(1.5)), 1.5f, false);
			}
		}
		else
		{
			Location loc = Destination.getLocation().add(0.5, 0, 0.5);
			
			UtilEnt.CreatureMoveFast(Entity, loc.add(UtilAlg.getTrajectory2d(loc, Entity.getLocation()).multiply(0.5)), 1.5f, false);
		}
	}

	public void FindTarget(ArrayList<TugCreature> mobs, ArrayList<Player> enemyPlayers) 
	{
		if (Target == null || !Target.isValid() || UtilMath.offset(Target, Entity) > 6)
		{
			Target = null;
			
			double bestDist = 0;
			
			for (TugCreature mob : mobs)
			{
				double dist = UtilMath.offset(mob.Entity, Entity);
				
				if (Target == null || dist < bestDist)
				{
					Target = mob.Entity;
					bestDist = dist;
				}
			}
			
			/*
			for (Player player : enemyPlayers)
			{
				double dist = UtilMath.offset(player, Entity);
				
				if (Target == null || dist < bestDist)
				{
					Target = player;
					bestDist = dist;
				}
			}
			*/
			
			if (Target == null || !Target.isValid() || UtilMath.offset(Target, Entity) > 4)
			{
				Target = null;
			}
		}
	}
}
