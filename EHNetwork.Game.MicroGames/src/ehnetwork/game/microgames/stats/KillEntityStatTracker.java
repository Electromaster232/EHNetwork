package ehnetwork.game.microgames.stats;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import ehnetwork.game.microgames.game.Game;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class KillEntityStatTracker extends StatTracker<Game>
{
	private final String _statName;
	private EntityType _entityType;

	public KillEntityStatTracker(Game game, String statName, EntityType entityType)
	{
		super(game);

		_statName = statName;
		setEntityType(entityType);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onKillingBlow(CustomDamageEvent event)
	{
		if (event.GetDamageeEntity().getType() != getEntityType())
			return;

		if (!event.GetDamageeEntity().isDead())
			return;
		
		Player player = event.GetDamagerPlayer(true);
		if (player == null)
			return;
		
		addStat(player, getStatName(), 1, false, false);
	}

	public String getStatName()
	{
		return _statName;
	}

	public EntityType getEntityType()
	{
		return _entityType;
	}

	public void setEntityType(EntityType entityType)
	{
		_entityType = entityType;
	}
}
