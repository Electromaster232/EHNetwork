package ehnetwork.game.arcade.game.games.sneakyassassins.npc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.sneakyassassins.SneakyAssassins;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;

public class NpcManager implements Listener
{
	private final SneakyAssassins _sneakyAssassins;
	private final Random _random;
	private EntityType _disguiseType = EntityType.VILLAGER;
	
	private NautHashMap<Entity, BribedData> _bribed = new NautHashMap<Entity, BribedData>();
	
	public NpcManager(SneakyAssassins sneakyAssassins, Random random)
	{
		_sneakyAssassins = sneakyAssassins;
		_random = random;

		getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
	}

	public SneakyAssassins getGame()
	{
		return _sneakyAssassins;
	}

	public Collection<? extends LivingEntity> getNpcs()
	{
		return (Collection<? extends LivingEntity>) getGame().WorldData.World.getEntitiesByClass(getDisguiseType().getEntityClass());
	}

	public Entity spawnNpc()
	{
		Location spawn = getNpcSpawn();

		getGame().CreatureAllowOverride = true;
		LivingEntity npc = (LivingEntity) spawn.getWorld().spawn(spawn, getDisguiseType().getEntityClass());
		npc.setCanPickupItems(false);
		npc.setRemoveWhenFarAway(false);
		UtilEnt.Vegetate(npc);
		getGame().CreatureAllowOverride = false;

		return npc;
	}

	public Location getNpcSpawn()
	{
		return UtilAlg.Random(getGame().GetTeamList().get(0).GetSpawns());
	}

	@EventHandler
	public void onBustleNpcs(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (getGame().GetState() == Game.GameState.Live || getGame().GetState() == Game.GameState.Prepare)
			move();
	}

	@EventHandler
	public void onKillNpc(UpdateEvent event)
	{
		if (event.getType() == UpdateType.SLOW && getGame().GetState() == Game.GameState.Live)
		{
			List<LivingEntity> npcs = new ArrayList<>(getNpcs());

			if (!npcs.isEmpty())
			{
				LivingEntity npc = UtilAlg.Random(npcs);

				UtilParticle.PlayParticle(UtilParticle.ParticleType.LARGE_SMOKE, npc.getLocation(), 0f, 0f, 0f, 0, 1,
						ViewDist.MAX, UtilServer.getPlayers());
				npc.remove();
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		event.setDroppedExp(0);
		event.getDrops().clear();
		
		_bribed.remove(event.getEntity());
	}

	public void move()
	{
		for (Entity npc : getNpcs())
		{
			if (_bribed.containsKey(npc))
			{
				if (bribedLogic(npc))
					continue;
			}
			
			if (getRandom().nextInt(40) == 0)
			{
				List<Location> nearby = new ArrayList<>();

				for (Location location : getGame().GetTeamList().get(0).GetSpawns())
				{
					if (Math.abs(npc.getLocation().getX() - location.getX()) <= 16 && Math.abs(npc.getLocation().getZ() - location.getZ()) <= 16)
						nearby.add(location);
				}

				if (!nearby.isEmpty())
				{
					Location location = UtilAlg.Random(nearby);

					for (int i = 0; i < 5; i++)
					{
						int dx = getRandom().nextInt(5) - 2;
						int dz = getRandom().nextInt(5) - 2;

						Location candidate = location.clone().add(dx, 0, dz);
						Block block = candidate.getBlock();

						if (!block.getRelative(BlockFace.DOWN).isEmpty() &&
								!block.getRelative(BlockFace.DOWN).isLiquid() &&
								block.isEmpty() &&
								block.getRelative(BlockFace.UP).isEmpty())
						{
							location = candidate;

							break;
						}
					}

					UtilEnt.CreatureMove(npc, location, 0.7f);
				}
			}
		}
	}

	private boolean bribedLogic(Entity npc)
	{
		if (!(npc instanceof LivingEntity))
			return false;
		
		BribedData data = _bribed.get(npc);
		if (data == null)
			return false;
		
		Player bestTarget = null;
		double bestDist = 0;
		
		for (Player other : _sneakyAssassins.GetPlayers(true))
		{
			if (data.Player.equals(other))
				continue;
			
			if (_sneakyAssassins.Manager.GetCondition().HasCondition(other, ConditionType.CLOAK, null))
				continue;
			
			double dist = UtilMath.offset(npc, other);
			
			if (bestTarget == null || dist < bestDist)
			{
				bestTarget = other;
				bestDist = dist;
			}
		}
		
		if (bestTarget == null)
			return false;
		
		//Move
		UtilEnt.CreatureMove(npc, bestTarget.getLocation().add(UtilAlg.getTrajectory(bestTarget, npc)), 0.8f);
		
		//Stuck
		if (data.LastLocation == null)
			data.LastLocation = npc.getLocation();
		
		if (UtilMath.offset(npc.getLocation(), data.LastLocation) > 1.5)
		{
			data.LastLocation = npc.getLocation();
			data.LastTime = System.currentTimeMillis();
		}
		else
		{
			if (UtilTime.elapsed(data.LastTime, 800))
			{
				UtilAction.velocity(npc, 0.3, 0.3, 0.7, true);
			}
		}
		
		if (UtilMath.offset(npc, bestTarget) < 1.5)
		{
			//Damage Event
			_sneakyAssassins.Manager.GetDamage().NewDamageEvent(bestTarget, (LivingEntity)npc, null, 
					DamageCause.CUSTOM, 1, true, false, false,
					data.Player.getName(), data.Player.getName() + "'s Bribed Villager");	
			
			npc.getWorld().playSound(npc.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
		}
		
		return true;
	}

	public Random getRandom()
	{
		return _random;
	}

	public Plugin getPlugin()
	{
		return getGame().Manager.getPlugin();
	}

	public EntityType getDisguiseType()
	{
		return _disguiseType;
	}

	public void setDisguiseType(EntityType disguiseType)
	{
		_disguiseType = disguiseType;
	}
	
	public void setBribed(Entity ent, Player player)
	{
		_bribed.put(ent, new BribedData(player));
	}
}
