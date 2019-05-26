package nautilus.game.arcade.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilInv;
import mineplex.core.disguise.disguises.DisguiseSquid;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.kit.perks.PerkDoubleJump;
import nautilus.game.arcade.kit.perks.PerkInkBlast;
import nautilus.game.arcade.kit.perks.PerkSmashStats;
import nautilus.game.arcade.kit.perks.PerkStormSquid;
import nautilus.game.arcade.kit.perks.PerkSuperSquid;

public class KitSkySquid extends SmashKit
{
	public KitSkySquid(ArcadeManager manager)
	{
		super(manager, "Sky Squid", KitAvailability.Gem, 3000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(6, 1.5, 0.25, 5),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkSuperSquid(),
				new PerkInkBlast(),
				new PerkStormSquid()
								}, 
								EntityType.SQUID,
								new ItemStack(Material.INK_SACK),
								"Storm Squid", 24000, Sound.SPLASH2);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Ink Shotgun",
				new String[]
						{
			ChatColor.RESET + "Blasts 6 ink pellets out at high velocity.",
			ChatColor.RESET + "They explode upon hitting something, dealing",
			ChatColor.RESET + "damage and knockback.",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold/Release Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Super Squid",
				new String[]
						{
			ChatColor.RESET + "You become invulnerable and fly through",
			ChatColor.RESET + "the sky in the direction you are looking.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Storm Squid",
					new String[]
							{
				ChatColor.RESET + "Gain permanent flight, as well as the ability",
				ChatColor.RESET + "to strike lightning at your target location",
				ChatColor.RESET + "after a short delay.",
							}));


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
				C.cYellow + C.Bold + "Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Lightning Strike",
				new String[]
						{
			ChatColor.RESET + "Strikes lightning at target location after",
			ChatColor.RESET + "a short delay. Deals large damage and knockback.",
						}));
		
		UtilInv.Update(player);
	}
	
	@Override
	public void GiveItems(Player player) 
	{		
		giveCoreItems(player);
		
		//Disguise
		DisguiseSquid disguise = new DisguiseSquid(player);

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
		Manager.GetGame().WorldWeatherEnabled = true;
		Manager.GetGame().WorldData.World.setStorm(true);
		Manager.GetGame().WorldData.World.setThundering(true);
		Manager.GetGame().WorldData.World.setThunderDuration(9999);
	}
	
	@Override
	public void deactivateSuperCustom(Player player)
	{
		Manager.GetGame().WorldWeatherEnabled = false;
	}
}
