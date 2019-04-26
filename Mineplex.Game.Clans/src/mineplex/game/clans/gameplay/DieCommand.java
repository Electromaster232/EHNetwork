package mineplex.game.clans.gameplay;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;

public class DieCommand extends CommandBase<Gameplay>
{
	public DieCommand(Gameplay plugin)
	{
		super(plugin, Rank.ALL, "die");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		Plugin.getDamageManager().NewDamageEvent(caller, null, null, DamageCause.SUICIDE, 5000, false, true, true, null, null);
	}
}
