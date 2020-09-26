package ehnetwork.game.arcade.game.games.wizards;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game.GameState;

public class WizardSpellMenu extends MiniPlugin
{
	private Wizards _wizards;
	private WizardSpellMenuShop _wizardShop;
	private ItemStack _wizardSpells = new ItemBuilder(Material.ENCHANTED_BOOK).setTitle(C.cGold + "Wizard Spells")
			.addLore(C.cGray + "Right click with this to view the spells").build();

	public WizardSpellMenu(String moduleName, JavaPlugin plugin, Wizards wizards)
	{
		super("Wizard Spell Menu", plugin);
		_wizardShop = new WizardSpellMenuShop(this, wizards.getArcadeManager().GetClients(), wizards.getArcadeManager()
				.GetDonation(), wizards);
		_wizards = wizards;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		if (_wizards.GetState() == GameState.Recruit || _wizards.GetState() == GameState.Live)
		{
			event.getPlayer().getInventory().setItem(0, _wizardSpells);
		}
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable()
		{
			public void run()
			{
				if (_wizards.IsLive())
				{
					event.getEntity().getInventory().setItem(0, _wizardSpells);
				}
			}
		});
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void Observer(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().equalsIgnoreCase("/spec"))
		{
			if (!_wizards.IsAlive(event.getPlayer())
					&& !UtilInv.contains(event.getPlayer(), _wizardSpells.getType(), (byte) 0, 1))
			{
				event.getPlayer().getInventory().setItem(0, _wizardSpells);
			}
		}
	}

	@EventHandler
	public void onJoin(GameStateChangeEvent event)
	{
		if (event.GetState() == GameState.Recruit)
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.getInventory().setItem(0, _wizardSpells);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.PHYSICAL && event.getAction().name().contains("RIGHT")
				&& (!_wizards.IsLive() || !_wizards.IsAlive(event.getPlayer())))
		{

			ItemStack item = event.getItem();

			if (item != null && item.isSimilar(_wizardSpells))
			{

				_wizardShop.attemptShopOpen(event.getPlayer());
			}
		}

		if (_wizards.IsLive() && _wizards.IsAlive(event.getPlayer()))
		{
			Player p = event.getPlayer();

			if (p.getInventory().getHeldItemSlot() < _wizards.getWizard(p).getWandsOwned())
			{
				if (event.getAction().name().contains("RIGHT"))
				{
					if (p.getInventory().getHeldItemSlot() < 5)
					{
						if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof InventoryHolder))
						{
							_wizardShop.attemptShopOpen(p);
						}
					}
				}
			}
		}
	}

}
