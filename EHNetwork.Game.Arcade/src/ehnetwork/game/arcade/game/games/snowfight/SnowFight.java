package ehnetwork.game.arcade.game.games.snowfight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.common.util.UtilWorld;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.snowfight.kits.KitMedic;
import ehnetwork.game.arcade.game.games.snowfight.kits.KitSportsman;
import ehnetwork.game.arcade.game.games.snowfight.kits.KitTactician;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;
import ehnetwork.minecraft.game.core.explosion.CustomExplosion;

public class SnowFight extends TeamGame
{

	private HashMap<Player, Integer> _tiles;
	private boolean _meteoroids;
	private boolean _peace;

	public SnowFight(ArcadeManager manager)
	{
		super(manager, GameType.SnowFight,

					new Kit[]
						{
					new KitSportsman(manager),
					new KitTactician(manager),
					new KitMedic(manager)
						},

						new String[]
								{
					"Just like... kill your enemies. with snow.",
					"Be careful if you are on Ice your body will freeze"
								});

		this.HungerSet = 20;
		this.CompassEnabled = true;
		this.CompassGiveItem = false;
		this.TeamArmor = true;
		this.TeamArmorHotbar = true;
		this.BlockPlace = true;
		this.BlockBreakAllow = new HashSet<>(Arrays.asList(Material.FENCE.getId()));
		this._tiles = new HashMap<Player, Integer>();
		this._meteoroids = false;
		this._peace = false;
	}

	@EventHandler
	public void GameState(GameStateChangeEvent event)
	{
		if(event.GetState() != GameState.Live)
			return;
		
		this.Announce(C.cRed + C.Bold + "ALERT: " + ChatColor.RESET + C.Bold + "15 seconds Peace Phase is starting!");
	}
	
	@EventHandler
	public void Weather(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		World world = UtilWorld.getWorldType(Environment.NORMAL);

		if (world == null)
			return;

		world.setStorm(true);
		world.setThundering(false);
		world.setWeatherDuration(40);
		world.setTime(4000);
	}
	
	@EventHandler
	public void IceDamage(UpdateEvent event) {
		if(event.getType() != UpdateType.FAST)
			return;
		
		if(!IsLive())
			return;
		
		for(Player player : GetPlayers(true)) 
		{
			if(IsOnIce(player))
			{
				Bukkit.getPluginManager().callEvent(new CustomDamageEvent(player, null, null, DamageCause.CUSTOM, 2.0D, false, true, true, "Ice", "Ice", false));
			}
		}
		
	}
	
	@EventHandler
	public void Meteor(UpdateEvent event) {
		if(event.getType() != UpdateType.TWOSEC)
			return;
		
		if(!IsLive()) 
			return;
		
		if(System.currentTimeMillis() <= getGameLiveTime() + (15 * 1000))
			return;
		
		if(!_peace)
		{
			for(Player player : GetPlayers(false))
			{
				player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
				UtilTextMiddle.display(C.cGold + C.Bold + "Peace Phase ended", "Kill your enemies", player);
			}
			this.Announce(C.cRed + C.Bold + "ALERT: " + ChatColor.RESET + C.Bold + "Peace Phase ended");
			_peace = true;
		}
		
		if(System.currentTimeMillis() <= getGameLiveTime() + (195 * 1000))
			return;
		
		if(!_meteoroids)
		{
			for(Player player : GetPlayers(false))
			{
				player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
				UtilTextMiddle.display(C.cGold + C.Bold + "ICE METEOROIDS!!!", "Avoid the Ice Fields", player);
			}
			this.Announce(C.cRed + C.Bold + "ALERT: " + ChatColor.RESET + C.Bold + "METEOROIDS START FALLING!");
			_meteoroids = true;
		}
		makeMeteor();
	}

