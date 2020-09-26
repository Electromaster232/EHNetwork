package ehnetwork.core.treasure.animation;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_7_R4.MathHelper;

import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.treasure.BlockInfo;
import ehnetwork.core.treasure.Treasure;
import ehnetwork.core.treasure.TreasureType;

/**
 * Created by Shaun on 8/29/2014.
 */
public class ChestSpawnAnimation extends Animation
{
	private static final int ANIMATION_DURATION = 80;

	private Block _block;
	private byte _direction;
	private Location _centerLocation;
	
	private Location _particleLocation;
	private Vector _particleDirection;

	private List<BlockInfo> _chestBlockInfo;
	
	private double _radialOffset;

	public ChestSpawnAnimation(Treasure tresure, Block block, List<BlockInfo> chestBlockInfo, Block openingCenter, double radialOffset)
	{
		super(tresure);
		_block = block;
		int relX = getTreasure().getCenterBlock().getX() - block.getX();
		int relZ = getTreasure().getCenterBlock().getZ() - block.getZ();
		if (Math.abs(relX) > Math.abs(relZ))
		{
			if (relX > 0)
				_direction = (byte) 5;
			else
				_direction = (byte) 4; 
		}
		else
		{
			if (relZ > 0)
				_direction = (byte) 3;
			else
				_direction = (byte) 2;
		}
		
		_centerLocation = block.getLocation().clone().add(0.5, 0.5, 0.5);
		_chestBlockInfo = chestBlockInfo;
		
		_particleLocation = openingCenter.getLocation().add(0.5, 4, 0.5);
		
		_particleDirection = UtilAlg.getTrajectory(_particleLocation, _centerLocation);
		_particleDirection.multiply(UtilMath.offset(_particleLocation, _centerLocation) / (double)ANIMATION_DURATION);
		
		
		_radialOffset = radialOffset;
	}

	@Override
	public void tick()
	{
		float scale = (float)((double)(ANIMATION_DURATION - getTicks()) / (double)ANIMATION_DURATION);

		//Move Paticle Forwards
		_particleLocation.add(_particleDirection);
		
		//Play Particels
		if (getTreasure().getTreasureType() == TreasureType.OLD)
		{
			UtilParticle.PlayParticle(getTreasure().getTreasureType().getStyle().getSecondaryParticle(), _centerLocation, 0.1f, 0.1f, 0.1f, 0, 1,
					UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
		}
		else if (getTreasure().getTreasureType() == TreasureType.ANCIENT)
		{
			float x = (float) (Math.sin(getTicks()/4D));
			float z = (float) (Math.cos(getTicks()/4D));
			
			Location newLoc = _particleLocation.clone();
			newLoc.add(UtilAlg.getLeft(_particleDirection).multiply(x * scale));
			newLoc.add(UtilAlg.getUp(_particleDirection).multiply(z * scale));
			
			UtilParticle.PlayParticle(getTreasure().getTreasureType().getStyle().getSecondaryParticle(), newLoc, 0f, 0f, 0f, 0, 1,
					UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
		}
		else if (getTreasure().getTreasureType() == TreasureType.MYTHICAL)
		{
			float y = 5 * scale;
			double width = 0.7 * ((double) getTicks() / (double) ANIMATION_DURATION);
			
			for (int i=0 ; i < 2 ; i++)
			{
				double lead = i * ((2d * Math.PI)/2);
				
				float x = (float) (Math.sin(getTicks()/4D + lead));
				float z = (float) (Math.cos(getTicks()/4D + lead));
				
				UtilParticle.PlayParticle(getTreasure().getTreasureType().getStyle().getSecondaryParticle(), _centerLocation.clone().add(x * width, y, z * width), 0f, 0f, 0f, 0, 1,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
		}
		
		//Spawn Chest
		if (getTicks() >= ANIMATION_DURATION)
		{
			_chestBlockInfo.add(new BlockInfo(_block));
			getTreasure().setBlock(_block, getTreasure().getTreasureType().getMaterial(), _direction);
			_block.getLocation().getWorld().playSound(_centerLocation, getTreasure().getTreasureType().getStyle().getChestSpawnSound(), 0.5f, 1f);

			UtilParticle.ParticleType particleType = getTreasure().getTreasureType().getStyle().getChestSpawnParticle();

			if (particleType != null)
			{
				UtilParticle.PlayParticle(particleType, _centerLocation, 0.2f, 0.2f, 0.2f, 0, 50,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
			else
			{
				// TODO This doesnt work for 1.8 clients
				int i = MathHelper.floor(_centerLocation.getX());
				int j = MathHelper.floor(_centerLocation.getY() - 0.20000000298023224D - 0.5);
				int k = MathHelper.floor(_centerLocation.getZ());
				((CraftWorld) _centerLocation.getWorld()).getHandle().triggerEffect(2006, i, j, k, MathHelper.f(60 - 3.0F));
			}
			finish();
		}
	}

	@Override
	protected void onFinish()
	{

	}
}
