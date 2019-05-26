package nautilus.game.arcade.game.games.lobbers.kits;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilInv;
import mineplex.core.itemstack.ItemBuilder;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.lobbers.kits.perks.PerkCraftman;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkDummy;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitArmorer extends Kit
{

	public KitArmorer(ArcadeManager manager)
	{
		super(manager, "Armorer", KitAvailability.Gem, 2000, new String[]
				{
				C.cGray + "He uses his expert armor-making",
				C.cGray + "skills to block excess damage!"
				}, new Perk[]
						{
				new PerkDummy("Armorer",
						new String[]
								{
						C.cGray + "Recieve " + C.cYellow + "Full Gold Armor"
								}),
				new PerkCraftman()
						}, EntityType.ZOMBIE,
				new ItemBuilder(Material.GOLD_HELMET).build());
	}
	
	@Override
	public void ApplyKit(Player player)
	{
		UtilInv.Clear(player);
		
		for (Perk perk : GetPerks())
			perk.Apply(player);
		
		GiveItemsCall(player);
		
		player.getInventory().setHelmet(new ItemBuilder(Material.GOLD_HELMET).setUnbreakable(true).build());
		player.getInventory().setChestplate(new ItemBuilder(Material.GOLD_CHESTPLATE).setUnbreakable(true).build());
		player.getInventory().setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS).setUnbreakable(true).build());
		player.getInventory().setBoots(new ItemBuilder(Material.GOLD_BOOTS).setUnbreakable(true).build());
		
		UtilInv.Update(player);
	}

	@Override
	public void SpawnCustom(LivingEntity entity)
	{
		entity.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		entity.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		entity.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
	}
	
	@Override
	public void GiveItems(Player player)
	{
		
	}

}
