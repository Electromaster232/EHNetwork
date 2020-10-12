package ehnetwork.game.microgames.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseMagmaCube;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkFlameDash;
import ehnetwork.game.microgames.kit.perks.PerkMagmaBlast;
import ehnetwork.game.microgames.kit.perks.PerkMagmaBoost;
import ehnetwork.game.microgames.kit.perks.PerkMeteorShower;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;

public class KitMagmaCube extends SmashKit
{
	public KitMagmaCube(MicroGamesManager manager)
	{
		super(manager, "Magma Cube", KitAvailability.Gem, 5000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(5, 1.75, 0.4, 5),
				new PerkDoubleJump("Double Jump", 1.2, 1, false),
				new PerkMagmaBoost(),
				new PerkMagmaBlast(),
				new PerkFlameDash(),
				new PerkMeteorShower()
				
				
								}, 
								EntityType.MAGMA_CUBE,
								new ItemStack(Material.MAGMA_CREAM),
								"Meteor Shower", 0, Sound.AMBIENCE_THUNDER);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Magma Blast",
				new String[]
						{
			ChatColor.RESET + "Release a powerful ball of magma which explodes",
			ChatColor.RESET + "on impact, dealing damage and knockback.",
			ChatColor.RESET + "",
			ChatColor.RESET + "You receive strong knockback when you shoot it.",
			ChatColor.RESET + "Use this knockback to get back onto the map!",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Flame Dash",
				new String[]
						{
			ChatColor.RESET + "Disappear in flames, and fly horizontally",
			ChatColor.RESET + "in the direction you are looking. You explode",
			ChatColor.RESET + "when you re-appear, dealing damage to enemies.",
			ChatColor.RESET + "",
			ChatColor.RESET + "Damage increases with distance travelled.",
			ChatColor.RESET + "",
			ChatColor.RESET + "Right-Click again to end Flame Dash early.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Meteor Shower",
					new String[]
							{
				ChatColor.RESET + "Summon a deadly meteor shower that will rain",
				ChatColor.RESET + "down on your target location, causing extreme",
				ChatColor.RESET + "damage and knockback to enemies!",
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
	
	}
	
	@Override
	public void GiveItems(Player player) 
	{
		giveCoreItems(player);
		
		//Disguise
		DisguiseMagmaCube disguise = new DisguiseMagmaCube(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
		 
		disguise.SetSize(1);
	}
}
