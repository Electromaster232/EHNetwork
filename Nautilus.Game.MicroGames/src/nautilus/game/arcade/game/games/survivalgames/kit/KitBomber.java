package nautilus.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitBomber extends Kit
{
	public KitBomber(ArcadeManager manager)
	{
		super(manager, "Bomber", KitAvailability.Gem, 5000,

				new String[] 
						{
				"BOOM! BOOM! BOOM!"
						}, 

						new Perk[] 
								{
				new PerkBomberHG(30, 2),
				new PerkTNTArrow()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.TNT));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
