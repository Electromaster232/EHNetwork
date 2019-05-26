package nautilus.game.arcade.game.games.sneakyassassins.kits;

import mineplex.core.common.util.*;
import mineplex.core.disguise.*;
import mineplex.core.disguise.disguises.*;
import mineplex.core.itemstack.*;
import nautilus.game.arcade.*;
import nautilus.game.arcade.kit.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

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

	public SneakyAssassinKit(ArcadeManager manager, String name, KitAvailability kitAvailability, String[] kitDesc, Perk[] kitPerks, ItemStack itemInHand, EntityType disguiseType)
	{
		super(manager, name, kitAvailability, kitDesc, kitPerks, disguiseType, itemInHand);
	}

	public SneakyAssassinKit(ArcadeManager manager, String name, KitAvailability kitAvailability, int cost, String[] kitDesc, Perk[] kitPerks, ItemStack itemInHand, EntityType disguiseType)
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
