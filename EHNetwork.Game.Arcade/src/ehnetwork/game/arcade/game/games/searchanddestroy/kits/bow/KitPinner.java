package ehnetwork.game.arcade.game.games.searchanddestroy.kits.bow;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.castlesiege.kits.KitHuman;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkPinned;

public class KitPinner extends KitHuman
{

    public KitPinner(ArcadeManager manager)
    {
        super(manager, "Pinner", KitAvailability.Hide, new String[]
            {
                    "Your arrows enchanted with gravity magic.", "Victims find it hard to lift", "Their bodies are sluggish"
            }, new Perk[]
            {
                new PerkPinned()
            }, EntityType.SKELETON, new ItemStack(Material.OBSIDIAN));
    }

    @Override
    public void GiveItems(Player player)
    {
        PlayerInventory inv = player.getInventory();
        inv.setArmorContents(new ItemStack[]
            {
                    new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET)
            });
        inv.setItem(2, new ItemBuilder(Material.BLAZE_POWDER).setTitle(ChatColor.GOLD + "Fuse").build());
        inv.addItem(new ItemBuilder(Material.IRON_SWORD).setUnbreakable(true).build());
        inv.addItem(new ItemBuilder(Material.BOW).setUnbreakable(true).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
        inv.setItem(8,
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(this.Manager.GetGame().GetTeam(player).GetColorBase())
                        .setTitle(ChatColor.WHITE + "Evolve Kit Menu").addLore("Right click to use").build());
        inv.setItem(9, new ItemStack(Material.ARROW, 1));
    }

}
