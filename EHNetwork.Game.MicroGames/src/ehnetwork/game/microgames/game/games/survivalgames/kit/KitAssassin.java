package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkBackstab;

public class KitAssassin extends Kit
{
	public KitAssassin(MicroGamesManager manager)
	{
		super(manager, "Assassin", KitAvailability.Gem, 5000,

		new String[]
			{
					"Sneak up on opponents while they're looting chests!",

					"Players can only see your nametag when 8 blocks away!"
			},

		new Perk[]
			{
				new PerkBackstab(),
			}, EntityType.ZOMBIE, new ItemStack(Material.IRON_SWORD));

	}

	@Override
	public void GiveItems(Player player)
	{

	}
}
