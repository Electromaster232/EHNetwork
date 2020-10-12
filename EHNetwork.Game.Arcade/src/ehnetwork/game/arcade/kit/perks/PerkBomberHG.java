package ehnetwork.game.arcade.kit.perks;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.Perk;

public class PerkBomberHG extends Perk
{
	private int _spawnRate;
	private int _max;

	public PerkBomberHG(int spawnRate, int max) 
	{
		super("Explosives", new String[] 
				{
				C.cGray + "Receive 1 TNT every " + spawnRate + " seconds. Maximum of " + max + ".",
				});
		
		_spawnRate = spawnRate;
		_max = max;
	}
	
	public void Apply(Player player) 
	{
		Recharge.Instance.use(player, GetName(), _spawnRate*1000, false, false);
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

			if (!Recharge.Instance.use(cur, GetName(), _spawnRate*1000, false, false))
				continue;

			if (UtilInv.contains(cur, Material.TNT, (byte)0, _max))
				continue;

			//Add
			cur.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.TNT, (byte)0, 1, F.item("Throwing TNT")));

			cur.playSound(cur.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
		}
	}

	@EventHandler
	public void TNTDrop(PlayerDropItemEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (!UtilInv.IsItem(event.getItemDrop().getItemStack(), Material.TNT, (byte)0))
			return;

		//Cancel
		event.setCancelled(true);

		//Inform
		UtilPlayer.message(event.getPlayer(), F.main(GetName(), "You cannot drop " + F.item("Throwing TNT") + "."));
	}

	@EventHandler
	public void TNTInvClick(InventoryClickEvent event)
	{	
		//boolean containerOpen = !(event.getView().getTopInventory().getHolder() instanceof Player);
		boolean clickInContainer = event.getClickedInventory() != null && !(event.getClickedInventory().getHolder() instanceof Player);
		
		if(clickInContainer)
		{
			return;
		}
		UtilInv.DisallowMovementOf(event, "Throwing TNT", Material.TNT, (byte) 0, true);
	}

	@EventHandler
	public void TNTDeathRemove(PlayerDeathEvent event)
	{	
		HashSet<org.bukkit.inventory.ItemStack> remove = new HashSet<org.bukkit.inventory.ItemStack>();

		for (org.bukkit.inventory.ItemStack item : event.getDrops())
			if (UtilInv.IsItem(item, Material.TNT, (byte)0))
				remove.add(item);

		for (org.bukkit.inventory.ItemStack item : remove)
			event.getDrops().remove(item);
	}
}
