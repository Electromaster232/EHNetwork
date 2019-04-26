package nautilus.game.arcade.game.games.smash.kits;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilInv;
import mineplex.core.disguise.disguises.DisguisePig;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.SmashKit;
import nautilus.game.arcade.kit.perks.PerkDoubleJump;
import nautilus.game.arcade.kit.perks.PerkPigBaconBomb;
import nautilus.game.arcade.kit.perks.PerkPigBaconBounce;
import nautilus.game.arcade.kit.perks.PerkPigZombie;
import nautilus.game.arcade.kit.perks.PerkSmashStats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class KitPig extends SmashKit
{
	public KitPig(ArcadeManager manager)
	{
		super(manager, "Pig", KitAvailability.Gem, 7000,

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkSmashStats(5, 1.7, 0.25, 5),
				new PerkDoubleJump("Double Jump", 0.9, 0.9, false),
				new PerkPigBaconBounce(),
				new PerkPigBaconBomb(),
				new PerkPigZombie(),
				
								}, 
								EntityType.PIG,
								new ItemStack(Material.PORK),
								"Pig Stink", 40000, Sound.PIG_DEATH);
	}
	
	@Override
	public void giveCoreItems(Player player)
	{
		UtilInv.Clear(player);
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Bouncy Bacon",
				new String[]
						{
			ChatColor.RESET + "Bouncy Bacon launches a piece of bacon,",
			ChatColor.RESET + "dealing damage and knockback to enemies.",
			ChatColor.RESET + "",
			ChatColor.RESET + "Eat the bacon to restore some Energy.",
			ChatColor.RESET + "Bacon that hit an enemy will restore Health.",
			
						}));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE, (byte)0, 1, 
				C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Baby Bacon Bombs",
				new String[]
						{
			ChatColor.RESET + "Give birth to a baby pig, giving",
			ChatColor.RESET + "yourself a boost forwards. ",
			ChatColor.RESET + "",
			ChatColor.RESET + "Your baby pig will run to annoy",
			ChatColor.RESET + "nearby enemies, exploding on them.",
						}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.PORK, (byte)0, 1, 
					C.cYellow + C.Bold + "Passive" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Nether Pig",
					new String[]
							{
				ChatColor.RESET + "When your health drops below 4, you morph",
				ChatColor.RESET + "into a Nether Pig. This gives you Speed I,",
				ChatColor.RESET + "8 Armor and reduces Energy costs by 33%.",
				ChatColor.RESET + "",
				ChatColor.RESET + "When your health returns to 6, you return",
				ChatColor.RESET + "back to Pig Form.",
							}));
		
		if (Manager.GetGame().GetState() == GameState.Recruit)
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, 
					C.cYellow + C.Bold + "Smash Crystal" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Pig Stink",
					new String[]
							{
				ChatColor.RESET + "Unleash your inner pig, causing all enemies",
				ChatColor.RESET + "to get nausea for a duration, while you become",
				ChatColor.RESET + "a powerful Nether Pig!",
							}));

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
		DisguisePig disguise = new DisguisePig(player);

		if (Manager.GetGame().GetTeam(player) != null)		
			disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		else			
			disguise.setName(player.getName());
		
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
	
	@EventHandler
	public void EnergyUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!HasKit(player))
				continue;

			player.setExp((float) Math.min(0.999, player.getExp() + (isSuperActive(player) ? 0.02 : 0.005)));
		}
	}
	
	@Override
	public void activateSuperCustom(Player player)
	{
		for (Player other : Manager.GetGame().GetPlayers(true))
			if (!player.equals(other))
				Manager.GetCondition().Factory().Confuse("Pig Stink " + player.getName(), other, player, 40, 0, false, false, false);
	}
	
	@Override
	public void deactivateSuperCustom(Player player)
	{

	}
}
