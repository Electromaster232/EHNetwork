package ehnetwork.game.microgames.game.games.evolution.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkRecharge;

public class KitRecharge extends Kit
{
	public KitRecharge(MicroGamesManager manager)
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
