package nautilus.game.arcade.kit.perks;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.disguise.disguises.DisguiseMagmaCube;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.combat.event.CombatDeathEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.Perk;

public class PerkMagmaBoost extends Perk
{
	private HashMap<Player, Integer> _kills = new HashMap<Player, Integer>();
	
	public PerkMagmaBoost() 
	{
		super("Fuel the Fire", new String[] 
				{ 
				C.cGray + "Kills give +1 Damage, -15% Knockback Taken and +1 Size.",
				C.cGray + "Kill bonuses can stack 3 times, and reset on death.",
				});
	}

	@EventHandler
	public void Kill(CombatDeathEvent event)
	{
		Player killed = (Player)event.GetEvent().getEntity();
		
		_kills.remove(killed);

		if (event.GetLog().GetKiller() == null)
			return;
		
		Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());

		if (killer == null || killer.equals(killed) || !Kit.HasKit(killer))
			return;

		DisguiseMagmaCube slime = (DisguiseMagmaCube)Manager.GetDisguise().getDisguise(killer);
		if (slime == null)
			return;
		
		int size = 1;
		if (_kills.containsKey(killer))
			size += _kills.get(killer);

		size = Math.min(3, size);
		
		_kills.put(killer, size);
		
		slime.SetSize(size + 1);
		Manager.GetDisguise().updateDisguise(slime);
		
		killer.setExp(0.99f * (size/3f));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void SizeDamage(CustomDamageEvent event)
	{	
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)			return;

		if (!Kit.HasKit(damager))
			return;

		if (!_kills.containsKey(damager))
			return;
		
		int bonus = _kills.get(damager);

		event.AddMod(damager.getName(), GetName(), bonus, false);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void SizeKnockback(CustomDamageEvent event)
	{	
		if (event.IsCancelled())
			return;
		
		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damagee = event.GetDamageePlayer();
		if (damagee == null)			return;

		if (!Kit.HasKit(damagee))
			return;

		if (!_kills.containsKey(damagee))
			return;
		
		int bonus = _kills.get(damagee);

		event.AddKnockback(GetName(), bonus*0.15d);
	}
	
	@EventHandler
	public void EnergyUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC && event.getType() != UpdateType.FAST && event.getType() != UpdateType.FASTER && event.getType() != UpdateType.FASTEST)
			return;
		
		for (Player player : UtilServer.getPlayers())
		{
			if (!Kit.HasKit(player))
				continue;
			
			float size = 0;
			if (_kills.containsKey(player))
				size += _kills.get(player);
			
			if (size == 0 && event.getType() == UpdateType.SEC)
				UtilParticle.PlayParticle(ParticleType.LAVA, player.getLocation().add(0,0.4,0), 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0, 1,
						ViewDist.LONG, UtilServer.getPlayers());
			else if (size == 1 && event.getType() == UpdateType.FAST)
				UtilParticle.PlayParticle(ParticleType.LAVA, player.getLocation().add(0,0.4,0), 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0, 1,
						ViewDist.LONG, UtilServer.getPlayers());
			else if (size == 2 && event.getType() == UpdateType.FASTER)
				UtilParticle.PlayParticle(ParticleType.LAVA, player.getLocation().add(0,0.4,0), 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0, 1,
						ViewDist.LONG, UtilServer.getPlayers());
			else if (size == 3 && event.getType() == UpdateType.FASTEST)
				UtilParticle.PlayParticle(ParticleType.LAVA, player.getLocation().add(0,0.4,0), 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0.15f + 0.15f * size, 0, 1,
						ViewDist.LONG, UtilServer.getPlayers());
		}
	}
}
