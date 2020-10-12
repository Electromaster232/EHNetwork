package ehnetwork.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseCreeper;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.Game.GameState;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.SmashKit;
import ehnetwork.game.arcade.kit.perks.PerkCreeperElectricity;
import ehnetwork.game.arcade.kit.perks.PerkCreeperExplode;
import ehnetwork.game.arcade.kit.perks.PerkCreeperSulphurBomb;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkSmashStats;

public class KitCreeper extends SmashKit
{
	public KitCreeper(ArcadeManager manager)
	{
		super(manager, "Creeper", KitAvailability.Gem, 4000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.65, 0.4, 3.5),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkCreeperElectricity(),
				new PerkCreeperSulphurBomb(),
				new PerkCreeperExplode(),
								}, 
								EntityType.CREEPER,
								new ItemStack(Material.TNT),
								"Atomic Blast", 0, Sound.CREEPER_DEATH);
	} 

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Sulphur Bomb",
				new String[]
						{
			ChatColor.RESET + "Throw a small bomb of sulphur.",
			ChatColor.RESET + "Explodes on contact with players,",
			ChatColor.RESET + "dealing some damage and knockback.",

						}));

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Explosive Leap",
				new String[]
						{
			ChatColor.RESET + "You freeze in location and charge up",
			ChatColor.RESET + "for 1.5 seconds. Then you explode!",
			ChatColor.RESET + "You are sent flying in the direction",
			ChatColor.RESET + "you are looking, while opponents take",
			ChatColor.RESET + "large damage and knockback.",

						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.COAL, (byte)0, 1, 
					C.cYellow + C.Bold + "Passive" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Lightning Shield",
					new String[]
							{
				ChatColor.RESET + "When attacked by a non-melee attack,",
				ChatColor.RESET + "you gain Lightning Shield for 2 seconds.",
				ChatColor.RESET + "",
				ChatColor.RESET + "Lightning Shield blocks 1 melee attack,",
				ChatColor.RESET + "striking lightning on the attacker.",
							}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Atomic Blast",
					new String[]
							{
				ChatColor.RESET + "After a short duration, you will explode",
				ChatColor.RESET + "a gigantic explosion which destroys the",
				ChatColor.RESET + "map and everyone nearby. You are sent flying",
				ChatColor.RESET + "in the direction you are looking."
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.LEATHER_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.LEATHER_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.LEATHER_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.LEATHER_BOOTS));
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
		DisguiseCreeper disguise = new DisguiseCreeper(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
