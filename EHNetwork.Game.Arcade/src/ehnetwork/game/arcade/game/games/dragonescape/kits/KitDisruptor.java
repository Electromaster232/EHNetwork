package ehnetwork.game.arcade.game.games.dragonescape.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.F;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkDisruptor;
import ehnetwork.game.arcade.kit.perks.PerkLeap;

public class KitDisruptor extends Kit
{
	public KitDisruptor(ArcadeManager manager)
	{
		super(manager, "Disruptor", KitAvailability.Gem, 

				new String[] 
						{
				"Place mini-explosives to stop other players!"
						}, 

						new Perk[] 
								{ 
				new PerkLeap("Leap", 1, 1, 8000, 3),
				new PerkDisruptor(8, 2)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.TNT));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.TNT, (byte)0, 1, F.item("Disruptor")));
		player.setExp(0.99f);
	}
}
