package nautilus.game.arcade.game.games.colorswap.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.games.event.EventGame;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkKnockback;

public class KitPlayer extends Kit
{
	public KitPlayer(ArcadeManager manager)
	{
		super(manager, "Player", KitAvailability.Free, 0,

				new String[] 
						{
				""
						}, 

						new Perk[] 
								{
										new PerkKnockback(1),
								}, 
								EntityType.PIG,
								new ItemStack(Material.PORK));

	}
	
	@Override
	public void GiveItems(Player player) 
	{

	}
}
