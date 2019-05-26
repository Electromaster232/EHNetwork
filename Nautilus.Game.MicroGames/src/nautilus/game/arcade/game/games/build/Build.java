package nautilus.game.arcade.game.games.build;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.ItemStack;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextBottom;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTextMiddle;
import mineplex.core.explosion.ExplosionEvent;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.events.PlayerPrepareTeleportEvent;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.game.games.build.gui.MobShop;
import nautilus.game.arcade.game.games.build.gui.OptionsShop;
import nautilus.game.arcade.game.games.build.kits.KitBuilder;
import nautilus.game.arcade.kit.Kit;

public class Build extends SoloGame 
{
	private NautHashMap<Player, BuildData> _data = new NautHashMap<Player, BuildData>();
	
	private NautHashMap<Player, NautHashMap<Player, Integer>> _votes = new NautHashMap<Player, NautHashMap<Player, Integer>>();

	private ArrayList<Entry<Player,Double>> _scoreboardPlaces = new ArrayList<Entry<Player,Double>>();
	
	private int _buildGameState = 0;
	private long _buildStateTime = 0;
	private long _buildTime = 480000;
	private long _voteTime = 14000;
	private long _viewTime = 18000;
	private BuildData _viewData = null;

	private boolean selectRandom = true;

	private int _countdownTimerState = 0;

	private String[] _words;

	private String _word = "?";

	private OptionsShop _optionsShop;
	private MobShop _mobShop;
	private ItemStack _shopItem;
	
	private String[] _hintText = new String[]  
			{
			"Click Creatures to change their settings!",
			"Set the Time/Weather in the Options Menu!",
			"Vote based upon Effort, Creativity and Quality!",
			"Create Particles in the Options Menu!",
			"Vote fairly for other players! It's more fun!",
			
			};
	
	private int _hintIndex = 0;
	private long _hintTimer = 0;
	private ChatColor _hintColor = ChatColor.YELLOW;
	private ChatColor _firstHintColor = ChatColor.YELLOW;
	
	public Build(ArcadeManager manager) 
	{
		super(manager, GameType.Build,

				new Kit[]
						{
				new KitBuilder(manager),
						},

						new String[]
								{
				"Be creative and build something",
				"based on the build theme!"
								});

		this.StrictAntiHack = true;
		this.Damage = false;
		this.HungerSet = 20;
		this.HealthSet = 20;

		this.BlockBreak = true;
		this.BlockPlace = true; 

		this.ItemDrop = true;
		this.ItemPickup = true;

		this.InventoryClick = true;

		this.WorldTimeSet = 6000;

		this.PrepareFreeze = false;

		this.CreatureAllow = true;
		
		this.WorldFireSpread = true;
		this.WorldBoneMeal = true;
		
		this.DontAllowOverfill = true;
		
		UtilServer.getServer().spigot().getConfig().set("view-distance", 4);

		_words = new String[]
				{
					"Rollercoaster", "Pokemon", "Pirates", "Vikings", "Dinosaur", "Dragon", "Toilet", "Farm", "Tree House",
					"Cat", "Truck", "Bicycle", "Soda", "Music Instrument", "Statue", "Pot of Gold", "Shrek", "Fruit", "Breakfast",
					"Toaster", "Robot", "Camping", "Rocket", "Aliens", "Shipwreck", "Spongebob", "Car", "Potted Plant", "Weapons",
					"Christmas", "King", "Queen", "Angel", "Demon", "Halloween", "Tank", "Helicopter", "Knight", "Rabbit",
					"Sandwich", "Snowman", "Ice Cream", "Sea Shell", "Rainbow",
					"Volcano", "Hot Tub", "Octopus", "Ghost", "Ant", "Cheese", "Kite Flying", "Reptile", 
					"Space Ship", "Pixel Art", "Chicken", "Shoe", "Owl", "Bear", "Flowers", "Lighthouse",
					"Lion", "Television", "Batman", "Tiger", "Castle", "House",
					"Bed", "Party", "Volleyball", "Toys", "Library", "Love", "Skull",
					"Hat", "Snake", "Vacation", "Umbrella", "Magic", "Tornado", "Candy", "Dentist", "Pizza", "Bird",
					"Superhero", "Turtle", "Chicken", "Build Anything!", "Food", "Picnic",
					"Trophy", "Pool Party", "Hot Air Balloon", "Train", "Chocolate Bar",
					"Clown", "Windmill", "Alligator",
					"Police", "Igloo", "Gift", "Bumblebee", "Jellyfish", "Speedboat",
					"Fall", "Summer", "Autumn", "Winter", "Disco", "Moose",
					"Water Gun", "Astronaut", "Wither", "Meteor"
				};

		_mobShop = new MobShop(getArcadeManager(), getArcadeManager().GetClients(), getArcadeManager().GetDonation());
		_optionsShop = new OptionsShop(this, getArcadeManager(), getArcadeManager().GetClients(), getArcadeManager().GetDonation());
		_shopItem = ItemStackFactory.Instance.CreateStack(Material.DIAMOND, (byte) 0, 1, C.cGreen + "Options");
	}

