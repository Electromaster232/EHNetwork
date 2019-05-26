package nautilus.game.arcade.stats;

import java.util.ArrayList;
import java.util.List;

import mineplex.minecraft.game.classcombat.Skill.Mage.LightningOrb.LightningOrbEvent;
import nautilus.game.arcade.game.Game;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ElectrocutionStatTracker extends StatTracker<Game>
{
	public ElectrocutionStatTracker(Game game)
	{
		super(game);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLightningOrb(LightningOrbEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;
		
		List<Player> viable = new ArrayList<Player>();
		
		for (LivingEntity en : event.getStruck())
		{
			if (en instanceof Player)
			{
				viable.add((Player) en);
			}
		}
		
		if (viable.size() >= 4)
		{
			addStat(event.getPlayer(), "MassElectrocution", 1, true, false);
		}
	}
}
