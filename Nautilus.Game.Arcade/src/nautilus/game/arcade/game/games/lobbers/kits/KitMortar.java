package nautilus.game.arcade.game.games.lobbers.kits;

import mineplex.core.itemstack.ItemBuilder;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.lobbers.kits.perks.PerkCraftman;
import nautilus.game.arcade.game.games.lobbers.kits.perks.PerkMortar;
import nautilus.game.arcade.game.games.lobbers.kits.perks.PerkMorterCraftman;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class KitMortar extends Kit
{
	public KitMortar(ArcadeManager manager)
	{
		super(manager, "Mortar", KitAvailability.Gem, 6000, new String[]
				{
				"He loves the big guns."
				}, new Perk[]
						{
				new PerkMortar(),
				new PerkCraftman(),
				new PerkMorterCraftman()
						}, EntityType.ZOMBIE, new ItemBuilder(Material.FIREBALL).build());
	}

	@Override
	public void GiveItems(Player player)
	{
		
	}
}
