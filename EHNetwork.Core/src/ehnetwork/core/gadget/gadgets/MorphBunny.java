package ehnetwork.core.gadget.gadgets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.CurrencyType;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MorphBunny extends MorphGadget
{	
	private HashSet<Player> _jumpCharge = new HashSet<Player>();
	private HashMap<Item, String> _eggs = new HashMap<Item, String>();
	
	public MorphBunny(GadgetManager manager)
	{
		super(manager, "Easter Bunny Morph", new String[] 
				{
				C.cWhite + "Happy Easter!",
				" ",
				C.cYellow + "Charge Crouch" + C.cGray + " to use " + C.cGreen + "Super Jump",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Hide Easter Egg",
				" ", 
				C.cRed +C.Bold + "WARNING: " + ChatColor.RESET + "Hide Easter Egg uses 500 Coins" ,
				" ",
				C.cPurple + "Special Limited Time Morph",
				C.cPurple + "Request at https://discord.gg/FttmSEQ",
				},
				-1,
				Material.MONSTER_EGG, (byte)98);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		Disguise d1 = Manager.getDisguiseManager().createDisguise(EntityType.RABBIT);
		MobDisguise d2 = (MobDisguise) d1;
		d2.setCustomName(player.getName() + Manager.getClientManager().Get(player).GetRank());
		d2.setCustomNameVisible(true);
		Manager.getDisguiseManager().applyDisguise(d2, player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 1));
	}

	@Override
	public void DisableCustom(Player player) 
	{
		_jumpCharge.remove(player);
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
		
		player.removePotionEffect(PotionEffectType.SPEED);
		player.removePotionEffect(PotionEffectType.JUMP);
		
	}
		
	@EventHandler
	public void jumpTrigger(PlayerToggleSneakEvent event)
	{
		Player player = event.getPlayer();
		
		if (!IsActive(player))
			return;
		
		//Start
		if (!event.getPlayer().isSneaking())
		{
			if (UtilEnt.isGrounded(event.getPlayer()))
				_jumpCharge.add(event.getPlayer());
		}
		//Jump
		else if (_jumpCharge.remove(event.getPlayer()))
		{
			float power = player.getExp();
			player.setExp(0f);
			
			UtilAction.velocity(player, power * 4, 0.4, 4, true);
			
			player.getWorld().playSound(player.getLocation(), Sound.CAT_HIT, 0.75f, 2f);
		}
	}
	
	@EventHandler
	public void jumpBoost(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Player> jumpIter = _jumpCharge.iterator();
		
		while (jumpIter.hasNext())
		{
			Player player = jumpIter.next();
			
			if (!player.isValid() || !player.isOnline() || !player.isSneaking())
			{
				jumpIter.remove();
				continue;
			}
			
			player.setExp(Math.min(0.9999f, player.getExp() + 0.03f));
			
			player.playSound(player.getLocation(), Sound.FIZZ, 0.25f + player.getExp() * 0.5f, 0.5f + player.getExp());
		}
	}
	
	@EventHandler
	public void eggHide(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, UtilEvent.ActionType.L))
			return;
		
		if (Manager.getDonationManager().Get(player.getName()).GetBalance(CurrencyType.Coins) < 500)
		{
			UtilPlayer.message(player, F.main("Gadget", "You do not have enough Coins."));
			return;
		}
		
		if (!Recharge.Instance.use(player, "Hide Egg", 30000, true, false))
			return;
		
		//Color
		
		
		//Item
		ItemStack eggStack = ItemStackFactory.Instance.CreateStack(Material.MONSTER_EGG, (byte)0, 1, "Hidden Egg" + System.currentTimeMillis());
		eggStack.setDurability((short) 98);
		
		Item egg = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), eggStack);
		UtilAction.velocity(egg, player.getLocation().getDirection(), 0.2, false, 0, 0.2, 1, false);

		
		Manager.getDonationManager().RewardCoinsLater(this.GetName() + " Egg Hide", player, -500);
		
		egg.setPickupDelay(40);
		
		_eggs.put(egg, player.getName());
		
		//Announce
		Bukkit.broadcastMessage(C.cYellow + C.Bold + player.getName() + 
				ChatColor.RESET + C.Bold + " hid an " +
				C.cYellow + C.Bold + "Easter Egg" +
				ChatColor.RESET + C.Bold + " worth " +
				C.cYellow + C.Bold + "450 Coins");
		
		for (Player other : UtilServer.getPlayers())
			other.playSound(other.getLocation(), Sound.CAT_HIT, 1.5f, 1.5f);
	}
	
	@EventHandler
	public void eggPickup(PlayerPickupItemEvent event)
	{
		if (_eggs.containsKey(event.getItem()) && !_eggs.get(event.getItem()).equals(event.getPlayer().getName()))
		{
			_eggs.remove(event.getItem());
			
			event.setCancelled(true);
			event.getItem().remove();
			
			Manager.getDonationManager().RewardCoinsLater(GetName() + " Egg Pickup", event.getPlayer(), 450);
			
			event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1.5f, 0.75f);
			event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1.5f, 1.25f);
			
			UtilFirework.playFirework(event.getItem().getLocation(), Type.BURST, Color.YELLOW, true, true);
			
			//Announce
			Bukkit.broadcastMessage(C.cGold + C.Bold + event.getPlayer().getName() + 
					ChatColor.RESET + C.Bold + " found an " +
					C.cGold + C.Bold + "Easter Egg" +
					ChatColor.RESET + C.Bold + "! " + _eggs.size() + " Eggs left!");
			}
	}
	
	@EventHandler
	public void eggClean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		Iterator<Item> eggIter = _eggs.keySet().iterator();
		
		while (eggIter.hasNext())
		{
			Item egg = eggIter.next();
			
			if (!egg.isValid() || egg.getTicksLived() > 24000)
			{
				egg.remove();
				eggIter.remove();
				
				//Announce
				Bukkit.broadcastMessage(
						ChatColor.RESET + C.Bold + "No one found an " +
						C.cGold + C.Bold + "Easter Egg" +
						ChatColor.RESET + C.Bold + "! " + _eggs.size() + " Eggs left!");
			}
			else
			{
				UtilParticle.PlayParticle(UtilParticle.ParticleType.SPELL, egg.getLocation().add(0, 0.1, 0), 0.1f, 0.1f, 0.1f, 0, 1,
						UtilParticle.ViewDist.NORMAL, UtilServer.getPlayers());
			}
		}
	}
	
	@EventHandler
	public void eggDespawnCancel(ItemDespawnEvent event)
	{
		if (_eggs.containsKey(event.getEntity()))
			event.setCancelled(true);
	}
}
