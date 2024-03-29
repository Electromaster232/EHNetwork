package ehnetwork.game.microgames.game.games.sneakyassassins.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.disguise.disguises.DisguiseFactory;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public abstract class SneakyAssassinKit extends Kit
{
	public static final ItemStack SMOKE_BOMB = ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte) 0, 1,
			C.cYellow + C.Bold + "Right-Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Smoke Bomb",
			new String[]
					{
							ChatColor.RESET + "Throw a Smoke Bomb.",
							ChatColor.RESET + "Everyone within 6 blocks",
							ChatColor.RESET + "gets Blindness for 6 seconds.",

					});

	public SneakyAssassinKit(MicroGamesManager manager, String name, KitAvailability kitAvailability, String[] kitDesc, Perk[] kitPerks, ItemStack itemInHand, EntityType disguiseType)
	{
		super(manager, name, kitAvailability, kitDesc, kitPerks, disguiseType, itemInHand);
	}

	public SneakyAssassinKit(MicroGamesManager manager, String name, KitAvailability kitAvailability, int cost, String[] kitDesc, Perk[] kitPerks, ItemStack itemInHand, EntityType disguiseType)
	{
		super(manager, name, kitAvailability, cost, kitDesc, kitPerks, disguiseType, itemInHand);
	}

	@Override
	public void GiveItems(Player player)
	{
		Manager.GetDisguise().disguise(DisguiseFactory.createDisguise(player, _entityType));
 
		player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
		player.getInventory().setArmorContents(new ItemStack[]{
				new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS),
				new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.LEATHER_HELMET)
		});
		player.getInventory().addItem(SMOKE_BOMB.clone());
	}
}
