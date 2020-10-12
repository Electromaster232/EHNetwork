package ehnetwork.game.microgames.game.games.lobbers.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.games.lobbers.kits.perks.PerkCraftman;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDummy;

public class KitArmorer extends Kit
{

	public KitArmorer(MicroGamesManager manager)
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
