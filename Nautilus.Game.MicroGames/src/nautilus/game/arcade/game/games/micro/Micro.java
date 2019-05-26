package nautilus.game.arcade.game.games.micro;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import mineplex.core.common.util.MapUtil;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilTime;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.game.TeamGame;
import nautilus.game.arcade.game.games.micro.kits.*;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.stats.KillsWithinGameStatTracker;

public class Micro extends TeamGame
{
	private ArrayList<Block> _blocks = new ArrayList<Block>();
	private ArrayList<Block> _glass = new ArrayList<Block>();
	
	public Micro(ArcadeManager manager) 
	{
		super(manager, GameType.Micro,

				new Kit[]
						{
				new KitArcher(manager),
				new KitWorker(manager),
				new KitFighter(manager)
						},

						new String[]
								{
				"Small game, big strategy!",
								});

		this.StrictAntiHack = true;
		
		this.TeamArmor = true;
		this.TeamArmorHotbar = true;
		
		this.InventoryClick = true;

		this.ItemDrop = true;
		this.ItemPickup = true;
		
		this.BlockBreak = true;
		this.BlockPlace = true;

		registerStatTrackers(new KillsWithinGameStatTracker(this, 8, "Annihilation"));
	}
	
	@Override
	public void ParseData() 
	{
		for (Location loc : WorldData.GetCustomLocs("20"))
		{
			_glass.add(loc.getBlock());
			loc.getBlock().setType(Material.STAINED_GLASS);
		}
			
		for (int y= WorldData.MinY ; y < WorldData.MaxY ; y++)
			for (int x= WorldData.MinX ; x < WorldData.MaxX ; x++)
				for (int z= WorldData.MinZ ; z < WorldData.MaxZ ; z++)
				{
					Block block = WorldData.World.getBlockAt(x, y, z);
					
					if (block.getType() == Material.AIR || block.isLiquid())
						continue;
					
					_blocks.add(block);
				}
	}
	
	@EventHandler
	public void BlockPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;
		
		_blocks.add(event.getBlock());
	}
	
	@EventHandler
	public void BlockUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		if (_blocks.isEmpty())
			return;
		
		if (!UtilTime.elapsed(GetStateTime(), 12000))
			return;
			
		int d = 1;
		if (UtilTime.elapsed(GetStateTime(), 16000))
			d = 2;
		if (UtilTime.elapsed(GetStateTime(), 20000))
			d = 3;
		
		//TimingManager.start("Block Fall");
		
		for (int i = 0 ; i < d ; i++)
		{
			Block bestBlock = null;
			double bestDist = 0;
			
			for (Block block : _blocks)
			{
				double dist = UtilMath.offset2d(GetSpectatorLocation(), block.getLocation().add(0.5, 0.5, 0.5));
				
				if (bestBlock == null || dist > bestDist)
				{
					bestBlock = block;
					bestDist = dist;
				}
			}
			
			//Shuffle Down
			while (bestBlock.getRelative(BlockFace.DOWN).getType() != Material.AIR)
				bestBlock = bestBlock.getRelative(BlockFace.DOWN);				
			
			_blocks.remove(bestBlock);
			
			if (bestBlock.getType() != Material.AIR)
			{
				if (Math.random() > 0.75)
					bestBlock.getWorld().spawnFallingBlock(bestBlock.getLocation().add(0.5, 0.5, 0.5), bestBlock.getType(), bestBlock.getData());
				
				MapUtil.QuickChangeBlockAt(bestBlock.getLocation(), Material.AIR);
			}
		}

		//TimingManager.stop("Block Fall");
	}
	
	@EventHandler
	public void TimeUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (!IsLive())
			return;
		
		WorldData.World.setTime(WorldData.World.getTime() + 1);
	}
	
	@EventHandler
	public void FoodUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (Player player : GetPlayers(true))
			if (player.getFoodLevel() < 2)
				player.setFoodLevel(2);
	}
	
	@EventHandler
	public void BarrierUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		if (!IsLive())
			return;
		
		if (_glass.isEmpty())
			return;
		
		if (!UtilTime.elapsed(GetStateTime(), 10000))
			return;
		
		for (Block block : _glass)
			MapUtil.QuickChangeBlockAt(block.getLocation(), Material.AIR);
		
		_glass.clear();
	}
	
	@EventHandler
	public void BarrierBreak(BlockBreakEvent event)
	{
		if (_glass.contains(event.getBlock()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void ArrowDecrease(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;
		
		event.AddMod(GetName(), "Projectile Reduce", -2, false);
		
		event.AddKnockback("Increase", 1.6d);
	}
}
