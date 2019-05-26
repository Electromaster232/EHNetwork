package nautilus.game.arcade.managers;

import java.util.HashMap;

import mineplex.core.achievement.Achievement;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.minecraft.game.core.combat.CombatComponent;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import nautilus.game.arcade.ArcadeFormat;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.events.PlayerStateChangeEvent;
import nautilus.game.arcade.game.Game;
import nautilus.game.arcade.game.GemData;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.game.GameTeam.PlayerState;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameGemManager implements Listener
{
	ArcadeManager Manager;

	boolean DoubleGem = true;

	public GameGemManager(ArcadeManager manager)
	{
		Manager = manager;

		Manager.getPluginManager().registerEvents(this, Manager.getPlugin());
	}

	@EventHandler
	public void PlayerKillAward(CombatDeathEvent event)
	{
		if (!Manager.IsRewardGems())
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		if (!(event.GetEvent().getEntity() instanceof Player))
			return;

		Player killed = (Player)event.GetEvent().getEntity();

		if (event.GetLog().GetKiller() != null)
		{
			Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

			if (killer != null && !killer.equals(killed))
			{
				//Kill
				game.AddGems(killer, game.GetKillsGems(killer, killed, false), "Kills", true, true);

				//First Kill
				if (game.FirstKill)
				{
					game.AddGems(killer, 10, "First Blood", false, false);

					game.FirstKill = false;

					game.Announce(F.main("Game", Manager.GetColor(killer) + killer.getName() + " drew first blood!"));
				}
			}
		}

		for (CombatComponent log : event.GetLog().GetAttackers())
		{
			if (event.GetLog().GetKiller() != null && log.equals(event.GetLog().GetKiller()))
				continue;

			Player assist = UtilPlayer.searchExact(log.GetName());

			//Assist
			if (assist != null)
				game.AddGems(assist, game.GetKillsGems(assist, killed, true), "Kill Assists", true, true);
		}
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		if (!Manager.IsRewardGems())
			return;

		Game game = Manager.GetGame();
		if (game == null)	return;

		RewardGems(game, event.getPlayer(), true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerStateChange(PlayerStateChangeEvent event)
	{
		if (!Manager.IsRewardGems())
			return;

		if (event.GetState() != PlayerState.OUT)
			return;

		if (event.GetGame().GetType() == GameType.WitherAssault || event.GetGame().GetType() == GameType.Paintball || event.GetGame().GetType() == GameType.MineStrike)
			return;

		RewardGems(event.GetGame(), event.GetPlayer(), false);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void GameStateChange(GameStateChangeEvent event)
	{
		if (!Manager.IsRewardGems())
			return;

		if (event.GetState() != GameState.Dead)
			return;

		for (Player player : UtilServer.getPlayers())
			RewardGems(event.GetGame(), player, true);
	}

	public void RewardGems(Game game, Player player, boolean give)
	{
		if (!Manager.IsRewardGems())
			return;

		//Inform Gems
		AnnounceGems(game, player, game.GetPlayerGems().get(player), give);

		//Give Gems
		if (give)
			GiveGems(game, player, game.GetPlayerGems().remove(player), game.GemMultiplier);
	}

	public void GiveGems(Game game, Player player, HashMap<String,GemData> gems, double gameMult)
	{
		if (!Manager.IsRewardGems())
			return;

		if (gems == null)	
			return;

		int earned = 0;

		for (GemData data : gems.values())
			earned += (int)data.Gems;

		if (earned <= 0)
			earned = 1;

		earned = (int) (earned * gameMult);

		int total = earned;

		//Gem Boooster
		if (game.GemBoosterEnabled)
			total += (int)(earned * game.GetGemBoostAmount());

		//Gem Finder
		if (game.GemHunterEnabled)
		{
			int gemFinder = Manager.GetAchievement().get(player.getName(), Achievement.GLOBAL_GEM_HUNTER).getLevel(); 
			if (gemFinder > 0)
			{
				total += (int)(earned * (gemFinder * 0.25));
			}
		}

		if (DoubleGem && game.GemDoubleEnabled && UtilPlayer.is1_8(player))
			total += earned;

		Manager.GetDonation().RewardGems(null, "Earned " + game.GetName(), player.getName(), player.getUniqueId(), total);

		//Stats
		Manager.GetStatsManager().incrementStat(player, "Global.GemsEarned", total);
		Manager.GetStatsManager().incrementStat(player, game.GetName()+".GemsEarned", total);
	}

	public void AnnounceGems(Game game, Player player, HashMap<String,GemData> gems, boolean give) 
	{
		if (gems == null)	
			return;

		player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);

		UtilPlayer.message(player, "");
		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, Manager.IsRewardGems() ? "§f§lGems Earned" : "§f§lGame Stats");
		UtilPlayer.message(player, "");

		int earnedGems = 0;

		for (String type : gems.keySet())
		{
			int gemCount = (int)gems.get(type).Gems;
			if (gemCount <= 0)
				gemCount = 1;

			earnedGems += gemCount;

			int amount = gems.get(type).Amount;
			String amountStr = "";
			if (amount > 0)
				amountStr = amount + " ";
			
			String out = "";
			if (Manager.IsRewardGems())
				out += F.elem(C.cGreen + "+" + (int)(gemCount * game.GemMultiplier) + " Gems") + " for ";
			out += F.elem(amountStr + type);

			UtilPlayer.message(player, out);
		}
		
		if (!Manager.IsRewardGems())
			return;

		earnedGems = (int) (earnedGems * game.GemMultiplier);

		int totalGems = earnedGems;

		//Gem Booster
		if (game.GetGemBoostAmount() > 0  && game.GemBoosterEnabled)
		{
			UtilPlayer.message(player, F.elem(C.cGreen + "+" + (int)(earnedGems*game.GetGemBoostAmount()) + " Gems") + " for " + 
					F.elem(game.GemBoosters.size() + " Gem Boosters " + C.cGreen + "+" + (int)(game.GetGemBoostAmount()*100) + "%"));

			totalGems += earnedGems * game.GetGemBoostAmount();
		}

		//Gem Finder
		if (game.GemHunterEnabled)
		{
			int gemFinder = Manager.GetAchievement().get(player.getName(), Achievement.GLOBAL_GEM_HUNTER).getLevel();
			if (gemFinder > 0)
			{
				UtilPlayer.message(player, F.elem(C.cGreen + "+" + ((int)(earnedGems*(gemFinder * 0.25)) + " Gems")) + " for " + 
						F.elem("Gem Hunter " + gemFinder + C.cGreen + " +" + (gemFinder*25) + "%"));

				totalGems += earnedGems * (gemFinder * 0.25);
			}
		}

		//Double Gem
		if (DoubleGem && game.GemDoubleEnabled && UtilPlayer.is1_8(player))
		{
			UtilPlayer.message(player, F.elem(C.cGreen + "+" + (earnedGems) + " Gems") + " for " + F.elem(C.cDGreen + "Double Gem Weekend"));

			totalGems += earnedGems;
		}

		//Inform
		UtilPlayer.message(player, "");
		if (give)
		{
			UtilPlayer.message(player, F.elem(C.cWhite + "§lYou now have " + 
					C.cGreen + C.Bold + (Manager.GetDonation().Get(player.getName()).GetGems() + totalGems) + " Gems"));
		}
		else
		{
			UtilPlayer.message(player, F.elem(C.cWhite + "§lGame is still in progress..."));
			UtilPlayer.message(player, F.elem(C.cWhite + "§lYou may earn more " + C.cGreen + C.Bold + "Gems" + C.cWhite + C.Bold + " when its completed."));
		}

		UtilPlayer.message(player, ArcadeFormat.Line);	
	}


}
