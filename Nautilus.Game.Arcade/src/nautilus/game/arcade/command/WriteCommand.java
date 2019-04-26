package nautilus.game.arcade.command;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import nautilus.game.arcade.ArcadeManager;
import mineplex.core.command.CommandBase;
import mineplex.core.common.Rank;

public class WriteCommand extends CommandBase<ArcadeManager>
{
	public WriteCommand(ArcadeManager plugin)
	{
		super(plugin, Rank.ADMIN, new Rank[] {Rank.JNR_DEV}, "write");
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args.length < 6)
		{
			caller.sendMessage("/write X Y Z BlockFace Line <Text>");
			return;
		}

		//Loc
		int x = 0;
		int y = 0;
		int z = 0;
		try
		{
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
		}
		catch (Exception e)
		{
			caller.sendMessage("Invalid Co-Ordinates");
			return;
		}

		//BlockFace
		BlockFace face = BlockFace.NORTH;
		try
		{
			face = BlockFace.valueOf(args[3].toUpperCase());
		}
		catch (Exception e)
		{
			caller.sendMessage("Invalid BlockFace");
			return;
		}

		//Line
		int line = 0;
		try
		{
			line = Integer.parseInt(args[4]);
		}
		catch (Exception e)
		{
			caller.sendMessage("Invalid Line");
			return;
		}

		//Text
		String text = "";
		for (int i=5 ; i < args.length ; i++)
			text += args[i] + " ";
		text = text.substring(0, text.length()-1);

		Plugin.GetLobby().WriteLine(caller, x, y, z, face, line, text);
	}
}
