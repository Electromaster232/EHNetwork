package ehnetwork.game.microgames.game.games.event;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseBat;
import ehnetwork.core.disguise.disguises.DisguiseChicken;
import ehnetwork.core.disguise.disguises.DisguiseEnderman;
import ehnetwork.core.disguise.disguises.DisguiseWither;
import ehnetwork.core.gadget.event.GadgetActivateEvent;
import ehnetwork.core.mount.event.MountActivateEvent;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.shop.item.SalesPackageBase;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.core.visibility.VisibilityManager;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game;
import ehnetwork.game.microgames.game.games.event.kits.KitPlayer;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.managers.GameHostManager;

public class EventGame extends Game
{
	private GameHostManager _mps;

	private String[] _sideText = new String[] {
			" ","  ","   ","    ","     ","      ","       ","        ","         ","          ","           ","            "
			,"             ","              ","               "};

	private boolean _doubleJump = false;
	private boolean _gadgetsEnabled = true;
	private NautHashMap<String, Integer> _forcefield = new NautHashMap<String, Integer>();

	private ItemStack[] _kitItems = new ItemStack[6];

	private boolean _allowAllGadgets = false;
	private HashSet<SalesPackageBase> _gadgetWhitelist = new HashSet<SalesPackageBase>();

	public EventGame(MicroGamesManager manager)
	{
		super(manager, GameType.Event,

				new Kit[]
						{
				new KitPlayer(manager),
						},

						new String[]
								{
				""
								});

		this.JoinInProgress = true;

		this.DamageTeamSelf = true;
		this.DamagePvP = false;
		this.DamageEvP = false;
		this.DamagePvE = false;

		this.DeathMessages = false;
		this.DeathOut = false;

		this.CanAddStats = false;
		this.CanGiveLoot = false;

		this.GadgetsDisabled = false;

		this.TeleportsDisqualify = false;

		this.PrepareFreeze = false;

		this.BlockPlaceCreative = true;
		this.BlockBreakCreative = true;

		this.InventoryClick = true;
		this.InventoryOpenBlock = true;
		this.InventoryOpenChest = true;

		//Dont timeout
		this.GameTimeout = -1;

		_mps = manager.GetGameHostManager();

		this.CreatureAllow = true;
	}

	//Before GamePlayerManager puts onto Spec!
	@EventHandler(priority = EventPriority.LOW)
	public void specToTeam(PlayerJoinEvent event)
	{
		if (InProgress())
			joinTeam(event.getPlayer());
	}

	public void joinTeam(Player player)
	{
		//Set Team
		SetPlayerTeam(player, GetTeamList().get(0), true);

		//Kit
		SetKit(player, GetKits()[0], true);
		GetKits()[0].ApplyKit(player);

		//Refresh
		VisibilityManager.Instance.refreshPlayerToAll(player);

		//Spawn
		GetTeamList().get(0).SpawnTeleport(player);

		//GameMode
		player.setGameMode(GameMode.SURVIVAL);
	}

	@EventHandler
	public void doubleJumpTrigger(PlayerToggleFlightEvent event)
	{
		if (!_doubleJump)
			return;

		Player player = event.getPlayer();

		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		//Chicken Cancel
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise != null && 
				((disguise instanceof DisguiseChicken && !((DisguiseChicken)disguise).isBaby()) || disguise instanceof DisguiseBat || disguise instanceof DisguiseEnderman || disguise instanceof DisguiseWither))
			return;

		event.setCancelled(true);
		player.setFlying(false);

		//Disable Flight
		player.setAllowFlight(false);

		//Velocity
		UtilAction.velocity(player, 1.4, 0.2, 1, true);

