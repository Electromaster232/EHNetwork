package ehnetwork.game.microgames.game.games.playerpop.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;

public class KitBrute extends Kit
{
	public KitBrute(MicroGamesManager manager)
	{
		super(manager, "Brute", KitAvailability.Gem, 1500,

				new String[]
						{
								"You stand tall but can fall!"
						},

				new Perk[]
						{
								new PerkDoubleJump("Double Jump", 0.9, 0.9, true)
						},
				EntityType.ENDERMAN,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
		return;
	}
}
