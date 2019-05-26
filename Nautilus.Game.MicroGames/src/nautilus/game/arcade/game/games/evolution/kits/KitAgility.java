package nautilus.game.arcade.game.games.evolution.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkDoubleJump;
import nautilus.game.arcade.kit.perks.PerkSpeed;

public class KitAgility extends Kit
{
	public KitAgility(ArcadeManager manager)
	{
		super(manager, "Agility", KitAvailability.Free, 

				new String[] 
						{
				"You are extremely agile and can double jump!"
						}, 

						new Perk[] 
								{
				new PerkDoubleJump("Double Jump", 0.8, 0.8, false),
				new PerkSpeed(0),
								}, 
								EntityType.ZOMBIE,
								null);
	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
