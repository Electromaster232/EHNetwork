package ehnetwork.game.arcade.game.games.quiver;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.GameStateChangeEvent;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.quiver.kits.KitBrawler;
import ehnetwork.game.arcade.game.games.quiver.kits.KitLeaper;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.minecraft.game.core.combat.event.CombatDeathEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class QuiverTeams extends TeamGame
{	
	private HashMap<GameTeam, Integer> _teamKills = new HashMap<GameTeam, Integer>();
	private HashMap<Player, Long> _deathTime = new HashMap<Player, Long>();

	private int _reqKills = 100;
	
	public QuiverTeams(ArcadeManager manager) 
	{
		super(manager, GameType.QuiverTeams,

				new Kit[]
						{
				new KitLeaper(manager),
				new KitBrawler(manager),
						},

						new String[]
								{
				"Bow and Arrow insta-kills.",
				"You receive 1 Arrow per kill.",
				"Glass blocks are breakable",
				"First team to 60 kills wins."
								});

		this.HungerSet = 20;
		this.DeathOut = false;
		this.DamageSelf = false;
		this.DamageTeamSelf = false;
		this.PrepareFreeze = false;
		this.BlockBreakAllow.add(102);
		this.BlockBreakAllow.add(20);
		this.BlockBreakAllow.add(18);
		
		this.TeamArmor = true;
		this.TeamArmorHotbar = true;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;

		for (Player player : GetPlayers(true))
		{
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)1, 1, F.item("Super Arrow")));
			player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 3f, 2f);
		}

		for (GameTeam team : GetTeamList())
		{
			_teamKills.put(team, 0);
		}
	}

	@EventHandler
	public void BowShoot(EntityShootBowEvent event)
	{
		if (!(event.getProjectile() instanceof Arrow))
			return;

		Arrow arrow = (Arrow)event.getProjectile();

		if (arrow.getShooter() == null)
			return;

		if (!(arrow.getShooter() instanceof Player))
			return;

		if (!_deathTime.containsKey(arrow.getShooter()))
			return;

		if (UtilTime.elapsed(_deathTime.get(arrow.getShooter()), 1000))
			return;

		event.getProjectile().remove();

		final Player player = (Player)arrow.getShooter();

		Manager.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				if (!player.getInventory().contains(Material.ARROW))
					player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)1, 1, F.item("Super Arrow")));
			}
		}, 10);

	}

	@EventHandler
	public void Death(CombatDeathEvent event)
	{
		if (event.GetEvent().getEntity() instanceof Player)
		{
			_deathTime.put((Player)event.GetEvent().getEntity(), System.currentTimeMillis());
		}

		if (event.GetLog().GetKiller() == null)
			return;

		if (!event.GetLog().GetKiller().IsPlayer())
			return;

		Player player = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
		if (player == null)	return;

		//New Arrow
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)1, 1, F.item("Super Arrow")));
		player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 3f, 2f);

		//Score
		AddKill(player);
	}


	public void AddKill(Player player)
	{
		GameTeam team = GetTeam(player);
		if (team == null)	return;

		_teamKills.put(team, _teamKills.get(team) + 1);

		WriteScoreboard();
		EndCheck();
	}

	@EventHandler
	public void ArrowDamage(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;

		event.AddMod("Projectile", "Instagib", 9001, false);
		event.SetKnockback(false);

		event.GetProjectile().remove();
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		WriteScoreboard();
	}

	private void WriteScoreboard()
	{
		//Wipe Last
		Scoreboard.Reset();

		for (GameTeam team : _teamKills.keySet())
		{
			int kills = _teamKills.get(team);
			
			Scoreboard.WriteBlank();
			Scoreboard.Write(team.GetColor() + team.GetName());
			Scoreboard.Write(kills + "" + team.GetColor() + " Kills");
		}
		
		Scoreboard.WriteBlank();
		Scoreboard.Write(C.Bold + "First To");
		Scoreboard.Write(C.cGold + C.Bold + _reqKills + " Kills");
		
		Scoreboard.Draw();
	}

	@EventHandler
	public void PickupCancel(PlayerPickupItemEvent event)
	{
		event.setCancelled(true);
	}

	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		GameTeam winner = null;
		
		for (GameTeam team : _teamKills.keySet())
		{
			if (_teamKills.get(team) >= _reqKills)
			{
				winner = team;
				break;
			}
		}
		
		ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

		for (GameTeam team : this.GetTeamList())
			if (team.GetPlayers(true).size() > 0)
				teamsAlive.add(team);

		if (winner != null || teamsAlive.size() <= 1 || GetPlayers(true).size() <= 1)
		{
			//Announce
			if (winner != null)
				AnnounceEnd(winner);
			else if (teamsAlive.size() == 1)
				AnnounceEnd(teamsAlive.get(0));
			
			for (GameTeam team : GetTeamList())
			{
				if (WinnerTeam != null && team.equals(WinnerTeam))
				{
					for (Player player : team.GetPlayers(false))
						AddGems(player, 10, "Winning Team", false, false);
				}
				
				for (Player player : team.GetPlayers(false))
					if (player.isOnline())
						AddGems(player, 10, "Participation", false, false);
			}
			
			//End
			SetState(GameState.End);	
		}
	}
}
