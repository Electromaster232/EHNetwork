package ehnetwork.game.microgames.game.games.dragonescape.kits;

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
				"You get twice as many leaps!"
						}, 

						new Perk[] 
								{ 
				new PerkLeap("Leap", 1, 1, 8000, 4)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		player.setExp(0.99f);
	}
}
