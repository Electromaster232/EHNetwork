package ehnetwork.game.arcade.command;


import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.minecraft.game.core.condition.Condition;


public class VanishCommand extends CommandBase<ArcadeManager> implements Listener
{
	private ArcadeManager _clientManager;
	public VanishCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.SNR_MODERATOR, new Rank[]{}, new String[]{"vanish", "v"});
		_clientManager = plugin;
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		boolean hideMe = _clientManager.GetCondition().HasCondition(caller, Condition.ConditionType.CLOAK, "Vanish");
		if(hideMe){
			UtilPlayer.message(caller, C.cBlue + "Vanish> " + C.cGray + "You are " + C.cRed + "no longer hidden.");
		}
		else{
			UtilPlayer.message(caller, C.cBlue + "Vanish> " + C.cGray + "You are now " + C.cGreen + "hidden.");
		}

			if (hideMe)
			{
				_clientManager.GetCondition().EndCondition(caller, Condition.ConditionType.CLOAK, "Vanish");
			}
			else
			{
				_clientManager.GetCondition().Factory().Cloak("Vanish", caller, caller, 7777, false, true);
			}
		}
	}
