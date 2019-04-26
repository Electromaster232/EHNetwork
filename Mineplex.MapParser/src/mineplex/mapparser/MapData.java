package mineplex.mapparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.bukkit.entity.Player;

public class MapData 
{
	public String MapFolder;

	public GameType MapGameType = null;
	public String MapName = "null";
	public String MapCreator = "null";

	public HashSet<String> AdminList;
	
	public MapData(String mapFolder)
	{
		MapFolder = mapFolder;

		AdminList = new HashSet<String>();
		
		if ((new File(MapFolder + File.separator + "Map.dat")).exists())
			Read();
		else
			Write();
	}

	public void Read()
	{
		String line = null;

		try
		{
			FileInputStream fstream = new FileInputStream(MapFolder + File.separator + "Map.dat");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			while ((line = br.readLine()) != null)  
			{
				String[] tokens = line.split(":");

				if (tokens.length < 2)
					continue;

				if (tokens[0].length() == 0)
					continue;

				//Name & Author
				if (tokens[0].equalsIgnoreCase("MAP_NAME"))
				{
					MapName = tokens[1];
				}
				else if (tokens[0].equalsIgnoreCase("MAP_AUTHOR"))
				{
					MapCreator = tokens[1];
				}
				else if (tokens[0].equalsIgnoreCase("GAME_TYPE"))
				{
					try
					{
						MapGameType = GameType.valueOf(tokens[1] == null ? "Unknown" : tokens[1]);
					}
					catch (Exception e)
					{
						MapGameType = GameType.Unknown;
					}
				}
				else if (tokens[0].equalsIgnoreCase("ADMIN_LIST") || tokens[0].equalsIgnoreCase("BUILD_LIST"))
				{
					for (String cur : tokens[1].split(","))
						AdminList.add(cur);
				}
			}

			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Line: " + line);
		}
	}

	public void Write()
	{
		//Save
		try
		{
			FileWriter fstream = new FileWriter(MapFolder + File.separator + "Map.dat");
			BufferedWriter out = new BufferedWriter(fstream);

			out.write("MAP_NAME:"+MapName);
			out.write("\n");
			out.write("MAP_AUTHOR:"+MapCreator);
			out.write("\n");
			out.write("GAME_TYPE:"+MapGameType);
			
			String adminList = "";
			for (String cur : AdminList)
				adminList += cur + ",";
			
			out.write("\n");
			out.write("ADMIN_LIST:"+adminList);

			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean HasAccess(Player player) 
	{
		return AdminList.contains(player.getName()) || player.isOp();
	}
	
	public boolean CanJoin(Player player) 
	{
		return true;
	}

	public boolean CanRename(Player player)
	{
		return true;
	}
}
