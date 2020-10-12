package ehnetwork.game.arcade.game.games.minestrike.items.guns;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextBottom;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.recharge.RechargedEvent;
import ehnetwork.game.arcade.game.games.minestrike.MineStrike;
import ehnetwork.game.arcade.game.games.minestrike.data.Bullet;
import ehnetwork.game.arcade.game.games.minestrike.items.StrikeItem;
import ehnetwork.game.arcade.game.games.minestrike.items.StrikeItemType;

public class Gun extends StrikeItem
{
	protected GunStats _gunStats;
	
	protected int _slot;

	//Active Data
	protected int _loadedAmmo;
	protected int _reserveAmmo;
	protected double _cone;
	protected double _lastMove;
	protected long _lastMoveTime;
	
	protected boolean _reloading = false;

	public Gun(GunStats gunStats)
	{
		super(gunStats.getItemType(), gunStats.getName(), gunStats.getDesc(), gunStats.getCost(), gunStats.getGemCost(), gunStats.getSkin());

		_gunStats = gunStats;
		
		if (gunStats.getItemType() == StrikeItemType.PRIMARY_WEAPON)
			_slot = 0;
		else
			_slot = 1;

		_cone = gunStats.getConeMin();

		_loadedAmmo = gunStats.getClipSize();
		_reserveAmmo = gunStats.getClipReserve() * gunStats.getClipSize();

		updateWeaponName(null);
	}

