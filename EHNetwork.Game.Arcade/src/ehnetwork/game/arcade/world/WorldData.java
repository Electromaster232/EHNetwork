package ehnetwork.game.arcade.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.event.world.ChunkUnloadEvent;
import net.minecraft.server.v1_7_R4.ChunkPreLoadEvent;

import ehnetwork.core.common.util.FileUtil;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.WorldUtil;
import ehnetwork.core.common.util.ZipUtil;
import ehnetwork.core.timing.TimingManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.Game;
import ehnetwork.game.arcade.game.games.uhc.UHC;

public class WorldData 
{
	public Game Host;
	
	public int Id = -1;
	
	public String File = null;
	public String Folder = null;
	
	public World World;
	public int MinX = 0;
	public int MinZ = 0;
	public int MaxX = 0;
	public int MaxZ = 0;
	public int CurX = 0; 
	public int CurZ = 0;
	
	public int MinY = -1;
	public int MaxY = 256;
	
	public String MapName = "Null";
	public String MapAuthor = "Null";
	
	public GameType Game = null;
	
	public HashMap<String, ArrayList<Location>> SpawnLocs = new HashMap<String, ArrayList<Location>>();
	private HashMap<String, ArrayList<Location>> DataLocs = new HashMap<String, ArrayList<Location>>();
	private HashMap<String, ArrayList<Location>> CustomLocs = new HashMap<String, ArrayList<Location>>();
	private final Map<String, String> _dataEntries = new HashMap<>();
	
	public WorldData(Game game)
	{
		Host = game;
		
		Initialize();
		
		Id = GetNewId();
	}
	
