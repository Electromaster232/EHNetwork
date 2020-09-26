package ehnetwork.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseWolf;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkSmashStats;
import ehnetwork.game.arcade.kit.perks.PerkWolf;

public class KitWolf extends SmashKit
{
	public KitWolf(ArcadeManager manager)
	{
		super(manager, "Wolf", KitAvailability.Gem, 4000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(5, 1.6, 0.3, 4.5),
				new PerkDoubleJump("Wolf Jump", 1.0, 1.0, true),
				new PerkWolf(),
								}, 
								EntityType.WOLF,
								new ItemStack(Material.BONE),
								"Frenzy", 30000, Sound.WOLF_HOWL);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Cub Tackle",
				new String[]
						{
			ChatColor.RESET + "Launch a wolf cub at an opponent.",
			ChatColor.RESET + "If it hits, the cub latches onto the",
			ChatColor.RESET + "opponent, preventing them from moving",
			ChatColor.RESET + "for up to 5 seconds.",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Wolf Strike",
				new String[]
						{
			ChatColor.RESET + "Leap forward with great power.",
			ChatColor.RESET + "If you collide with an enemy, you deal",
			ChatColor.RESET + "damage to them. If they are being tackled",
			ChatColor.RESET + "by a cub, it deals 300% Knockback.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BONE, (byte)0, 1, 
					C.cYellow + C.Bold + "Passive" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Ravage",
					new String[]
							{
				ChatColor.RESET + "When you attack someone, you receive",
				ChatColor.RESET + "+1 Damage for 3 seconds. Bonus damage",
				ChatColor.RESET + "stacks from multiple hits.",
							}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Frenzy",
					new String[]
							{
				ChatColor.RESET + "Gain incredible speed, regeneration",
				ChatColor.RESET + "and damage. All your abilities recharge",
				ChatColor.RESET + "extremely rapidly.",
							}));

		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
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
		DisguiseWolf disguise = new DisguiseWolf(player);

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
		Manager.GetCondition().Factory().Strength(GetName(), player, player, 30, 1, false, false, false);
		Manager.GetCondition().Factory().Speed(GetName(), player, player, 30, 2, false, false, false);
		Manager.GetCondition().Factory().Regen(GetName(), player, player, 30, 2, false, false, false);
		
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise instanceof DisguiseWolf)
		{
			((DisguiseWolf)disguise).setAngry(true);
			Manager.GetDisguise().updateDisguise(disguise);
		}
	}
	
	@Override
	public void deactivateSuperCustom(Player player)
	{
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise instanceof DisguiseWolf)
		{
			((DisguiseWolf)disguise).setAngry(false);
			Manager.GetDisguise().updateDisguise(disguise);
		}
	}
}