	public void shoot(final Player player, final MineStrike game)
	{
		if (_reloading)
			return;
		
		//Standard (300) RPM
		shootOnce(player, game);

		//600RPM
		if (_gunStats.getFireRate() <= 100 && _gunStats.getFireRate() > 50)
		{
			UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(game.Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					if (game.IsAlive(player))
						shootOnce(player, game);
				}
			}, 2);
		}

		//1200RPM
		if (_gunStats.getFireRate() <= 50)
		{
			for (int i=1 ; i<4 ; i++)
			{
				UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(game.Manager.getPlugin(), new Runnable()
				{
					public void run()
					{
						if (game.IsAlive(player))
							shootOnce(player, game);
					}
				}, i);
			}
		}	
	}

	public void shootOnce(Player player, MineStrike game)
	{
		if (_reloading)
			return;
		
		//Check Ammo
		if (!ammoCheck(player))
		{
			reload(player);
			return;
		}
			
		if (!Recharge.Instance.use(player, getName() + " Shoot", _gunStats.getFireRate(), false, false))
			return;

		//Use Ammo
		_loadedAmmo--;
		updateWeaponName(player);
 
		//Effect
		soundFire(player.getLocation());

		//Visual 
		Location loc = player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.2));
		loc.add(UtilAlg.getRight(player.getLocation().getDirection()).multiply(0.5));
		loc.add(UtilAlg.getDown(player.getLocation().getDirection()).multiply(0.3));
		UtilParticle.PlayParticle(ParticleType.EXPLODE, loc, 0, 0, 0, 0, 1,
				ViewDist.LONG, UtilServer.getPlayers());

		game.registerBullet(fireBullet(player, game));
		
		//Unscope
		if (_gunStats.getGunType() == GunType.SNIPER)
			game.removeScope(player);
		
		//Reload
		if (_loadedAmmo == 0)
			reload(player);
	}

	public Bullet fireBullet(Player player, MineStrike game)
	{
		//Instant?
		boolean instant = game.getBulletType() == 1 || (game.getBulletType() == 2 && _gunStats.getGunType() == GunType.SNIPER);
		
		//Shoot
		Entity bullet;
		if (instant)
			bullet = player.launchProjectile(Arrow.class);
		else
			bullet = player.launchProjectile(Snowball.class);

		//COF
		double cone = getCone(player);

		//Calc
		Vector cof = new Vector(Math.random() - 0.5, (Math.random() - 0.2) * (5d/8d), Math.random() - 0.5);
		cof.normalize();
		cof.multiply(cone);

		cof.add(player.getLocation().getDirection());
		cof.normalize();

		//Velocity
		bullet.setVelocity(cof.multiply(instant ? 200 : 4));

		//Increase COF
		_cone = Math.min(_gunStats.getConeMax(), _cone + _gunStats.getConeIncreaseRate());

		return new Bullet(bullet, this, player, game);
	}
	
	public double getArmorPenetration()
	{
		return _gunStats.getArmorPen();
	}

	public double getCone(Player player)
	{
		double cone = _cone;

		//Airborne Penalty
		if (!UtilEnt.isGrounded(player))
			cone += _gunStats.getGunType().getJumpPenalty();
		
		//Sprint Penalty
		else if (player.isSprinting())
			cone += _gunStats.getGunType().getSprintPenalty();

		//Move Penalty
		else if (!UtilTime.elapsed(_lastMoveTime, 75))
					cone += _lastMove * _gunStats.getGunType().getMovePenalty();

		//Crouch
		else if (player.isSneaking() && _gunStats.getGunType() != GunType.SNIPER) 
			cone = cone * 0.8;

		//System.out.println("Vision: " + player.hasPotionEffect(PotionEffectType.NIGHT_VISION));
		
		//Sniper Zoomed
		if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION) && _gunStats.getScope() && UtilGear.isMat(player.getInventory().getHelmet(), Material.PUMPKIN))
		{
			//Snipers Perfectly Accurate if not jumping
			if (_gunStats.getGunType() == GunType.SNIPER)
			{
				cone = 0;
				
				//Airborne Penalty
				if (!UtilEnt.isGrounded(player))
					cone += _gunStats.getGunType().getJumpPenalty();
			}
			//25% Recoil Decrease
			else
			{
				cone = cone * 0.75;
			}
		}
		
		//System.out.println("Cone: " + cone);

		return cone;
	}

	public boolean ammoCheck(Player player)
	{
		if (_loadedAmmo > 0)
			return true;

		//Sound
		if (Recharge.Instance.use(player, getName() + " Ammo Tick", 200, false, false))
			soundEmpty(player.getLocation());

		return false;
	}
	
	public long getReloadTime()
	{
		return _gunStats.getReloadTime();
	}

	public void reload(Player player)
	{
		if (_reloading)
			return;
		
		if (_loadedAmmo == _gunStats.getClipSize())
			return;
		
		int ammo = _reserveAmmo + _loadedAmmo;

		if (ammo == 0 || _reserveAmmo == 0)
		{
			UtilPlayer.message(player, F.main("Gun", "You have no more ammo for " + F.name(getName()) + "."));
			return;
		}
		 
		//Recharge
		Recharge.Instance.use(player, getName() + " Reload", getReloadTime(), false, true, true);

		//Sound
		soundReload(player.getLocation());
		
		_reloading = true;
	}

	@Override
	public void fixStackName()
	{
		updateWeaponName(null);
	}

	public void updateWeaponName(Player player)
	{
		ItemMeta meta = getStack().getItemMeta();
		meta.setDisplayName(ChatColor.RESET + (getOwnerName() == null ? "" : getOwnerName() + "'s ") + C.Bold + getName() + ChatColor.RESET + "   " + C.cGreen + _loadedAmmo + ChatColor.RESET + " / " + C.cYellow + _reserveAmmo);
		getStack().setItemMeta(meta);
		
		getStack().setAmount(Math.max(1, _loadedAmmo));

		if (player != null)
			player.getInventory().setItem(_slot, getStack());
	}

	public double getDropOff()
	{
		return _gunStats.getDropoff();
	}

	public void soundFire(Location loc)
	{
		loc.getWorld().playSound(loc, _gunStats.getFireSound(), _gunStats.getGunType().getVolume(), (float)(Math.random() * 0.2 + 0.9));
	}

	public void soundEmpty(Location loc)
	{
		loc.getWorld().playSound(loc, Sound.NOTE_STICKS, 1f, 2f);
		loc.getWorld().playSound(loc, Sound.NOTE_STICKS, 1f, 2f);
		loc.getWorld().playSound(loc, Sound.NOTE_STICKS, 1f, 2f);
	}

	public void soundReload(Location loc)
	{
		loc.getWorld().playSound(loc, Sound.PISTON_RETRACT, 1f, 0.8f);
	}

	public void cancelReloadCheck(Player player, MineStrike game)
	{
		if (!_reloading)
			return;
		
		if (!isHolding(player))
		{
			_reloading = false;

			Recharge.Instance.recharge(player, getName() + " Reload");
			
			UtilTextBottom.display(C.cRed + C.Bold + getName() + " Reload Cancelled", player);
			return;
		}
	}
	
	public void handleReloaded(Player player)
	{
		if (!_reloading)
			return;
		
		_reloading = false;
		
		//Update Ammo
		int ammo = _reserveAmmo + _loadedAmmo;
		
		_loadedAmmo = Math.min(ammo, _gunStats.getClipSize());
		_reserveAmmo = Math.max(0, ammo - _gunStats.getClipSize());
		
		//Update
		updateWeaponName(player);
		
		//Sound
		player.getWorld().playSound(player.getEyeLocation(), Sound.PISTON_EXTEND, 1f, 1.6f);
	}

	public void soundRefire(Location loc)
	{

	}

	public void giveToPlayer(Player player, boolean setOwner)
	{
		giveToPlayer(player, _slot, setOwner);
	}

	public int getSlot()
	{
		return _slot;
	}

	public void moveEvent(PlayerMoveEvent event)
	{
		double dist = UtilMath.offset(event.getFrom(), event.getTo());

		_lastMove = dist;
		_lastMoveTime = System.currentTimeMillis();
	}

	public void reduceCone()
	{
		_cone = Math.max(_gunStats.getConeMin(), _cone - (_gunStats.getConeReduceRate() / 20d));
	}
	
	public void reloadEvent(RechargedEvent event)
	{
		if (!isHolding(event.GetPlayer()))
			return;

		if (event.GetAbility().equals(getName() + " Shoot"))
		{
			soundRefire(event.GetPlayer().getEyeLocation());
		}
		else if (event.GetAbility().equals(getName() + " Reload"))
		{
			handleReloaded(event.GetPlayer());
		}
	}

	@Override
	public boolean pickup(MineStrike game, Player player)
	{
		if (player.getInventory().getItem(_slot) != null && player.getInventory().getItem(_slot).getType() != Material.AIR)
			return false;

		giveToPlayer(player, false);

		game.registerGun(this, player);
		game.deregisterDroppedGun(this);

		return true;
	}

	public void restockAmmo(Player player)
	{
		_loadedAmmo = _gunStats.getClipSize();
		_reserveAmmo = _gunStats.getClipReserve() * _gunStats.getClipSize();

		updateWeaponName(player);	
	}
	
	public double getDamage()
	{
		return _gunStats.getDamage()/5d;
	}

	@Override
	public String getShopItemType()
	{
		return C.cDGreen + C.Bold + _gunStats.getGunType().getName() + ChatColor.RESET;
	}
	
	@Override
	public String[] getShopItemCustom()
	{
		return new String[]
				{
				C.cYellow + C.Bold + "Clip / Spare Ammo: " + ChatColor.RESET + _gunStats.getClipSize() + "/" + _gunStats.getClipReserve()*_gunStats.getClipSize(),
				C.cYellow + C.Bold + "Damage per Bullet: " + ChatColor.RESET + UtilMath.trim(1, getDamage()),
				C.cYellow + C.Bold + "Armor Penetration: " + ChatColor.RESET + (int)(_gunStats.getArmorPen()*100) + "%",
				C.cYellow + C.Bold + "Damage Dropoff: " + ChatColor.RESET + (int)(_gunStats.getDropoff()*1000d) + "% per 10 Blocks",
				C.cYellow + C.Bold + "Recoil per Shot: " + ChatColor.RESET + _gunStats.getConeIncreaseRate(),
				C.cYellow + C.Bold + "Recoil Recovery: " + ChatColor.RESET + _gunStats.getConeReduceRate() + " per Second",
				};
	}

	public boolean hasScope()
	{
		return _gunStats.getScope();
	}

	public GunType getGunType()
	{
		return _gunStats.getGunType();
	}
}
