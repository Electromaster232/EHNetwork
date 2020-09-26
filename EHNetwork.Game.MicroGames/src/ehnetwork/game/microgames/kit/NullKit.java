package ehnetwork.game.microgames.kit;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.perks.PerkNull;

public class NullKit extends Kit
{
	public NullKit(MicroGamesManager manager)
	{
		super(manager, "Null Kit", KitAvailability.Null, 

				new String[] 
						{
				"It does nothing!"
						}, 

						new Perk[] 
								{
				new PerkNull()
								}, 

								null, null);

	}

	@Override
	public void GiveItems(Player player) 
	{

	}
	
	@Override
	public Creature SpawnEntity(Location loc)
	{
		return null;
	}
}
