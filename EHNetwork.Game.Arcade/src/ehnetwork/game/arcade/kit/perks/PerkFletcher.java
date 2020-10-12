package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkFletcher extends Perk
{
	private HashSet<Entity> _fletchArrows = new HashSet<Entity>();

	private int _max = 0;
	private int _time = 0;
	private boolean _remove;
	
	public PerkFletcher(int time, int max, boolean remove) 
	{
		super("Fletcher", new String[] 
				{
				C.cGray + "Receive 1 Arrow every " + time + " seconds. Maximum of " + max + ".",
				});
		
		_time = time;
		_max = max;
		_remove = remove;
	}
	
	public boolean isFletchedArrow(ItemStack stack)
	{
		if (!UtilGear.isMat(stack, Material.ARROW))
			return false;
		
		ItemMeta meta = stack.getItemMeta();
		
		if (meta.getDisplayName() == null)
			return false;
		
		if (!meta.getDisplayName().contains("Fletched Arrow"))
			return false;
		
		return true;
	}

	@EventHandler
	public void FletchShootBow(EntityShootBowEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player)event.getEntity();

		if (!Kit.HasKit(player))
			return;

		for (int i=0 ; i<=8 ; i++)
			if (isFletchedArrow(player.getInventory().getItem(i)))
			{
				_fletchArrows.add(event.getProjectile());
				((CraftArrow) event.getProjectile()).getHandle().fromPlayer = 0;

				return;
			}
	}

	@EventHandler
	public void FletchProjectileHit(ProjectileHitEvent event)
	{
		if (_remove)
			if (_fletchArrows.remove(event.getEntity()))
				event.getEntity().remove();
	}

	@EventHandler
	public void Fletch(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (Player cur : UtilServer.getPlayers())
		{
			if (Manager.isSpectator(cur))
				continue;
			
			if (!Kit.HasKit(cur))
				continue;

			if (!Manager.GetGame().IsAlive(cur))
				continue;

			// Enabling this causes a bug that will sometimes prevent players from getting new arrows
			// Believe this bug is related to when a player fires an arrow and gets an arrow in their inventory at the same
			// time which causes their inventory to not be in sync with the server. Best known fix right now
			// is to remove this check or keep calling UtilInv.update()
//			if (UtilPlayer.isChargingBow(cur))
//				continue;

			if (!Recharge.Instance.use(cur, GetName(), _time * 1000, false, false))
				continue;

			if (UtilInv.contains(cur, "Fletched Arrow", Material.ARROW, (byte)0, _max))
				continue;

			//Add
			cur.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)0, 1, F.item("Fletched Arrow")));

			cur.playSound(cur.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
		}
	}

	@EventHandler
	public void FletchDrop(PlayerDropItemEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (!isFletchedArrow(event.getItemDrop().getItemStack()))
			return;

		//Cancel
		event.setCancelled(true);

		//Inform
		UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot drop " + F.item("Fletched Arrow") + "."));
	}

	@EventHandler
	public void FletchDeathRemove(PlayerDeathEvent event)
	{	
		HashSet<org.bukkit.inventory.ItemStack> remove = new HashSet<org.bukkit.inventory.ItemStack>();

		for (org.bukkit.inventory.ItemStack item : event.getDrops())
			if (isFletchedArrow(item))
				remove.add(item);

		for (org.bukkit.inventory.ItemStack item : remove)
			event.getDrops().remove(item);
	}

	@EventHandler
	public void FletchInvClick(InventoryClickEvent event)
	{
		UtilInv.DisallowMovementOf(event, "Fletched Arrow", Material.ARROW, (byte)0, true);
	}

	@EventHandler
	public void FletchClean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Iterator<Entity> arrowIterator = _fletchArrows.iterator(); arrowIterator.hasNext();) 
		{
			Entity arrow = arrowIterator.next();

			if (arrow.isDead() || !arrow.isValid())
				arrowIterator.remove();
		}
	}
}
