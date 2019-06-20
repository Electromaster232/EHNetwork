package nautilus.game.arcade.game.games.murder.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

public class KitPlayer extends Kit
{
	public KitPlayer(ArcadeManager manager)
	{
		super(manager, "Player", KitAvailability.Free, 0,

				new String[]
						{
								"Survive the game!"
						},

				new Perk[]
						{
						},
				EntityType.ZOMBIE,
				new ItemStack(Material.AIR));

	}

	@Override
	public void GiveItems(Player player)
	{
	}
}