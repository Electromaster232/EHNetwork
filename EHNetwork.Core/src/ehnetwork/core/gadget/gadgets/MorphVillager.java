package ehnetwork.core.gadget.gadgets;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.disguise.disguises.DisguiseVillager;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MorphVillager extends MorphGadget implements IThrown
{	
	private HashSet<Item> _gems = new HashSet<Item>();
	
	public MorphVillager(GadgetManager manager)
	{
		super(manager, "Villager Morph", new String[] 
				{
				C.cWhite + "HURRRR! MURR HURRR!",
				" ",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Gem Throw",
				" ", 
				C.cRed +C.Bold + "WARNING: " + ChatColor.RESET + "Gem Throw uses 20 Gems" 
				},
				12000,
				Material.EMERALD, (byte)0);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguiseVillager disguise = new DisguiseVillager(player);
		disguise.setName(player.getName(), Manager.getClientManager().Get(player).GetRank());
		disguise.setCustomNameVisible(true);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
	}

	@EventHandler
	public void skill(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, UtilEvent.ActionType.L))
			return;
		
		if (Manager.getDonationManager().Get(player.getName()).GetBalance(CurrencyType.Gems) < 20)
		{
			UtilPlayer.message(player, F.main("Gadget", "You do not have enough Gems."));
			return;
		}
		
		if (!Recharge.Instance.use(player, GetName(), 800, false, false))
			return;
		
		player.getWorld().playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1f, 1f);
		
		//Item
		Item gem = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), new ItemStack(Material.EMERALD));
		UtilAction.velocity(gem, player.getLocation().getDirection(), 1, false, 0, 0.2, 1, false);
		
		//Throw
		Manager.getProjectileManager().AddThrow(gem, player, this, -1, true, true, true, 
				null, 1.4f, 0.8f, null, null, 0, UpdateType.TICK, 0.5f);
		
		Manager.getDonationManager().RewardGems(null, this.GetName() + " Throw", player.getName(), player.getUniqueId(), -20);
		
		gem.setPickupDelay(40);
		
		_gems.add(gem);
	}
	
	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target == null)
			return;
		
		if (target instanceof Player)
			if (Manager.collideEvent(this, (Player) target))
				return;

		//Pull
		UtilAction.velocity(target, 
				UtilAlg.getTrajectory(data.GetThrown().getLocation(), target.getEyeLocation()),
				1, false, 0, 0.2, 0.8, true);
		
		UtilAction.velocity(data.GetThrown(), 
				UtilAlg.getTrajectory(target, data.GetThrown()), 
				0.5, false, 0, 0, 0.8, true);

		//Effect
		target.playEffect(EntityEffect.HURT);
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		
	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent event)
	{
		if (_gems.contains(event.getItem()))
		{
			event.setCancelled(true);
			event.getItem().remove();
			
			Manager.getDonationManager().RewardGemsLater(GetName() + " Pickup", event.getPlayer(), 16);
			
			event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1f, 2f);
		}
	}
	
	@EventHandler
	public void Clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Iterator<Item> gemIterator = _gems.iterator();
		
		while (gemIterator.hasNext())
		{
			Item gem = gemIterator.next();
			
			if (!gem.isValid() || gem.getTicksLived() > 1200)
			{
				gem.remove();
				gemIterator.remove();
			}
		}
	}
}
