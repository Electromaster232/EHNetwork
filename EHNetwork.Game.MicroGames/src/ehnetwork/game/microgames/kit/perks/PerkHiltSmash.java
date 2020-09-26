package ehnetwork.game.microgames.kit.perks;

import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkHiltSmash extends Perk
{
	private HashSet<Player>	_used = new HashSet<Player>();
	
	public PerkHiltSmash() 
	{
		super("Hilt Smash", new String[] 
				{ 
				C.cYellow + "Block on Player" + C.cGray + " to use " + C.cGreen + "Hilt Smash"
				});
	}
		
	public boolean CanUse(Player player)
	{
		if (!Kit.HasKit(player))
			return false;
		
		//Check Material
		if (!UtilGear.isSword(player.getItemInHand()))
			return false;

		//Check Energy/Recharge
		if (!Recharge.Instance.use(player, GetName(), 10000, true, true))
			return false;

		//Allow
		return true;
	}
	
	@EventHandler
	public void Hit(PlayerInteractEntityEvent event)
	{
		if (event.isCancelled())
			return;
		
		Player player = event.getPlayer();

		if (!CanUse(player))
			return;

		Entity ent = event.getRightClicked();

		if (ent == null)
			return;

		if (!(ent instanceof LivingEntity))
			return;

		if (UtilMath.offset(player, ent) > 3)
		{
			UtilPlayer.message(player, F.main("Skill", "You missed " + F.skill(GetName()) + "."));
			return;
		}

		//Set Used
		_used.add(player);

		//Damage Event
		Manager.GetDamage().NewDamageEvent((LivingEntity)ent, player, null, 
				DamageCause.ENTITY_ATTACK, 5, false, true, false,
				player.getName(), GetName());
	}

	@EventHandler
	public void Damage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;

		if (event.GetCause() != DamageCause.ENTITY_ATTACK)
			return;

		Player damager = event.GetDamagerPlayer(false);
		if (damager == null)	return;

		LivingEntity damagee = event.GetDamageeEntity();
		if (damagee == null)	return;

		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		//Condition
		Manager.GetCondition().Factory().Slow(GetName(), damagee, damager, 2, 1, false, false, true, true);
		Manager.GetCondition().Factory().Blind(GetName(), damagee, damager, 2, 1, false, false, false);

		//Effect
		damagee.getWorld().playSound(damagee.getLocation(), Sound.ZOMBIE_WOOD, 1f, 1.2f);
		damagee.getWorld().playEffect(damagee.getLocation(), Effect.STEP_SOUND, 17);

		//Inform
		UtilPlayer.message(damager, F.main("Skill", "You used " + F.skill(GetName()) + "."));
		UtilPlayer.message(damagee, F.main("Skill", F.name(damager.getName()) + " hit you with " + F.skill(GetName()) + "."));
	}
}
