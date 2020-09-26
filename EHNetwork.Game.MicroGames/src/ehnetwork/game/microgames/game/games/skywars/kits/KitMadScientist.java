package ehnetwork.game.microgames.game.games.skywars.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkMadScientist;

public class KitMadScientist extends Kit
{

	public KitMadScientist(MicroGamesManager manager)
	{
		super(manager, "Mad Scientist", 
				KitAvailability.Gem, 
				new String[]{ "Spawn crazy animals to fight for you!" }, 
				new Perk[]
						{ 
				new PerkMadScientist(manager) }, 
				EntityType.ZOMBIE, new ItemStack(
						Material.MONSTER_EGG));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(new ItemStack(Material.WOOD_AXE));
		player.getInventory().addItem(new ItemStack(Material.WOOD_PICKAXE));
	}

}
