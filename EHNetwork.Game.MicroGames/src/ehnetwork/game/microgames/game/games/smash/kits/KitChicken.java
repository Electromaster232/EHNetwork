package ehnetwork.game.microgames.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseChicken;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkChickenRocket;
import ehnetwork.game.microgames.kit.perks.PerkEggGun;
import ehnetwork.game.microgames.kit.perks.PerkFlap;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;

public class KitChicken extends SmashKit
{
	public KitChicken(MicroGamesManager manager)
	{
		super(manager, "Chicken", KitAvailability.Gem, 8000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(4, 2.0, 0.1, 1.5),
				new PerkFlap(0.8, 0.8, false),
				new PerkEggGun(),
				new PerkChickenRocket()
								}, 
								EntityType.CHICKEN,
								new ItemStack(Material.EGG),
								"Aerial Gunner", 20000, Sound.CHICKEN_HURT);

	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Egg Blaster",
				new String[]
						{
			ChatColor.RESET + "Unleash a barrage of your precious eggs.",
			ChatColor.RESET + "They won't deal any knockback, but they",
			ChatColor.RESET + "can deal some serious damage.",
						}));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Chicken Missile",
				new String[]
						{
			ChatColor.RESET + "Launch one of your newborn babies.",
			ChatColor.RESET + "It will fly forwards and explode if it",
			ChatColor.RESET + "collides with anything, giving large",
			ChatColor.RESET + "damage and knockback to players.",
						}));

		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.FEATHER, (byte)0, 1, 
					C.cYellow + C.Bold + "Passive" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Flap",
					new String[]
							{
				ChatColor.RESET + "You are able to use your double jump",
				ChatColor.RESET + "up to 6 times in a row. However, with",
				ChatColor.RESET + "each flap, it loses some potency.",
				ChatColor.RESET + "",
				ChatColor.RESET + C.cAqua + "Flap uses Energy (Experience Bar)",
							}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Airial Gunner",
					new String[]
							{
				ChatColor.RESET + "Unleash an unlimited barrage of eggs",
				ChatColor.RESET + "while also gaining permanant flight.",
							}));

		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.LEATHER_CHESTPLATE));
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
		DisguiseChicken disguise = new DisguiseChicken(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());

		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}

	@Override
	public void activateSuperCustom(Player player)
	{
		player.setExp(0.99f);
	}
}
