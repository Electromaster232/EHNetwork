package ehnetwork.game.arcade.game.games.searchanddestroy.kits.bow;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitHuman;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkQuickshot;

public class KitQuickshooter extends KitHuman
{
    public KitQuickshooter(ArcadeManager manager)
    {
        super(manager, "Quickshooter", KitAvailability.Hide, new String[]
            {
                "Able to instantly fire an arrow"
            }, new Perk[]
            {
                new PerkQuickshot("Quickshot", 2, 8000)
            }, EntityType.SKELETON, new ItemStack(Material.INK_SACK, 1, (short) 14));
    }

    @Override
    public void GiveItems(Player player)
    {
        PlayerInventory inv = player.getInventory();
        inv.setItem(2, new ItemBuilder(Material.BLAZE_POWDER).setTitle(ChatColor.GOLD + "Fuse").build());
        inv.addItem(new ItemBuilder(Material.IRON_SWORD).setUnbreakable(true).build());
        inv.addItem(new ItemBuilder(Material.BOW).setTitle(C.cGreen + "Left-Click" + C.cWhite + " - " + C.cYellow + "Quickshot")
                .setUnbreakable(true).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
        inv.setItem(9, new ItemStack(Material.ARROW, 1));

        player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_HELMET));
        player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.CHAINMAIL_BOOTS));
        inv.setItem(8,
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(this.Manager.GetGame().GetTeam(player).GetColorBase())
                        .setTitle(ChatColor.WHITE + "Evolve Kit Menu").addLore("Right click to use").build());
    }

}
