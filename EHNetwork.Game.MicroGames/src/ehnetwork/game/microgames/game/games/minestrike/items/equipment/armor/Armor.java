package ehnetwork.game.microgames.game.games.minestrike.items.equipment.armor;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.game.games.minestrike.MineStrike;
import ehnetwork.game.microgames.game.games.minestrike.items.StrikeItem;
import ehnetwork.game.microgames.game.games.minestrike.items.StrikeItemType;

public class Armor extends StrikeItem
{
	public Armor(String name, String[] desc, int cost, int gemCost, Material skin)
	{
		super(StrikeItemType.ARMOR, name, desc, cost, gemCost, skin);
	}

	@Override
	public boolean pickup(MineStrike game, Player player)
	{
		return false;
	}
	
	public void giveToPlayer(Player player, Color color)
	{
		ItemStack armor = new ItemStack(getSkin());
		LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
		meta.setColor(color);
		meta.setDisplayName(getName());
		armor.setItemMeta(meta);
		
		if (getSkin() == Material.LEATHER_CHESTPLATE)
			player.getInventory().setChestplate(armor);
		else
			player.getInventory().setHelmet(armor);
		
		UtilPlayer.message(player, F.main("Game", "You equipped " + getName() + "."));
		
		player.getWorld().playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);
	}
	
	public static boolean isArmor(ItemStack stack)
	{
		if (stack == null)
			return false;

		try
		{
			LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
			return (meta.getColor().getBlue() == 100 || meta.getColor().getRed() == 100);
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	@Override
	public String getShopItemType()
	{
		return C.cDGreen + C.Bold + "Armor" + ChatColor.RESET;
	}
}
