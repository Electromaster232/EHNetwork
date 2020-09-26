package ehnetwork.game.microgames.game.games.sneakyassassins.powerups;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

/**
 * Created by Tim on 8/5/2014.
 */
public class PowerUpItem
{
	private final PowerUpManager _powerUpManager;
	private final Location _location;
	private Location _effectLocation;
	private Block _beaconBlock;
	private BlockState[][][] _originalBeaconBlocks = new BlockState[3][2][3];
	private Skeleton _npc;

	public PowerUpItem(PowerUpManager powerUpManager, Location location)
	{
		_powerUpManager = powerUpManager;
		_location = location;
		_effectLocation = getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
		_effectLocation.setY(250);
	}

	public PowerUpManager getPowerUpManager()
	{
		return _powerUpManager;
	}

	public Location getLocation()
	{
		return _location;
	}

	@SuppressWarnings("deprecation")
	public void activate()
	{
		_beaconBlock = getLocation().getBlock().getRelative(BlockFace.DOWN);

		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 2; y++)
			{
				for (int z = 0; z < 3; z++)
				{
					Block beaconBaseBlock = _beaconBlock.getRelative(x - 1, y - 1, z - 1);

					_originalBeaconBlocks[x][y][z] = beaconBaseBlock.getState();

					if (y == 0)
						beaconBaseBlock.setType(Material.IRON_BLOCK);
					else if (x == 1 && z == 1)
						beaconBaseBlock.setType(Material.BEACON);
					else
						beaconBaseBlock.setTypeIdAndData(Material.STAINED_GLASS.getId(), DyeColor.YELLOW.getWoolData(), false);
				}
			}
		}
	}

	public void onUpdate(UpdateEvent event)
	{
		if (event.getType() == UpdateType.FASTEST && _effectLocation != null)
		{
			FireworkEffect effect = FireworkEffect
					.builder()
					.flicker(false)
					.withColor(Color.YELLOW)
					.with(FireworkEffect.Type.BURST)
					.trail(false)
					.build();

			UtilFirework.playFirework(_effectLocation, effect);

			_effectLocation.setY(_effectLocation.getY() - 2);

			if (_effectLocation.getY() - getLocation().getY() < 2)
			{
				_effectLocation = null;

				Location itemLocation = _beaconBlock.getLocation().add(0.5, 1.5, 0.5);

				effect = FireworkEffect
						.builder()
						.flicker(false)
						.withColor(Color.YELLOW)
						.with(FireworkEffect.Type.BALL_LARGE)
						.trail(true)
						.build();

				UtilFirework.playFirework(itemLocation, effect);

				_powerUpManager.getGame().CreatureAllowOverride = true;
				_npc = itemLocation.getWorld().spawn(itemLocation, Skeleton.class);
				_powerUpManager.getGame().CreatureAllowOverride = false;
				UtilEnt.Vegetate(_npc);
				UtilEnt.ghost(_npc, true, false);
				
				_npc.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
				_npc.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
				_npc.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 0, true));
				_npc.setRemoveWhenFarAway(false);
				//_npc.setSkeletonType(SkeletonType.WITHER);
				
				_beaconBlock.setType(Material.GLASS);
			}
		}
	}

	public Skeleton getNPC()
	{
		return _npc;
	}

	public void remove()
	{
		if (getNPC() != null)
		{
			getNPC().remove();
			_npc = null;
		}
		
		getPowerUpManager().removePowerUp(this);
		
		//Remove Beacon
		for (int x = 0; x < _originalBeaconBlocks.length; x++)
			for (int y = 0; y < _originalBeaconBlocks[x].length; y++)
				for (int z = 0; z < _originalBeaconBlocks[x][y].length; z++)
					_originalBeaconBlocks[x][y][z].update(true, false);
	}
	
	public void powerupParticles()
	{
		if (getNPC() == null)
			return;
		
		float x = (float) (Math.sin(getNPC().getTicksLived()/4d) * 1f);
		float z = (float) (Math.cos(getNPC().getTicksLived()/4d) * 1f);
		float y = (float) (Math.cos(getNPC().getTicksLived()/7d) * 1f + 1f);
		
		UtilParticle.PlayParticle(ParticleType.HAPPY_VILLAGER, getNPC().getLocation().add(x, y, z), 0f, 0f, 0f, 0, 1,
				ViewDist.LONG, UtilServer.getPlayers());
	}
}
