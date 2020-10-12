package ehnetwork.game.arcade.game.games.lobbers.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.lobbers.kits.perks.PerkCraftman;
import ehnetwork.game.arcade.game.games.lobbers.kits.perks.PerkMortar;
import ehnetwork.game.arcade.game.games.lobbers.kits.perks.PerkMorterCraftman;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

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
