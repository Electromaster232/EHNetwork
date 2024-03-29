package ehnetwork.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkSmashStats;
import ehnetwork.game.arcade.kit.perks.PerkWitherForm;
import ehnetwork.game.arcade.kit.perks.PerkWitherImage;
import ehnetwork.game.arcade.kit.perks.PerkWitherSkull;

public class KitWitherSkeleton extends SmashKit
{
	public KitWitherSkeleton(ArcadeManager manager)
	{
		super(manager, "Wither Skeleton", KitAvailability.Gem, 6000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.2, 0.3, 6),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkWitherSkull(),
				new PerkWitherImage(),
				new PerkWitherForm()
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.IRON_SWORD),
								"Wither Form", 18000, Sound.WITHER_SPAWN);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Guided Wither Skull",
				new String[]
						{
			ChatColor.RESET + "Launch a Wither Skull forwards, hold",
			ChatColor.RESET + "block to guide the missile! Release",
			ChatColor.RESET + "block to detonate it midair.",
						}));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wither Image",
				new String[]
						{
			ChatColor.RESET + "Create an exact image of yourself.",
			ChatColor.RESET + "The copy is launched forwards with",
			ChatColor.RESET + "high speeds. Lasts 8 seconds.",
			ChatColor.RESET + "",
			ChatColor.RESET + "Use the skill again to swap positions",
			ChatColor.RESET + "with your image.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wither Form",
					new String[]
							{
				ChatColor.RESET + "Transform into a legendary Wither that is",
				ChatColor.RESET + "able to launch wither skulls at opponents,",
				ChatColor.RESET + "dealing damage and knockback.",
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
		player.getInventory().remove(Material.IRON_SWORD);
		player.getInventory().remove(Material.IRON_AXE);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wither Skull",
				new String[]
						{
			ChatColor.RESET + "Launch a deadly Wither Skull forwards.",
						}));
		
		UtilInv.Update(player);
	}
	
	@Override
	public void GiveItems(Player player) 
	{
		giveCoreItems(player);
		
		//Disguise
		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.SKELETON);
		MobDisguise disguise = (MobDisguise) d1;
		if (Manager.GetGame().GetTeam(player) != null)
			disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else
			disguise.setCustomName(player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
	}
}
