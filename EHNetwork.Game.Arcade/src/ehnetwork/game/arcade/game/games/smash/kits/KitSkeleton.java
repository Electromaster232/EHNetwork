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
import ehnetwork.game.arcade.kit.perks.PerkBarrage;
import ehnetwork.game.arcade.kit.perks.PerkBoneExplosion;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;
import ehnetwork.game.arcade.kit.perks.PerkKnockbackArrow;
import ehnetwork.game.arcade.kit.perks.PerkRopedArrow;
import ehnetwork.game.arcade.kit.perks.PerkSkeletonArrowStorm;
import ehnetwork.game.arcade.kit.perks.PerkSmashStats;

public class KitSkeleton extends SmashKit
{
	public KitSkeleton(ArcadeManager manager)
	{
		super(manager, "Skeleton", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(5, 1.25, 0.20, 6),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkFletcher(2, 4, false),	
				new PerkKnockbackArrow(1.75),	
				new PerkBoneExplosion(),
				new PerkRopedArrow("Roped Arrow", 1, 5000),
				new PerkBarrage(5, 300, true, false, true),
				new PerkSkeletonArrowStorm()
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.BOW),
								"Arrow Storm", 8000, Sound.SKELETON_HURT);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bone Explosion",
				new String[]
						{
			ChatColor.RESET + "Releases an explosion of bones from",
			ChatColor.RESET + "your body, repelling all nearby enemies.",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, 
				C.cYellow + C.Bold + "Left-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Roped Arrow",
				new String[]
						{
			ChatColor.RESET + "Instantly fires an arrow. When it ",
			ChatColor.RESET + "collides with something, you are pulled",
			ChatColor.RESET + "towards it, with great power.",
						}));

		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.ARROW, (byte)0, 1, 
					C.cYellow + C.Bold + "Charge Bow" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Barrage",
					new String[]
							{
				ChatColor.RESET + "Slowly load more arrows into your bow.",
				ChatColor.RESET + "When you release, you will quickly fire",
				ChatColor.RESET + "all the arrows in succession.",
							}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Arrow Storm",
					new String[]
							{
				ChatColor.RESET + "Fire hundreds of arrows in quick succession",
				ChatColor.RESET + "which deal damage and knockback to enemies.",
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
