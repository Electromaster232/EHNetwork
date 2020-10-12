package ehnetwork.hub.modules.parkour;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;

import ehnetwork.core.common.util.UtilMath;


public class ParkourSnake extends ParkourData
{
	private ArrayList<SnakePart> _snakes;

	public ParkourSnake(String name, String[] desc, int gems, Location npc,	Location cornerA, Location cornerB)
	{
		super(name, desc, gems, npc, cornerA, cornerB);

		//THIS SHOULD BE COMMENTED OUT
		//parseSnakes();

		loadSnakes();
	}

	public void loadSnakes()
	{
		_snakes = new ArrayList<SnakePart>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("SnakeRoutes.dat"));
			try 
			{
				String line = br.readLine();

				while (line != null) 
				{
					if (line.length() > 6)
						_snakes.add(loadSnake(line));					
					
					line = br.readLine();
				}
			} 
			finally 
			{
				br.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public SnakePart loadSnake(String locString)
	{
		ArrayList<Location> locs = new ArrayList<Location>();

		for (String cur : locString.split(":"))
		{
			String[] tokens = cur.split(",");

			try
			{
				Location loc = new Location(getWorld(), Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));

				if (InBoundary(loc))
					locs.add(loc);
			}
			catch (Exception e)
			{
				System.out.println("Invalid Hub Snake Location: " + cur);
			}			
		}

		return new SnakePart(locs);
	}

	public void parseSnakes()
	{
		String locString = "-22,78,-72:-22,79,-72:-22,80,-72:-22,80,-71:-22,80,-70:-22,80,-69:-22,78,-68:-22,79,-68:-22,80,-68:-22,79,-57:-22,79,-56:-22,79,-55:-22,79,-54:-22,79,-53:-21,75,-78:-21,76,-78:-21,77,-78:-21,75,-77:-21,75,-76:-21,75,-75:-21,75,-74:-21,76,-74:-21,77,-74:-21,78,-74:-21,78,-73:-21,78,-72:-21,76,-68:-21,77,-68:-21,78,-68:-21,76,-67:-21,76,-66:-21,76,-65:-21,76,-64:-21,77,-64:-21,78,-64:-21,79,-64:-21,79,-63:-21,77,-62:-21,78,-62:-21,79,-62:-21,79,-57:-21,79,-56:-21,79,-55:-21,79,-54:-21,79,-53:-20,77,-78:-20,77,-62:-20,79,-57:-20,79,-56:-20,79,-55:-20,79,-54:-20,79,-53:-19,75,-78:-19,76,-78:-19,77,-78:-19,75,-77:-19,75,-76:-19,75,-75:-19,75,-74:-19,77,-74:-19,78,-74:-19,79,-74:-19,75,-73:-19,76,-73:-19,77,-73:-19,77,-62:-18,79,-74:-18,77,-62:-17,79,-74:-17,77,-62:-16,79,-74:-16,77,-62:-15,77,-74:-15,78,-74:-15,79,-74:-15,75,-73:-15,76,-73:-15,77,-73:-15,75,-72:-15,75,-71:-15,75,-70:-15,75,-69:-15,76,-69:-15,77,-69:-15,77,-68:-15,77,-67:-15,77,-66:-15,77,-65:-15,77,-64:-15,77,-63:-15,77,-62:-11,77,-68:-11,78,-68:-11,79,-68:-11,77,-67:-11,77,-66:-11,77,-65:-11,78,-65:-11,79,-65:-10,79,-68:-10,79,-65:-9,79,-68:-9,79,-65:-8,77,-68:-8,78,-68:-8,79,-68:-8,77,-67:-8,77,-66:-8,77,-65:-8,78,-65:-8,79,-65:-7,75,-75:-7,75,-74:-7,75,-73:-7,76,-73:-7,77,-73:-7,73,-71:-7,74,-71:-7,75,-71:-7,73,-70:-7,73,-69:-7,72,-66:-7,73,-66:-7,74,-66:-7,72,-65:-7,72,-64:-7,72,-63:-7,73,-63:-7,74,-63:-7,75,-63:-6,71,-79:-6,72,-79:-6,73,-79:-6,74,-79:-6,75,-79:-6,75,-78:-6,76,-78:-6,77,-78:-6,75,-75:-6,77,-73:-6,75,-71:-6,73,-69:-6,74,-66:-6,75,-63:-5,71,-79:-5,77,-78:-5,75,-75:-5,75,-74:-5,75,-73:-5,76,-73:-5,77,-73:-5,73,-71:-5,74,-71:-5,75,-71:-5,73,-70:-5,73,-69:-5,77,-69:-5,77,-68:-5,77,-67:-5,74,-66:-5,75,-66:-5,76,-66:-5,77,-66:-5,72,-63:-5,73,-63:-5,74,-63:-5,75,-63:-4,71,-79:-4,77,-78:-4,77,-69:-4,72,-63:-3,71,-79:-3,77,-78:-3,77,-69:-3,72,-63:-2,71,-79:-2,73,-79:-2,74,-79:-2,75,-79:-2,75,-78:-2,76,-78:-2,77,-78:-2,74,-69:-2,75,-69:-2,76,-69:-2,77,-69:-2,74,-68:-2,74,-67:-2,74,-66:-2,74,-65:-2,75,-65:-2,76,-65:-2,76,-64:-2,72,-63:-2,73,-63:-2,74,-63:-2,75,-63:-2,76,-63:-1,71,-79:-1,73,-79:0,71,-79:0,73,-79:1,71,-79:1,73,-79:1,74,-79:1,75,-79:1,75,-78:1,76,-78:1,77,-78:2,71,-79:2,77,-78:3,71,-79:3,77,-78:4,71,-79:4,73,-79:4,74,-79:4,75,-79:4,75,-78:4,76,-78:4,77,-78:5,71,-79:5,73,-79:6,71,-79:6,73,-79:7,71,-79:7,73,-79:7,74,-79:7,75,-79:7,75,-78:7,76,-78:7,77,-78:7,69,-67:7,70,-67:7,71,-67:7,71,-66:7,71,-65:7,69,-64:7,70,-64:7,71,-64:8,71,-79:8,77,-78:8,66,-74:8,67,-74:8,68,-74:8,68,-73:8,68,-72:8,68,-71:8,68,-70:8,68,-69:8,69,-69:8,70,-69:8,71,-69:8,69,-67:8,69,-64:9,71,-79:9,77,-78:9,66,-74:9,71,-69:9,69,-67:9,69,-64:10,71,-79:10,72,-79:10,73,-79:10,74,-79:10,75,-79:10,75,-78:10,76,-78:10,77,-78:10,66,-74:10,68,-74:10,69,-74:10,70,-74:10,71,-74:10,71,-73:10,69,-72:10,70,-72:10,71,-72:10,69,-71:10,69,-70:10,69,-69:10,70,-69:10,71,-69:10,69,-67:10,70,-67:10,71,-67:10,71,-66:10,71,-65:10,69,-64:10,70,-64:10,71,-64:11,66,-74:11,68,-74:12,66,-74:12,68,-74:13,75,-80:13,76,-80:13,77,-80:13,75,-79:13,75,-78:13,75,-77:13,76,-77:13,77,-77:13,66,-74:13,68,-74:13,69,-74:13,70,-74:13,71,-74:13,72,-74:13,71,-67:13,71,-66:13,71,-65:13,69,-64:13,70,-64:13,71,-64:13,69,-63:13,69,-62:13,69,-61:13,69,-60:13,69,-59:13,69,-58:14,77,-80:14,77,-77:14,66,-74:14,72,-74:14,71,-67:14,69,-58:15,77,-80:15,77,-77:15,66,-74:15,71,-74:15,72,-74:15,69,-67:15,70,-67:15,71,-67:15,69,-58:16,75,-80:16,76,-80:16,77,-80:16,75,-79:16,75,-78:16,75,-77:16,76,-77:16,77,-77:16,66,-74:16,71,-74:16,69,-67:16,69,-58:17,66,-74:17,71,-74:17,69,-67:17,69,-58:18,66,-74:18,71,-74:18,72,-74:18,73,-74:18,69,-67:18,70,-67:18,69,-58:19,74,-79:19,74,-78:19,74,-77:19,66,-74:19,73,-74:19,70,-67:19,67,-62:19,68,-62:19,69,-62:19,69,-61:19,69,-60:19,69,-59:19,69,-58:20,74,-79:20,74,-77:20,66,-74:20,67,-74:20,68,-74:20,69,-74:20,70,-74:20,71,-74:20,72,-74:20,73,-74:20,68,-67:20,69,-67:20,70,-67:20,67,-62:21,72,-79:21,73,-79:21,74,-79:21,72,-78:21,72,-77:21,73,-77:21,74,-77:21,68,-67:21,67,-62:22,68,-67:22,69,-67:22,70,-67:22,70,-66:22,69,-65:22,70,-65:22,69,-64:22,69,-63:22,67,-62:22,68,-62:22,69,-62:";

		ArrayList<Location> locs = new ArrayList<Location>();

		for (String cur : locString.split(":"))
		{
			String[] tokens = cur.split(",");

			try
			{
				Location loc = new Location(getWorld(), Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));

				if (InBoundary(loc))
					locs.add(loc);
			}
			catch (Exception e)
			{
				System.out.println("Invalid Hub Snake Location: " + cur);
			}			
		}


