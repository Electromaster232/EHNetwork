package ehnetwork.game.microgames.game.games.skywars.data;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.games.skywars.Skywars;
import ehnetwork.game.microgames.game.games.skywars.events.TNTPickupEvent;

public class TNTGenerator
{
	private Skywars Host;

	private Location _loc;

	private long _time;
	private long _timeDelay = 25000;
	private Item _ent;

	public TNTGenerator(Skywars host, Location loc)
	{
		Host = host;

		_time = System.currentTimeMillis();

		_loc = loc;

		_loc.getBlock().getRelative(BlockFace.DOWN)
				.setType(Material.IRON_BLOCK);
	}

	public void update()
	{
		if (_ent != null)
		{
			if (!_ent.isValid())
			{
				_ent.remove();
				_ent = null;
			}

			return;
		}

		if (!UtilTime.elapsed(_time, _timeDelay))
			return;

		// Spawn
		_ent = _loc.getWorld().dropItem(_loc.clone().add(0, 1, 0),
				new ItemStack(Material.TNT));
		_ent.setVelocity(new Vector(0, 1, 0));

		// Block
		_loc.getBlock().getRelative(BlockFace.DOWN)
				.setType(Material.REDSTONE_BLOCK);

		// Firework
		UtilFirework.playFirework(_loc.clone().add(0, 1, 0), FireworkEffect
				.builder().flicker(false).withColor(Color.RED).with(Type.BURST)
				.trail(true).build());
	}

	public void pickup(Player player, Item item)
	{
		if (_ent == null)
		{
			_ent.remove();
			return;
		}

		if (!_ent.equals(item))
			return;

		if (!Host.IsAlive(player))
			return;

		if (Host.Manager.isSpectator(player))
			return;

		GameTeam team = Host.GetTeam(player);
		if (team == null)
			return;

		// Remove
		_ent.remove();
		_ent = null;
		_time = System.currentTimeMillis();
		_loc.getBlock().getRelative(BlockFace.DOWN)
				.setType(Material.IRON_BLOCK);

		// Inform
		UtilPlayer.message(
				player,
				F.main("Game", "You collected " + C.cYellow
						+ "Super Throwing TNT"));

		// Firework
		UtilFirework.playFirework(_loc.clone().add(0, 1, 0),
				FireworkEffect.builder().flicker(false).withColor(Color.YELLOW)
						.with(Type.BALL_LARGE).trail(true).build());

		player.getInventory().addItem(
				ItemStackFactory.Instance.CreateStack(
						Material.TNT,
						(byte) 0,
						2,
						F.item(C.cYellow + C.Bold + "Left Click - Far "
								+ C.cWhite + " / " + C.cYellow + C.Bold
								+ " Right Click - Drop")));

		player.playSound(player.getLocation(), Sound.ENDERDRAGON_HIT, 3F, 1F);
		
		Bukkit.getPluginManager().callEvent(new TNTPickupEvent(player));
	}

	public String getScoreboardInfo()
	{
		if (_ent != null)
			return "Lootable";
		else
			return UtilTime.MakeStr(_timeDelay - (System.currentTimeMillis() - _time), 0);
	}

	public boolean active()
	{
		return _ent != null;
	}
}
