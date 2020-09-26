package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkBomber extends Perk
{
	public static class BomberExplodeDiamondBlock extends PlayerEvent
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

		private final Block _block;

		public BomberExplodeDiamondBlock(Player who, Block block)
		{
			super(who);

			_block = block;
		}

		public Block getBlock()
		{
			return _block;
		}
	}

	private HashMap<Entity, Player> _tntMap = new HashMap<Entity, Player>();

	private int _spawnRate;
	private int _max;
	private int _fuse;

	public PerkBomber(int spawnRate, int max, int fuse)
	{
		super("Bomber", new String[]
				{
						C.cGray + "Receive 1 TNT every " + spawnRate + " seconds. Maximum of " + max + ".",
						C.cYellow + "Click" + C.cGray + " with TNT to " + C.cGreen + "Throw TNT"
				});

		_spawnRate = spawnRate;
		_max = max;
		_fuse = fuse;
	}

	public void Apply(Player player)
	{
		Recharge.Instance.use(player, GetName(), _spawnRate * 1000, false, false);
	}

	@EventHandler
	public void TNTSpawn(UpdateEvent event)
	{
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

			if (UtilInv.contains(cur, Material.TNT, (byte) 0, _max))
				continue;

			//Add
			cur.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.TNT, (byte) 0, 1, F.item("Throwing TNT")));

			cur.playSound(cur.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
		}
	}

	@EventHandler
	public void TNTDrop(PlayerDropItemEvent event)
	{
		if (event.isCancelled())
			return;

		if (!UtilInv.IsItem(event.getItemDrop().getItemStack(), Material.TNT, (byte) 0))
			return;

		//Cancel
		event.setCancelled(true);

		//Inform
		UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot drop " + F.item("Throwing TNT") + "."));
	}

	@EventHandler
	public void TNTDeathRemove(PlayerDeathEvent event)
	{
		HashSet<org.bukkit.inventory.ItemStack> remove = new HashSet<org.bukkit.inventory.ItemStack>();

		for (org.bukkit.inventory.ItemStack item : event.getDrops())
			if (UtilInv.IsItem(item, Material.TNT, (byte) 0))
				remove.add(item);

		for (org.bukkit.inventory.ItemStack item : remove)
			event.getDrops().remove(item);
	}

	@EventHandler
	public void TNTInvClick(InventoryClickEvent event)
	{
		// If they have a inventory open
		if (event.getView().getTopInventory().getHolder() instanceof Player)
		{
			return;
		}

		// If they are clicking in their own inventory
		if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Player)
		{
			// If its not a shift click
			if (!event.isShiftClick())
			{
				return;
			}
		} // Else if they are not clicking in their own inventory make sure they don't have tnt in their hands
		else if (event.getCursor() == null || event.getCursor().getType() != Material.TNT)
		{
			return;
		}

		UtilInv.DisallowMovementOf(event, "Throwing TNT", Material.TNT, (byte) 0, true);
	}

	@EventHandler
	public void TNTThrow(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK &&
				event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_AIR)
			return;

		Player player = event.getPlayer();

		if (!UtilInv.IsItem(player.getItemInHand(), Material.TNT, (byte) 0))
			return;

		if (!Kit.HasKit(player))
			return;

		event.setCancelled(true);

		if (!Manager.GetGame().CanThrowTNT(player.getLocation()))
		{
			//Inform
			UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot use " + F.item("Throwing TNT") + " here."));
			return;
		}

		UtilInv.remove(player, Material.TNT, (byte) 0, 1);
		UtilInv.Update(player);

		TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);

		if (_fuse != -1)
			tnt.setFuseTicks(_fuse);

		UtilAction.velocity(tnt, player.getLocation().getDirection(), 0.5, false, 0, 0.1, 10, false);

		tnt.setMetadata("owner", new FixedMetadataValue(Manager.getPlugin(), player.getUniqueId()));

		_tntMap.put(tnt, player);
	}

	@EventHandler
	public void ExplosionPrime(ExplosionPrimeEvent event)
	{
		Player player = _tntMap.get(event.getEntity());
		if (player != null)
		{
			for (Player other : UtilPlayer.getNearby(event.getEntity().getLocation(), 14))
			{
				Manager.GetCondition().Factory().Explosion("Throwing TNT", other, player, 50, 0.1, false, false);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		Player player = _tntMap.remove(event.getEntity());

		if (player != null)
		{
			for (Iterator<Block> it = event.blockList().iterator(); it.hasNext(); )
			{
				Block block = it.next();

				if (block.getType() == Material.DIAMOND_ORE)
				{
					Bukkit.getPluginManager().callEvent(new BomberExplodeDiamondBlock(player, block));
					block.breakNaturally();
					it.remove();
				}
			}
		}
	}
}
