package ehnetwork.game.microgames.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkIronSkin;
import ehnetwork.game.microgames.kit.perks.PerkMammoth;

public class KitMammoth extends Kit
{
	public KitMammoth(MicroGamesManager manager)
	{
		super(manager, "Brawler", KitAvailability.Gem, 

				new String[] 
						{
				"Giant and muscular, easily smacks others around."
						}, 

						new Perk[] 
								{
				new PerkMammoth(),
				new PerkIronSkin(1)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_SWORD));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
	}
}
