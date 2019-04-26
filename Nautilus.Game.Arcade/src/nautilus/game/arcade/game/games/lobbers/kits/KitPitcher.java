package nautilus.game.arcade.game.games.lobbers.kits;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilInv;
import mineplex.core.itemstack.ItemBuilder;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.lobbers.events.TNTThrowEvent;
import nautilus.game.arcade.game.games.lobbers.kits.perks.PerkCraftman;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkDummy;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class KitPitcher extends Kit
{
	public KitPitcher(ArcadeManager manager)
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
