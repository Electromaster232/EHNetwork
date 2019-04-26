package nautilus.game.arcade.game.games.evolution.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkRecharge;

public class KitRecharge extends Kit
{
	public KitRecharge(ArcadeManager manager)
	{
		super(manager, "Stamina", KitAvailability.Free, 

				new String[] 
						{
				"You are able to use your abilities more often!"
						}, 

						new Perk[] 
								{
				new PerkRecharge(0.5),
								}, 
								EntityType.ZOMBIE,
								null);

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
