package ehnetwork.game.microgames.game.games.wither.kit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkFletcher;
import ehnetwork.game.microgames.kit.perks.PerkWitherArrowBlind;

public class KitHumanArcher extends Kit
{
	public KitHumanArcher(MicroGamesManager manager)
	{
		super(manager, "Human Archer", KitAvailability.Free,

		new String[]
		{
			""
		},

		new Perk[]
		{
				new PerkDoubleJump("Double Jump", 1.2, 1, true, 4000, true),
				new PerkWitherArrowBlind(6),
				new PerkFletcher(4, 4, true),

		}, EntityType.ZOMBIE, null);

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));

		ItemStack potion = new ItemStack(Material.POTION, 2, (short) 16429); // 16422
		PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
		potionMeta.setDisplayName(ChatColor.RESET + "Revival Potion");
		potion.setItemMeta(potionMeta);
		player.getInventory().addItem(potion);

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
	}

	@Override
	public void SpawnCustom(LivingEntity ent)
	{

	}
}
