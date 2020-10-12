package ehnetwork.game.microgames.game.games.evolution.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDoubleJump;
import ehnetwork.game.microgames.kit.perks.PerkSpeed;

public class KitAgility extends Kit
{
	public KitAgility(MicroGamesManager manager)
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
