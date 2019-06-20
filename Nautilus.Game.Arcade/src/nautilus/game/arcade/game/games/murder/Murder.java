package nautilus.game.arcade.game.games.murder;


import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextMiddle;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.condition.ConditionFactory;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeFormat;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.GameStateChangeEvent;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.games.murder.kits.KitDetective;
import nautilus.game.arcade.game.games.murder.kits.KitMurderer;
import nautilus.game.arcade.game.games.murder.kits.KitPlayer;
import nautilus.game.arcade.kit.Kit;

public class Murder extends SoloGame
{
	private boolean MurdererShot;
	private Player murderer;
	private Player detective;
	private Kit KitMurderer;
	private Kit KitDetective;
	public Murder(ArcadeManager manager)
	{
		super(manager, GameType.Murder,
				new Kit[]
						{
								new KitPlayer(manager),
								new KitDetective(manager),
								new KitMurderer(manager)
						},

				new String[]
						{
								"Avoid being killed by the murderer!",
								"If you are the detective, find and kill the murderer!"
						});
		//this.DisableKillCommand = false;
		this.BlockPlace = false;
		this.BlockBreak = false;
		this.PrepareFreeze = false;
		this.DamageFall = false;
		this.DeathOut = true;
		this.HungerSet = 20;
		this.DeathMessages = false;
		this.FirstKill = false;
		//this.StrictAntiHack = true;
		this.DamageTeamOther = true;
		this.Damage = true;
		this.DamagePvP = true;
		this.DamageTeamSelf = true;
		// whoopsie this did not work lmao this.WorldWaterDamage = 5;

	}

	@EventHandler
	public void SetPlayers(GameStateChangeEvent event){
		if(event.GetState() != GameState.Prepare){
			return;
		}


		for(Kit kit : getArcadeManager().GetGame().GetKits()){
			if(kit.GetFormattedName().contains("Detective")){
				KitDetective = kit;
			}
			else if(kit.GetFormattedName().contains("Murderer")){
				KitMurderer = kit;
			}
		}
	}

	@EventHandler
	public void SetPlayerKits(GameStateChangeEvent event){
		if (!IsLive())
			return;

		Random randomizer = new Random();
		murderer = getArcadeManager().GetGame().GetPlayers(false).get(randomizer.nextInt(getArcadeManager().GetGame().GetPlayers(false).size()));
		detective = getArcadeManager().GetGame().GetPlayers(false).get(randomizer.nextInt(getArcadeManager().GetGame().GetPlayers(false).size()));
		MurdererShot = false;
		getArcadeManager().GetGame().SetKit(murderer, KitMurderer, false);
		UtilTextMiddle.display(C.cRed + "YOU ARE THE MURDERER", "Kill everyone!", murderer);
		getArcadeManager().GetGame().SetKit(detective, KitDetective, false);
		UtilTextMiddle.display(C.cBlue + "YOU ARE THE DETECTIVE", "Find and kill the murderer!", detective);
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

		Scoreboard.Write(C.cYellow + "Players Alive:");


		Integer num = 0;
		for(Player player : getArcadeManager().GetGame().GetPlayers(true)){
			num = num + 1;
		}
		Scoreboard.Write(C.cGreen + num.toString());
		Scoreboard.Draw();
	}


	@Override
	public void EndCheck()
	{
		if (!IsLive())
			return;

		if (MurdererShot == true)
		{
			//Set Places
			for(Player player : getArcadeManager().GetGame().GetPlayers(false)){
				AddGems(player, 20, "Winning", false, false);
			}



			//Participation
			for (Player player : GetPlayers(false))
				if (player.isOnline())
				{
					AddGems(player, 10, "Participation", false, false);
				}
			SetState(GameState.End);
			AnnounceEnd(ChatColor.GREEN + "Players");
			return;
		}

		if (GetPlayers(true).size() <= 1)
		{
			//Set Places
			for(Player player : getArcadeManager().GetGame().GetPlayers(true)){
				AddGems(player, 20, "Winning", false, false);
			}



			//Participation
			for (Player player : GetPlayers(false))
				if (player.isOnline())
				{
					AddGems(player, 10, "Participation", false, false);
				}
			SetState(GameState.End);
			AnnounceEnd(ChatColor.RED + "Murderer");
		}
	}

	public void AnnounceEnd(String WinningTeam){

		String winnerText = WinningTeam;
		ChatColor subColor = ChatColor.WHITE;

		for (Player player : UtilServer.getPlayers())
		{
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1f);

			UtilPlayer.message(player, "");
			UtilPlayer.message(player, ArcadeFormat.Line);

			UtilPlayer.message(player, "§aGame - §f§l" + this.GetName());
			UtilPlayer.message(player, "");
			UtilPlayer.message(player, "");


			UtilPlayer.message(player, winnerText + " won the game!");
			UtilPlayer.message(player, C.cRed + C.Bold + "The murderer was " + murderer.getDisplayName());
			UtilPlayer.message(player, C.cBlue + C.Bold + "The detective was " + detective.getDisplayName());


			UtilPlayer.message(player, "");
			UtilPlayer.message(player, "§aMap - §f§l" + WorldData.MapName + C.cGray + " created by " + "§f§l" + WorldData.MapAuthor);

			UtilPlayer.message(player, ArcadeFormat.Line);
		}

		UtilTextMiddle.display(winnerText, subColor + "won the game", 20, 120, 20);

		if (AnnounceSilence)
			Manager.GetChat().Silence(5000, false);
	}



	@EventHandler
	public void Damage(EntityDamageByEntityEvent event)
	{
		if (!IsLive())
			return;

		Player damager = (Player) event.getDamager();
		Player entity = (Player) event.getEntity();

		/*
		if (damager != detective || damager != murderer){
			System.out.println("W R O N G");
			System.out.println(damager.getName());
			event.setCancelled(true);
			return;
		}*/

		if(damager == murderer){
			entity.damage(9001);
			if(entity == detective){
				NewDetective();
			}
			getArcadeManager().GetCondition().AddCondition(getArcadeManager().GetCondition().Factory().Blind("blind", entity, damager, 3, 1000, false, true, true));
		}

		if(damager == detective){
			if(entity == murderer){
				entity.damage(9001);
				MurdererShot = true;
			}
			else{
				entity.damage(9001);
				damager.damage(9001);
				NewDetective();
			}
		}
		EndCheck();


	}


	public void NewDetective(){
		UtilServer.broadcast(C.cGreen + C.Bold + "The detective has died! A new one has been randomly selected!");
		Random randomizer = new Random();
		detective = getArcadeManager().GetGame().GetPlayers(true).get(randomizer.nextInt(getArcadeManager().GetGame().GetPlayers(true).size()));
		getArcadeManager().GetGame().SetKit(detective, KitDetective, false);
		UtilTextMiddle.display(C.cBlue + "YOU ARE THE DETECTIVE", "Find and kill the murderer!", detective);
	}

}
