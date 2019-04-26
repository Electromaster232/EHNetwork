package mineplex.core.donation.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UUIDFetcher;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.donation.DonationManager;

public class GemCommand extends CommandBase<DonationManager>
{
	public GemCommand(DonationManager plugin)
	{
		super(plugin, Rank.ADMIN, "gem");
	}

	@Override
	public void Execute(final Player caller, String[] args)
	{
		if (args.length < 2)
		{
			UtilPlayer.message(caller, F.main("gem", "Missing Args: " + F.elem("/gem <player> <amount>")));
			return;
		}

		String targetName = args[0];
		String gemsString = args[1];
		Player target = UtilPlayer.searchExact(targetName);

		if (target == null)
		{
			UUID uuid = UUIDFetcher.getUUIDOf(targetName);
			if (uuid != null)
			{
				rewardGems(caller, null, targetName, uuid, gemsString);
			}
			else
			{
				UtilPlayer.message(caller, F.main("Gem", "Could not find player " + F.name(targetName)));
			}
		}
		else
		{
			rewardGems(caller, target, target.getName(), target.getUniqueId(), gemsString);
		}
	}

	private void rewardGems(final Player caller, final Player target, final String targetName, final UUID uuid, String gemsString)
	{
		try
		{
			int gems = Integer.parseInt(gemsString);
			rewardGems(caller, target, targetName, uuid, gems);
		}
		catch (Exception e)
		{
			UtilPlayer.message(caller, F.main("gem", "Invalid gems Amount"));
		}
	}

	private void rewardGems(final Player caller, final Player target, final String targetName, final UUID uuid, final int gems)
	{
		Plugin.RewardGems(new Callback<Boolean>()
		{
			public void run(Boolean completed)
			{
				if (completed)
				{
					UtilPlayer.message(caller, F.main("gem", "You gave " + F.elem(gems + " gems") + " to " + F.name(targetName) + "."));
	
					if (target != null)
					{
						UtilPlayer.message(target, F.main("gem", F.name(caller.getName()) + " gave you " + F.elem(gems + " gems") + "."));
					}
				}
				else
				{
					UtilPlayer.message(caller, F.main("gem", "There was an error giving " + F.elem(gems + " gems") + " to " + F.name(targetName) + "."));
				}
			}
		}, caller.getName(), targetName, uuid, gems);
	}
}