		//Sound
		player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
	}

	@EventHandler
	public void doubleJumpRefresh(UpdateEvent event)
	{
		if (!_doubleJump)
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (player.getGameMode() == GameMode.CREATIVE)
				continue;

			if (UtilEnt.isGrounded(player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN)))
			{
				player.setAllowFlight(true);
				player.setFlying(false);
			}
		}
	}

	@EventHandler
	public void gadgetActivate(GadgetActivateEvent event)
	{
		if (!_gadgetsEnabled)
			event.setCancelled(true);
	}

	@EventHandler
	public void mountActivate(MountActivateEvent event)
	{
		if (!_gadgetsEnabled)
			event.setCancelled(true);
	}

	@EventHandler
	public void forcefieldUpdate(UpdateEvent event)
	{
		if (!InProgress())
			return;

		if (event.getType() != UpdateType.FASTER)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (_forcefield.containsKey(player.getName()))
			{
				for (Player other : UtilServer.getPlayers())
				{
					if (player.equals(other))
						continue;

					if (_mps.isAdmin(other, false))
						continue;

					int range = _forcefield.get(player.getName());

					if (UtilMath.offset(other, player) > range)
						continue;

					if (Recharge.Instance.use(other, "Forcefield Bump", 500, false, false))
					{
						Entity bottom = other;
						while (bottom.getVehicle() != null)
							bottom = bottom.getVehicle();

						UtilAction.velocity(bottom, UtilAlg.getTrajectory2d(player, bottom), 1.6, true, 0.8, 0, 10, true);
						other.getWorld().playSound(other.getLocation(), Sound.CHICKEN_EGG_POP, 2f, 0.5f);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void explosionBlocks(EntityExplodeEvent event)
	{
		event.blockList().clear();	
	}

	//	@EventHandler
	//	public void updateVisibility(UpdateEvent event)
	//	{
	//		if (!InProgress())
	//			return;
	//		
	//		if (event.getType() != UpdateType.FAST)
	//			return;
	//
	//		for (Player player : UtilServer.getPlayers())
	//		{
	//			if (!Manager.getPreferences().Get(player).ShowPlayers)
	//			{
	//				for (Player other : UtilServer.getPlayers())
	//				{
	//					if (player.equals(other))
	//						continue;
	//					
	//					((CraftPlayer)player).hidePlayer(other, true, false);
	//				}
	//			}
	//			else
	//			{
	//				for (Player other : UtilServer.getPlayers())
	//				{
	//					if (player.equals(other))
	//						continue;
	//
	//					if ((Manager.getPreferences().Get(player).Invisibility && _mps.isAdmin(player, false)) || )
	//					{
	//						((CraftPlayer)other).hidePlayer(player, true, false);
	//					}
	//					else
	//					{
	//						other.showPlayer(player);
	//					}
	//				}
	//			}
	//		}
	//	}	

	@Override
	public void EndCheck()
	{

	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (!InProgress())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		if (GetTeamList().isEmpty())
			return;

		Scoreboard.Reset();

		Scoreboard.WriteBlank();

		for (int i=_sideText.length-1 ; i>=0 ; i--)
		{
			Scoreboard.Write(_sideText[i]);
		}

		Scoreboard.Draw();
	}

	//This re-enables cosmetic hotbar, because MPS disables it - temp fix until i find out whats disabling it repeatedly
	@EventHandler(priority = EventPriority.MONITOR)
	public void fixHotbarItemTemp(UpdateEvent event)
	{	
		Manager.GetServerConfig().HotbarInventory = true;
	}

	public void giveItems(Player player)
	{
		UtilInv.Clear(player);

		for (int i=0 ; i<_kitItems.length ; i++)
		{
			if (_kitItems[i] == null)
				continue;

			player.getInventory().addItem(_kitItems[i].clone());
		}

		UtilInv.Update(player);
	}

	@EventHandler
	public void creatureNaturalRemove(CreatureSpawnEvent event)
	{
		if (event.getSpawnReason() != SpawnReason.CUSTOM)
			event.setCancelled(true);
	}

	@EventHandler
	public void gadgetDisable(GadgetActivateEvent event)
	{
		if (_allowAllGadgets)
			return;

		if (!_gadgetWhitelist.contains(event.getGadget()))
		{
			event.setCancelled(true);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			//event.getPlayer().closeInventory();
		}
	}

	@EventHandler
	public void mountDisable(MountActivateEvent event)
	{
		if (_allowAllGadgets)
			return;

		if (!_gadgetWhitelist.contains(event.getMount()))
		{
			event.setCancelled(true);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_BASS, 0.5f, 0.5f);
			//event.getPlayer().closeInventory();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void entityDeathEvent(EntityDeathEvent event)
	{
		event.getDrops().clear();
	}
	
	public boolean isAllowGadget()
	{
		return _allowAllGadgets;
	}
	
	public void setAllowGadget(boolean var)
	{
		_allowAllGadgets = var;
	}
	
	public HashSet<SalesPackageBase> getGadgetWhitelist()
	{
		return _gadgetWhitelist;
	}
	
	public boolean isDoubleJump()
	{
		return _doubleJump;
	}
	
	public void setDoubleJump(boolean var)
	{
		_doubleJump = var;
	}
	
	public NautHashMap<String, Integer> getForcefieldList() 
	{
		return _forcefield;
	}
	
	public ItemStack[] getKitItems()
	{
		return _kitItems;
	}
	
	public void setKitItems(ItemStack[] kit)
	{
		_kitItems = kit;
	}
	
	public String[] getSideText()
	{
		return _sideText;
	}

	@Override
	public List<Player> getWinners()
	{
		if (GetState().ordinal() >= GameState.End.ordinal())
		{
			List<Player> places = GetTeamList().get(0).GetPlacements(true);

			if (places.isEmpty() || !places.get(0).isOnline())
				return Arrays.asList();
			else
				return Arrays.asList(places.get(0));
		}
		else
			return null;
	}

	@Override
	public List<Player> getLosers()
	{
		List<Player> winners = getWinners();

		if (winners == null)
			return null;

		List<Player> losers = GetTeamList().get(0).GetPlayers(false);

		losers.removeAll(winners);

		return losers;
	}
	
}