	@EventHandler
	public void BlockDamage(BlockDamageEvent event)
	{
		Player player = event.getPlayer();

		if (!IsLive())
			return;

		if (!IsPlaying(player))
			return;

		if (!IsSnow(event.getBlock()))
			return;

		if (UtilInv.contains(player, Material.SNOW_BALL, (byte) 0, 16))
			return;

		// Item

		if (!_tiles.containsKey(player))
		{
			_tiles.put(player, 0);
		}

		if (GetKit(player) instanceof KitSportsman)
			UtilInv.insert(player, new ItemStack(Material.SNOW_BALL));


		int tiles = _tiles.get(player);
		_tiles.put(player, tiles + 1);

		if (!(GetKit(player) instanceof KitSportsman))
		{
			if (_tiles.get(player) == 2)
			{
				UtilInv.insert(player, new ItemStack(Material.SNOW_BALL));
				_tiles.put(player, 0);
			}
		}

		// Snow Height
		SnowDecrease(event.getBlock(), 1);

		// Effect
		event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, 80);
	}

	// @EventHandler
	// public void InteractSnowball(PlayerInteractEvent event)
	// {
	// Player player = event.getPlayer();
	//
	// if (!IsLive())
	// return;
	//
	// if (!IsPlaying(player))
	// return;
	//
	// if (!UtilGear.isMat(player.getItemInHand(), Material.SNOW_BALL))
	// return;
	//
	// event.setCancelled(true);
	//
	// if (UtilEvent.isAction(event, ActionType.L))
	// SnowballThrow(player);
	//
	// else if (UtilEvent.isAction(event, ActionType.R_BLOCK))
	// SnowballPlace(player, event.getClickedBlock(), 1);
	// }
	//
	// private void SnowballPlace(Player player, Block block, int above)
	// {
	// if (block.getTypeId() == 78 || UtilBlock.airFoliage(block))
	// {
	// //Build
	// if (block.getTypeId() == 78)
	// {
	// block.setTypeIdAndData(78, (byte)(block.getData() + 1), true);
	//
	// if (block.getData() >= 7)
	// block.setTypeIdAndData(80, (byte)0, true);
	// }
	// else
	// {
	// block.setTypeIdAndData(78, (byte)0, true);
	// }
	//
	// //Sound
	// block.getWorld().playSound(block.getLocation(), Sound.STEP_SNOW, 1f,
	// 0.6f);
	//
	// //Use Snow
	// SnowballCount(player, -1);
	// }
	// else if ((IsSnow(block) || UtilBlock.solid(block)) && above > 0)
	// {
	// SnowballPlace(player, block.getRelative(BlockFace.UP), above - 1);
	// }
	// }
	//
	// private void SnowballThrow(Player player)
	// {
	// //Throw
	// player.launchProjectile(Snowball.class);
	//
	// //Use Snow
	// SnowballCount(player, -1);
	//
	// //Sound
	// player.getWorld().playSound(player.getLocation(), Sound.STEP_SNOW, 3f,
	// 1.5f);
	// }

	// private void SnowballCount(Player player, int count)
	// {
	// if (player.getInventory().getItem(1) != null)
	// count += player.getInventory().getItem(1).getAmount();
	//
	// if (count > 16)
	// count = 16;
	//
	// if (count > 0)
	// player.getInventory().setItem(1,
	// ItemStackFactory.Instance.CreateStack(Material.SNOW_BALL, count));
	// else
	// player.getInventory().setItem(1, null);
	// }

	private void SnowDecrease(Block block, int height)
	{
		if (height <= 0)
			return;

		if (!IsSnow(block))
			return;

		// Shuffle Up
		while (IsSnow(block.getRelative(BlockFace.UP)))
			block = block.getRelative(BlockFace.UP);

		// Snow Block
		int snowLevel = 8;
		if (block.getTypeId() == 78)
			snowLevel = block.getData() + 1;

		// Lower
		if (height >= snowLevel)
		{
			block.setTypeIdAndData(0, (byte) 0, true);
			SnowDecrease(block.getRelative(BlockFace.DOWN), height - snowLevel);
		}
		else
		{
			block.setTypeIdAndData(78, (byte) (snowLevel - height - 1), true);
		}
	}
	
	@EventHandler
	public void HealthRegen(EntityRegainHealthEvent event) 
	{
		if(event.getRegainReason() == RegainReason.SATIATED)
		{
			event.setAmount(1);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void GenerallDamage(CustomDamageEvent event)
	{
		if(event.GetCause() == DamageCause.ENTITY_ATTACK)
			event.SetCancelled("No Melee");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void SnowballEggDamage(CustomDamageEvent event)
	{
		
		if(!_peace)
		{
			event.SetCancelled("Peace Phase");
			return;
		}
		
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.PROJECTILE)
			return;

		Projectile proj = event.GetProjectile();
		if (proj == null)
			return;
		
		if(proj instanceof Fireball)
			return;
			
		if(proj instanceof Snowball) 
			event.AddMod("Snowball", "Snowball", 2, true);
		
		event.SetIgnoreRate(true);
		event.SetIgnoreArmor(true);

		// Effect
		proj.getWorld().playEffect(proj.getLocation(), Effect.STEP_SOUND, 80);
		((Player) proj.getShooter()).playSound(((Player) proj.getShooter()).getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
	}

	private boolean IsSnow(Block block)
	{
		return block.getTypeId() == 78 || block.getTypeId() == 80;
	}
	
	private boolean IsOnIce(Player player)
	{
		return player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.ICE;
	}
	
	
	private void makeMeteor()
	{

		Location loc = getEndgameLocation();

		if (loc == null)
		{
			return;
		}

		summonMeteor(loc, 20F);

	}

	private void summonMeteor(Location loc, float fireballSize)
	{
		Vector vector = new Vector(UtilMath.random.nextDouble() - 0.5D, 0.8, UtilMath.random.nextDouble() - 0.5D).normalize();

		vector.multiply(40);

		loc.add((UtilMath.random.nextDouble() - 0.5) * 7, 0, (UtilMath.random.nextDouble() - 0.5) * 7);

		loc.add(vector);

		final FallingBlock fallingblock = loc.getWorld().spawnFallingBlock(loc, Material.ICE, (byte) 0);

		fallingblock.setMetadata("Meteor", new FixedMetadataValue(getArcadeManager().getPlugin(), fireballSize));

		new BukkitRunnable()
		{
			int i;

			public void run()
			{
				if (fallingblock.isValid() && IsLive())
				{
					UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, fallingblock.getLocation(), 0.3F, 0.3F, 0.3F, 0, 3,
							ViewDist.MAX, UtilServer.getPlayers());

					if (i++ % 6 == 0)
					{
						fallingblock.getWorld().playSound(fallingblock.getLocation(), Sound.CAT_HISS, 1.3F, 0F);
					}
				}
				else
				{
					cancel();
				}
			}
		}.runTaskTimer(getArcadeManager().getPlugin(), 0, 0);

		vector.normalize().multiply(-(0.04 + ((0.5 - 0.05) / 2)));
		fallingblock.setFireTicks(9999);
	}
	
	@EventHandler
	public void MeteorHit(EntityChangeBlockEvent event)
	{
		Entity projectile = event.getEntity();

		float size = 2.5F;
		double damage = 2.5D;
		
		for(int i = 1; i <= 10; i++)
		{
			if(System.currentTimeMillis() >= getGameLiveTime() + (((30 * i) + 180) * 1000))
			{
				size = 2.5F * i;
				damage = 2.5D * i;
			}
		}
		
		if (projectile.hasMetadata("Meteor"))
		{

			CustomExplosion explosion = new CustomExplosion(getArcadeManager().GetDamage(), getArcadeManager().GetExplosion(),
					projectile.getLocation(), size, "Meteor");

			explosion.setBlockExplosionSize(size);
			explosion.setFallingBlockExplosion(false);
			explosion.setDropItems(false);
			explosion.setBlocksDamagedEqually(true);
			
			UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, projectile.getLocation(), 1.0F, 1.0F, 1.0F, 1, 3, ViewDist.MAX, UtilServer.getPlayers());
			for(Player player : UtilServer.getPlayers()) {
				player.playSound(projectile.getLocation(), Sound.EXPLODE, 1, 1);
			}
			
			boolean fall = true;
			
			for(Entity player : projectile.getNearbyEntities(size, size, size))
			{
				if(player instanceof Player)
				{
					Player damagee = (Player) player;
					Bukkit.getPluginManager().callEvent(new CustomDamageEvent(damagee, null, null, DamageCause.CUSTOM, damage, false, true, true, "Ice Meteoroid", "Ice Meteoroid", false));
				}
			}
			
			for(Block block : UtilBlock.getInRadius(event.getEntity().getLocation(), size).keySet())
			{
				if(block.getType() != Material.AIR)
				{
					block.setType(Material.ICE);
					if(block.getRelative(BlockFace.DOWN).getType() == Material.AIR)
					{
						// to reduce lag
						if(fall)
						{
							block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
							fall = false;
						} 
						else
						{
							fall = true;
						}
						block.setType(Material.AIR);
					}
				}
			}
			
		}
	}
	
	private Location getEndgameLocation()
	{
		int chance = UtilMath.r(50) + 3;
		int accuracy = Math.max((int) (chance - (0.0001 * chance)), 1);

		ArrayList<Player> players = GetPlayers(true);

		for (int a = 0; a < 50; a++)
		{
			Player player = players.get(UtilMath.r(players.size()));

			Location location = player.getLocation().add(UtilMath.r(accuracy * 2) - accuracy, 0,
					UtilMath.r(accuracy * 2) - accuracy);

			location = WorldData.World.getHighestBlockAt(location).getLocation().add(0.5, 0, 0.5);

			if (location.getBlock().getType() == Material.AIR)
			{
				location.add(0, -1, 0);
			}

			if (location.getBlockY() > 0 && location.getBlock().getType() != Material.AIR)
			{
				return location;
			}
		}

		return null;
	}
	
	@EventHandler
	public void Place(BlockPlaceEvent event)
	{
		if(event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.ICE
				|| event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.PACKED_ICE 
				|| event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.FENCE)
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You cant place a Barrier here!"));
			event.setCancelled(true);
		}
	}
	
	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Scoreboard.Reset();

		Scoreboard.WriteBlank();
		if(GetPlayers(true).size() <= 8)
		{
			for (GameTeam team : this.GetTeamList())
			{			
				if (!team.IsTeamAlive())
					continue;
				
				for(Player player : team.GetPlayers(true))
				{
					Scoreboard.Write(team.GetColor() + player.getName());
				}

				Scoreboard.WriteBlank();
			}
		} 
		else
		{
			for (GameTeam team : this.GetTeamList())
			{			
				Scoreboard.Write(team.GetColor() + C.Bold + team.GetName());
				Scoreboard.Write(team.GetColor() + "Alive " + team.GetPlayers(true).size());
				Scoreboard.WriteBlank();
			}
		}

		long time = 1000 * 195 - (System.currentTimeMillis() - this.GetStateTime());

		if (time > 0)
		{
			Scoreboard.Write(C.cYellow + C.Bold + "Meteoroids:");
			if(IsLive())
				Scoreboard.Write(UtilTime.MakeStr(time, 0));
		}
		else
		{
			Scoreboard.Write(C.cGold + C.Bold + "Meteoroids!");
		}	

		Scoreboard.Draw();
	}
	
}
