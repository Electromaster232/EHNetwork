package ehnetwork.game.microgames.game.games.smash.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.disguise.disguises.DisguiseIronGolem;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.SmashKit;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkEarthquake;
import ehnetwork.game.microgames.kit.perks.PerkFissure;
import ehnetwork.game.microgames.kit.perks.PerkIronHook;
import ehnetwork.game.microgames.kit.perks.PerkSeismicSlam;
import ehnetwork.game.microgames.kit.perks.PerkSlow;
import ehnetwork.game.microgames.kit.perks.PerkSmashStats;

public class KitGolem extends SmashKit
{
	public KitGolem(MicroGamesManager manager)
	{
		super(manager, "Iron Golem", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(7, 1.0, 0.25, 8),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkSlow(0),
				new PerkFissure(),
				new PerkIronHook(),
				new PerkSeismicSlam(),
				new PerkEarthquake()
				
								}, 
								EntityType.IRON_GOLEM,
								new ItemStack(Material.IRON_BLOCK),
								"Earthquake", 0, Sound.IRONGOLEM_DEATH);
	}

	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Fissure",
				new String[]
						{
			ChatColor.RESET + "Smash the ground, creating a fissure",
			ChatColor.RESET + "which slowly rises in a line, dealing",
			ChatColor.RESET + "damage and knockback to anyone it hits!",
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_PICKAXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Iron Hook",
				new String[]
						{
			ChatColor.RESET + "Throw a metal hook at opponents.",
			ChatColor.RESET + "If it hits, it deals damage and pulls",
			ChatColor.RESET + "them towards you with great force.",
						}));
		
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Seismic Slam",
				new String[]
						{
			ChatColor.RESET + "Take a mighty leap into the air, then",
			ChatColor.RESET + "slam back into the ground with huge force.",
			ChatColor.RESET + "Nearby opponents take damage and knockback.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Earthquake",
					new String[]
							{
				ChatColor.RESET + "Begin an earthquake that will give damage",
				ChatColor.RESET + "and knockback to any player who is touching",
				ChatColor.RESET + "the ground, anywhere on the map!",
							}));

		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_BOOTS));
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
		DisguiseIronGolem disguise = new DisguiseIronGolem(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
