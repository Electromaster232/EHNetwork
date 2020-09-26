package ehnetwork.game.microgames.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseBlaze;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkFirefly;
import ehnetwork.game.microgames.kit.perks.PerkInferno;
import ehnetwork.game.microgames.kit.perks.PerkKnockbackFire;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;
import ehnetwork.game.microgames.kit.perks.PerkSpeed;

public class KitBlaze extends SmashKit
{
	public KitBlaze(MicroGamesManager manager)
	{
		super(manager, "Blaze", KitAvailability.Gem, 8000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.5, 0.15, 5),
				new PerkDoubleJump("Double Jump", 1, 1, false),
				new PerkKnockbackFire(1.50),
				new PerkSpeed(0),
				new PerkInferno(),
				new PerkFirefly()
								}, 
								EntityType.BLAZE,
								new ItemStack(Material.BLAZE_ROD),
								"Phoenix", 18000, Sound.BLAZE_DEATH);

	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Inferno",
				new String[]
						{
			ChatColor.RESET + "Releases a deadly torrent of flames,",
			ChatColor.RESET + "which ignite and damage opponents.",
						}));


		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Firefly",
				new String[]
						{
			ChatColor.RESET + "After a short startup time, you fly",
			ChatColor.RESET + "forward with great power, destroying",
			ChatColor.RESET + "anyone you touch.",
			ChatColor.RESET + "",
			ChatColor.RESET + "If you are hit by a projectile during",
			ChatColor.RESET + "startup time, the skill is cancelled.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Phoenix",
					new String[]
							{
				ChatColor.RESET + "Unleash all your fiery power and",
				ChatColor.RESET + "propel yourself forwards, destroying",
				ChatColor.RESET + "everything that comes into your path."
							}));

		

		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
		player.getInventory().remove(Material.IRON_SWORD);
		player.getInventory().remove(Material.IRON_AXE);
		
		UtilInv.Update(player);
	}

	@Override
	public void GiveItems(Player player) 
	{
		giveCoreItems(player);

		//Disguise
		DisguiseBlaze disguise = new DisguiseBlaze(player);
		
		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}


	@EventHandler
	public void FireItemResist(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (Manager.GetGame() == null)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!HasKit(player))
				continue;

			Manager.GetCondition().Factory().FireItemImmunity(GetName(), player, player, 1.9, false);
		}
	}
}
