package ehnetwork.game.microgames.game.games.milkcow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.Navigation;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.core.visibility.VisibilityManager;
import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.events.GameStateChangeEvent;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.game.GameTeam.PlayerState;
import ehnetwork.game.microgames.game.SoloGame;
import ehnetwork.game.microgames.game.games.milkcow.kits.KitCow;
import ehnetwork.game.microgames.game.games.milkcow.kits.KitFarmerJump;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.NullKit;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class MilkCow extends SoloGame
{
	private GameTeam _farmers;
	private GameTeam _cows;

	private ArrayList<Location> _chickens;
	private ArrayList<Location> _pigs;
	private ArrayList<Location> _villager;

	private ArrayList<CowScore> _ranks = new ArrayList<CowScore>();
	private ArrayList<String> _lastScoreboard = new ArrayList<String>();

	private HashSet<Cow> _herd = new HashSet<Cow>();

	private Objective _scoreObj;

	public MilkCow(MicroGamesManager manager)
	{
		super(manager, GameType.MilkCow,

				new Kit[] 
						{ 
				new KitFarmerJump(manager),
				new NullKit(manager),
				new KitCow(manager),
						},

						new String[]
								{
				"Farmers get 1 point for drinking milk.",
				"You lose 5 points for dying!",
				"",
				"Cows get 1 point for killing farmers.",
				"Defend your herd to stop farmers!",
				"",
				"First player to 15 points wins!"
								});

		this.CompassEnabled = true;
		this.DeathOut = false;

		_scoreObj = Scoreboard.GetScoreboard().registerNewObjective("Milk", "dummy");
		_scoreObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}

	@Override
	public void ParseData() 
	{
		_chickens = WorldData.GetDataLocs("WHITE");
		_pigs = WorldData.GetDataLocs("PINK");
		_villager = WorldData.GetDataLocs("PURPLE");
	}

	@Override
	public void RestrictKits()
	{
		for (Kit kit : GetKits())
		{
			for (GameTeam team : GetTeamList())
			{
				if (team.GetColor() == ChatColor.RED)
				{
					if (kit.GetName().contains("Farmer"))
						team.GetRestrictedKits().add(kit);
				}
				else
				{
					if (kit.GetName().contains("Cow"))
						team.GetRestrictedKits().add(kit);
				}
			}
		}
	}

	@Override
	@EventHandler
	public void CustomTeamGeneration(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Recruit)
			return;

		_farmers = this.GetTeamList().get(0);
		_farmers.SetName("Farmers");

		//Undead Team
		_cows = new GameTeam(this, "Cow", ChatColor.RED, _farmers.GetSpawns());	
		GetTeamList().add(_cows);

		RestrictKits();
	}

	@Override
	public GameTeam ChooseTeam(Player player) 
	{
		return _farmers;
	}

	@EventHandler
	public void SpawnAnimals(GameStateChangeEvent event) 
	{
		if (event.GetState() != GameState.Live)
			return;

		for (Location loc : _chickens)
		{
			this.CreatureAllowOverride = true;
			Chicken ent = loc.getWorld().spawn(loc, Chicken.class);
			if (Math.random() > 0.75)
			{
				ent.setBaby();
				ent.setAgeLock(true);
			}

			this.CreatureAllowOverride = false;
		}

		for (Location loc : _pigs)
		{
			this.CreatureAllowOverride = true;
			Pig ent = loc.getWorld().spawn(loc, Pig.class);
			if (Math.random() > 0.75)
			{
				ent.setBaby();
				ent.setAgeLock(true);
			}

			this.CreatureAllowOverride = false;
		}

		for (Location loc : _villager)
		{
			this.CreatureAllowOverride = true;
			Villager ent = loc.getWorld().spawn(loc, Villager.class);
			if (Math.random() > 0.75)
			{
				ent.setCustomName("Bob");
				ent.setCustomNameVisible(true);
			}

			this.CreatureAllowOverride = false;
		}
	}

	@EventHandler
	public void HerdUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		if (!IsLive())
			return;

		if (_cows.GetPlayers(true).size() <= 0)
			return;

		Player host = _cows.GetPlayers(true).get(0);

		Iterator<Cow> herdIterator = _herd.iterator();

		while (herdIterator.hasNext())
		{
			Cow cow = herdIterator.next();

			if (!cow.isValid())
			{
				cow.remove();
				herdIterator.remove();
				continue;
			}

			//Move
			EntityCreature ec = ((CraftCreature)cow).getHandle();
			Navigation nav = ec.getNavigation();

			if (UtilMath.offset(cow, host) > 6)
			{
				if (UtilMath.offset(cow, host) > 16)
				{
					Location target = cow.getLocation();

					target.add(UtilAlg.getTrajectory(cow, host).multiply(16));

					nav.a(target.getX(), target.getY(), target.getZ(), 1.8f);
				}
				else
					nav.a(host.getLocation().getX(), host.getLocation().getY(),host.getLocation().getZ(), 1.4f);
			}		
		}

		while (_herd.size() < 5)
		{
			this.CreatureAllowOverride = true;
			Cow cow = host.getWorld().spawn(host.getLocation(), Cow.class);
			if (Math.random() > 0.5)
			{
				cow.setBaby();
				cow.setAgeLock(true);
			}

			_herd.add(cow);
			this.CreatureAllowOverride = false;
		}	
	}

	@EventHandler
	public void HerdDamage(CustomDamageEvent event)
	{
		if (event.GetDamageeEntity() instanceof Creature)
			event.SetCancelled("Cow Immunity");
	}

	@EventHandler
	public void CowUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.FAST)
			return;

		int req = 1;

		while (_cows.GetPlayers(true).size() < req && _farmers.GetPlayers(true).size() > 0)
		{
			Player player = _farmers.GetPlayers(true).get(UtilMath.r(_farmers.GetPlayers(true).size()));
			SetCow(player, true);
		}
	}

	public void SetCow(Player player, boolean forced)
	{
		//Set them as OUT!
		if (GetTeam(player) != null)
			GetTeam(player).SetPlacement(player, PlayerState.OUT);

		SetPlayerTeam(player, _cows, true);

		//Kit
		Kit newKit = 	GetKits()[2];		

		SetKit(player, newKit, false);
		newKit.ApplyKit(player);

		//Refresh
		VisibilityManager.Instance.refreshPlayerToAll(player);

		if (forced)
		{
			AddGems(player, 10, "Forced Cow", false, false);

			Announce(F.main("Game", F.elem(_farmers.GetColor() + player.getName()) + " has become " + 
					F.elem(_cows.GetColor() + newKit.GetName()) + "."));

			player.getWorld().strikeLightningEffect(player.getLocation());
		}
	}

	@EventHandler
	public void GetMilk(PlayerInteractEntityEvent event)
	{
		if (!UtilGear.isMat(event.getPlayer().getItemInHand(), Material.BUCKET))
			return;

		if (!(event.getRightClicked() instanceof Player))
			return;

		Player cow = (Player)event.getRightClicked();

		if (!_cows.HasPlayer(cow))
			return;

		event.setCancelled(true);

		event.getPlayer().setItemInHand(new ItemStack(Material.MILK_BUCKET));
	}

	@EventHandler
	public void DrinkMilk(PlayerItemConsumeEvent event)
	{
		if (!IsLive())
			return;

		if (event.getItem().getType() != Material.MILK_BUCKET)
			return;

		SetScore(event.getPlayer(), GetScore(event.getPlayer())+1);
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BURP, 2f, 1f);
		UtilPlayer.health(event.getPlayer(), 2);

		this.AddGems(event.getPlayer(), 0.5, "Milk Drunk", true, true);
	}

	@EventHandler
	public void LoseMilk(PlayerDeathEvent event)
	{
		SetScore(event.getEntity(), Math.max(0, GetScore(event.getEntity()) - 5));
	}

	@EventHandler
	public void KillFarmer(CombatDeathEvent event)
	{
		if (event.GetLog().GetKiller() == null)
			return;

		if (!event.GetLog().GetKiller().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (player == null)	return;

		//Score
		SetScore(player, GetScore(player)+1);
	}

	public void SetScore(Player player, double level)
	{
		_scoreObj.getScore(player.getName()).setScore((int)level);

		//Rank
		for (CowScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				score.Score = level;

				if (level == 15)
					End();

				return;
			}
		}

		_ranks.add(new CowScore(player, level));
	}

	public double GetScore(Player player)
	{
		if (!IsAlive(player))
			return 0;

		//Rank
		for (CowScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				return score.Score;
			}
		}

		return 0;
	}

	private void SortScores() 
	{
		for (int i=0 ; i<_ranks.size() ; i++)
		{
			for (int j=_ranks.size()-1 ; j>0 ; j--)
			{
				if (_ranks.get(j).Score > _ranks.get(j-1).Score)
				{
					CowScore temp = _ranks.get(j);
					_ranks.set(j, _ranks.get(j-1));
					_ranks.set(j-1, temp);
				}
			}
		}
	}

	private void End()
	{
		SortScores();

		//Set Places
		ArrayList<Player> places = new ArrayList<Player>();
		for (int i=0 ; i<_ranks.size() ; i++)
			places.add(i, _ranks.get(i).Player);

		//Award Gems
		if (_ranks.size() >= 1)
			AddGems(_ranks.get(0).Player, 20, "1st Place", false, false);

		if (_ranks.size() >= 2)
			AddGems(_ranks.get(1).Player, 15, "2nd Place", false, false);

		if (_ranks.size() >= 3)
			AddGems(_ranks.get(2).Player, 10, "3rd Place", false, false);

		//Participation
		for (Player player : GetPlayers(false))
			if (player.isOnline())
				AddGems(player, 10, "Participation", false, false);

		SetState(GameState.End);
		AnnounceEnd(places);
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		if (!UtilTime.elapsed(GetStateTime(), 5000))
			return;

		if (_cows.GetPlayers(true).size() <= 0)
			End();

		if (_farmers.GetPlayers(true).size() <= 0)
		{
			SetScore(_cows.GetPlayers(true).get(0), 20);
			End();
		}
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		//Wipe Last
		Scoreboard.Reset();
		
		Scoreboard.WriteBlank();

		for (Player player : GetPlayers(true))
		{
			int score = (int)GetScore(player);

			GameTeam team = GetTeam(player);
			if (team == null)	continue;
			
			Scoreboard.WriteOrdered("Milk", team.GetColor() + player.getName(), score, true);
		}
		
		Scoreboard.Draw();
	}

	@Override
	public boolean CanJoinTeam(GameTeam team)
	{
		int cows = (int)(GetPlayers(true).size()/5);

		if (team.GetColor() == ChatColor.RED)
			return team.GetSize() < cows;

		return team.GetSize() < GetPlayers(true).size() - cows;
	}

	@EventHandler
	public void BucketFill(PlayerBucketFillEvent event)
	{
		if (!IsLive())
		{
			event.setCancelled(true);
			UtilInv.Update(event.getPlayer());
		}

		if (event.getBlockClicked() == null)
			return;

		if (event.getBlockClicked().getTypeId() != 8 && event.getBlockClicked().getTypeId() != 9)
			return;

		event.setCancelled(true);
		UtilInv.Update(event.getPlayer());
		
		if (event.getBlockClicked() != null)
			event.getPlayer().sendBlockChange(event.getBlockClicked().getLocation(), 8, (byte)0);
	}
}
