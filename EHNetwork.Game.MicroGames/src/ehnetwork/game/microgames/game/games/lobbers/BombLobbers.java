package ehnetwork.game.microgames.game.games.lobbers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.explosion.ExplosionEvent;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.TeamGame;
import ehnetwork.game.microgames.game.games.lobbers.events.TNTPreExplodeEvent;
import ehnetwork.game.microgames.game.games.lobbers.events.TNTThrowEvent;
import ehnetwork.game.microgames.game.games.lobbers.kits.KitArmorer;
import ehnetwork.game.microgames.game.games.lobbers.kits.KitJumper;
import ehnetwork.game.microgames.game.games.lobbers.kits.KitPitcher;
import ehnetwork.game.microgames.game.games.lobbers.kits.KitWaller;
import ehnetwork.game.microgames.game.games.lobbers.trackers.Tracker6Kill;
import ehnetwork.game.microgames.game.games.lobbers.trackers.TrackerBlastProof;
import ehnetwork.game.microgames.game.games.lobbers.trackers.TrackerDirectHit;
import ehnetwork.game.microgames.game.games.lobbers.trackers.TrackerNoDamage;
import ehnetwork.game.microgames.game.games.lobbers.trackers.TrackerTNTThrown;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.minecraft.game.core.combat.CombatComponent;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class BombLobbers extends TeamGame implements IThrown
{
	/**
	 * @author Mysticate
	 * Created in July, 2015
	 */
	
	private NautHashMap<GameTeam, Location> _averageSpawns = new NautHashMap<GameTeam, Location>();
	
	private NautHashMap<TNTPrimed, BombToken> _tnt = new NautHashMap<TNTPrimed, BombToken>();
	
	private NautHashMap<Player, Double> _kills = new NautHashMap<Player, Double>();
		
	@SuppressWarnings("unchecked")
	public BombLobbers(MicroGamesManager manager)
	{
		super(manager, GameType.Lobbers, new Kit[]
				{
				new KitJumper(manager),
				new KitArmorer(manager),
				new KitPitcher(manager),
				new KitWaller(manager)
				}, new String[]
						{
				"Fight against your enemies using",
				"the power of explosives!",
				"Left click TNT to throw at your enemy.",
				"Last team alive wins!"
						});
		DamageFall = true;
		DamageEvP = true;
		
		WorldWaterDamage = 5;
		
		PrepareFreeze = false;
		
		TeamArmor = true;
		TeamArmorHotbar = true;
		
		InventoryOpenChest = false;
		InventoryOpenBlock = false;
		
		ItemDrop = false;
		
		BlockPlace = false;
				
		Manager.GetExplosion().SetLiquidDamage(false);
		
		HungerSet = 20;
		
		WorldTimeSet = 6000;
		
		registerStatTrackers(new Tracker6Kill(this), new TrackerBlastProof(this), new TrackerNoDamage(this), new TrackerTNTThrown(this), new TrackerDirectHit(this));
	}
	
	@EventHandler
	public void setTime(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Prepare)
			return;
		
		if (WorldData.MapName.equalsIgnoreCase("Intergalactic"))
		{
			WorldTimeSet = 18000;
		}
	}
	
	public void addKill(Player player)
	{
		_kills.put(player, _kills.containsKey(player) ? _kills.get(player) + 1 : 1);
	}
	
	public void addAssist(Player player)
	{
		_kills.put(player, _kills.containsKey(player) ? _kills.get(player) + .5 : .5);
	}
	
	public double getKills(Player player)
	{
		if (_kills.containsKey(player))
		{
			return _kills.get(player);
		}
		else
		{
			_kills.put(player, 0.0);
			return 0;
		}
	}
	
	@EventHandler
	public void onKill(CombatDeathEvent event)
	{
		if (!IsLive())
			return;
		
		Player dead = UtilPlayer.searchExact(((Player) event.GetEvent().getEntity()).getName());
		
		if (!IsAlive(dead))
			return;
		
		for (CombatComponent damager : event.GetLog().GetAttackers())
		{
			Player killer = UtilPlayer.searchExact(damager.GetName());
			
			if (killer == null || !killer.isOnline())
				continue;
			
			if (IsAlive(killer))
			{
				if (event.GetLog().GetKiller() == damager)
				{				
					addKill(killer);
				}
				else
				{
					addAssist(killer);
				}
			}
		}
	}
	
	@EventHandler
	public void loadTeamLocations(GameStateChangeEvent event)
	{
		if (event.GetState() == GameState.Live)
		{
			for (GameTeam team : _teamList)
			{
				_averageSpawns.put(team, UtilAlg.getAverageLocation(team.GetSpawns()));
			}
		}
	}
	
	@EventHandler
	public void disableFlying(GameStateChangeEvent event)
	{
		for (Player player : GetPlayers(true))
		{
			player.setAllowFlight(false);
		}
	}
	
	@EventHandler
	public void throwTNT(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.PHYSICAL)
			return;
		
		if (!IsLive())
			return;

		Player player = event.getPlayer();

		if (!IsAlive(player))
			return;

		if (!UtilInv.IsItem(player.getItemInHand(), Material.TNT, (byte) 0))
			return;
				
		event.setCancelled(true);

		UtilInv.remove(player, Material.TNT, (byte) 0, 1);
		UtilInv.Update(player);

		TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);
		tnt.setMetadata("owner", new FixedMetadataValue(Manager.getPlugin(), player.getUniqueId()));
		tnt.setFuseTicks(60);

		UtilAction.velocity(tnt, player.getLocation().getDirection(), 2.0D, false, 0.0D, 0.1D, 10.0D, false);
		Manager.GetProjectile().AddThrow(tnt, player, this, -1L, true, false, true, .2F);
				
		Manager.getPlugin().getServer().getPluginManager().callEvent(new TNTThrowEvent(player, tnt));

		_tnt.put(tnt, new BombToken(player));
	}
	
	public Player getThrower(TNTPrimed tnt)
	{
		if (_tnt.get(tnt) == null)
			return null;
			
		return UtilPlayer.searchExact(_tnt.get(tnt).Thrower);
	}
	
	@EventHandler
	public void onTNTExplode(ExplosionPrimeEvent event)
	{
		if (!IsLive())
			return;

		if (!(event.getEntity() instanceof TNTPrimed))
			return;

		TNTPrimed tnt = (TNTPrimed) event.getEntity();
				
		if (!_tnt.containsKey(tnt))
			return;

		Player thrower = UtilPlayer.searchExact(_tnt.get(tnt).Thrower);
				
		if (thrower == null)
		{
			event.setCancelled(true);

			_tnt.remove(tnt);

			event.getEntity().remove();
			return;
		}
		
		if (GetTeam(thrower) == getSide(tnt.getLocation()))
		{
			event.setCancelled(true);

			_tnt.remove(tnt);
			
			event.getEntity().remove();
			
			return;
		}
		
		TNTPreExplodeEvent preExplode = new TNTPreExplodeEvent(thrower, tnt);
		Manager.getPlugin().getServer().getPluginManager().callEvent(preExplode);
		
		if (preExplode.isCancelled())
		{
			event.setCancelled(true);
			tnt.remove();
		}
		else
		{
			for (Player other : UtilPlayer.getNearby(event.getEntity().getLocation(), 14))
			{
				Manager.GetCondition().Factory().Explosion("Throwing TNT", other, thrower, 50, 0.1, false, false);
			}
		}
		
		_tnt.remove(tnt);
	}
	
	@EventHandler
	public void updateTNT(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		NautHashMap<TNTPrimed, BombToken> toAdd = new NautHashMap<TNTPrimed, BombToken>();
		
		Iterator<Entry<TNTPrimed, BombToken>> iterator = _tnt.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<TNTPrimed, BombToken> tnt = iterator.next();
			
			if (tnt.getKey() == null)
				continue;
			
			if (!tnt.getKey().isValid())
				continue;
			
			BombToken token = tnt.getValue();
			Player thrower = UtilPlayer.searchExact(token.Thrower);
			
			if (thrower == null)
				continue;
			
			if (!IsPlaying(thrower))
				continue;
			
			if (!token.Primed)
			{
				if (tnt.getKey().getFuseTicks() <= 20)
				{
					//Respawn
					TNTPrimed newTNT = tnt.getKey().getWorld().spawn(tnt.getKey().getLocation(), TNTPrimed.class);
					newTNT.setMetadata("owner", new FixedMetadataValue(Manager.getPlugin(), thrower.getUniqueId()));
					newTNT.setVelocity(tnt.getKey().getVelocity());
					newTNT.setFuseTicks(60);

					Manager.GetProjectile().AddThrow(newTNT, thrower, this, -1L, true, false, true, .2F);
					
					tnt.getKey().remove();
					
					iterator.remove();
					toAdd.put(newTNT, token);
				}
			}
		}
		
		//Prevent concurrent modification thigns
		for (Entry<TNTPrimed, BombToken> entry : toAdd.entrySet())
		{
			_tnt.put(entry.getKey(), entry.getValue());
		}
	}
	
	@EventHandler
	public void blockCollision(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		for (Entry<TNTPrimed, BombToken> tnt : _tnt.entrySet())
		{
			if (tnt.getKey() == null)
				continue;
			
			if (!tnt.getKey().isValid())
				continue;
			
			BombToken token = tnt.getValue();
			Player thrower = UtilPlayer.searchExact(token.Thrower);
			
			if (thrower == null)
				continue;
			
			if (!IsPlaying(thrower))
				continue;
			
			if (!token.Primed)
			{
				//8+ insta explode
				if (UtilTime.elapsed(token.Created, 8000))
				{
					token.Primed = true;
					tnt.getKey().setFuseTicks(0);
					continue;
				}
				else if (UtilTime.elapsed(token.Created, 3000))
				{
					for (Block block : UtilBlock.getSurrounding(tnt.getKey().getLocation().getBlock(), true))
					{
						if (block.getType() != Material.AIR)
						{
							token.Primed = true;
							tnt.getKey().setFuseTicks(0);
							break;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void updateParticles(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		for (Entry<TNTPrimed, BombToken> tnt : _tnt.entrySet())
		{
			if (tnt.getKey() == null)
				continue;
			
			if (!tnt.getKey().isValid())
				continue;
			
			if (UtilEnt.isGrounded(tnt.getKey()) || tnt.getKey().isOnGround())
				continue;
			
			BombToken token = tnt.getValue();
			Player thrower = UtilPlayer.searchExact(token.Thrower);
			
			if (thrower == null)
				continue;
			
			if (!IsPlaying(thrower))
				continue;
			
			GameTeam team = GetTeam(thrower);
			
			if (team == null)
				continue;
			
			//A is current
			//B is previous
			token.B = token.A;
			token.A = tnt.getKey().getLocation();

			if (token.A == null || token.B == null)
				continue;
						
			//Adapted from static lazer code
			double curRange = 0;
			double distance = Math.abs(token.A.distance(token.B));
						
			while (curRange <= distance)
			{
				Location newTarget = token.B.add(UtilAlg.getTrajectory(token.B, token.A).multiply(curRange));
//				Location newTarget = player.getEyeLocation().add(player.getLocation().getDirection().multiply(curRange));
								
				//Progress Forwards
				curRange += 0.2;
				
				if (team.GetColor() == ChatColor.AQUA)
				{
					for (int i = 0 ; i < 2 ; i++)
						UtilParticle.PlayParticle(ParticleType.RED_DUST, newTarget.clone().add(0.0, 0.5, 0.0), -1, 1, 1, 1, 0,
								ViewDist.NORMAL, UtilServer.getPlayers());
				}
				else
				{
					for (int i = 0 ; i < 2 ; i++)
						UtilParticle.PlayParticle(ParticleType.RED_DUST, newTarget.clone().add(0.0, 0.5, 0.0), 0, 0, 0, 0, 1,
								ViewDist.NORMAL, UtilServer.getPlayers());
				}

				//UtilParticle.PlayParticle(ParticleType.RED_DUST, newTarget.clone().add(0.0, 0.6, 0.0), team.GetColorBase().getRed(), team.GetColorBase().getGreen(), team.GetColorBase().getBlue(), 1, 0, ViewDist.LONG, UtilServer.getPlayers());
			}
		}
	}
	
	@EventHandler
	public void preventCheating(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		for (Player player : GetPlayers(true))
		{
			if (GetTeam(player) != getSide(player.getLocation()))
			{
				player.damage(500);
				UtilPlayer.message(player, F.main("Game", "You were killed for trying to cheat!"));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void damageBlocks(ExplosionEvent event)
	{
		if (!Manager.GetGame().IsLive())
			return;

		Iterator<Block> iterator = event.GetBlocks().iterator();

		while (iterator.hasNext())
		{
			Block block = iterator.next();
			
			//Stone
			if (block.getType() == Material.STONE)
			{
				block.setType(Material.COBBLESTONE);
				iterator.remove();
				continue;
			}

			//Stone Brick
			if (block.getType() == Material.SMOOTH_BRICK && block.getData() != 2)
			{
				block.setData((byte) 2);
				iterator.remove();
				continue;
			}
		}
	}
	
	private GameTeam getSide(Location entityLoc)
	{		
		Location nearest = UtilAlg.findClosest(entityLoc, new ArrayList<Location>(_averageSpawns.values()));
		for (Entry<GameTeam, Location> entry : _averageSpawns.entrySet())
		{
			if (entry.getValue().equals(nearest))
				return entry.getKey();
		}
		return null;
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (!(data.GetThrown() instanceof TNTPrimed))
			return;
				
		if (!(data.GetThrower() instanceof Player))
			return;

		if (!(target instanceof Player))
			return;

		if (GetTeam((Player) target) == GetTeam((Player) data.GetThrower()))
			return;
			
		data.GetThrown().setVelocity(new Vector());
		
		if (target != null)
			UtilAction.velocity(target, UtilAlg.getTrajectory2d(data.GetThrown().getLocation(), target.getLocation()), .2, false, 0, 0.2, .4, true);
		
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, DamageCause.PROJECTILE, 10.0, false, false, false, "Throwing TNT", "Throwing TNT Direct Hit");
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		
	}
	
	public void knockbackIncrease(CustomDamageEvent event)
	{
		if (event.GetCause() != DamageCause.ENTITY_EXPLOSION && event.GetCause() != DamageCause.BLOCK_EXPLOSION)
			return;

		event.AddKnockback("Explosion", 1.7D);
	}
}
