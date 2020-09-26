package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkBoneExplosion extends SmashPerk
{	
	public PerkBoneExplosion() 
	{
		super("Bone Explosion", new String[]  
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Bone Explosion"
				});
	}

	@EventHandler
	public void Skill(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
		
		if (event.getPlayer().getItemInHand() == null)
			return;
		
		if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE"))
			return;
		
		Player player = event.getPlayer();
		
		if (isSuperActive(player))
        	return;
		
		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 10000, true, true))
			return;
		
		HashMap<Player, Double> nearby = UtilPlayer.getInRadius(player.getLocation(), 10);
		for (Player other : nearby.keySet())
		{
			if (player.equals(other))
				continue;
			
			//Inform
			UtilPlayer.message(other, F.main("Game", F.elem(Manager.GetColor(player) + player.getName()) + " used " + F.skill(GetName()) + "."));
			
			//Damage Event
			Manager.GetDamage().NewDamageEvent(other, player, null, 
					DamageCause.CUSTOM, 6 * nearby.get(other), true, true, false,
					player.getName(), GetName());
		}
		
		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
		
		//Effect
		Manager.GetBlood().Effects(null, player.getLocation().add(0, 0.5, 0), 48, 0.8, Sound.SKELETON_HURT, 2f, 1.2f, Material.BONE, (byte)0, 40, false);
	}
	
	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;
		
		event.AddKnockback(GetName(), 2.5);
	}
}
