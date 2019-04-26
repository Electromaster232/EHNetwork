package nautilus.game.arcade.game.games.evolution.kits;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkRegeneration;
import nautilus.game.arcade.kit.perks.PerkVampire;

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
