package nautilus.game.arcade.game.games.dragons;

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
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.GameType;
import nautilus.game.arcade.events.PlayerStateChangeEvent;
import nautilus.game.arcade.game.SoloGame;
import nautilus.game.arcade.game.GameTeam.PlayerState;
import nautilus.game.arcade.game.games.dragons.DragonData;
import nautilus.game.arcade.game.games.dragons.kits.*;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkSparkler;
import nautilus.game.arcade.stats.SparklezStatTracker;

public class Dragons extends SoloGame
{
	private HashMap<EnderDragon, DragonData> _dragons = new HashMap<EnderDragon, DragonData>();
	private ArrayList<Location> _dragonSpawns = new ArrayList<Location>();
	
	private PerkSparkler _sparkler = null; 
	
	public Dragons(ArcadeManager manager) 
	{
		super(manager, GameType.Dragons,

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
				"Last player alive wins!"
								});
		
		this.DamagePvP = false;
		this.HungerSet = 20;
		this.WorldWaterDamage = 4;

		registerStatTrackers(
				new SparklezStatTracker(this)
		);
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
			if (item.getLocation().getY() < 4 || item.getLocation().getBlock().isLiquid())
				continue;
			
			for (DragonData data : _dragons.values())
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
			
			_dragons.put(ent, new DragonData(this, ent));
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
		for (DragonData data : _dragons.values())
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
	
	/*
	@EventHandler
	public void DragonKnockback(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
			for (DragonData data : _dragons.values())
			{
				UtilParticle.PlayParticle(ParticleType.FIREWORKS_SPARK, data.Dragon.getLocation(), 0f, 0f, 0f, 0, 1);
				
				for (Player player : GetPlayers(true))
				{
					if (!Recharge.Instance.use(player, "Dragon Hit", 500, false, false))
						continue;
				
					if (UtilMath.offset(player, data.Dragon) < 6)
					{
						UtilAction.velocity(player, UtilAlg.getTrajectory(data.Dragon, player), 1, false, 0, 0.6, 2, true);
						
						player.damage(1);
					}
			}
		}
	}
	*/
}
