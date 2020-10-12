package ehnetwork.game.arcade.game.games.dragons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;

import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.events.PlayerStateChangeEvent;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.GameTeam.PlayerState;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.dragons.kits.KitCoward;
import ehnetwork.game.arcade.game.games.dragons.kits.KitMarksman;
import ehnetwork.game.arcade.game.games.dragons.kits.KitPyrotechnic;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkSparkler;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class DragonsTeams extends TeamGame
{
	private HashMap<EnderDragon, DragonTeamsData> _dragons = new HashMap<EnderDragon, DragonTeamsData>();
	private ArrayList<Location> _dragonSpawns = new ArrayList<Location>();
	
	private ArrayList<String> _lastScoreboard = new ArrayList<String>();
	
	private HashMap<GameTeam, Integer> _teamScore = new HashMap<GameTeam, Integer>();
	
	private PerkSparkler _sparkler = null;
	
	public DragonsTeams(ArcadeManager manager) 
	{
		super(manager, GameType.DragonsTeams,

				new Kit[]
						{
				new KitCoward(manager),
				new KitMarksman(manager),
				new KitPyrotechnic(manager)
						},

						new String[]
								{
				"You have angered the Dragons!",
				"Survive as best you can!!!",
				"Team with longest time survived wins!"
								});
		
		this.DamagePvP = false;
		this.HungerSet = 20;
		this.WorldWaterDamage = 4;
		this.PrepareFreeze = false;
		
		this.TeamArmor = true;
		this.TeamArmorHotbar = true;
	}
	
	@Override
	public void ParseData() 
	{
		_dragonSpawns = WorldData.GetDataLocs("RED");
	}
		
	@EventHandler
	public void SparklerAttract(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (_sparkler == null)
		{
			for (Kit kit : GetKits())
			{
				for (Perk perk : kit.GetPerks())
				{
					if (perk instanceof PerkSparkler)
					{
						_sparkler = (PerkSparkler)perk;
					}
				}
			}
		}
		
		for (Item item : _sparkler.GetItems())
		{
			for (DragonTeamsData data : _dragons.values())
			{
				if (UtilMath.offset(data.Location, item.getLocation()) < 48)
				{
					data.TargetEntity = item;
				}
			}
		}
	}
	
	@EventHandler
	public void Death(PlayerStateChangeEvent event)
	{
		if (event.GetState() != PlayerState.OUT)
			return;
		
		long time = (System.currentTimeMillis() - GetStateTime());
		double gems = time/10000d;
		String reason = "Survived for " + UtilTime.MakeStr(time);
		
		this.AddGems(event.GetPlayer(), gems, reason, false, false);
	}
		
	@EventHandler
	public void DragonSpawn(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		if (GetState() != GameState.Live)
			return;
		
		Iterator<EnderDragon> dragonIterator = _dragons.keySet().iterator();
		
		while (dragonIterator.hasNext())
		{
			EnderDragon ent = dragonIterator.next();
			
			if (!ent.isValid())
			{
				dragonIterator.remove();
				ent.remove();
			}
		}

		if (_dragons.size() < 7)	
		{
			CreatureAllowOverride = true;
			EnderDragon ent = GetSpectatorLocation().getWorld().spawn(_dragonSpawns.get(0), EnderDragon.class);
			UtilEnt.Vegetate(ent);
			CreatureAllowOverride = false;
			
			ent.getWorld().playSound(ent.getLocation(), Sound.ENDERDRAGON_GROWL, 20f, 1f);
			
			_dragons.put(ent, new DragonTeamsData(this, ent));
		}
	}
	
	@EventHandler
	public void DragonLocation(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (GetState() != GameState.Live)
			return;
		
		//Dragon Update!
		for (DragonTeamsData data : _dragons.values())
		{
			data.Target();
			data.Move();
		}
	}
	
	@EventHandler
	public void DragonTargetCancel(EntityTargetEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void DragonArrowDamage(CustomDamageEvent event)
	{
		if (event.GetProjectile() == null)
			return;
		
		if (!_dragons.containsKey(event.GetDamageeEntity()))
			return;
		
		_dragons.get(event.GetDamageeEntity()).HitByArrow();
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player damagee = event.GetDamageePlayer();
		if (damagee == null)		return;
		
		if (event.GetDamagerEntity(true) == null)
			return;
		
		event.SetCancelled("Dragon");
		event.AddMod("Dragon", "Damage Reduction", -1 * (event.GetDamageInitial()-1), false);
		
		event.SetKnockback(false);
		
		damagee.playEffect(EntityEffect.HURT);
		
		UtilAction.velocity(damagee, UtilAlg.getTrajectory(event.GetDamagerEntity(true), damagee), 1, false, 0, 0.6, 2, true);
	}
	
	@EventHandler
	public void FallDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() == DamageCause.FALL)
			event.AddMod("Fall Reduction", "Fall Reduction", -1, false);
	}
	
	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		if (IsLive())
			for (GameTeam team : GetTeamList())
			{
				if (!_teamScore.containsKey(team))
					_teamScore.put(team, 0);
				
				if (team.IsTeamAlive())
					_teamScore.put(team, _teamScore.get(team) + team.GetPlayers(true).size());
			}
		
		WriteScoreboard();
	}

	private void WriteScoreboard()
	{
		//Wipe Last
		Scoreboard.Reset();

		for (GameTeam team : _teamScore.keySet())
		{
			//Time
			int seconds = _teamScore.get(team);
			
			Scoreboard.WriteBlank();
			Scoreboard.Write(team.GetColor() + team.GetName());
			Scoreboard.Write(team.GetColor() + "" + seconds + " Seconds");
		}
		
		Scoreboard.Draw();
	}
	
	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();

		for (GameTeam team : this.GetTeamList())
			if (team.GetPlayers(true).size() > 0)
				teamsAlive.add(team);

		if (teamsAlive.size() <= 0)
		{
			//Get Winner
			GameTeam winner = null;
			int bestTime = 0;
			
			for (GameTeam team : _teamScore.keySet())
			{
				if (winner == null || _teamScore.get(team) > bestTime)
				{
					winner = team;
					bestTime = _teamScore.get(team);
				}
			}
			
			//Announce
			if (winner != null)
			{
				AnnounceEnd(winner);
				this.SetCustomWinLine("Survived " + bestTime + " Seconds!");
			}
				
			
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
