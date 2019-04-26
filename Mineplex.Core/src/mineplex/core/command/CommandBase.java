package mineplex.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mineplex.core.MiniPlugin;
import mineplex.core.common.Rank;
import mineplex.core.common.util.UtilServer;
import mineplex.core.recharge.Recharge;

public abstract class CommandBase<PluginType extends MiniPlugin> implements ICommand
{	
	private Rank _requiredRank;
	private Rank[] _specificRank;
	
	private List<String> _aliases;
	
	protected PluginType Plugin;
	protected String AliasUsed;
	protected CommandCenter CommandCenter;
	
	public CommandBase(PluginType plugin, Rank requiredRank, String...aliases)
	{
		Plugin = plugin;
		_requiredRank = requiredRank;
		_aliases = Arrays.asList(aliases);
	}
	
	public CommandBase(PluginType plugin, Rank requiredRank, Rank[] specificRank, String...aliases)
	{
		Plugin = plugin;
		_requiredRank = requiredRank;
		_specificRank = specificRank;
		
		_aliases = Arrays.asList(aliases);
	}
	
	public Collection<String> Aliases()
	{
		return _aliases;
	}

	public void SetAliasUsed(String alias)
	{
		AliasUsed = alias;
	}
	
	public Rank GetRequiredRank()
	{
		return _requiredRank;
	}
	
	public Rank[] GetSpecificRanks()
	{		
		return _specificRank;
	}
	
	public void SetCommandCenter(CommandCenter commandCenter)
	{
		CommandCenter = commandCenter;
	}
	
	protected void resetCommandCharge(Player caller)
	{
		Recharge.Instance.recharge(caller, "Command");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args)
	{
		return null;
	}

	protected List<String> getMatches(String start, List<String> possibleMatches)
	{
		List<String> matches = new ArrayList<String>();

		for (String possibleMatch : possibleMatches)
		{
			if (possibleMatch.toLowerCase().startsWith(start.toLowerCase()))
				matches.add(possibleMatch);
		}

		return matches;
	}

	@SuppressWarnings("rawtypes")
	protected List<String> getMatches(String start, Enum[] numerators)
	{
		List<String> matches = new ArrayList<String>();

		for (Enum e : numerators)
		{
			String s = e.toString();
			if (s.toLowerCase().startsWith(start.toLowerCase()))
				matches.add(s);
		}

		return matches;
	}

	protected List<String> getPlayerMatches(Player sender, String start)
	{
		List<String> matches = new ArrayList<String>();

		for (Player player : UtilServer.getPlayers())
		{
			if (sender.canSee(player) && player.getName().toLowerCase().startsWith(start.toLowerCase()))
			{
				matches.add(player.getName());
			}
		}

		return matches;
	}

}
