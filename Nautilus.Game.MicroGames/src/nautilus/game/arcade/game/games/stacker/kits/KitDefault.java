package nautilus.game.arcade.game.games.stacker.kits;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitDefault extends Kit
{
	public KitDefault(ArcadeManager manager)
	{
		super(manager, "Default", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{ 
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.WOOD_BUTTON));

	}
	
	@Override
	public void GiveItems(Player player)
	{

	}
}
