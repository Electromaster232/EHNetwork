package ehnetwork.game.microgames.game.games.searchanddestroy.kits.rogue;

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
import ehnetwork.game.microgames.kit.perks.PerkEvade;
import ehnetwork.game.microgames.kit.perks.PerkSpeed;
import ehnetwork.game.microgames.kit.perks.PerkWraith;

public class KitWraith extends Kit
{
    public KitWraith(MicroGamesManager manager)
    {
        super(manager, "Rewind", KitAvailability.Hide, new String[]
            {

            }, new Perk[]
            {
                    new PerkSpeed(1), new PerkWraith(), new PerkEvade()

            }, EntityType.ZOMBIE, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public void GiveItems(Player player)
    {
        PlayerInventory inv = player.getInventory();
        inv.setItem(2, new ItemBuilder(Material.BLAZE_POWDER).setTitle(ChatColor.GOLD + "Fuse").build());
        inv.addItem(new ItemBuilder(Material.DIAMOND_SWORD)
                .setTitle(C.cGreen + "Hold Block" + C.cWhite + " - " + C.cYellow + "Evade").setUnbreakable(true).build());
        inv.setItem(8,
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(this.Manager.GetGame().GetTeam(player).GetColorBase())
                        .setTitle(ChatColor.WHITE + "Evolve Kit Menu").addLore("Right click to use").build());
    }
}
