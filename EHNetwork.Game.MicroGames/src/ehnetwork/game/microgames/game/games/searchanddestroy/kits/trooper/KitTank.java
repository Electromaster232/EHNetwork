package ehnetwork.game.microgames.game.games.searchanddestroy.kits.trooper;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkHarden;

public class KitTank extends Kit
{

    public KitTank(MicroGamesManager manager)
    {
        super(manager, "Tank", KitAvailability.Hide, new String[]
            {
                "Able to absorb a lot of damage!"
            }, new Perk[]
            {
                new PerkHarden(true)
            }, EntityType.ZOMBIE, new ItemStack(Material.IRON_SWORD));
    }

    @Override
    public void GiveItems(Player player)
    {
        PlayerInventory inv = player.getInventory();
        inv.setItem(2, new ItemBuilder(Material.BLAZE_POWDER).setTitle(ChatColor.GOLD + "Fuse").build());
        inv.addItem(new ItemBuilder(Material.IRON_SWORD)
                .setTitle(C.cGreen + "Right-Click" + C.cWhite + " - " + C.cYellow + "Harden").setUnbreakable(true).build());
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE, 2));

        inv.setArmorContents(new ItemStack[]
            {
                    new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET)
            });

        inv.setItem(8,
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(this.Manager.GetGame().GetTeam(player).GetColorBase())
                        .setTitle(ChatColor.WHITE + "Evolve Kit Menu").addLore("Right click to use").build());
    }

}
