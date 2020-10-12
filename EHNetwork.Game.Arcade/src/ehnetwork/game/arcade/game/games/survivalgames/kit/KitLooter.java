package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkLooter;

public class KitLooter extends Kit
{
	public KitLooter(ArcadeManager manager)
	{
		super(manager, "Looter", KitAvailability.Free, 

				new String[] 
						{
				"Defeat your opponents with your swag loots!"
						}, 

						new Perk[] 
								{
				new PerkLooter(),
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.CHEST));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
