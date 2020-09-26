package ehnetwork.game.arcade.kit;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.perks.PerkNull;

public class NullKit extends Kit
{
	public NullKit(ArcadeManager manager)
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