	public void Initialize()
	{
		final WorldData worldData = this;
		GetFile();
		
		UtilServer.getServer().getScheduler().runTaskAsynchronously(Host.Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				//Unzip
				worldData.UnzipWorld();
				
				//Load World Data Sync
				UtilServer.getServer().getScheduler().runTask(Host.Manager.getPlugin(), new Runnable()
				{
					public void run()
					{
						TimingManager.start("WorldData loading world.");
						//Start World
						
						if (Host instanceof UHC)
						{
							//Delete Old World
							File dir = new File(GetFolder() + "/data");
							FileUtil.DeleteFolder(dir);
							
							dir  = new File(GetFolder() + "/region");
							FileUtil.DeleteFolder(dir);
							
							dir  = new File(GetFolder() + "/level.dat");
							if (dir.exists())
								dir.delete();
							
							//Create Fresh World with Random Seed
							WorldCreator creator = new WorldCreator(GetFolder());
							creator.seed(UtilMath.r(999999999));
							creator.environment(Environment.NORMAL);
							creator.generateStructures(true);
							World = WorldUtil.LoadWorld(creator);
						}
						else
						{
							World = WorldUtil.LoadWorld(new WorldCreator(GetFolder()));
						}						
						TimingManager.stop("WorldData loading world.");
						
						World.setDifficulty(Difficulty.HARD);

						TimingManager.start("WorldData loading WorldConfig.");
						//Load World Data
						worldData.LoadWorldConfig();
						TimingManager.stop("WorldData loading WorldConfig.");
					}
				});
			}
		});
	}
	
	protected GameType GetGame()
	{
		return Game;
	}
	
	protected String GetFile()
	{
		if (File == null)
		{
			GameType game = null;
			int gameRandom = UtilMath.r(Host.GetFiles().size());
			int i = 0;
			for(GameType type : Host.GetFiles().keySet())
			{
				if(i == gameRandom)
				{
					game = type; 
					break;
				}
				i++;
			}
			Game = game;
			int map = UtilMath.r(Host.GetFiles().get(game).size());
			File = Host.GetFiles().get(game).get(map);
			
			//Don't allow repeat maps.
			if (Host.GetFiles().size() > 1)
			{
				while (File.equals(Host.Manager.GetGameCreationManager().GetLastMap()))
				{
					GameType _game = null;
					int _gameRandom = UtilMath.r(Host.GetFiles().size());
					int _i = 0;
					for(GameType _type : Host.GetFiles().keySet())
					{
						if(_i == _gameRandom)
						{
							_game = _type; 
							break;
						}
						_i++;
					}
					int _map = UtilMath.r(Host.GetFiles().get(game).size());
					File = Host.GetFiles().get(_game).get(_map);
				}
			}
		}
			
		Host.Manager.GetGameCreationManager().SetLastMap(File);
		
		return File;
	}
	
	public String GetFolder()
	{
		if (Folder == null) 
		{
			Folder = "Game" + Id + "_" + GetGame().GetName() + "_" + GetFile();
		}	
		return Folder;
	}
	
	protected void UnzipWorld() 
	{
		TimingManager.start("UnzipWorld creating folders");
		String folder = GetFolder();
		new File(folder).mkdir();
		new File(folder + java.io.File.separator + "region").mkdir();
		new File(folder + java.io.File.separator + "data").mkdir();
		TimingManager.stop("UnzipWorld creating folders");
		
		TimingManager.start("UnzipWorld UnzipToDirectory");
		ZipUtil.UnzipToDirectory("../../update/maps/" + GetGame().GetName() + "/" + GetFile() + ".zip", folder);
		TimingManager.stop("UnzipWorld UnzipToDirectory");
	}
	
	public void LoadWorldConfig() 
	{
		//Load Track Data
		String line = null;
		
		try
		{
			FileInputStream fstream = new FileInputStream(GetFolder() + java.io.File.separator + "WorldConfig.dat");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			ArrayList<Location> currentTeam = null;
			ArrayList<Location> currentData = null;
			
			int currentDirection = 0;
		
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
					MapAuthor = tokens[1];
				}
				
				//Spawn Locations
				else if (tokens[0].equalsIgnoreCase("TEAM_NAME"))
				{
					SpawnLocs.put(tokens[1], new ArrayList<Location>());
					currentTeam = SpawnLocs.get(tokens[1]);
					currentDirection = 0;
				}
				else if (tokens[0].equalsIgnoreCase("TEAM_DIRECTION"))
				{
					currentDirection = Integer.parseInt(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("TEAM_SPAWNS"))
				{
					for (int i=1 ; i<tokens.length ; i++)
					{
						Location loc = StrToLoc(tokens[i]);
						if (loc == null)	continue;
						
						loc.setYaw(currentDirection);
						
						currentTeam.add(loc);
					}
				}
				
				//Data Locations
				else if (tokens[0].equalsIgnoreCase("DATA_NAME"))
				{
					DataLocs.put(tokens[1], new ArrayList<Location>());
					currentData = DataLocs.get(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("DATA_LOCS"))
				{
					for (int i=1 ; i<tokens.length ; i++)
					{
						Location loc = StrToLoc(tokens[i]);
						if (loc == null)	continue;
						
						currentData.add(loc);
					}
				}
				
				//Custom Locations
				else if (tokens[0].equalsIgnoreCase("CUSTOM_NAME"))
				{
					CustomLocs.put(tokens[1], new ArrayList<Location>());
					currentData = CustomLocs.get(tokens[1]);
				}
				else if (tokens[0].equalsIgnoreCase("CUSTOM_LOCS"))
				{
					for (int i=1 ; i<tokens.length ; i++)
					{
						Location loc = StrToLoc(tokens[i]);
						if (loc == null)	continue;
						
						currentData.add(loc);
					}
				}
				
				//Map Bounds
				else if (tokens[0].equalsIgnoreCase("MIN_X"))
				{
					try
					{
						MinX = Integer.parseInt(tokens[1]);
						CurX = MinX;
					}
					catch (Exception e)
					{
						System.out.println("World Data Read Error: Invalid MinX [" + tokens[1] + "]");
					}
					
				}
				else if (tokens[0].equalsIgnoreCase("MAX_X"))
				{
					try
					{
						MaxX = Integer.parseInt(tokens[1]);
					}
					catch (Exception e)
					{
						System.out.println("World Data Read Error: Invalid MaxX [" + tokens[1] + "]");
					}
				}
				else if (tokens[0].equalsIgnoreCase("MIN_Z"))
				{
					try
					{
						MinZ = Integer.parseInt(tokens[1]);
						CurZ = MinZ;
					}
					catch (Exception e)
					{
						System.out.println("World Data Read Error: Invalid MinZ [" + tokens[1] + "]");
					}
				}
				else if (tokens[0].equalsIgnoreCase("MAX_Z"))
				{
					try
					{
						MaxZ = Integer.parseInt(tokens[1]);
					}
					catch (Exception e)
					{
						System.out.println("World Data Read Error: Invalid MaxZ [" + tokens[1] + "]");
					}
				}
				else if (tokens[0].equalsIgnoreCase("MIN_Y"))
				{
					try
					{
						MinY = Integer.parseInt(tokens[1]);
					}
					catch (Exception e)
					{
						System.out.println("World Data Read Error: Invalid MinY [" + tokens[1] + "]");
					}
				}
				else if (tokens[0].equalsIgnoreCase("MAX_Y"))
				{
					try
					{
						MaxY = Integer.parseInt(tokens[1]);
					}
					catch (Exception e)
					{
						System.out.println("World Data Read Error: Invalid MaxY [" + tokens[1] + "]");
					}
				}
				else
				{
					_dataEntries.put(tokens[0], tokens[1]);
				}
			}

			in.close();
			
			Host.Manager.GetGameWorldManager().RegisterWorld(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Line: " + line);
		}
	}
	
	protected Location StrToLoc(String loc)
	{
		String[] coords = loc.split(",");
		
		try
		{
			return new Location(World, Integer.valueOf(coords[0])+0.5, Integer.valueOf(coords[1]), Integer.valueOf(coords[2])+0.5);
		}
		catch (Exception e)
		{
			System.out.println("World Data Read Error: Invalid Location String [" + loc + "]");
		}
	
		return null;
	}
	
	public boolean LoadChunks(long maxMilliseconds)
	{
		long startTime = System.currentTimeMillis();
		
		for (; CurX <= MaxX; CurX += 16)
        {	
            for (; CurZ <= MaxZ; CurZ += 16) 
            {
    			if (System.currentTimeMillis() - startTime >= maxMilliseconds)
    				return false;
                
    			World.getChunkAt(new Location(World, CurX, 0, CurZ));
            }
            
            CurZ = MinZ;
        }
		
    	return true;
	}
	
	public void Uninitialize() 
	{	
		if (World == null)
			return;
		
		//Wipe World
		MapUtil.UnloadWorld(Host.Manager.getPlugin(), World);
		MapUtil.ClearWorldReferences(World.getName());
		FileUtil.DeleteFolder(new File(World.getName()));
		
		World = null;
	}
	
	public void ChunkUnload(ChunkUnloadEvent event) 
	{
		if (World == null)
			return;
		
		if (!event.getWorld().equals(World))
			return;
		
		event.setCancelled(true);
	}

	public void ChunkLoad(ChunkPreLoadEvent event) 
	{
		if (World == null)
			return;
		
		if (!event.GetWorld().equals(World))
			return;
		
		int x = event.GetX();
		int z = event.GetZ();
		

		if (x >= MinX >> 4 && x <= MaxX >> 4 && z >= MinZ >> 4 && z <= MaxZ >> 4)
		{
			return;
		}
	
		
		event.setCancelled(true);
	}
	
	public int GetNewId() 
	{
		File file = new File("GameId.dat");

		//Write If Blank
		if (!file.exists())
		{
			try
			{
				FileWriter fstream = new FileWriter(file);
				BufferedWriter out = new BufferedWriter(fstream);

				out.write("0");

				out.close();
			}
			catch (Exception e)
			{
				System.out.println("Error: Game World GetId Write Exception");
			}
		}

		int id = 0;

		//Read
		try
		{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();

			id = Integer.parseInt(line);

			in.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: Game World GetId Read Exception");
			id = 0;
		}

		try
		{
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);

			out.write("" + (id + 1));

			out.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: Game World GetId Re-Write Exception");
		}

		return id;
	}

	public ArrayList<Location> GetDataLocs(String data)
	{
		if (!DataLocs.containsKey(data))
			return new ArrayList<Location>();
		
		return DataLocs.get(data);
	}
	
	public ArrayList<Location> GetCustomLocs(String id)
	{
		if (!CustomLocs.containsKey(id))
			return new ArrayList<Location>();
		
		return CustomLocs.get(id);
	}
	
	public HashMap<String, ArrayList<Location>> GetAllCustomLocs()
	{
		return CustomLocs;
	}

	public Location GetRandomXZ() 
	{
		Location loc = new Location(World, 0, 250, 0);
		
		int xVar = MaxX - MinX;
		int zVar = MaxZ - MinZ;
		
		loc.setX(MinX + UtilMath.r(xVar));
		loc.setZ(MinZ + UtilMath.r(zVar));

		return loc;
	}

	public String get(String key)
	{
		return _dataEntries.get(key);
	}
	
}
