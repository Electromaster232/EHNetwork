package ehnetwork.game.arcade.game.games.evolution.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkRegeneration;
import ehnetwork.game.arcade.kit.perks.PerkVampire;

public class KitHealth extends Kit
{
	public KitHealth(ArcadeManager manager)
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
