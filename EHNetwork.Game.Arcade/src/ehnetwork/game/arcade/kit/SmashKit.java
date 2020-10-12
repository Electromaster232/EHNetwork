package ehnetwork.game.arcade.kit;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextMiddle;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeFormat;
import ehnetwork.game.arcade.ArcadeManager;

public abstract class SmashKit extends Kit
{	
	private NautHashMap<Player, Long> _superActive = new NautHashMap<Player, Long>();
	
	private int _superCharges = 1;
	private String _superName;
	private long _superDuration;
	private Sound _superSound;
	
	public SmashKit(ArcadeManager manager, String name,
			KitAvailability kitAvailability, String[] kitDesc, Perk[] kitPerks,
			EntityType entityType, ItemStack itemInHand,
			String superName, long superDuration, Sound superSound) 
	{
		super(manager, name, kitAvailability, 3000, kitDesc, kitPerks, entityType, itemInHand);
		
		_superName = superName;
		_superDuration = superDuration;
		_superSound = superSound;
	}

	public SmashKit(ArcadeManager manager, String name,
					KitAvailability kitAvailability, int cost, String[] kitDesc, Perk[] kitPerks,
					EntityType entityType, ItemStack itemInHand,
					String superName, long superDuration, Sound superSound)
	{
		super(manager, name, kitAvailability, cost, kitDesc, kitPerks, entityType, itemInHand);
		
		_superName = superName;
		_superDuration = superDuration;
		_superSound = superSound;
	}

	@Override
	public void DisplayDesc(Player player) 
	{
		for (int i=0 ; i<3 ; i++)
			UtilPlayer.message(player, "");
		
		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, "§aKit - §f§l" + GetName());

		//Desc
		for (String line : GetDesc())
		{
			UtilPlayer.message(player, C.cGray + "  " + line);
		}

		//Perk Descs
		for (Perk perk : GetPerks())
		{
			if (!perk.IsVisible())
				continue;

			for (String line : perk.GetDesc())
			{
				UtilPlayer.message(player, C.cGray + "  " + line);
			}
		}

		UtilPlayer.message(player, ArcadeFormat.Line);

	}
	
	@EventHandler
	public void triggerSuper(PlayerInteractEvent event)
	{
		if (!Manager.GetGame().IsLive())
			return;
		
		if (!HasKit(event.getPlayer()))
			return;
		
		if (!UtilGear.isMat(event.getItem(), Material.NETHER_STAR))
			return;
		
		if (!Recharge.Instance.use(event.getPlayer(), _superName, 500, true, false))
			return;
		
		UtilInv.remove(event.getPlayer(), Material.NETHER_STAR, (byte)0, 1);
		
		activateSuper(event.getPlayer());
		
		//Heal
		event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
		
		//Inform + Effect
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), _superSound, 20f, 1f);
		
		if (Recharge.Instance.use(event.getPlayer(), _superName + " Announce", 20000, false, false))
		{
			Manager.GetGame().Announce(C.Bold + event.getPlayer().getName() + " activated " + C.cGreen + C.Bold + _superName + ChatColor.RESET + C.Bold + "!");
			UtilTextMiddle.display("Smash Crystal", event.getPlayer().getName() + " used " + C.cGreen + _superName, 5, 50, 5, UtilServer.getPlayers());
		}
	}
	
	@EventHandler
	public void expireSuper(UpdateEvent event)
	{
		Iterator<Player> superIter = _superActive.keySet().iterator();
		
		while (superIter.hasNext())
		{
			Player player = superIter.next();
			
			if (System.currentTimeMillis() > _superActive.get(player))
			{
				superIter.remove();
				deactivateSuper(player);
			}
		}
	}
	
	@EventHandler
	public void deathSuper(PlayerDeathEvent event)
	{
		if (_superActive.remove(event.getEntity()) != null)
			deactivateSuper(event.getEntity());
	}
	
	public void activateSuper(Player player)
	{
		//Duration Super
		if (_superDuration > 0)
		{
			_superActive.put(player, System.currentTimeMillis() + _superDuration);
			
			Recharge.Instance.recharge(player, _superName);
			Recharge.Instance.use(player, _superName, _superDuration, false, false);
			Recharge.Instance.setDisplayForce(player, _superName, true);
			Recharge.Instance.setCountdown(player, _superName, true); 	
		}
		
		//Disable Perks for Duration
		for (Perk perk : GetPerks())
			if (perk instanceof SmashPerk)
				((SmashPerk)perk).addSuperActive(player);
			
		//Items
		giveSuperItems(player);
		
		//Custom
		activateSuperCustom(player);
		
		//Deactivate if instant
		if (_superDuration <= 0)
		{
			deactivateSuper(player);
		}
	}
	
	public void activateSuperCustom(Player player)
	{
		//Null Default
	}

	public void deactivateSuper(Player player)
	{
		//Perks
		for (Perk perk : GetPerks())
			if (perk instanceof SmashPerk)
				((SmashPerk)perk).removeSuperActive(player);
		
		//Items
		giveCoreItems(player);
		
		//Custom
		deactivateSuperCustom(player);
	}

	public void deactivateSuperCustom(Player player)
	{
		//Null Default
	}
	
	public Collection<Player> getSuperActive()
	{
		return _superActive.keySet();
	}
	
	public boolean isSuperActive(Player player)
	{
		return _superActive.containsKey(player);
	}
	
	public String getSuperName()
	{
		return _superName;
	}
	
	public int getSuperCharges()
	{
		return _superCharges;
	}
	
	public void setSuperCharges(int charges)
	{
		_superCharges = charges;
	}
	
	public abstract void giveCoreItems(Player player);
	public abstract void giveSuperItems(Player player);
}
