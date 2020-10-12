package ehnetwork.game.arcade.game.games.wither.kit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBlockRestorer;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;
import ehnetwork.game.arcade.kit.perks.PerkWitherMedicRefill;

public class KitHumanMedic extends Kit
{
	public KitHumanMedic(ArcadeManager manager)
	{
		super(manager, "Human Medic", KitAvailability.Gem, 2000, 

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
				new PerkDoubleJump("Double Jump", 1, 0.8, true, 6000, true),
				new PerkWitherMedicRefill(45, 1),
				new PerkBlockRestorer()
								}, 
								EntityType.ZOMBIE,
								null);

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP));
		
		ItemStack potion = new ItemStack(Material.POTION, 2, (short)16429); // 16422
		PotionMeta potionMeta = (PotionMeta)potion.getItemMeta();
		potionMeta.setDisplayName(ChatColor.RESET + "Revival Potion");
		potion.setItemMeta(potionMeta);
		player.getInventory().addItem(potion);
		
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		
	}
}
