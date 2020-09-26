package ehnetwork.game.microgames.game.games.wither.kit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;

public class KitHumanEditor extends Kit
{
	public KitHumanEditor(MicroGamesManager manager)
	{
		super(manager, "Human Editor", KitAvailability.Gem, 5000,
		new String[]
		{
				" ", "Can " + C.cYellow + "Edit " + C.cGray + "the terrain to they and their comrade's benefits", " "
		}, 
		new Perk[]
		{
				new PerkDoubleJump("Double Jump", 1, 0.8, true, 6000, true),
		},
		EntityType.ZOMBIE, new ItemStack(Material.STONE_PICKAXE));
	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_AXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_PICKAXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_SPADE));

		ItemStack potion = new ItemStack(Material.POTION, 2, (short) 16429); // 16422
		PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
		potionMeta.setDisplayName(ChatColor.RESET + "Revival Potion");
		potion.setItemMeta(potionMeta);
		player.getInventory().addItem(potion);

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
	}
}
