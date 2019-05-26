package nautilus.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilInv;
import mineplex.core.disguise.disguises.DisguiseZombie;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.kit.perks.PerkDoubleJump;
import nautilus.game.arcade.kit.perks.PerkFletcher;
import nautilus.game.arcade.kit.perks.PerkKnockbackArrow;
import nautilus.game.arcade.kit.perks.PerkNightLivingDead;
import nautilus.game.arcade.kit.perks.PerkOvercharge;
import nautilus.game.arcade.kit.perks.PerkSmashStats;
import nautilus.game.arcade.kit.perks.PerkDeathsGrasp;
import nautilus.game.arcade.kit.perks.PerkZombieBile;

public class KitZombie extends SmashKit
{
	public KitZombie(ArcadeManager manager)
	{
		super(manager, "Zombie", KitAvailability.Gem, 6000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.25, 0.3, 4.5),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkFletcher(2, 2, false),	
				new PerkKnockbackArrow(1.5),	
				new PerkOvercharge(6, 250, true),
				new PerkZombieBile(),
				new PerkDeathsGrasp(),
				new PerkNightLivingDead()
				
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.BOW),
								"Night of the Living Dead", 0, Sound.AMBIENCE_CAVE);
	}
	
	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Left-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bile Blaster",
				new String[]
						{
			ChatColor.RESET + "Spew up your dinner from last night.",
			ChatColor.RESET + "Deals damage and knockback to enemies.",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Deaths Grasp",
				new String[]
						{
			ChatColor.RESET + "Leap forwards. If you collide with an ",
			ChatColor.RESET + "opponent, you deal damage, throw them ",
			ChatColor.RESET + "behind you and recharge the ability.",
			ChatColor.RESET + "",
			ChatColor.RESET + "Arrows deal double damage to enemies",
			ChatColor.RESET + "recently hit by Deaths Grasp.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.ARROW, (byte)0, 1, 
					C.cYellow + C.Bold + "Left-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Corrupted Arrow",
					new String[]
							{
				ChatColor.RESET + "Charge your arrows to corrupt them,",
				ChatColor.RESET + "adding up to an additional 6 damage.",
							}));
		
				
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Night of the Living Dead",
					new String[]
							{
				ChatColor.RESET + "Cast the world into darkness as hundreds",
				ChatColor.RESET + "of undead minions sprout up from the ground",
				ChatColor.RESET + "to attack your enemies.",
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
		DisguiseZombie disguise = new DisguiseZombie(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
