package ehnetwork.game.microgames.game.games.sheep.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkThrower;

public class KitBrute extends Kit
{
	public KitBrute(MicroGamesManager manager)
	{
		super(manager, "Brute", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Strong enough to throw things around!",
						}, 

						new Perk[] 
								{ 
				new PerkThrower(manager)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_SWORD));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.SADDLE, (byte)0, 1, 
				C.cYellow + C.Bold + "Hold This" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Grab/Hold Sheep"));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		ent.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		ent.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		ent.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		ent.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
	}
}
