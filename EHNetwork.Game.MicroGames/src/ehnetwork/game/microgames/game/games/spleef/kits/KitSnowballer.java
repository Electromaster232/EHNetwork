package ehnetwork.game.microgames.game.games.spleef.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkKnockback;

public class KitSnowballer extends Kit
{
	public KitSnowballer(MicroGamesManager manager)
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
