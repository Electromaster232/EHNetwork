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

public class KitNecromancer extends Kit
{
	public KitNecromancer(ArcadeManager manager)
	{
		super(manager, "Necromancer", KitAvailability.Gem, 5000,

				new String[] 
						{
				"Cool undead guy and stuff"
						}, 

						new Perk[] 
								{
					new PerkSkeletons(true)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.SKULL_ITEM));

	}

	@Override
	public void GiveItems(Player player)
	{
		
	}
}
