package nautilus.game.arcade.game.games.searchanddestroy.kits.bow;

import mineplex.core.itemstack.ItemBuilder;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.castlesiege.kits.KitHuman;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkKnockbackArrow;
import nautilus.game.arcade.kit.perks.PerkSharpshooter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class KitSharpshooter extends KitHuman
{
    public KitSharpshooter(ArcadeManager manager)
    {
        super(manager, "Sharpshooter", KitAvailability.Hide, new String[]
            {
                    "Each consecutive arrow hit deals an additional 2 damage.",
                    "Max of +6. Missing an arrow resets the count to 0."
            }, new Perk[]
            {
                    new PerkKnockbackArrow(2.5D), new PerkSharpshooter()
            }, EntityType.SKELETON, new ItemStack(Material.FLINT));
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
