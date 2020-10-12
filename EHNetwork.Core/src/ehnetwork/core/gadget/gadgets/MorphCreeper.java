package ehnetwork.core.gadget.gadgets;

import java.util.HashMap;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.gadget.GadgetManager;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MorphCreeper extends MorphGadget
{
	private HashMap<Player, Long> _active = new HashMap<Player, Long>();

	public MorphCreeper(GadgetManager manager)
	{
		super(manager, "Creeper Morph", new String[] 
				{
				C.cWhite + "Transforms the wearer into a creepy Creeper!",
				" ",
				C.cYellow + "Crouch" + C.cGray + " to use " + C.cGreen + "Detonate",
				" ",
				C.cPurple + "Unlocked with Hero Rank",
				},
				-1,
				Material.SKULL_ITEM, (byte)4);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		Disguise d1 = Manager.getDisguiseManager().createDisguise(EntityType.CREEPER);
		MobDisguise d2 = (MobDisguise) d1;
		d2.setCustomName(player.getName() + Manager.getClientManager().Get(player).GetRank());
		d2.setCustomNameVisible(true);
		Manager.getDisguiseManager().applyDisguise(d2, player);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);
	}

	@EventHandler
	public void Trigger(UpdateEvent event)
	{
		if (event.getType() == UpdateType.TICK)
		{
			for (Player player : GetActive())
			{
				if (player.isSneaking())
				{
					player.leaveVehicle();
					player.eject();

					if (!_active.containsKey(player))
						_active.put(player, System.currentTimeMillis());

					double elapsed = (System.currentTimeMillis() - _active.get(player))/1000d;

					player.setExp(Math.min(0.99f, (float)(elapsed/1.5)));
					
					//Sound
					player.getWorld().playSound(player.getLocation(), Sound.CREEPER_HISS, (float)(0.5 + (elapsed/3d)), (float)(0.5 + elapsed));

					IncreaseSize(player);
				}
				else if (_active.containsKey(player))	
				{
					//Unpower
					DecreaseSize(player);
					
					player.setExp(0f);
					
					double elapsed = (System.currentTimeMillis() - _active.remove(player))/1000d;

					if (elapsed < 1.5)
						continue;

					//Explode
					UtilParticle.PlayParticle(UtilParticle.ParticleType.HUGE_EXPLOSION, player.getLocation(), 0, 0.5f, 0, 0, 1,
							UtilParticle.ViewDist.MAX, UtilServer.getPlayers());
					player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1f, 0.8f);

					player.playEffect(EntityEffect.HURT);
					
					//Knockback
					HashMap<Player, Double> players = UtilPlayer.getInRadius(player.getLocation(), 8);
					for (Player other : players.keySet())
					{	
						if (other.equals(player))
							continue;			
						
						if (Manager.collideEvent(this, other))
							continue;
						
						double mult = players.get(other);

						//Knockback
						UtilAction.velocity(other, UtilAlg.getTrajectory(player.getLocation(), other.getLocation()), 1 + 1.5 * mult, false, 0, 0.5 + 1 * mult, 3, true);
					}
				}
			}
		}
	}



	public void DecreaseSize(Player player)
	{

	}

	public void IncreaseSize(Player player)
	{

	}

	@EventHandler
	public void HeroOwner(PlayerJoinEvent event)
	{
		if (Manager.getClientManager().Get(event.getPlayer()).GetRank().Has(Rank.HERO))
		{
			Manager.getDonationManager().Get(event.getPlayer().getName()).AddUnknownSalesPackagesOwned(GetName());
		}	
	}
	
	@EventHandler
	public void Clean(PlayerQuitEvent event)
	{
		_active.remove(event.getPlayer());
	}
}
