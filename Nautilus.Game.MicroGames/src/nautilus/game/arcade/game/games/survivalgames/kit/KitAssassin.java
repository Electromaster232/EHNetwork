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

public class KitAssassin extends Kit
{
	public KitAssassin(ArcadeManager manager)
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
