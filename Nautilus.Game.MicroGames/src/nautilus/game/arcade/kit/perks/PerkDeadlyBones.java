package nautilus.game.arcade.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.kit.SmashPerk;

public class PerkDeadlyBones extends SmashPerk
{
	private HashMap<Item, Player> _active = new HashMap<Item, Player>();
	
	public PerkDeadlyBones() 
	{
		super("Deadly Bones", new String[] 
				{
				C.cGray + "Drop explosive bones when you take damage."
				});
	}

	@EventHandler
	public void damageActivate(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		Player player = event.GetDamageePlayer();
		if (player == null)
			return;
		
		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 400, false, false))
			return;

		_active.put(player.getWorld().dropItemNaturally(player.getLocation().add(0, 0.5, 0), 
				ItemStackFactory.Instance.CreateStack(Material.BONE, (byte)0, 1, "Explosive Bone " + System.currentTimeMillis())), player);
	}
	
	@EventHandler
	public void update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		Iterator<Item> itemIter = _active.keySet().iterator();
		
		while (itemIter.hasNext())
		{
			Item item = itemIter.next();
			
			//Not Ready
			if (item.isValid() && item.getTicksLived() < 50)
				continue;
		
			//Effect
			UtilParticle.PlayParticle(ParticleType.LARGE_EXPLODE, item.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
			item.getWorld().playSound(item.getLocation(), Sound.EXPLODE, 0.8f, 1.4f);
			
			Player player = _active.get(item);
				
			//Damage
			HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(item.getLocation(), 4);
			for (LivingEntity cur : targets.keySet())
			{
				if (cur.equals(player))
					continue;

				Manager.GetDamage().NewDamageEvent(cur, player, null, 
						DamageCause.CUSTOM, 4 * targets.get(cur) + 1, true, true, false,
						player.getName(), GetName());	
			}
			
			//Remove
			item.remove();
			itemIter.remove();
		}
	}

	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		event.AddKnockback(GetName(), 3);
	}
}
