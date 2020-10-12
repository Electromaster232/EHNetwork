package ehnetwork.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseSheep;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkLazer;
import ehnetwork.game.arcade.kit.perks.PerkSheepHoming;
import ehnetwork.game.arcade.kit.perks.PerkSmashStats;
import ehnetwork.game.arcade.kit.perks.PerkWoolBomb;
import ehnetwork.game.arcade.kit.perks.PerkWoolCloud;

public class KitSheep extends SmashKit
{
	public KitSheep(ArcadeManager manager)
	{
		super(manager, "Sir. Sheep", KitAvailability.Achievement, 99999,

				new String[] 
						{
						},  

						new Perk[] 
								{
				new PerkSmashStats(5, 1.7, 0.25, 5),
				new PerkDoubleJump("Double Jump", 1, 1, false),
				new PerkLazer(40, 6000),
				new PerkWoolBomb(),
				new PerkWoolCloud(),
				new PerkSheepHoming()
				
								}, 
								EntityType.SHEEP,
								new ItemStack(Material.WOOL),
								"Homing Sheeples", 0, Sound.SHEEP_IDLE);
		
		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.SMASH_MOBS_1V3,
				Achievement.SMASH_MOBS_FREE_KITS,
				Achievement.SMASH_MOBS_MLG_PRO,
				Achievement.SMASH_MOBS_RECOVERY_MASTER,
				Achievement.SMASH_MOBS_TRIPLE_KILL,
				Achievement.SMASH_MOBS_WINS,
				});
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Static Lazer",
				new String[]
						{
			ChatColor.RESET + "Charge up static electricity in your",
			ChatColor.RESET + "wooly coat, and then unleash it upon",
			ChatColor.RESET + "enemies in a devastating laser beam!",
			
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wool Mine",
				new String[]
						{
			ChatColor.RESET + "Shear yourself and use the wool as",
			ChatColor.RESET + "an explosive device. You can Right-Click",
			ChatColor.RESET + "a second time to solidify the bomb, and",
			ChatColor.RESET + "a third time to detonate it on command.",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wooly Rocket",
				new String[]
						{
			ChatColor.RESET + "Become like a cloud and shoot yourself",
			ChatColor.RESET + "directly upwards. You deal damage to anyone",
			ChatColor.RESET + "you collide with on your ascent.",
			ChatColor.RESET + "",
			ChatColor.RESET + "Using this recharges your Double Jump.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Homing Sheeples",
					new String[]
							{
				ChatColor.RESET + "Release one Homing Sheeple towards every player.",
				ChatColor.RESET + "They will slowly home in on their target and",
				ChatColor.RESET + "explode to deal devastating damage.",
							}));

		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
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
		DisguiseSheep disguise = new DisguiseSheep(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setColor(DyeColor.WHITE);
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