		ArrayList<ArrayList<Location>> snakes = new ArrayList<ArrayList<Location>>();

		//Get Blacks
		Iterator<Location> locIterator = locs.iterator();
		while (locIterator.hasNext())
		{
			Location loc = locIterator.next();

			if (loc.getBlock().getData() != 0)
				continue;

			ArrayList<Location> newSnake = new ArrayList<Location>();
			newSnake.add(loc);
			snakes.add(newSnake);
			locIterator.remove();
		}

		//Get Body
		int partsAdded = 1;
		while (partsAdded > 0)
		{
			partsAdded = 0;

			locIterator = locs.iterator();
			while (locIterator.hasNext())
			{
				Location loc = locIterator.next();

				if (loc.getBlock().getData() == 15)
					continue;

				for (ArrayList<Location> snake : snakes)
				{
					if (UtilMath.offset(loc, snake.get(snake.size() - 1)) != 1)
						continue;

					snake.add(loc);
					locIterator.remove();

					partsAdded++;
				}
			}
		}

		//Get Tail
		try
		{
			locIterator = locs.iterator();
			while (locIterator.hasNext())
			{
				Location loc = locIterator.next();

				for (ArrayList<Location> snake : snakes)
				{
					if (UtilMath.offset(loc, snake.get(snake.size() - 1)) != 1)
						continue;

					snake.add(loc);
					locIterator.remove();

					partsAdded++;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//Write
		try 
		{
			File file = new File("SnakeRoutes.dat");
			if (!file.exists()) 
				file.createNewFile();

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (ArrayList<Location> snake : snakes)
			{
				String out = "";

				for (Location loc : snake)
					out += loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ":";

				bw.write(out + "\n\n\n");
			}

			bw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void Update()
	{
		if (_snakes == null)
			return;
		
		for (SnakePart snake : _snakes)
			snake.Update();
	}
}
