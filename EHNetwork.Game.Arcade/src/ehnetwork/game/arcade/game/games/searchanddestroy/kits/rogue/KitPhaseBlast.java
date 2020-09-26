package ehnetwork.game.arcade.game.games.searchanddestroy.kits.rogue;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkFlash;
import ehnetwork.game.arcade.kit.perks.PerkShadowmeld;
import ehnetwork.game.arcade.kit.perks.PerkSpeed;

public class KitPhaseBlast extends Kit
{
    public KitPhaseBlast(ArcadeManager manager)
    {
        super(manager, "Phase Blast", KitAvailability.Hide, new String[]
            {

            }, new Perk[]
            {
                    new PerkSpeed(1), new PerkShadowmeld(), new PerkFlash(1, true)
            }, EntityType.ZOMBIE, new ItemStack(Material.DIAMOND_AXE));
    }

    @Override
    public void GiveItems(Player player)
    {
        PlayerInventory inv = player.getInventory();
        inv.setItem(2, new ItemBuilder(Material.BLAZE_POWDER).setTitle(ChatColor.GOLD + "Fuse").build());
        inv.addItem(new ItemBuilder(Material.DIAMOND_AXE)
                .setTitle(C.cGreen + "Right-Click" + C.cWhite + " - " + C.cYellow + "Flash").setUnbreakable(true).build());

        inv.setArmorContents(new ItemStack[]
            {
                    new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS),
                    new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)
            });

        inv.setItem(8,
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(this.Manager.GetGame().GetTeam(player).GetColorBase())
                        .setTitle(ChatColor.WHITE + "Evolve Kit Menu").addLore("Right click to use").build());
    }
}
