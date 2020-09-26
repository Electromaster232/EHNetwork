package ehnetwork.game.arcade.game.games.milkcow.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkDoubleJump;

public class KitFarmerJump extends Kit
{
	public KitFarmerJump(ArcadeManager manager)
	{
		super(manager, "Rabbit Farmer", KitAvailability.Free, 

				new String[] 
						{
				"Learned a thing or two from his rabbits!"
						}, 

						new Perk[] 
								{
						new PerkDoubleJump("Double Jump", 1, 0.8, false)
								}, 

								EntityType.ZOMBIE, 
								new ItemStack(Material.IRON_HOE));

	}
	
	@EventHandler
	public void FireItemResist(UpdateEvent event)
	{

	}
	
	@Override 
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BUCKET));
	}
}
