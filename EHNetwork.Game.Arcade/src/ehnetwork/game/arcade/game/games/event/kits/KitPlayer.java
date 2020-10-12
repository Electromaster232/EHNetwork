package ehnetwork.game.arcade.game.games.event.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.event.EventGame;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitPlayer extends Kit
{
	public KitPlayer(ArcadeManager manager)
	{
		super(manager, "Party Animal", KitAvailability.Free, 0,

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
								}, 
								EntityType.PIG,
								new ItemStack(Material.PORK));

	}
	
	@Override
	public void GiveItems(Player player) 
	{
		((EventGame)Manager.GetGame()).giveItems(player);
	}
}
