package ehnetwork.game.microgames.game.games.runner.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkLeap;

public class KitLeaper extends Kit
{
	public KitLeaper(MicroGamesManager manager)
	{
		super(manager, "Jumper", KitAvailability.Free, 

				new String[] 
						{
				"Leap to avoid falling to your death!"
						}, 

						new Perk[] 
								{ 
				new PerkLeap("Leap", 1.2, 1.2, 8000)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.IRON_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
	}
}
