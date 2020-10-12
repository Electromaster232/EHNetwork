package ehnetwork.game.microgames.game.games.micro.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkIronSkin;

public class KitFighter extends Kit
{
	public KitFighter(MicroGamesManager manager)
	{
		super(manager, "Fighter", KitAvailability.Free, 

				new String[] 
						{
				"HE LIKES TO FIGHT!",
				" ",
				C.cGreen + "Wood Sword",
				C.cGreen + "5 Apples",
						}, 

						new Perk[] 
								{
				new PerkIronSkin(0.5)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.WOOD_SWORD));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.WOOD_SWORD));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.APPLE, 5));
	}
}
