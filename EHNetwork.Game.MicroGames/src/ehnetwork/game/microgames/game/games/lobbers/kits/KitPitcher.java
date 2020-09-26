package ehnetwork.game.microgames.game.games.lobbers.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.lobbers.events.TNTThrowEvent;
import ehnetwork.game.microgames.game.games.lobbers.kits.perks.PerkCraftman;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDummy;

public class KitPitcher extends Kit
{
	public KitPitcher(MicroGamesManager manager)
	{
		super(manager, "Pitcher", KitAvailability.Gem, 4000, new String[]
				{
				C.cGray + "He can easily pitch the perfect",
				C.cGray + "shot for any occasion."
				}, new Perk[]
						{
				new PerkDummy("Pitcher",
						new String[]
								{
						C.cYellow + "Left Click" + C.cGray + " lever to " + C.cGreen + "Decrease Velocity. " + C.cGray + "Minimum of 1.",
						C.cYellow + "Right Click" + C.cGray + " lever to " + C.cGreen + "Increase Velocity. " + C.cGray + "Maximum of 3.",
								}),
				new PerkCraftman()
						}, EntityType.ZOMBIE,
				new ItemBuilder(Material.LEVER).build());
	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().setItem(1, new ItemBuilder(Material.LEVER).setTitle(F.item("Velocity Selector")).setAmount(2).build());
	}

	@EventHandler
	public void setFuse(TNTThrowEvent event)
	{
		if (!Manager.GetGame().IsLive())
			return;
		
		if (!HasKit(event.getPlayer()))
			return;
		
		ItemStack lever = event.getPlayer().getInventory().getItem(1);
		
		if (lever == null || lever.getType() == Material.AIR)
		{
			GiveItems(event.getPlayer());
		}
		else
		{
			if (lever.getAmount() < 1 || lever.getAmount() > 3)
			{
				GiveItems(event.getPlayer());
			}
			
			UtilAction.velocity(event.getTNT(), event.getPlayer().getLocation().getDirection(), getVelocity(lever.getAmount()), false, 0.0D, 0.1D, 10.0D, false);
		}		
	}
	
	@EventHandler
	public void changeFuse(PlayerInteractEvent event)
	{
		if (!Manager.GetGame().IsLive())
			return;

		if (!Manager.IsAlive(event.getPlayer()))
			return;

		if (!HasKit(event.getPlayer()))
			return;
		
		if (!UtilInv.IsItem(event.getItem(), Material.LEVER, (byte) 0))
			return;
		
		int amount = event.getPlayer().getInventory().getItem(1).getAmount();
		
		//Right
		if (UtilEvent.isAction(event, ActionType.R))
		{
			if (amount >= 3)
				return;
			
			UtilInv.insert(event.getPlayer(), new ItemBuilder(Material.LEVER).setTitle(F.item("Velocity Selector")).build());
			UtilInv.Update(event.getPlayer());
		}
		//Left
		else if (UtilEvent.isAction(event, ActionType.L))
		{
			if (amount <= 1)
				return;
			
			UtilInv.remove(event.getPlayer(), Material.LEVER, (byte) 0, 1);
			UtilInv.Update(event.getPlayer());
		}
	}

	private double getVelocity(int amount)
	{
		if (amount == 1)
			return 1.75;
		
		if (amount == 3)
			return 2.25;
		
		return 2.0;
	}
}
