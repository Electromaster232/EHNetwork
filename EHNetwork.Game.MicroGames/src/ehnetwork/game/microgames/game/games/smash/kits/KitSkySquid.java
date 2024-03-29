package ehnetwork.game.microgames.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseSquid;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkInkBlast;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;
import ehnetwork.game.microgames.kit.perks.PerkStormSquid;
import ehnetwork.game.microgames.kit.perks.PerkSuperSquid;

public class KitSkySquid extends SmashKit
{
	public KitSkySquid(MicroGamesManager manager)
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
