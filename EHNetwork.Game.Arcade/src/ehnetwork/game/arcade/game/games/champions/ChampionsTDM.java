package ehnetwork.game.arcade.game.games.champions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryType;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.games.champions.kits.KitAssassin;
import ehnetwork.game.arcade.game.games.champions.kits.KitBrute;
import ehnetwork.game.arcade.game.games.champions.kits.KitKnight;
import ehnetwork.game.arcade.game.games.champions.kits.KitMage;
import ehnetwork.game.arcade.game.games.champions.kits.KitRanger;
import ehnetwork.game.arcade.game.games.common.TeamDeathmatch;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.stats.ElectrocutionStatTracker;
import ehnetwork.game.arcade.stats.KillAllOpposingStatTracker;
import ehnetwork.game.arcade.stats.KillReasonStatTracker;
import ehnetwork.game.arcade.stats.SeismicSlamStatTracker;
import ehnetwork.game.arcade.stats.TheLongestShotStatTracker;
import ehnetwork.game.arcade.stats.WinWithoutLosingTeammateStatTracker;
import ehnetwork.minecraft.game.core.combat.DeathMessageType;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class ChampionsTDM extends TeamDeathmatch
{
	public ChampionsTDM(ArcadeManager manager) 
	{
		super(manager, GameType.ChampionsTDM,

				new Kit[]
						{
				new KitBrute(manager),
				new KitRanger(manager),
				new KitKnight(manager),
				new KitMage(manager),
				new KitAssassin(manager),
						});
		
		_help = new String[]  
				{ 
				"Capture Beacons faster with more people!",
				"Make sure you use all of your Skill/Item Tokens",
				"Collect Resupply Chests to restock your inventory",
				"Customize your Class to suit your play style",
				"Gold Sword boosts Sword Skill by 1 Level",
				"Gold Axe boosts Axe Skill by 1 Level",
				"Gold/Iron Weapons deal 6 damage",
				"Diamond Weapons deal 7 damage",
				  
				};

		this.Manager.GetDamage().UseSimpleWeaponDamage = false;
		Manager.getCosmeticManager().setHideParticles(true);

		this.StrictAntiHack = true;
		
		InventoryOpenChest = true;
		
		this.DisableKillCommand = false;
		
		this.DontAllowOverfill = true;

		registerStatTrackers(
				new WinWithoutLosingTeammateStatTracker(this, "FlawlessVictory"),
				new KillAllOpposingStatTracker(this),
				new KillReasonStatTracker(this, "Backstab", "Assassination", false),
				new ElectrocutionStatTracker(this),
				new TheLongestShotStatTracker(this),
				new SeismicSlamStatTracker(this)
		);
	}       
	
	@Override    
	public void ValidateKit(Player player, GameTeam team)
	{ 
		//Set to Default Knight
		if (GetKit(player) == null)
		{
			SetKit(player, GetKits()[2], true);
			player.closeInventory();
		}
	}
	
	@Override
	public DeathMessageType GetDeathMessageType()
	{
		return DeathMessageType.Detailed;
	}
	
	@EventHandler
	public void WaterArrowCancel(EntityShootBowEvent event)
	{
		if (event.getEntity().getLocation().getBlock().isLiquid())
		{
			UtilPlayer.message(event.getEntity(), F.main("Game", "You cannot use your Bow while swimming."));
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void InventoryDamageCancel(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player player = event.GetDamageePlayer();
		if (player == null)
			return;
		
		if (!IsAlive(player))
			return;
		
		if (player.getOpenInventory() == null || player.getOpenInventory().getTopInventory() == null)
			return;
		
		if (player.getOpenInventory().getTopInventory().getType() == InventoryType.CHEST)
			player.closeInventory();
	}
	
	@EventHandler
	public void validateSkills(UpdateEvent event)
	{
		if (event.getType() == UpdateType.SEC)
		{
			for (Player player : GetPlayers(true))
			{
				Manager.getClassManager().Get(player).validateClassSkills(player);
			}
		}
	}
}
