package ehnetwork.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.event.PerkDestructorBlockEvent;

public class PerkDestructor extends Perk
{
	private boolean _enabled;
	
	private int _spawnRate;
	private int _max;
	
	private long _fallTime;

	private HashMap<Block, Long> _blocks = new HashMap<Block, Long>();

	public PerkDestructor(int spawnRate, int max, long fallTime, boolean enabled)
	{
		super("Seismic Charge", new String[]
				{
						C.cGray + "Receive 1 Seismic Charge every " + spawnRate + " seconds. Maximum of " + max + ".",
						C.cYellow + "Right-Click" + C.cGray + " with Seismic Charge to " + C.cGreen + "Throw Seismic Charge",
						enabled ? "" : C.cGray + "You will not receive them until bridges drop",
				});

		_spawnRate = spawnRate;
		_max = max;
		_fallTime = fallTime;
		
		_enabled = enabled;
	}

	public void Apply(Player player)
	{
		Recharge.Instance.use(player, GetName(), _spawnRate * 1000, false, false);
	}

	@EventHandler
	public void spawn(UpdateEvent event)
	{
		if (!_enabled)
			return;
		
		if (event.getType() != UpdateType.FAST)
			return;

		for (Player cur : UtilServer.getPlayers())
		{
			if (!Kit.HasKit(cur))
				continue;

			if (!Manager.GetGame().IsAlive(cur))
				continue;

			if (!Recharge.Instance.use(cur, GetName(), _spawnRate * 1000, false, false))
				continue;

			if (UtilInv.contains(cur, Material.ENDER_PEARL, (byte) 0, _max))
				continue;

			//Add
			cur.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.ENDER_PEARL, (byte) 0, 1, F.item("Seismic Charge")));

			cur.playSound(cur.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
		}
	}

	@EventHandler
	public void drop(PlayerDropItemEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (!UtilInv.IsItem(event.getItemDrop().getItemStack(), "Seismic Charge", Material.ENDER_PEARL, (byte) 0))
			return;

		//Cancel
		event.setCancelled(true);

		//Inform
		UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot drop " + F.item("Seismic Charge") + "."));
	}

	@EventHandler
	public void deathRemove(PlayerDeathEvent event)
	{
		HashSet<org.bukkit.inventory.ItemStack> remove = new HashSet<org.bukkit.inventory.ItemStack>();

		for (org.bukkit.inventory.ItemStack item : event.getDrops())
			if (UtilInv.IsItem(item, Material.ENDER_PEARL, (byte) 0))
				remove.add(item);

		for (org.bukkit.inventory.ItemStack item : remove)
			event.getDrops().remove(item);
	}

	@EventHandler
	public void invClick(InventoryClickEvent event)
	{
		UtilInv.DisallowMovementOf(event, "Seismic Charge", Material.ENDER_PEARL, (byte) 0, true);
	}

	@EventHandler
	public void throwItem(ProjectileLaunchEvent event)
	{
		if (Manager.GetGame() == null || !Manager.GetGame().IsLive())
			return;
		
		if (!(event.getEntity() instanceof EnderPearl))
			return;
		
		event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(0.7));
	}
	
	@EventHandler
	public void collide(PlayerTeleportEvent event)
	{
		if (Manager.GetGame() == null || !Manager.GetGame().IsLive())
			return;
		
		if (event.getCause() != TeleportCause.ENDER_PEARL)
			return;
		
		if (!Kit.HasKit(event.getPlayer()))
			return;
		
		for (Block block : UtilBlock.getInRadius(event.getTo(), 4).keySet())
		{
			if (block.getType() == Material.AIR  || block.getType() == Material.BEDROCK || block.isLiquid())
				continue;
			
			//Event
			PerkDestructorBlockEvent blockEvent = new PerkDestructorBlockEvent(event.getPlayer(), block);
			UtilServer.getServer().getPluginManager().callEvent(blockEvent);
			
			if (!blockEvent.isCancelled())
			{
				_blocks.put(block, System.currentTimeMillis());
				
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
			}
		}
		
		UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, event.getTo(), 0f, 0f, 0f, 0f, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		event.getTo().getWorld().playSound(event.getTo(), Sound.EXPLODE, 1f, 0.5f);
		event.getTo().getWorld().playSound(event.getTo(), Sound.FIREWORK_TWINKLE, 2f, 0.5f);
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void fall(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Block lowest = null;
		int lowestY = 0;
		
		for (Block block : _blocks.keySet())
		{	
			if (!UtilTime.elapsed(_blocks.get(block), _fallTime))
				continue;
			
			if (lowest == null || block.getY() < lowestY)
			{
				lowest = block;
				lowestY = block.getY();
			}
		}
		
		if (lowest != null)
		{
			if (lowest.getType() != Material.AIR && UtilBlock.airFoliage(lowest.getRelative(BlockFace.DOWN)))
			{
				lowest.getWorld().playEffect(lowest.getLocation(), Effect.STEP_SOUND, lowest.getType());
				
				Material type = lowest.getType();
				byte data = lowest.getData();
				lowest.setType(Material.AIR);
				
				lowest.getWorld().spawnFallingBlock(lowest.getLocation().add(0.5, 0.5, 0.5), type, data);
			}
			
			_blocks.remove(lowest);
		}
	}

	public void setEnabled(boolean var)
	{
		_enabled = var;
	}
}
