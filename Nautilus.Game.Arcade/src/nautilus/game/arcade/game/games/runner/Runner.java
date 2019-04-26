package nautilus.game.arcade.game.games.runner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.common.util.MapUtil;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilTime;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.runner.kits.*;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.stats.DistanceTraveledStatTracker;

import net.minecraft.server.v1_7_R4.EntityArrow;

public class Runner extends SoloGame implements IThrown
{
	private HashMap<Block, Long> _blocks = new HashMap<Block, Long>(); 
	
	public Runner(ArcadeManager manager) 
	{
		super(manager, GameType.Runner,

				new Kit[]
						{
				new KitLeaper(manager),
				new KitArcher(manager),
				new KitFrosty(manager)
						},

						new String[]
								{
				"Blocks fall from underneath you",
				"Keep running to stay alive",
				"Avoid falling blocks from above",
				"Last player alive wins!"
								});
		
		this.DamagePvP = false;
		this.HungerSet = 20;
		this.WorldWaterDamage = 4;
		
		this.PrepareFreeze = false;

		registerStatTrackers(new DistanceTraveledStatTracker(this, "MarathonRunner"));
	}
	
	@EventHandler
	public void ArrowDamage(ProjectileHitEvent event)
	{
		if (!(event.getEntity() instanceof Arrow))
			return;
		
		final Arrow arrow = (Arrow)event.getEntity();
		
		Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				try
				{
					EntityArrow entityArrow = ((CraftArrow)arrow).getHandle();

					Field fieldX = EntityArrow.class.getDeclaredField("d");
					Field fieldY = EntityArrow.class.getDeclaredField("e");
					Field fieldZ = EntityArrow.class.getDeclaredField("f");

					fieldX.setAccessible(true);
					fieldY.setAccessible(true); 
					fieldZ.setAccessible(true);

					int x = fieldX.getInt(entityArrow);
					int y = fieldY.getInt(entityArrow);
					int z = fieldZ.getInt(entityArrow);

					Block block = arrow.getWorld().getBlockAt(x, y, z);

					double radius = 2.5;
					
					for (Block other : UtilBlock.getInRadius(block.getLocation().add(0.5, 0.5, 0.5), radius).keySet())
					{
						AddBlock(other);
					}
					
					arrow.remove();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}  
			}
		}, 0);
	}
	
	public void AddBlock(Block block)
	{
		if (block == null || block.getTypeId() == 0 || block.getTypeId() == 7 || block.isLiquid())
			return;
		
		if (block.getRelative(BlockFace.UP).getTypeId() != 0)
			return;
		
		if (_blocks.containsKey(block))
			return;
		
		_blocks.put(block, System.currentTimeMillis());
		
		block.setTypeIdAndData(159, (byte) 14, false);
	}
	
	@EventHandler
	public void BlockBreak(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		//Add Blocks
		for (Player player : GetPlayers(true))
		{
			//Side Standing
			double xMod = player.getLocation().getX() % 1;
			if (player.getLocation().getX() < 0)
				xMod += 1;
			
			double zMod = player.getLocation().getZ() % 1;
			if (player.getLocation().getZ() < 0)
				zMod += 1;
	
			int xMin = 0;
			int xMax = 0;
			int zMin = 0;
			int zMax = 0;
			
			if (xMod < 0.3)	xMin = -1;
			if (xMod > 0.7)	xMax = 1;
			
			if (zMod < 0.3)	zMin = -1;
			if (zMod > 0.7)	zMax = 1;

			for (int x=xMin ; x<=xMax ; x++)
			{
				for (int z=zMin ; z<=zMax ; z++)
				{
					AddBlock(player.getLocation().add(x, -0.5, z).getBlock());
				}	
			}
		}
		
		//Decay
		HashSet<Block> readd = new HashSet<Block>();
		
		Iterator<Block> blockIterator = _blocks.keySet().iterator();
		
		while (blockIterator.hasNext())
		{
			Block block = blockIterator.next();
		
			if (!UtilTime.elapsed(_blocks.get(block), 600))
				continue;
			
			//Fall
			int id = block.getTypeId();
			byte data = block.getData();
			MapUtil.QuickChangeBlockAt(block.getLocation(), Material.AIR);
			FallingBlock ent = block.getWorld().spawnFallingBlock(block.getLocation(), id, data);	
			Manager.GetProjectile().AddThrow(ent, null, this, -1, true, false, false, false, 0.3f);
			
			blockIterator.remove();
		}
//			
////			if (!UtilTime.elapsed(_blocks.get(block), 120))
////				continue;
//			
//			blockIterator.remove();
//			
//			//Degrade
//			if (block.getTypeId() == 98)
//			{
//				if (block.getData() == 0)
//				{
//					readd.add(block);
//					block.setData((byte)2);
//					continue;
//				}
//			}
//			
//			//Degrade
//			if (block.getTypeId() == 35 || block.getTypeId() == 159)
//			{
//				if (block.getData() == 3)
//				{
//					readd.add(block);
//					block.setData((byte)5);
//					continue;
//				}
//				
//				if (block.getData() == 5)
//				{
//					readd.add(block);
//					block.setData((byte)4);
//					continue;
//				}
//				
//				if (block.getData() == 4)
//				{
//					readd.add(block);
//					block.setData((byte)1);
//					continue;
//				}
//				
//				if (block.getData() == 1)
//				{
//					readd.add(block);
//					block.setData((byte)14);
//					continue;
//				}
//				
//				else if (block.getData() != 14)
//				{
//					readd.add(block);
//					block.setData((byte)3);
//					continue;
//				}
//			}
//			
//			//Fall
//			int id = block.getTypeId();
//			byte data = block.getData();
//			MapUtil.QuickChangeBlockAt(block.getLocation(), Material.AIR);
//			FallingBlock ent = block.getWorld().spawnFallingBlock(block.getLocation(), id, data);	
//			Manager.GetProjectile().AddThrow(ent, null, this, -1, true, false, false, false, 1d);
//		}
//		
//		//Re-add
//		for (Block block : readd)
//		{
//			_blocks.put(block, System.currentTimeMillis());
//		}
	}
	
	@EventHandler
	public void BlockForm(EntityChangeBlockEvent event)
	{
		BlockSmash(event.getEntity());
		
		event.setCancelled(true);
	}

	public void BlockSmash(Entity ent)
	{
		if (!(ent instanceof FallingBlock))
			return;
		
		FallingBlock block = (FallingBlock)ent;
		
		int id = block.getBlockId();
		if (id == 35 || id == 159)
			id = 152;
		
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, id);
		
		ent.remove();
	}
	
	
	
	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		if (target == null)
			return;
		
		if (target instanceof Player)
		{
			if (!Manager.GetGame().IsAlive((Player)target))
			{
				return;
			}
		}
	
		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.ENTITY_ATTACK, 6, true, true, false,
				"Falling Block", "Falling Block");

		BlockSmash(data.GetThrown());
	}

	@Override
	public void Idle(ProjectileUser data) 
	{

	}

	@Override
	public void Expire(ProjectileUser data) 
	{

	}
}
