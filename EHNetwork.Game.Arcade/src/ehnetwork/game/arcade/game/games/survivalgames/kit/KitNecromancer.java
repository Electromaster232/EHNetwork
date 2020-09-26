package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkSkeletons;

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
