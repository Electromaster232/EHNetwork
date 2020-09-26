package ehnetwork.game.arcade.game.games.minestrike.items.grenades;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.game.games.minestrike.MineStrike;
import ehnetwork.game.arcade.game.games.minestrike.items.StrikeItem;
import ehnetwork.game.arcade.game.games.minestrike.items.StrikeItemType;

public abstract class Grenade extends StrikeItem
{
	public static class GrenadeExplodeEvent extends Event
	{
		private static final HandlerList handlers = new HandlerList();

		public static HandlerList getHandlerList()
		{
			return handlers;
		}

		@Override
		public HandlerList getHandlers()
		{
			return getHandlerList();
		}

		private final Grenade _grenade;
		private final Player _thrower;
		private final List<Player> _damagedPlayers;

		public GrenadeExplodeEvent(Grenade grenade, Player thrower, List<Player> damagedPlayers)
		{
			_grenade = grenade;
			_thrower = thrower;
			_damagedPlayers = damagedPlayers;
		}

		public Grenade getGrenade()
		{
			return _grenade;
		}

		public List<Player> getDamagedPlayers()
		{
			return _damagedPlayers;
		}

		public Player getThrower()
		{
			return _thrower;
		}
	}

	protected Player _thrower = null;
	
	protected Vector _vel;
	protected Location _lastLoc;
	protected ArrayList<Vector> _velHistory = new ArrayList<Vector>();
	
	protected int _limit;
	
	public Grenade(String name, String[] desc, int cost, int gemCost, Material skin, int limit)
	{
		super(StrikeItemType.GRENADE, name, desc, cost, gemCost, skin);
		
		_limit = limit;
	}
	
	public boolean giveToPlayer(Player player, boolean setOwnerName)
	{
		int slot = 3;
		
		int alreadyHas = 0;
		
		while (player.getInventory().getItem(slot) != null && player.getInventory().getItem(slot).getType() != Material.AIR && slot <= 6)
		{
			if (player.getInventory().getItem(slot).getType() == getSkin())
			{
				alreadyHas++;
				
				if (alreadyHas >= _limit)
					return false;
			}
			
			slot++;
		}
			
		if (slot > 6)
			return false;
		
		giveToPlayer(player, slot, setOwnerName);
		
		return true;
	}
	
	public boolean canGiveToPlayer(Player player)
	{
		int slot = 3;
		
		int alreadyHas = 0;
		
		while (player.getInventory().getItem(slot) != null && player.getInventory().getItem(slot).getType() != Material.AIR && slot <= 6)
		{
			if (player.getInventory().getItem(slot).getType() == getSkin())
			{
				alreadyHas++;
				
				if (alreadyHas >= _limit)
					return false;
			}
			
			slot++;
		}
			
		if (slot > 6)
			return false;
		
		return true;
	}
	
	public void throwGrenade(Player player, boolean wasLeftClick, MineStrike game)
	{
		player.setItemInHand(null);
		
		_thrower = player;
		
		Entity ent = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), getStack());
		
		if (wasLeftClick)
			UtilAction.velocity(ent, player.getLocation().getDirection(), 0.4, false, 0, 0, 2, false);
		else
			UtilAction.velocity(ent, player.getLocation().getDirection(), 1.2, false, 0, 0.2, 2, false);
		
		//Rebound Data
		_vel = player.getLocation().getDirection();
		_velHistory.add(_vel);
		_lastLoc = ent.getLocation();
		
		UtilPlayer.message(player, F.main("Game", "You threw " + getName() + "."));
		
		game.registerThrownGrenade(ent, this);
		game.deregisterGrenade(this);
		
		//Sound
		playSound(game, player);
	}
	
	public boolean update(MineStrike game, Entity ent)
	{
		//Invalid
		if (!ent.isValid())
			return true;
		
		//Rebound Off Blocks
		rebound(ent);
		
		//Custom
		if (updateCustom(game, ent))
			return true;
		
		return false;
	}
	
	public void rebound(Entity ent)
	{
		if (UtilEnt.isGrounded(ent) || ent.getVelocity().length() < 0.1 || ent.getTicksLived() < 4)
			return;
		
		/*
		 * What we must do here, is record the velocity every tick. 
		 * Then when it collides, we get the velocity from a few ticks before and apply it, reversing the direction of collision.
		 * We record history because as soon as it collides the collision direction is set to 0.
		 */
		
		//X Rebound
		if ((_vel.getX() > 0.05 && ent.getLocation().getX() - _lastLoc.getX() <= 0) ||
			(_vel.getX() < 0.05 && ent.getLocation().getX() - _lastLoc.getX() >= 0))
		{
			_vel = _velHistory.get(0);
			_vel.setX(-_vel.getX());
			_vel.multiply(0.75);
			
			ent.setVelocity(_vel);
			
			ent.getWorld().playSound(ent.getLocation(), Sound.ZOMBIE_WOOD, 1f, 2f);
		}
		
		//Z Rebound
		else if ((_vel.getZ() > 0.05 && ent.getLocation().getZ() - _lastLoc.getZ() <= 0) ||
				 (_vel.getZ() < 0.05 && ent.getLocation().getZ() - _lastLoc.getZ() >= 0))
		{
			_vel = _velHistory.get(0);
			_vel.setZ(-_vel.getZ());
			_vel.multiply(0.75);
			
			ent.setVelocity(_vel);
			
			ent.getWorld().playSound(ent.getLocation(), Sound.ZOMBIE_WOOD, 1f, 2f);
		}

		else
		{
			_velHistory.add(ent.getVelocity());
			
			while (_velHistory.size() > 4)
				_velHistory.remove(0);
		}

		_lastLoc = ent.getLocation();
	}
	
	public abstract boolean updateCustom(MineStrike game, Entity ent);
		
	@Override
	public boolean pickup(MineStrike game, Player player)
	{
		if (giveToPlayer(player, false))
		{
			game.registerGrenade(this, player);
			game.deregisterDroppedGrenade(this);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getShopItemType()
	{
		return C.cDGreen + C.Bold + "Grenade" + ChatColor.RESET;
	}
	
	public abstract void playSound(MineStrike game, Player player);
}
