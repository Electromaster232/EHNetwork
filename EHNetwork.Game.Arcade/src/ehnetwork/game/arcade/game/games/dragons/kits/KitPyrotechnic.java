package ehnetwork.game.arcade.game.games.dragons.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkSparkler;

public class KitPyrotechnic extends Kit
{
	public KitPyrotechnic(ArcadeManager manager)
	{
		super(manager, "Pyrotechnic", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Dragons love sparklers!!"
						}, 

						new Perk[] 
								{ 
				new PerkSparkler(25, 2),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.EMERALD));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.GOLD_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.GOLD_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.GOLD_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.GOLD_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
	}
}
