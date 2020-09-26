package ehnetwork.game.arcade.stats;

import java.util.List;

import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;

import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.kit.perks.PerkSkeletons;

public class SimultaneousSkeletonStatTracker extends StatTracker<Game>
{
	private final int _requiredCount;

	public SimultaneousSkeletonStatTracker(Game game, int requiredCount)
	{
		super(game);

		_requiredCount = requiredCount;
	}

	@EventHandler
	public void onMinionSpawn(PerkSkeletons.MinionSpawnEvent event)
	{
		if (getGame().GetState() != Game.GameState.Live)
			return;

		List<Skeleton> skeletons = event.getPerkSkeletons().getSkeletons(event.getPlayer());

		if (skeletons != null)
		{
			int aliveCount = 0;

			for (Skeleton skeleton : skeletons)
			{
				if (!skeleton.isDead())
					aliveCount++;
			}

			if (aliveCount >= getRequiredCount())
				addStat(event.getPlayer(), "Skeletons", 1, true, false);
		}
	}

	public int getRequiredCount()
	{
		return _requiredCount;
	}
}
