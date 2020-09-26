package ehnetwork.game.arcade.addons;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTextBottom;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.gui.spectatorMenu.SpectatorShop;

public class CompassAddon extends MiniPlugin
{
	public ArcadeManager Manager;

	private SpectatorShop _spectatorShop;

	public CompassAddon(JavaPlugin plugin, ArcadeManager manager)
	{
		super("Compass Addon", plugin);

		Manager = manager;

		_spectatorShop = new SpectatorShop(this, manager, manager.GetClients(), manager.GetDonation());
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;

		if (Manager.GetGame() == null)
			return;

		if (!Manager.GetGame().IsLive())
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (!Manager.GetGame().CompassEnabled && Manager.GetGame().IsAlive(player)) 
				continue;

			GameTeam team = Manager.GetGame().GetTeam(player);

			Player target = null;
			GameTeam targetTeam = null;
			double bestDist = 0;

			for (Player other : Manager.GetGame().GetPlayers(true))
			{
				if (other.equals(player))
					continue;

				GameTeam otherTeam = Manager.GetGame().GetTeam(other);

				//Same Team (Not Solo Game) && Alive
				if (Manager.GetGame().GetTeamList().size() > 1 && (team != null && team.equals(otherTeam)) && Manager.GetGame().IsAlive(player))
					continue;

				double dist = UtilMath.offset(player, other);

				if (target == null || dist < bestDist)
				{
					target = other;
					targetTeam = otherTeam;
					bestDist = dist;
				}
			}

			if (target != null)
			{
				if (Manager.GetGame().CompassGiveItem || Manager.isSpectator(player))
					if (!player.getInventory().contains(Material.COMPASS))
					{
						if (player.getOpenInventory() == null || player.getOpenInventory().getCursor() == null || player.getOpenInventory().getCursor().getType() != Material.COMPASS)
						{
							ItemStack stack = new ItemStack(Material.COMPASS);
							
							ItemMeta itemMeta = stack.getItemMeta();
							itemMeta.setDisplayName(C.cGreen + C.Bold + "Tracking Compass");
							stack.setItemMeta(itemMeta);
							
							player.getInventory().addItem(stack);
						}
					}


				player.setCompassTarget(target.getLocation());

				double heightDiff = target.getLocation().getY() - player.getLocation().getY();

				//Action Bar
				if (UtilPlayer.is1_8(player))
				{
					if (UtilGear.isMat(player.getItemInHand(), Material.COMPASS))
					{
						UtilTextBottom.display(
								"    " + C.cWhite + C.Bold + "Nearest Player: " + targetTeam.GetColor() + target.getName() + 
								"    " + C.cWhite + C.Bold + "Distance: " + targetTeam.GetColor() + UtilMath.trim(1, bestDist) +
								"    " + C.cWhite + C.Bold + "Height: " + targetTeam.GetColor() + UtilMath.trim(1, heightDiff), player);
					}
				}
				//Name Compass
				else
				{
					for (int i : player.getInventory().all(Material.COMPASS).keySet()) 
					{
						ItemStack stack = player.getInventory().getItem(i);

						ItemMeta itemMeta = stack.getItemMeta();
						itemMeta.setDisplayName(
								"    " + C.cWhite + C.Bold + "Nearest Player: " + targetTeam.GetColor() + target.getName() + 
								"    " + C.cWhite + C.Bold + "Distance: " + targetTeam.GetColor() + UtilMath.trim(1, bestDist) +
								"    " + C.cWhite + C.Bold + "Height: " + targetTeam.GetColor() + UtilMath.trim(1, heightDiff));
						stack.setItemMeta(itemMeta);

						player.getInventory().setItem(i, stack);
					}
				}
			}
		}
	}

	@EventHandler
	public void DropItem(PlayerDropItemEvent event)
	{
		if (Manager.GetGame() == null || !Manager.GetGame().CompassEnabled)	
			return;

		if (!UtilInv.IsItem(event.getItemDrop().getItemStack(), Material.COMPASS, (byte)0))
			return;

		//Cancel
		event.setCancelled(true);

		//Inform
		UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot drop " + F.item("Target Compass") + "."));
	}

	@EventHandler
	public void DeathRemove(PlayerDeathEvent event)
	{	
		if (Manager.GetGame() == null || !Manager.GetGame().CompassEnabled)	
			return;

		HashSet<org.bukkit.inventory.ItemStack> remove = new HashSet<org.bukkit.inventory.ItemStack>();

		for (org.bukkit.inventory.ItemStack item : event.getDrops())
			if (UtilInv.IsItem(item, Material.COMPASS, (byte)0))
				remove.add(item);

		for (org.bukkit.inventory.ItemStack item : remove)
			event.getDrops().remove(item);
	}

	@EventHandler
	public void SpectatorTeleport(PlayerInteractEvent event)
	{
		if (Manager.GetGame() == null)
			return;
		
		if (event.getAction() == Action.PHYSICAL)
		    return;

		Player player = event.getPlayer();

		if (!UtilGear.isMat(player.getItemInHand(), Material.COMPASS))
			return;

		if (Manager.GetGame().IsAlive(player))
			return;

		event.setCancelled(true);

		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			// Teleport to nearest player when you left click compass

			if (!Recharge.Instance.use(player, "Spectate", 5000, true, false))
			{
				return;
			}

			spectateNearestPlayer(player);
		}
		else
		{
			// Right click - open spectator menu

			_spectatorShop.attemptShopOpen(player);
		}
	}

	private void spectateNearestPlayer(Player spectator)
	{
		GameTeam team = Manager.GetGame().GetTeam(spectator);

		Player target = null;
		double bestDist = 0;

		for (Player other : Manager.GetGame().GetPlayers(true))
		{
			GameTeam otherTeam = Manager.GetGame().GetTeam(other);

			//Same Team (Not Solo Game) && Alive
			if (Manager.GetGame().GetTeamList().size() > 1 && (team != null && team.equals(otherTeam)) && Manager.GetGame().IsAlive(spectator))
				continue;

			double dist = UtilMath.offset(spectator, other);

			if (target == null || dist < bestDist)
			{
				target = other;
				bestDist = dist;
			}
		}

		if (target != null)
		{
			spectator.teleport(target.getLocation().add(0, 1, 0));
		}
	}

	@EventHandler
	public void closeShop(GameStateChangeEvent event)
	{
		// Close shop when a game ends
		if (event.GetState().equals(Game.GameState.End))
		{
			for (Player player : UtilServer.getPlayers())
			{
				if (_spectatorShop.isPlayerInShop(player))
					player.closeInventory();
			}
		}
	}

	@EventHandler
	public void updateShop(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		_spectatorShop.update();
	}

}