	@EventHandler
	public void prepare(PlayerPrepareTeleportEvent event)
	{
		event.GetPlayer().setGameMode(GameMode.CREATIVE);
		event.GetPlayer().setFlying(true);
		event.GetPlayer().setFlySpeed(0.04f);
	}

	@EventHandler
	public void prepare(GameStateChangeEvent event)
	{
		if (event.GetState() == GameState.Prepare)
		{
			if (GetPlayers(true).size() > GetTeamList().get(0).GetSpawns().size())
			{
				SetState(GameState.End);
				Announce(C.Bold + "Too Many Players...");
				return;
			}
		}

		else if (event.GetState() == GameState.Live)
		{
			for (Player player : GetPlayers(true))
			{
				Location spawn = UtilAlg.findClosest(player.getLocation(), this.GetTeamList().get(0).GetSpawns());

				_data.put(player, new BuildData(player, spawn, WorldData.GetDataLocs("YELLOW")));
				
				player.setFlySpeed(0.1f);
			}

			if(selectRandom)
			{
				_word = _words[UtilMath.r(_words.length)];
			}

			UtilTextMiddle.display(null, C.cYellow + "Build " + C.cWhite + _word, 0, 80, 5);
			
			this.WorldTimeSet = -1;
		}
	}

	@EventHandler
	public void stateChange(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (!IsLive())
			return;

		//Game
		if (_buildGameState == 0)
		{
			if (_countdownTimerState == 0 && UtilTime.elapsed(GetStateTime(), 60000))
			{
				UtilTextMiddle.display(null, C.cYellow + "4 Minutes Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 1 && UtilTime.elapsed(GetStateTime(), 120000))
			{
				UtilTextMiddle.display(null, C.cYellow + "3 Minutes Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 2 && UtilTime.elapsed(GetStateTime(), 180000))
			{
				UtilTextMiddle.display(null, C.cYellow + "2 Minutes Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 3 && UtilTime.elapsed(GetStateTime(), 240000))
			{
				UtilTextMiddle.display(null, C.cYellow + "1 Minute Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 4 && UtilTime.elapsed(GetStateTime(), 270000))
			{
				UtilTextMiddle.display(null, C.cYellow + "30 Seconds Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 5 && UtilTime.elapsed(GetStateTime(), 285000))
			{
				UtilTextMiddle.display(null, C.cYellow + "15 Seconds Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 6 && UtilTime.elapsed(GetStateTime(), 295000))
			{
				UtilTextMiddle.display(null, C.cYellow + "5 Seconds Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 7 && UtilTime.elapsed(GetStateTime(), 296000))
			{
				UtilTextMiddle.display(null, C.cYellow + "4 Seconds Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 8 && UtilTime.elapsed(GetStateTime(), 297000))
			{
				UtilTextMiddle.display(null, C.cYellow + "3 Seconds Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 9 && UtilTime.elapsed(GetStateTime(), 298000))
			{
				UtilTextMiddle.display(null, C.cYellow + "2 Seconds Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			else if (_countdownTimerState == 10 && UtilTime.elapsed(GetStateTime(), 299000))
			{
				UtilTextMiddle.display(null, C.cYellow + "1 Second Remaining", 0, 60, 5);
				_countdownTimerState++;
			}
			
			if (System.currentTimeMillis() - GetStateTime() > _buildTime)
			{
				_buildGameState++;
				_buildStateTime = System.currentTimeMillis();

				//Flags
				this.BlockBreak = false;
				this.BlockPlace = false;
				
				this.ItemDrop = false;
				this.ItemPickup = false;

				this.InventoryClick = false;

				UtilTextMiddle.display(null, C.cYellow + "TIME IS UP!", 0, 60, 5);

				for (Player player : GetPlayers(true))
				{
					player.getInventory().clear(8);
					player.closeInventory();
				}
			}
		}
		//Pause
		else if (_buildGameState == 1)
		{
			if (UtilTime.elapsed(_buildStateTime, 5000))
			{
				_buildGameState++;
				_buildStateTime = System.currentTimeMillis();
			}
		}
		//Review Phase
		else if (_buildGameState == 2)
		{			
			//End Vote Time
			if (_viewData != null && (UtilTime.elapsed(_buildStateTime, _voteTime)))
					//_viewData.Score.size() == GetPlayers(true).size() - 1))	//All votes cast
			{
				for (Player player : GetPlayers(true))
					UtilInv.Clear(player);

				//Verdict
				if (!_viewData.Judged)
				{
					boolean hasDecentVote = false;
					
					
					for (Player player : _votes.keySet())
					{
						NautHashMap<Player, Integer> votes = _votes.get(player);
						
						if (votes.containsKey(_viewData.Player))
						{
							if (votes.get(_viewData.Player) > 2)
							{
								hasDecentVote = true;
								break;
							}
						}
					}
					
					String result = null;
					
					//More than half think its abusive
					if (GetPlayers(true).size() >= 4 && _viewData.AbuseVotes.size() >= (double)(GetPlayers(true).size() - 1) / 2d)
					{
						result = C.cWhite + "Inappropriate Build";
						_viewData.setAbusive();
						
						//Record Abuse
						AddStat(_viewData.Player, "Build Draw Abuse", 1, false, true);
						
						//Announce
						Announce(C.cWhite + C.Bold + _viewData.Player.getName() + " has been reported for an inappropriate build.", false);
						_viewData.Spawn.getWorld().playSound(_viewData.Spawn, Sound.ENDERDRAGON_GROWL, 10f, 1f);
						
						UtilPlayer.message(_viewData.Player, C.cWhite + C.Bold + "Inappropriate Builds can result in a Master Buildres ban.");
						
						//Return to Hub
						getArcadeManager().GetPortal().sendPlayerToServer(_viewData.Player, "Lobby");
					}
					else if (!hasDecentVote)
					{
						Manager.GetExplosion().BlockExplosion(_viewData.Blocks, _viewData.Spawn, false);

						//Effects
						_viewData.Spawn.getWorld().playSound(_viewData.Spawn, Sound.EXPLODE, 3f, 1f);
						UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, _viewData.Spawn, 4f, 4f, 4f, 0, 10,
								ViewDist.MAX, UtilServer.getPlayers());
						
						result = C.cRed + "Failure";
					}

					//Announce Builder
					UtilTextMiddle.display(result, "Built by: " + C.Bold + _viewData.Player.getName(), 0, 80, 5);
				}

				_viewData.Judged = true;
			}

			//Start Vote
			if (UtilTime.elapsed(_buildStateTime, _viewTime) || _viewData == null)
			{
				_viewData = null;

				//Get Next View Data
				for (BuildData data : _data.values())
				{
					if (!data.Judged)
					{
						_viewData = data;
						break;
					}
				}

				//All Builds are Viewed
				if (_viewData == null)
				{
					//Wait a little longer after voting, give time for EXPLODES
					if (UtilTime.elapsed(_buildStateTime, _voteTime))
					{
						_buildGameState++;
						_buildStateTime = System.currentTimeMillis();
					}
				}
				//Start Viewing
				else
				{
					teleportPlayers(_viewData);

					//Give Items
					for (Player player : GetPlayers(true))
					{
						UtilInv.Clear(player);

						player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(160, (byte)14, 1, C.cRed + C.Bold + "MY EYES ARE BLEEDING!"));
						player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(160, (byte)1, 1, C.cGold + C.Bold + "MEH..."));
						player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(160, (byte)4, 1, C.cYellow + C.Bold + "It's okay..."));
						player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(160, (byte)5, 1, C.cGreen + C.Bold + "Good"));
						player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(160, (byte)3, 1, C.cAqua + C.Bold + "Amazing"));
						player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(160, (byte)10, 1, C.cPurple + C.Bold + "WOW! EVERYTHING IS AWESOME!"));
						
						player.getInventory().setItem(8, ItemStackFactory.Instance.CreateStack(Material.BOOK, (byte)0, 1, C.cWhite + C.Bold + "Report Inappropriate Build"));
						
						UtilTextMiddle.display(null, C.cYellow + "Click to Vote", 0, 60, 5, player);
					} 
						

					_buildStateTime = System.currentTimeMillis();
				}
			}
		}
		//Pause
		else if (_buildGameState == 3)
		{
			if (UtilTime.elapsed(_buildStateTime, 5000))
			{
				_buildGameState++;
				_buildStateTime = System.currentTimeMillis();
			}
		}
		else if (_buildGameState == 4)
		{
			tallyScores();
			
			ArrayList<Player> places = new ArrayList<Player>();

			//Calculate Places
			boolean first = true;
			while (!_data.isEmpty())
			{
				Player bestPlayer = null;
				double bestPoints = 0;

				for (Player player : _data.keySet())
				{
					double points = _data.get(player).getPoints();

					if (bestPlayer == null || points > bestPoints)
					{
						bestPlayer = player;
						bestPoints = points;
					}
				}

				//Average points per player is 1000, so divided by 50 = 20 gems
				AddGems(bestPlayer, bestPoints / 50, "Build Votes", false, false);

				BuildData data = _data.remove(bestPlayer);

				//Teleport to winner
				if (first)
				{
					teleportPlayers(data);
					first = false;
				}

				places.add(bestPlayer);
				_scoreboardPlaces.add(new AbstractMap.SimpleEntry<Player, Double>(bestPlayer, bestPoints));
			}

			writeScoreboard();
			
			//Announce
			AnnounceEnd(places);

			//Gems
			if (places.size() >= 1)
				AddGems(places.get(0), 20, "1st Place", false, false);

			if (places.size() >= 2)
				AddGems(places.get(1), 15, "2nd Place", false, false);

			if (places.size() >= 3)
				AddGems(places.get(2), 10, "3rd Place", false, false);

			for (Player player : GetPlayers(false))
				if (player.isOnline())
					AddGems(player, 10, "Participation", false, false);

			//End
			SetState(GameState.End);
		}
	}

	private void tallyScores() 
	{
		//Reset, if being re-called
		for (BuildData data : _data.values())
		{
			data.clearPoints();
		}
		
		//Each player has 1000 points to give to the other builders. 
		//They are assigned based on a ratio of points given.
		
		//in a 5 player game, a player who gives everyone +0 will be giving everyone equally 250 Points
		for (Player voter : _votes.keySet())
		{
			//Gather Data
			double votesCast = 0;
			double voteTotal = 0;
			
			NautHashMap<Player, Integer> votes = _votes.get(voter);
			
			for (int vote : votes.values())
			{
				votesCast++;
				voteTotal += vote;
			}
			
			AddGems(voter, (int)(voteTotal / 3), "Voting Fairly", false, false);
			
			double votesNotCast = (GetPlayers(true).size() - 1) - votesCast;
			
			double averageVote = 1;
			if (votesCast > 0)
				averageVote = voteTotal/votesCast;
			
			//This ensures that only 1000 points will be shared among builds
			voteTotal += votesNotCast * averageVote;
			
			//Apply Points to builds
			for (Player builder : _data.keySet())
			{
				if (builder.equals(voter))
					continue;
				
				//If the voter didnt vote on this build, it will be given the average of their votes
				double vote = averageVote;
				if (votes.containsKey(builder))
					vote = votes.get(builder);
				
				double points = 1000d * (vote/voteTotal);
				
				_data.get(builder).addPoints(points);
				
//				System.out.println(voter.getName() + " = " + builder.getName() + " " + 
//				(votes.containsKey(builder) ? vote : "No Vote (" + averageVote + ")") + " ~ " + points);
			}
			
			//System.out.println( " " );
		}
	}

	public boolean isBuildTime()
	{
		return _buildStateTime == 0;
	}

	private void teleportPlayers(BuildData data) 
	{
		//Teleport 
		for (int i=0 ; i<UtilServer.getPlayers().length ; i++)
		{
			double lead =  i * ((2d * Math.PI)/UtilServer.getPlayers().length);

			double oX = -Math.sin(lead) * 26;
			double oZ = Math.cos(lead) * 26;

			Location loc = data.Spawn.clone().add(oX, 0, oZ);
			loc.setDirection(UtilAlg.getTrajectory(loc, data.Spawn));

			// This is supposed to be a quick fix for players being teleported into blocks
			// I am not sure the exact cause of this bug, it seems to happen when there is a specific amount of players
			if (loc.getBlock() != null && loc.getBlock().getType() != Material.AIR)
				loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);

			UtilServer.getPlayers()[i].closeInventory();
			UtilServer.getPlayers()[i].eject();
			UtilServer.getPlayers()[i].leaveVehicle();
			UtilServer.getPlayers()[i].teleport(loc);
			
			UtilServer.getPlayers()[i].setFlying(true);
		}
	}

	public BuildData getBuildData(Player player)
	{
		return _data.get(player);
	}

	@EventHandler
	public void blockPlace(BlockPlaceEvent event)
	{
		BuildData data = _data.get(event.getPlayer());

		if (data == null)
		{
			event.setCancelled(true);
			return;
		}

		if (!inBuildArea(data, event.getBlock()))
			event.setCancelled(true);
		else
			data.addBlock(event.getBlock());
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent event)
	{
		BuildData data = _data.get(event.getPlayer());

		if (data == null)
		{
			event.setCancelled(true);
			return;
		}

		if (!inBuildArea(data, event.getBlock()))
			event.setCancelled(true);
	}

	@EventHandler
	public void bucketEmpty(PlayerBucketEmptyEvent event)
	{
		if (_buildGameState != 0)
		{
			event.setCancelled(true);
			return;
		}

		BuildData data = _data.get(event.getPlayer());

		if (data == null)
		{
			event.setCancelled(true);
			return;
		}

		if (!inBuildArea(data, event.getBlockClicked().getRelative(event.getBlockFace())))
			event.setCancelled(true);
	}

	@EventHandler
	public void moveCheck(PlayerMoveEvent event)
	{
		BuildData data = _data.get(event.getPlayer());

		if (data == null)
			return;

		if (event.getTo().getY() > Math.max(data.CornerA.getBlockY(), data.CornerB.getBlockY()) + 3 &&
			UtilMath.offset(data.Spawn, event.getFrom()) < UtilMath.offset(data.Spawn, event.getTo()))
		{
			//Inform
			if (Recharge.Instance.use(event.getPlayer(), "Boundary Check", 1000, false, false))
				UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot leave your designated area!"));

			//Stop
			event.setTo(event.getFrom());

			//Velocity
			event.getPlayer().setVelocity(UtilAlg.getTrajectory(event.getTo(), data.Spawn));
		}
	}

	public boolean inBuildArea(BuildData data, Block block)
	{
		if (!data.inBuildArea(block))
		{
			UtilPlayer.message(data.Player, F.main("Game", "You can only build in your designated area!"));
			return false;
		}

		return true;
	}

	@EventHandler
	public void voteRegister(PlayerInteractEvent event)
	{
		if (!IsLive())
			return;

		if (_buildGameState != 2)
			return;

		if (_viewData == null)
			return;

		if (!IsAlive(event.getPlayer()))
			return;

		if (!UtilEvent.isAction(event, ActionType.R) && !UtilEvent.isAction(event, ActionType.L))
			return;

		if (event.getPlayer().equals(_viewData.Player))
		{
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You cannot vote on your own creation!"));
			return;
		}
		
		if (!UtilTime.elapsed(_buildStateTime, 1500))
			return;
		
		//Vote Abuse
		if (UtilGear.isMat(event.getPlayer().getItemInHand(), Material.BOOK))
		{
			_viewData.addAbuseVote(event.getPlayer());
			UtilTextMiddle.display(null, C.cWhite + C.Bold + "Inappropriate Build", 0, 40, 5, event.getPlayer());
			UtilPlayer.message(event.getPlayer(), C.cWhite + C.Bold + "You reported " + _viewData.Player.getName() + " for inappropriate build!");
			UtilPlayer.message(event.getPlayer(), C.cWhite + C.Bold + "Thanks for helping us keep Master Builders clean!");
			return;
		}
		
		if (!UtilGear.isMat(event.getPlayer().getItemInHand(), Material.STAINED_GLASS_PANE))
			return;

		if (!_votes.containsKey(event.getPlayer()))
			_votes.put(event.getPlayer(), new NautHashMap<Player, Integer>());
		
		switch (event.getPlayer().getItemInHand().getData().getData())
		{
		case 14:
			_votes.get(event.getPlayer()).put(_viewData.Player, 1);
			UtilTextMiddle.display(null, event.getPlayer().getItemInHand().getItemMeta().getDisplayName(), 0, 40, 5, event.getPlayer());
			break;
		case 1:
			_votes.get(event.getPlayer()).put(_viewData.Player, 2);
			UtilTextMiddle.display(null, event.getPlayer().getItemInHand().getItemMeta().getDisplayName(), 0, 40, 5, event.getPlayer());
			break;
		case 4:
			_votes.get(event.getPlayer()).put(_viewData.Player, 3);
			UtilTextMiddle.display(null, event.getPlayer().getItemInHand().getItemMeta().getDisplayName(), 0, 40, 5, event.getPlayer());
			break;
		case 5:
			_votes.get(event.getPlayer()).put(_viewData.Player, 4);
			UtilTextMiddle.display(null, event.getPlayer().getItemInHand().getItemMeta().getDisplayName(), 0, 40, 5, event.getPlayer());
			break;
		case 3:
			_votes.get(event.getPlayer()).put(_viewData.Player, 5);
			UtilTextMiddle.display(null, event.getPlayer().getItemInHand().getItemMeta().getDisplayName(), 0, 40, 5, event.getPlayer());
			break;
		case 10:
			_votes.get(event.getPlayer()).put(_viewData.Player, 6);
			UtilTextMiddle.display(null, event.getPlayer().getItemInHand().getItemMeta().getDisplayName(), 0, 40, 5, event.getPlayer());
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		_data.remove(event.getPlayer());
		
		for (NautHashMap<Player, Integer> votedFor : _votes.values())
			votedFor.remove(event.getPlayer());
		
		_votes.remove(event.getPlayer());
	}

	@EventHandler
	public void cleanTNT(UpdateEvent event)
	{
		if (!InProgress())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		for (Entity ent : this.WorldData.World.getEntities())
		{
			if (ent instanceof TNTPrimed)
			{
				ent.remove();
			}
		}
	}

	@Override
	@EventHandler
	public void onHangingPlace(HangingPlaceEvent event)
	{
		Player player = event.getPlayer();
		if (_buildGameState == 0 && IsLive() && IsAlive(player))
		{
			BuildData buildData = _data.get(player);

			if (buildData != null)
			{
				// Allow placing paintings and item frames in your own plot
				if (buildData.inBuildArea(event.getBlock())) return;
			}
		}

		event.setCancelled(true);
	}

	@Override
	@EventHandler
	public void onHangingBreak(HangingBreakEvent event)
	{
		if (event instanceof HangingBreakByEntityEvent)
		{
			HangingBreakByEntityEvent ev = ((HangingBreakByEntityEvent) event);
			if (ev.getRemover() instanceof Player)
				if (_buildGameState != 0 || !IsAlive(ev.getRemover()))
					event.setCancelled(true);
		}
	}

	@Override
	@EventHandler
	public void onDamageHanging(EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof Hanging)) return;

		if (event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent ev = ((EntityDamageByEntityEvent) event);

			if (ev.getDamager() instanceof Player)
			{
				Player player = ((Player) ev.getDamager());

				if (_buildGameState != 0 || !IsAlive(player))
					event.setCancelled(true);
			}
		}
		else super.onDamageHanging(event);
	}

	@EventHandler
	public void preventRotateHanging(PlayerInteractEntityEvent event)
	{
		EntityType type = event.getRightClicked().getType();
		if (_buildGameState != 0 && (type == EntityType.PAINTING || type == EntityType.ITEM_FRAME))
			event.setCancelled(true);
	}

	@EventHandler
	public void potionThrow(ProjectileLaunchEvent event)
	{
		if (event.getEntity() instanceof ThrownPotion)
			event.getEntity().remove();
	}

	@EventHandler
	public void bowShoot(EntityShootBowEvent event)
	{
		event.getProjectile().remove();
	}

	@EventHandler
	public void blockFromTo(BlockFromToEvent event)
	{
		for (BuildData data : _data.values())
			if (data.inBuildArea(event.getToBlock()))
				return;

		event.setCancelled(true);
	}

	@EventHandler
	public void pistonExtend(BlockPistonExtendEvent event)
	{
		for (BuildData data : _data.values())
			for (Block block : event.getBlocks())
				if (!data.inBuildArea(block))
				{
					event.setCancelled(true);
					return;
				}
	}

	@EventHandler
	public void pistonRetract(BlockPistonRetractEvent event)
	{
		for (BuildData data : _data.values())
			if (!data.inBuildArea(event.getBlock()))
			{
				event.setCancelled(true);
				return;
			}
	}

	@EventHandler
	public void entityCombust(EntityCombustEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void itemClean(UpdateEvent event)
	{
		if (!InProgress())
			return;
		
		if (event.getType() != UpdateType.FAST)
			return;

		for (BuildData data : _data.values())
		{
			data.clean();
		}

		for (Entity ent : this.WorldData.World.getEntities())
		{
			if (!(ent instanceof Item))
				continue;

			boolean isPlacedItem = false;
			for (BuildData data : _data.values())
			{
				if (data.Items.contains(ent))
				{
					isPlacedItem = true;
					break;
				}
			}

			if (!isPlacedItem)
				ent.remove();
		}
	}
	
	@EventHandler
	public void itemDrop(PlayerDropItemEvent event)
	{
		if (_buildGameState != 0)
			return;
		
		BuildData data = _data.get(event.getPlayer());

		if (data == null)
		{
			event.setCancelled(true);
			return;
		}

		data.addItem(event.getItemDrop());
	}
	
	@EventHandler
	public void itemPickup(PlayerPickupItemEvent event)
	{
		if (_buildGameState != 0)
			return;
		
		event.setCancelled(true);
		
		BuildData data = _data.get(event.getPlayer());

		if (data == null)
			return;
		
		if (data.Player.equals(event.getPlayer()))
		{
			event.getItem().remove();
			UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, event.getItem().getLocation().add(0, 0.5, 0), 0, 0, 0, 0, 1,
					ViewDist.LONG, UtilServer.getPlayers());
		}
	}
	
	@EventHandler
	public void itemExpire(ItemDespawnEvent event)
	{
		for (BuildData data : _data.values())
		{
			if (data.Items.contains(event.getEntity()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void dataClean(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (BuildData data : _data.values())
		{
			data.clean();
		}
	}

	@EventHandler
	public void entSpawn(CreatureSpawnEvent event)
	{
		if (event.getSpawnReason() != SpawnReason.SPAWNER_EGG)
		{
			event.setCancelled(true);
			return;
		}

		Player bestPlayer = null;
		double bestDist = 0;

		for (Player player : GetPlayers(true))
		{
			double dist = UtilMath.offset(event.getLocation(), player.getLocation());

			if (bestPlayer == null || dist < bestDist)
			{
				bestPlayer = player;
				bestDist = dist;
			}
		}

		if (bestPlayer == null)
		{
			event.setCancelled(true);
			return;
		}


		BuildData data = _data.get(bestPlayer);

		if (data == null)
		{
			event.setCancelled(true);
			return;
		}

		//Not in area
		if (!inBuildArea(data, event.getLocation().getBlock()))
		{
			event.setCancelled(true);
		}

		//Maxed
		else if (!data.addEntity(event.getEntity()))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void entKill(CustomDamageEvent event)
	{
		if (event.GetDamagerPlayer(false) == null)
			return;
		
		BuildData data = _data.get(event.GetDamagerPlayer(false));

		if (data == null)
			return;
		
		data.removeEntity(event.GetDamageeEntity());
	}
	
	@EventHandler
	public void vehicleSpawn(VehicleCreateEvent event)
	{
		if (event.getVehicle() instanceof Boat ||
				event.getVehicle() instanceof Minecart ||
				event.getVehicle() instanceof ExplosiveMinecart ||
				event.getVehicle() instanceof HopperMinecart ||
				event.getVehicle() instanceof CommandMinecart ||
				event.getVehicle() instanceof StorageMinecart ||
				event.getVehicle() instanceof SpawnerMinecart ||
				event.getVehicle() instanceof PoweredMinecart ||
				event.getVehicle() instanceof RideableMinecart ||
				event.getVehicle() instanceof Painting ||
				event.getVehicle() instanceof ItemFrame)
		{
			Player bestPlayer = null;
			double bestDist = 0;
			
			for (Player player : GetPlayers(true))
			{
				double dist = UtilMath.offset(event.getVehicle().getLocation(), player.getLocation());

				if (bestPlayer == null || dist < bestDist)
				{
					bestPlayer = player;
					bestDist = dist;
				}
			}

			if (bestPlayer == null)
			{
				event.getVehicle().remove();
				return;
			}


			BuildData data = _data.get(bestPlayer);

			if (data == null)
			{
				event.getVehicle().remove();
				return;
			}

			//Not in area
			if (!inBuildArea(data, event.getVehicle().getLocation().getBlock()))
			{
				event.getVehicle().remove();
			}

			//Maxed
			else if (!data.addEntity(event.getVehicle()))
			{
				event.getVehicle().remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void vehicleDamage(VehicleDamageEvent event)
	{
		if (_buildGameState != 0 || event.getAttacker() == null || !(event.getAttacker() instanceof Player))
		{
			event.setCancelled(true);
			return;
		}
		
		Player player = (Player)event.getAttacker();
		
		BuildData data = _data.get(player);

		if (data == null)
		{
			event.setCancelled(true);
			return;
		}
		
		data.removeEntity(event.getVehicle());
	}
	
	@EventHandler
	public void fireAllow(BlockIgniteEvent event)
	{
		if (event.getCause() != IgniteCause.FLINT_AND_STEEL && event.getCause() != IgniteCause.FIREBALL)
			event.setCancelled(true);
	}
	
	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (!InProgress())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		writeScoreboard();
	}
	
	public void writeScoreboard()
	{
		//Wipe Last
		Scoreboard.Reset();

		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cYellow + C.Bold + "Build Theme");
		Scoreboard.Write(_word);


		Scoreboard.WriteBlank();

		if (_buildGameState == 0)
		{
			Scoreboard.Write(C.cYellow + C.Bold + "Build Time");
			Scoreboard.Write(UtilTime.MakeStr(Math.max(0, _buildTime - (System.currentTimeMillis() - this.GetStateTime())), 0));
		}
		else if (_buildGameState == 2)
		{
			Scoreboard.Write(C.cYellow + C.Bold + "Vote Time");
			Scoreboard.Write(UtilTime.MakeStr(Math.max(0, _voteTime - (System.currentTimeMillis() - _buildStateTime)), 0));

//			if (_viewData != null)
//			{
//				Scoreboard.WriteBlank();
//				Scoreboard.Write(C.cYellow + C.Bold + "Votes " + ChatColor.RESET + _viewData.Score.size() + " / " + (GetPlayers(true).size()-1));
//				Scoreboard.Write(C.cYellow + C.Bold + "Avg " + ChatColor.RESET + UtilMath.trim(2, _viewData.getScore()) + "");
//				Scoreboard.Write(C.cYellow + C.Bold + "Rating " + ChatColor.RESET + BuildQuality.getQuality(_viewData.getScore()) + "");
//				Scoreboard.WriteBlank();
//				
//				for (Player player : _viewData.Score.keySet())
//				{
//					Scoreboard.Write("+" + _viewData.Score.get(player) + " " + player.getName());
//				}
//			}
		}
		else if (_buildGameState == 4)
		{	
			for (Entry<Player, Double> score : _scoreboardPlaces)
			{
				Scoreboard.Write(BuildQuality.getFinalQuality(score.getValue()).getColor() + (int)(score.getValue().intValue()/10) + " " + ChatColor.RESET + score.getKey().getName());
			}
		}


		Scoreboard.Draw();
	}

	@EventHandler
	public void disableInteraction(PlayerInteractEvent event)
	{
		if (_buildGameState != 0 && IsLive())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void openShop(PlayerInteractEvent event)
	{
		if (IsAlive(event.getPlayer()) && _shopItem.equals(event.getPlayer().getItemInHand()))
			_optionsShop.attemptShopOpen(event.getPlayer());
	}

	@EventHandler
	public void giveItemStart(GameStateChangeEvent event)
	{
		if (event.GetGame() == this && event.GetState() == GameState.Live)
		{
			for (Player player : GetPlayers(true))
				player.getInventory().setItem(8, _shopItem);
		}
	}

	@EventHandler
	public void giveItem(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST) 
			return;

		if (IsLive() && _buildGameState == 0)
			for (Player player : GetPlayers(true))
				player.getInventory().setItem(8, _shopItem);
	}

	@EventHandler
	public void stopShopItemMove(InventoryClickEvent event)
	{
		if (event.getClickedInventory() != null && _shopItem.equals(event.getClickedInventory().getItem(event.getSlot())))
			event.setCancelled(true);
	}

	@EventHandler
	public void stopShopItemDrop(PlayerDropItemEvent event)
	{
		if (_shopItem.equals(event.getItemDrop().getItemStack()))
			event.setCancelled(true);
	}

	@EventHandler
	public void placeParticles(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) 
			return;

		ItemStack itemInHand = event.getPlayer().getItemInHand();

		if (itemInHand != null && 
			itemInHand.getItemMeta() != null &&
			itemInHand.getItemMeta().getDisplayName() != null && 
			itemInHand.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Place "))
		{
			ParticleType particleType = ParticleType.getFromFriendlyName(itemInHand.getItemMeta().getDisplayName().substring(8));

			if (particleType != null)
			{
				BuildData data = _data.get(event.getPlayer());

				if (data != null)
				{
					data.addParticles(particleType);					
				}

				event.setCancelled(true);
			}
			else
			{
				System.out.println("Place particles error! This shouldn't happen!");
			}
		}
	}

	@EventHandler
	public void playParticles(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (_viewData != null)
		{ 
			_viewData.playParticles(true);
		}
		else
		{
			for (BuildData data : _data.values())
			{   
				data.playParticles(false);
			}
		}
	}

	@EventHandler
	public void setWeather(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		if (_viewData != null)
		{
			_viewData.playWeather(true);
		}
		else
		{
			for (BuildData data : _data.values())
			{
				data.playWeather(false);
			}
		}
	}

	@EventHandler
	public void clickInventory(InventoryClickEvent event)
	{
		if (event.getWhoClicked() instanceof Player)
		{
			Player player = ((Player) event.getWhoClicked());

			if (IsLive() && IsAlive(player))
			{
				BuildData buildData = _data.get(player);
				if (buildData != null)
					buildData.ClickedInventory = true;
			}
		}
	}

	@EventHandler
	public void showHints(UpdateEvent event)
	{
		if (!IsLive())
			return;
		
		if (_buildGameState != 0)
			return;

		if (event.getType() != UpdateType.FAST) 
			return;
		
		//Hints
		if (UtilTime.elapsed(_hintTimer, 8000))
		{
			if (_hintColor == ChatColor.AQUA)
				_hintColor = ChatColor.GREEN;
			else
				_hintColor = ChatColor.AQUA;
			
			_hintIndex = (_hintIndex + 1)%_hintText.length;
			
			_hintTimer = System.currentTimeMillis();
		}
		
		boolean showHint = !UtilTime.elapsed(_hintTimer, 1100);

		//Initial
		for (Player player : GetPlayers(true))
		{
			BuildData buildData = _data.get(player);
			if (buildData != null && !buildData.ClickedInventory)
			{
				UtilTextBottom.display(_firstHintColor + "Open Inventory to get Blocks!", player);
			}
			else if (showHint)
			{
				UtilTextBottom.display(_hintColor + _hintText[_hintIndex], player);
			}
		}
		
		//Initial Flash
		if (_firstHintColor == ChatColor.YELLOW)
			_firstHintColor = ChatColor.GREEN;
		else
			_firstHintColor = ChatColor.YELLOW;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void explode(ExplosionEvent event)
	{
		event.GetBlocks().clear();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void explode(EntityExplodeEvent event)
	{
		event.blockList().clear();
	}

	@EventHandler
	public void openMobGui(PlayerInteractEntityEvent event)
	{
		if (!IsLive() || !IsAlive(event.getPlayer()) || _buildGameState != 0) return;

		Player player = event.getPlayer();
		BuildData buildData = _data.get(player);

		if (buildData != null)
		{
			for (Entity e : buildData.Entities)
			{
				if (e instanceof LivingEntity || e instanceof Minecart || e instanceof Boat)
				{
					if (e.equals(event.getRightClicked()))
					{
						_mobShop.attemptShopOpen(player, buildData, e);
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	@Override
	public Location GetSpectatorLocation()
	{
		if (GetPlayers(true).size() > 0)
			return UtilAlg.Random(GetPlayers(true)).getLocation().add(0, 1, 0);
		
		return GetTeamList().get(0).GetSpawn();
	}
	
	@EventHandler
	public void kickAbusers(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		for (Player player : UtilServer.getPlayers())
		{
			if (Manager.GetStatsManager().Get(player).getStat("Global.Build Draw Abuse") >= 3)
			{
				UtilPlayer.message(player, C.cRed + C.Bold + "You have been flagged as an Inappropriate Builder!");
				UtilPlayer.message(player, C.cRed + C.Bold + "As a result, you are banned from this game.");
				player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10f, 1f);
				getArcadeManager().GetPortal().sendPlayerToServer(player, "Lobby");
			}
		}
	}

	@EventHandler
	public void overrideWord(PlayerCommandPreprocessEvent event){
		if (!event.getMessage().startsWith("/selectword "))
			return;

		event.setCancelled(true);

		if(GetState() != GameState.Prepare){
			UtilPlayer.message(event.getPlayer(), F.main("Game", "The game has already started!"));
			return;
		}

		Rank pRank = Manager.GetClients().Get(event.getPlayer()).GetRank();

		if(pRank == Rank.ADMIN || pRank == Rank.OWNER || pRank == Rank.YOUTUBE){
			selectRandom = false;
			_word = event.getMessage().substring(12);
			UtilPlayer.message(event.getPlayer(), F.main("Game", "You have set the theme to " + C.cGreen + event.getMessage().substring(12)));
		}
		else{
			UtilPlayer.message(event.getPlayer(), F.main("Permissions", "You do not have access to that command!"));
			return;
		}


	}
}
