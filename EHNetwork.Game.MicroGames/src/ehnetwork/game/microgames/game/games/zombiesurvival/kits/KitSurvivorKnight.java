package ehnetwork.game.microgames.game.games.zombiesurvival.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkIronSkin;
import ehnetwork.game.microgames.kit.perks.PerkMammoth;

public class KitSurvivorKnight extends Kit
{
	public KitSurvivorKnight(MicroGamesManager manager)
	{
		super(manager, "Survivor Knight", KitAvailability.Free, 

				new String[] 
						{
				"Smash and kill through Zombies"
						}, 

						new Perk[] 
								{ 
				new PerkIronSkin(1),
				new PerkMammoth()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_SWORD));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
		player.getInventory().setHelmet(ItemStackFactory.Instance.CreateStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(ItemStackFactory.Instance.CreateStack(Material.IRON_CHESTPLATE));
		player.getInventory().setLeggings(ItemStackFactory.Instance.CreateStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(ItemStackFactory.Instance.CreateStack(Material.IRON_BOOTS));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
	}
}
