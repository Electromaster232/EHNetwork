package nautilus.game.arcade.game.games.event.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.event.EventGame;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

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
