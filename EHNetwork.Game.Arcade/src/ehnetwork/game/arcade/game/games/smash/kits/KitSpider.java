package ehnetwork.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseSpider;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.perks.PerkNeedler;
import ehnetwork.game.arcade.kit.perks.PerkSmashStats;
import ehnetwork.game.arcade.kit.perks.PerkSpiderLeap;
import ehnetwork.game.arcade.kit.perks.PerkSpidersNest;
import ehnetwork.game.arcade.kit.perks.PerkWebShot;

public class KitSpider extends SmashKit
{
	public KitSpider(ArcadeManager manager)
	{
		super(manager, "Spider", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(7, 1.5, 0.3, 6),
				new PerkSpiderLeap(),
				new PerkNeedler(),
				new PerkWebShot(),
				new PerkSpidersNest()
								}, 
								EntityType.SPIDER,
								new ItemStack(Material.WEB),
								"Spider Nest", 30000, Sound.SPIDER_DEATH);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold/Release Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Needler",
				new String[]
						{
			ChatColor.RESET + "Quickly spray up to 5 needles from ",
			ChatColor.RESET + "your mouth, dealing damage and small",
			ChatColor.RESET + "knockback to opponents.",
						}));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Spin Web",
				new String[]
						{
			ChatColor.RESET + "Spray out webs behind you, launching",
			ChatColor.RESET + "yourself forwards. Webs will damage",
			ChatColor.RESET + "opponents and spawn temporary web blocks.",
						}));

		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.SPIDER_EYE, (byte)0, 1, 
					C.cYellow + C.Bold + "Double Jump" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Spider Leap",
					new String[]
							{
				ChatColor.RESET + "Your double jump is special. It goes",
				ChatColor.RESET + "exactly in the direction you are looking.",
				ChatColor.RESET + "",
				ChatColor.RESET + C.cAqua + "Spider Leap uses Energy (Experience Bar)",
							}));

		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.FERMENTED_SPIDER_EYE, (byte)0, 1, 
					C.cYellow + C.Bold + "Crouch" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wall Climb",
					new String[]
							{
				ChatColor.RESET + "While crouching, you climb up walls.",
				ChatColor.RESET + "",
				ChatColor.RESET + "Climbing a wall allows you to use",
				ChatColor.RESET + "Spider Leap one more time.",
				ChatColor.RESET + "",
				ChatColor.RESET + C.cAqua + "Wall Climb uses Energy (Experience Bar)",
							}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Spiders Nest",
					new String[]
							{
				ChatColor.RESET + "Spawn a nest of webs around you to trap",
				ChatColor.RESET + "enemy players. Your attacks heal you and",
				ChatColor.RESET + "permanently increase your health. ",
				ChatColor.RESET + "",
				ChatColor.RESET + "Your abilities have a one second recharge.",
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
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
		DisguiseSpider disguise = new DisguiseSpider(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
