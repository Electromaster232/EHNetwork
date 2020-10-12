package ehnetwork.core.gadget.gadgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MorphWither extends MorphGadget
{
	private ArrayList<WitherSkull> _skulls = new ArrayList<WitherSkull>();
	
	public MorphWither(GadgetManager manager)
	{
		super(manager, "Wither Morph", new String[] 
				{
				C.cWhite + "Become a legendary Wither!",
				" ",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Wither Skull",
				" ",
				C.cPurple + "Unlocked with Legend Rank",
				},
				-1,
				Material.SKULL_ITEM, (byte)1);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		player.setMaxHealth(300);
		player.setHealth(300);

		Disguise d1 = Manager.getDisguiseManager().createDisguise(EntityType.WITHER);
		MobDisguise d2 = (MobDisguise) d1;
		d2.setCustomName(player.getName() + Manager.getClientManager().Get(player).GetRank());
		d2.setCustomNameVisible(true);
		Manager.getDisguiseManager().applyDisguise(d2, player);

		player.setMaxHealth(20);
		player.setHealth(20);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
		
		player.setAllowFlight(false);
		player.setFlying(false);
		
		player.setMaxHealth(20);
		player.setHealth(20);
	}

	@EventHandler
	public void witherSkull(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, UtilEvent.ActionType.L))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 1600, false, false))
			return;
		
		Vector offset = player.getLocation().getDirection();
		if (offset.getY() < 0)
			offset.setY(0);
		
		_skulls.add(player.launchProjectile(WitherSkull.class));
		 
		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 0.5f, 1f);
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent event)
	{
		if (!_skulls.contains(event.getEntity()))
			return;
		
		event.setCancelled(true);
		
		WitherSkull skull = (WitherSkull)event.getEntity();
		
		UtilParticle.PlayParticle(UtilParticle.ParticleType.LARGE_EXPLODE, skull.getLocation(), 0, 0, 0, 0, 1,
				UtilParticle.ViewDist.MAX, UtilServer.getPlayers());
		skull.getWorld().playSound(skull.getLocation(), Sound.EXPLODE, 2f, 1f);
		
		HashMap<Player, Double> players = UtilPlayer.getInRadius(event.getLocation(), 6);
		for (Player player : players.keySet())
		{	
			if (Manager.collideEvent(this, player))
				continue;
			
			double mult = players.get(player);
					
			//Knockback
			UtilAction.velocity(player, UtilAlg.getTrajectory(event.getLocation(), player.getLocation()), 2 * mult, false, 0, 0.6 + 0.4 * mult, 2, true);
		}
	}
	
	@EventHandler
	public void clean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		Iterator<WitherSkull> skullIterator = _skulls.iterator();
		
		while (skullIterator.hasNext())
		{
			WitherSkull skull = skullIterator.next();
			
			if (!skull.isValid())
			{
				skullIterator.remove();
				skull.remove();
				continue;
			}
		}
	}
	
	@EventHandler
	public void flight(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetActive())
		{
			if (UtilPlayer.isSpectator(player))
				continue;
			
			player.setAllowFlight(true);
			player.setFlying(true);
			
			if (UtilEnt.isGrounded(player))
				player.setVelocity(new Vector(0,1,0));
		}
	}
	
	@EventHandler
	public void legendOwner(PlayerJoinEvent event)
	{
		if (Manager.getClientManager().Get(event.getPlayer()).GetRank() == Rank.LEGEND ||
			Manager.getClientManager().Get(event.getPlayer()).GetRank() == Rank.ADMIN ||
			Manager.getClientManager().Get(event.getPlayer()).GetRank() == Rank.DEVELOPER ||
			Manager.getClientManager().Get(event.getPlayer()).GetRank() == Rank.OWNER)
		{
			Manager.getDonationManager().Get(event.getPlayer().getName()).AddUnknownSalesPackagesOwned(GetName());
		}	
	}

	public void setWitherData(String text, double healthPercent)
	{
		Iterator<Player> activeIterator = GetActive().iterator();
		
		while (activeIterator.hasNext())
		{
			Player player = activeIterator.next();
			/*
			DisguiseBase disguise = Manager.getDisguiseManager().getDisguise(player);
			
			if (disguise == null || !(disguise instanceof DisguiseWither))
			{
				DisableCustom(player);
				activeIterator.remove();
				continue;
			}
			
			((DisguiseWither)disguise).setName(text);
			((DisguiseWither)disguise).setHealth((float) (healthPercent * 300));
			Manager.getDisguiseManager().updateDisguise(disguise);

			 */
		}		
	}
}
