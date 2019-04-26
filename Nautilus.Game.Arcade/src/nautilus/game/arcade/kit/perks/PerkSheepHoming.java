package nautilus.game.arcade.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;
import nautilus.game.arcade.kit.perks.data.HomingSheepData;

public class PerkSheepHoming extends SmashPerk
{
	private ArrayList<HomingSheepData> _sheep = new ArrayList<HomingSheepData>();
	
	public PerkSheepHoming() 
	{
		super("Homing Sheeples", new String[] 
				{ 
				}, false);
	}

	@Override
	public void addSuperCustom(Player player)
	{			
		//Fire Sheep
		for (Player target : Manager.GetGame().GetPlayers(true))
		{
			if (target.equals(player))
				continue;
			
			Manager.GetGame().CreatureAllowOverride = true;
			Sheep sheep = player.getWorld().spawn(player.getEyeLocation(), Sheep.class);
			Manager.GetGame().CreatureAllowOverride = false;
			
			sheep.setBaby();
			
			_sheep.add(new HomingSheepData(player, target, sheep));
		}
	}
	
	@EventHandler
	public void sheepUpdate(UpdateEvent event)
	{
		 if (event.getType() != UpdateType.TICK)
			 return;
		 
		 Iterator<HomingSheepData> sheepIter = _sheep.iterator();
		 
		 while (sheepIter.hasNext())
		 {
			 HomingSheepData data = sheepIter.next();
			 
			 if (data.update())
			 {
				 sheepIter.remove();
				 explode(data);
			 }
		 }
	}

	private void explode(HomingSheepData data)
	{
		double scale = 0.4 + 0.6 * Math.min(1, data.Sheep.getTicksLived()/60d);
		
		//Players
		HashMap<Player, Double> players = UtilPlayer.getInRadius(data.Sheep.getLocation(), 10);
		for (Player player : players.keySet())
		{
			if (!Manager.GetGame().IsAlive(player))
				continue;

			//Damage Event
			Manager.GetDamage().NewDamageEvent(player, data.Shooter, null, 
					DamageCause.CUSTOM, 20 * scale, true, true, false,
					data.Shooter.getName(), GetName());
		}
		
		UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, data.Sheep.getLocation(), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		data.Sheep.getWorld().playSound(data.Sheep.getLocation(), Sound.EXPLODE, 2f, 1f);
		
		data.Sheep.remove();
	}
	
	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 3);
	}
}
