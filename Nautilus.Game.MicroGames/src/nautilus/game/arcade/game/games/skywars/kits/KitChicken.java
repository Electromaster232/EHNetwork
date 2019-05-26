package nautilus.game.arcade.game.games.skywars.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkChicken;

public class KitChicken extends Kit
{

	static PerkChicken p;
	
	public KitChicken(ArcadeManager manager)
	{ 
		super(manager,
				"Chicken Farmer",
				KitAvailability.Free,
				0,
				new String[]
				{
					"BAWK BAWK. Eggs!"
				},
				new Perk[]{p = new PerkChicken(manager)},
				EntityType.ZOMBIE,
				new ItemStack(Material.EGG));
	}

	@Override
	public void GiveItems(Player player)
	{
		p.spawnChicken(player, player.getLocation());
	
		player.getInventory().addItem(new ItemStack(Material.WOOD_AXE));
		player.getInventory().addItem(new ItemStack(Material.WOOD_PICKAXE));
	}
}
