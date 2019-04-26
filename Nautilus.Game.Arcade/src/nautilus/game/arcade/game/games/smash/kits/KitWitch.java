package nautilus.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilInv;
import mineplex.core.disguise.disguises.DisguiseWitch;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.kit.perks.*;

public class KitWitch extends SmashKit
{
	public KitWitch(ArcadeManager manager)
	{
		super(manager, "Witch", KitAvailability.Gem, 6000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.5, 0.3, 5),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkWitchPotion(),
				new PerkBatWave(),
				new PerkBatForm()
				
				
								}, 
								EntityType.WITCH,
								new ItemStack(Material.POTION),
								"Bat Form", 20000, Sound.BAT_HURT);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Daze Potion",
				new String[]
						{
			ChatColor.RESET + "Throw a potion that damages and slows",
			ChatColor.RESET + "anything it splashes onto!",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bat Wave",
				new String[]
						{
			ChatColor.RESET + "Release a wave of bats which give",
			ChatColor.RESET + "damage and knockback to anything they",
			ChatColor.RESET + "collide with.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.LEASH, (byte)0, 1, 
					C.cYellow + C.Bold + "Double Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bat Leash",
					new String[]
							{
				ChatColor.RESET + "Attach a rope to your wave of bats,",
				ChatColor.RESET + "causing you to be pulled behind them!",
							}));
				
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bat Form",
					new String[]
							{
				ChatColor.RESET + "Transform into a bat that can fly and",
				ChatColor.RESET + "launch powerful sonic blasts at opponents,",
				ChatColor.RESET + "dealing damage and knockback.",
							}));

		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
	}
	
	@Override
	public void giveSuperItems(Player player)
	{
		player.getInventory().remove(Material.IRON_SPADE);
		player.getInventory().remove(Material.IRON_AXE);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Sonic Boom",
				new String[]
						{
			ChatColor.RESET + "Screech loudly to create a sonic boom",
			ChatColor.RESET + "that deals damage and knockback to enemies!",
						}));
		
		UtilInv.Update(player);
	}
	
	@Override
	public void GiveItems(Player player) 
	{
		giveCoreItems(player);
		
		//Disguise
		DisguiseWitch disguise = new DisguiseWitch(player);

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
		for (Perk perk : GetPerks())
			if (perk instanceof PerkDoubleJump)
				((PerkDoubleJump)perk).disableForPlayer(player);
	}
	
	@Override
	public void deactivateSuperCustom(Player player)
	{
		for (Perk perk : GetPerks())
			if (perk instanceof PerkDoubleJump)
				((PerkDoubleJump)perk).enableForPlayer(player);
	}
}
