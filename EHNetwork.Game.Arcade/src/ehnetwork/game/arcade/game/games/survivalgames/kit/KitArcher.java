package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBarrage;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;

public class KitArcher extends Kit
{
	public KitArcher(ArcadeManager manager)
	{
		super(manager, "Archer", KitAvailability.Gem, 

				new String[] 
						{
				"Passively crafts arrows from surrounding terrain."
						}, 

						new Perk[] 
								{
				new PerkFletcher(20, 3, true),
				new PerkBarrage(5, 250, true, false),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.BOW));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
