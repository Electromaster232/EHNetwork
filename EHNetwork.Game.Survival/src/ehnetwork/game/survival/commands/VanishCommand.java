package ehnetwork.game.survival.commands;


import org.bukkit.entity.Player;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.survival.SurvivalManager;
import ehnetwork.minecraft.game.core.condition.Condition;


public class VanishCommand extends CommandBase<SurvivalManager>
{
	private SurvivalManager _clientManager;
	public VanishCommand(SurvivalManager plugin)
	{
		super(plugin, Rank.MODERATOR, new Rank[]{}, new String[]{"vanish", "v"});
		_clientManager = plugin;
	}


	@Override
	public void Execute(Player caller, String[] args)
	{
		boolean hideMe = _clientManager.GetCondition().HasCondition(caller, Condition.ConditionType.CLOAK, "Vanish");
		if(hideMe){
			UtilPlayer.message(caller, F.main("Vanish",C.cGray + "You are " + C.cRed + "no longer hidden."));
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
