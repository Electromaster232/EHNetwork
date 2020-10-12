package ehnetwork.game.arcade.game.games.bridge.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkAxeman;
import ehnetwork.game.arcade.kit.perks.PerkLeap;

public class KitBeserker extends Kit
{
	public KitBeserker(ArcadeManager manager)
	{
		super(manager, "Beserker", KitAvailability.Free, 

				new String[] 
						{
				"Agile warrior trained in the ways of axe combat."
						}, 

						new Perk[] 
								{ 
				new PerkLeap("Beserker Leap", 1.2, 1.2, 8000),
				new PerkAxeman(),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.STONE_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_AXE));
	}
}
