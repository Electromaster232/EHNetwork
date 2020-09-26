package ehnetwork.game.arcade.game.games.spleef;

import java.lang.reflect.Field;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import net.minecraft.server.v1_7_R4.EntityArrow;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.spleef.kits.KitArcher;
import ehnetwork.game.arcade.game.games.spleef.kits.KitBrawler;
import ehnetwork.game.arcade.game.games.spleef.kits.KitSnowballer;
import ehnetwork.game.arcade.kit.Kit;

public class SpleefTeams extends TeamGame
{
	public SpleefTeams(ArcadeManager manager) 
	{
		super(manager, GameType.SpleefTeams,

				new Kit[]
						{
				new KitSnowballer(manager),
				new KitBrawler(manager),
				new KitArcher(manager)
						},

						new String[]
								{
				"Punch blocks to break them!",
				"Last team alive wins!"
								});
		
		this.DamagePvP = false;
		this.WorldWaterDamage = 4;
		
		this.PrepareFreeze = false;
		
		this.TeamArmor = true;
		this.TeamArmorHotbar = true;
	}

	@EventHandler
	public void SnowballDamage(ProjectileHitEvent event)
	{
		if (!(event.getEntity() instanceof Snowball))
			return;

		Snowball ball = (Snowball)event.getEntity();
		
		if (ball.getShooter() == null || !(ball.getShooter() instanceof Player))
			return;
		
		Location loc = ball.getLocation().add(ball.getVelocity().multiply(0.8));
		
		Block block = loc.getBlock();
		
		//Find Nearest if hit nothing :O
		if (block.getType() == Material.AIR)
		{
			Block closest = null;
			double closestDist = 0;
			
			for (Block other : UtilBlock.getSurrounding(block, true))
			{
				if (other.getType() == Material.AIR)
					continue;
				
				double dist = UtilMath.offset(loc, other.getLocation().add(0.5, 0.5, 0.5));

				if (closest == null || dist < closestDist)
				{
					closest = other;
					closestDist = dist;
				}
			}
			
			if (closest != null)
				block = closest;
		}

		BlockFade(block, (Player)ball.getShooter(), false);
	}

	@EventHandler
	public void ArrowDamage(ProjectileHitEvent event)
	{
		if (!(event.getEntity() instanceof Arrow))
			return;

		final Arrow arrow = (Arrow)event.getEntity();
		final double velocity = arrow.getVelocity().length();

		if (!(arrow.getShooter() instanceof Player))
			return;

		final Player player = (Player)arrow.getShooter(); 

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

					double radius = 0.5 + velocity/1.6d;

					BlockFade(block, player, false);

					for (Block other : UtilBlock.getInRadius(block.getLocation().add(0.5, 0.5, 0.5), radius).keySet())
					{
						BlockFade(other, player, true);
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

	@EventHandler(priority = EventPriority.LOW)
	public void BlockDamage(BlockDamageEvent event)
	{
		if (!this.IsLive())
			return;

		if (!this.IsAlive(event.getPlayer()))
			return;

		event.setCancelled(true);

		BlockFade(event.getBlock(), event.getPlayer(), false);

		//Snowball
		if (GetKit(event.getPlayer()) instanceof KitSnowballer)
			if (!UtilInv.contains(event.getPlayer(), Material.SNOW_BALL, (byte)0, 16))
				event.getPlayer().getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.SNOW_BALL));
	}

	public void BlockFade(Block block, Player player, boolean slowDamage)
	{
		AddStat(player, "BlocksBroken", 1, false, false);

		//Prevent Super Hunger from Bow
		if (Recharge.Instance.use(player, GetName() + " Hunger", 50, false, false))
			if (block.getTypeId() != 7)	
				UtilPlayer.hunger(player, 1);

		if (!slowDamage)
		{
			Break(block);
			return;
		}

		//Wool and Stained Clay
		if (block.getTypeId() == 35 || block.getTypeId() == 159)
		{
			//Greens
			if (block.getData() == 5 || block.getData() == 13)
				block.setData((byte)4);

			//Yellow
			else if (block.getData() == 4)
				block.setData((byte)14);

			else
				Break(block);
		}

		//Stone
		else if (block.getTypeId() == 1)
		{
			block.setTypeId(4);
		}

		//Stone Brick
		else if (block.getTypeId() == 98)
		{
			if (block.getData() == 0 || block.getData() == 1)
				block.setData((byte)2);	

			else
				Break(block);
		}

		//Grass
		else if (block.getTypeId() == 2)
		{
			block.setTypeId(3);
		}

		//Wood Planks
		else if (block.getTypeId() == 5)
		{
			if (block.getData() == 1)
				block.setData((byte)0);

			else if (block.getData() == 0)
				block.setData((byte)2);

			else
				Break(block);
		}

		//Other
		else if (block.getTypeId() != 7)
		{
			Break(block);
		}
	}

	public void Break(Block block)
	{
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
		block.setTypeId(0);
	}

	@EventHandler
	public void Hunger(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;

		if (!IsLive())
			return;

		for (Player player : GetPlayers(true))
		{
			if (player.getFoodLevel() <= 0)
			{
				Manager.GetDamage().NewDamageEvent(player, null, null, 
						DamageCause.STARVATION, 1, false, true, false,
						"Starvation", GetName());

				UtilPlayer.message(player, F.main("Game", "Break blocks to restore hunger!"));
			}

			UtilPlayer.hunger(player, -2);
		}
	}
}
