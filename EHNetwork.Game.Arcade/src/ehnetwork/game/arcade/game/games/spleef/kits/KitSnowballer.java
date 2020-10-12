package ehnetwork.game.arcade.game.games.spleef.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkKnockback;

public class KitSnowballer extends Kit
{
	public KitSnowballer(ArcadeManager manager)
	{
		super(manager, "Snowballer", KitAvailability.Free, 

				new String[] 
						{
				"Throw snowballs to break blocks!",
				"Receives 1 Snowball when you punch blocks!"
						}, 

						new Perk[] 
								{
				new PerkKnockback(0.3)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.SNOW_BALL));

	}
	
	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.SNOW_BALL));
	}
}
