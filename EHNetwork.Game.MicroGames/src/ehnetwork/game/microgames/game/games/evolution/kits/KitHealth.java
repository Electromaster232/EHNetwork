package ehnetwork.game.microgames.game.games.evolution.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkRegeneration;
import ehnetwork.game.microgames.kit.perks.PerkVampire;

public class KitHealth extends Kit
{
	public KitHealth(MicroGamesManager manager)
	{
		super(manager, "Vitality", KitAvailability.Free, 

				new String[] 
						{
				"You have improved survivability."
						}, 

						new Perk[] 
								{
				new PerkRegeneration(0),
				new PerkVampire(6),
								}, 
								EntityType.ZOMBIE,
								null);
	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
