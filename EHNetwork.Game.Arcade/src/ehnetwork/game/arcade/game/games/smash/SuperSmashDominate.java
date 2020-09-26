package ehnetwork.game.arcade.game.games.smash;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.arcade.ArcadeFormat;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.games.common.Domination;
import ehnetwork.game.arcade.game.games.smash.kits.KitBlaze;
import ehnetwork.game.arcade.game.games.smash.kits.KitChicken;
import ehnetwork.game.arcade.game.games.smash.kits.KitCreeper;
import ehnetwork.game.arcade.game.games.smash.kits.KitEnderman;
import ehnetwork.game.arcade.game.games.smash.kits.KitGolem;
import ehnetwork.game.arcade.game.games.smash.kits.KitMagmaCube;
import ehnetwork.game.arcade.game.games.smash.kits.KitPig;
import ehnetwork.game.arcade.game.games.smash.kits.KitSkeletalHorse;
import ehnetwork.game.arcade.game.games.smash.kits.KitSkeleton;
import ehnetwork.game.arcade.game.games.smash.kits.KitSkySquid;
import ehnetwork.game.arcade.game.games.smash.kits.KitSlime;
import ehnetwork.game.arcade.game.games.smash.kits.KitSnowman;
import ehnetwork.game.arcade.game.games.smash.kits.KitSpider;
import ehnetwork.game.arcade.game.games.smash.kits.KitWitch;
import ehnetwork.game.arcade.game.games.smash.kits.KitWitherSkeleton;
import ehnetwork.game.arcade.game.games.smash.kits.KitWolf;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;
 
public class SuperSmashDominate extends Domination
{       
	public SuperSmashDominate(ArcadeManager manager)
	{ 
		super(manager, GameType.SmashDomination,
 
				new Kit[]
						{
				
				new KitSkeleton(manager),
				new KitGolem(manager),
				new KitSpider(manager),
				new KitSlime(manager),
				
				new KitCreeper(manager),
				new KitEnderman(manager),
				new KitSnowman(manager),
				new KitWolf(manager),
				
				
				new KitBlaze(manager),
				new KitWitch(manager),
				new KitChicken(manager),
				new KitSkeletalHorse(manager),
				new KitPig(manager),
				new KitSkySquid(manager),
				new KitWitherSkeleton(manager),
				new KitMagmaCube(manager),

						});
	} 
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void FallDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() == DamageCause.FALL)
			event.SetCancelled("No Fall Damage");
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetDamageePlayer() != null)
			event.AddKnockback("Smash Knockback", 1 + 0.1 * (20 - event.GetDamageePlayer().getHealth()));
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void ArenaWalls(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() == DamageCause.VOID || event.GetCause() == DamageCause.LAVA)
		{
			event.GetDamageeEntity().eject();
			event.GetDamageeEntity().leaveVehicle();
			
			if (event.GetDamageePlayer() != null)
				event.GetDamageeEntity().getWorld().strikeLightningEffect(event.GetDamageeEntity().getLocation());
			
			event.AddMod("Smash", "Super Smash Mobs", 5000, false);
		}	
	}
	
	@EventHandler
	public void HealthChange(EntityRegainHealthEvent event)
	{
		if (event.getRegainReason() == RegainReason.SATIATED)
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void EntityDeath(EntityDeathEvent event)
	{
		event.getDrops().clear();
	}
	
	@Override
	public void SetKit(Player player, Kit kit, boolean announce) 
	{
		GameTeam team = GetTeam(player);
		if (team != null)
		{
			if (!team.KitAllowed(kit))
			{
				player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 0.5f);
				UtilPlayer.message(player, F.main("Kit", F.elem(team.GetFormattedName()) + " cannot use " + F.elem(kit.GetFormattedName() + " Kit") + "."));
				return;
			}
		}

		_playerKit.put(player, kit);

		if (announce)
		{
			player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2f, 1f);
			UtilPlayer.message(player, F.main("Kit", "You equipped " + F.elem(kit.GetFormattedName() + " Kit") + "."));
			kit.ApplyKit(player);
			UtilInv.Update(player);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void AbilityDescription(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		
		if (player.getItemInHand() == null)
			return;
		
		if (player.getItemInHand().getItemMeta() == null)
			return;
		
		if (player.getItemInHand().getItemMeta().getDisplayName() == null)
			return;
		
		if (player.getItemInHand().getItemMeta().getLore() == null)
			return;
		
		if (Manager.GetGame() == null || Manager.GetGame().GetState() != GameState.Recruit)
			return;
		
		for (int i=player.getItemInHand().getItemMeta().getLore().size() ; i<=7 ; i++)
			UtilPlayer.message(player, " ");
		
		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, "§aAbility - §f§l" + player.getItemInHand().getItemMeta().getDisplayName());
		
		//Perk Descs
		for (String line : player.getItemInHand().getItemMeta().getLore())
		{
			UtilPlayer.message(player, line);
		}
		
		UtilPlayer.message(player, ArcadeFormat.Line);
		
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void ExplosionDamageCancel(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.ENTITY_EXPLOSION || event.getCause() == DamageCause.BLOCK_EXPLOSION)
		{
			event.setCancelled(true);
		}
	}
	
	@Override
	public double GetKillsGems(Player killer, Player killed, boolean assist)
	{
		return 2;
	}
	
	@EventHandler
	public void BlockFade(BlockFadeEvent event)
	{
		event.setCancelled(true);
	}
}
