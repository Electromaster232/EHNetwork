package ehnetwork.game.arcade.kit.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.kit.SmashPerk;
import ehnetwork.game.arcade.kit.perks.data.MeteorShowerData;

public class PerkMeteorShower extends SmashPerk
{
	private ArrayList<MeteorShowerData> _meteors = new ArrayList<MeteorShowerData>();
	
	public PerkMeteorShower() 
	{
		super("Meteor Shower", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{ 
		_meteors.add(new MeteorShowerData(player, player.getTargetBlock(null, 128).getLocation()));
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		 if (event.getType() != UpdateType.TICK)
			 return;
		 
		 Iterator<MeteorShowerData> meteorIter = _meteors.iterator();
		 
		 while (meteorIter.hasNext())
		 {
			 MeteorShowerData data = meteorIter.next();
			 
			 if (data.update())
			 {
				 meteorIter.remove();
			 }
		 }
	}
}
