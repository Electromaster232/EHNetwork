package nautilus.game.arcade.game.games.quiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import mineplex.minecraft.game.core.condition.Condition.ConditionType;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.quiver.kits.*;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.stats.SharpShooterStatTracker;
import nautilus.game.arcade.stats.WinWithoutBowStatTracker;
import nautilus.game.arcade.stats.WinWithoutDyingStatTracker;

public class Quiver extends SoloGame
{
	private ArrayList<QuiverScore> _ranks = new ArrayList<QuiverScore>();
	private HashMap<Player, Integer> _combo = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> _bestCombo = new HashMap<Player, Integer>();
	private HashMap<Player, Long> _deathTime = new HashMap<Player, Long>();
	
	private Objective _scoreObj;

	public Quiver(ArcadeManager manager) 
	{
		super(manager, GameType.Quiver,

				new Kit[]
						{
				new KitLeaper(manager),
				new KitBrawler(manager),
				new KitEnchanter(manager),
				new KitSlamShot(manager)
						},

						new String[]
								{
				"Bow and Arrow insta-kills.",
				"You receive 1 Arrow per kill.",
				"Glass blocks are breakable",
				"First player to 20 kills wins."
								});

		this.HungerSet = 20;
		this.DeathOut = false;
		this.DamageSelf = false;
		this.DamageTeamSelf = true;
		this.PrepareFreeze = false;
		this.BlockBreakAllow.add(102);
		this.BlockBreakAllow.add(20);
		
		_scoreObj = Scoreboard.GetScoreboard().registerNewObjective("Kills", "dummy");
		_scoreObj.setDisplaySlot(DisplaySlot.BELOW_NAME);

		registerStatTrackers(
				new WinWithoutDyingStatTracker(this, "Perfectionist"),
				new SharpShooterStatTracker(this),
				new WinWithoutBowStatTracker(this, "WhatsABow")
		);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (event.GetState() != GameState.Live)
			return;
		
		for (Player player : GetPlayers(true))
		{
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)0, 1, F.item("Super Arrow")));
			player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 3f, 2f);
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
					player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)0, 1, F.item("Super Arrow")));
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

		int amount = 1;
		
		if (GetKit(player) instanceof KitSlamShot)
		{
			if (Manager.GetCondition().HasCondition(event.GetEvent().getEntity(), ConditionType.FALLING, null))
			{
				amount = 2;
			}
		}
		
		//New Arrow
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)0, amount, F.item("Super Arrow")));
		player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 3f, 2f);

		//Score
		AddKill(player);
	}

	@EventHandler
	public void ComboReset(CombatDeathEvent event)
	{
		if (!(event.GetEvent().getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.GetEvent().getEntity();
		
		if (!_combo.containsKey(player))
			return;

		int combo = _combo.remove(player);

		int best = 0;
		if (_bestCombo.containsKey(player))
			best = _bestCombo.get(player);

		if (combo > best)
			_bestCombo.put(player, combo);
	}

	public void AddKill(Player player)
	{
		//Combo
		int combo = 1;
		if (_combo.containsKey(player))
			combo += _combo.get(player);

		_combo.put(player, combo);

		AnnounceCombo(player, combo);

		//Rank
		for (QuiverScore score : _ranks)
		{
			if (score.Player.equals(player))
			{
				score.Kills += 1;
				_scoreObj.getScore(player.getName()).setScore(score.Kills);
				EndCheck();
				return;
			}
		}

		_ranks.add(new QuiverScore(player, 1));
		_scoreObj.getScore(player.getName()).setScore(1);
	}

	private void AnnounceCombo(Player player, int combo) 
	{
		String killType = null;
		if (combo == 20)		killType = "PERFECT RUN";
		else if (combo == 13)	killType = "GODLIKE";
		else if (combo == 11)	killType = "UNSTOPPABLE";
		else if (combo == 9)	killType = "ULTRA KILL";
		else if (combo == 7)	killType = "MONSTER KILL";
		else if (combo == 5)	killType = "MEGA KILL";
		else if (combo == 3)	killType = "TRIPLE KILL";

		if (killType == null)
			return;

		//Announce
		for (Player other : UtilServer.getPlayers())
		{
			UtilPlayer.message(other, F.main("Game", C.cGreen + C.Bold + player.getName() + ChatColor.RESET + " got " + 
					F.elem(C.cAqua + C.Bold + killType +" (" + combo + " Kills)!")));
			other.playSound(other.getLocation(), Sound.ENDERDRAGON_GROWL, 1f + (combo/10f), 1f + (combo/10f));
		}
	}

	private void SortScores() 
	{
		for (int i=0 ; i<_ranks.size() ; i++)
		{
			for (int j=_ranks.size()-1 ; j>0 ; j--)
			{
				if (_ranks.get(j).Kills > _ranks.get(j-1).Kills)
				{
					QuiverScore temp = _ranks.get(j);
					_ranks.set(j, _ranks.get(j-1));
					_ranks.set(j-1, temp);
				}
			}
		}
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

		SortScores();
		
		//Wipe Last
		Scoreboard.Reset();
		
		Scoreboard.WriteBlank();

		//Write New
		for (QuiverScore score : _ranks)
		{
			Scoreboard.WriteOrdered("Score", C.cGreen + score.Player.getName(), score.Kills, true);
		}
		
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

		SortScores();

		if ((!_ranks.isEmpty() && _ranks.get(0).Kills >= 20) || GetPlayers(true).size() <= 1)
		{
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

			//Combo Gems
			for (Player player : _bestCombo.keySet())
			{
				int combo = _bestCombo.get(player);
				
				if (combo >= 20)		AddGems(player, 40, "PERFECT - 20 Kill Combo", false, false);
				else if (combo >= 13)	AddGems(player, 24, "GODLIKE - 13 Kill Combo", false, false);
				else if (combo >= 11)	AddGems(player, 20, "UNSTOPPABLE - 11 Kill Combo", false, false);
				else if (combo >= 9)	AddGems(player, 16, "ULTRA KILL - 9 Kill Combo", false, false);
				else if (combo >= 7)	AddGems(player, 12, "MONSTER KILL - 7 Kill Combo", false, false);
				else if (combo >= 5)	AddGems(player, 8, "MEGA KILL - 5 Kill Combo", false, false);
				else if (combo >= 3)	AddGems(player, 4, "TRIPLE KILL - 3 Kill Combo", false, false);
			}
			
			//Participation
			for (Player player : GetPlayers(false))
				if (player.isOnline())
					AddGems(player, 10, "Participation", false, false);

			SetState(GameState.End);
			AnnounceEnd(places);
		}
	}
	
	@Override
	public List<Player> getWinners()
	{
		if (GetState().ordinal() >= GameState.End.ordinal())
		{
			SortScores();
			
			//Set Places
			ArrayList<Player> places = new ArrayList<Player>();
			for (int i=0 ; i<_ranks.size() ; i++)
				places.add(i, _ranks.get(i).Player);

			if (places.isEmpty() || !places.get(0).isOnline())
				return Arrays.asList();
			else
				return Arrays.asList(places.get(0));
		}
		else
			return null;
	}
}
