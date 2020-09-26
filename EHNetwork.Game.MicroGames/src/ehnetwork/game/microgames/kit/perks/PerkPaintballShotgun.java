package ehnetwork.game.microgames.kit.perks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.recharge.RechargedEvent;
import ehnetwork.game.microgames.game.GameTeam;
import ehnetwork.game.microgames.kit.Perk;

public class PerkPaintballShotgun extends Perk 
{
	public PerkPaintballShotgun() 
	{
		super("Shotgun", new String[] 
				{
				C.cYellow + "Right-Click" + C.cGray + " to use " + C.cGreen + "Shotgun"
				});
	}

	@EventHandler
	public void Recharge(RechargedEvent event)
	{
		if (!event.GetAbility().equals(GetName()))
			return;
		
		event.GetPlayer().playSound(event.GetPlayer().getLocation(), Sound.NOTE_STICKS, 2f, 1f);
		event.GetPlayer().playSound(event.GetPlayer().getLocation(), Sound.NOTE_STICKS, 2f, 1.5f);
	}
	
	@EventHandler
	public void Shoot(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (event.getPlayer().getItemInHand().getType() != Material.GOLD_BARDING)
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;
		
		GameTeam team = Manager.GetGame().GetTeam(player);
		if (team == null)
			return;
		
		event.setCancelled(true);

		if (!Recharge.Instance.use(player, GetName(), 1400, true, false))
			return;
		
		for (int i=0 ; i<8 ; i++)
		{
			Vector rand = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
			rand.multiply(0.4);
			
			if (team.GetColor() == ChatColor.AQUA)
			{
				Projectile proj = player.launchProjectile(Snowball.class);
				proj.setVelocity(proj.getVelocity().multiply(1).add(rand));
				
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 0.8f, 1f);
			}
			else
			{
				Projectile proj = player.launchProjectile(EnderPearl.class);
				proj.setVelocity(proj.getVelocity().multiply(1).add(rand));
				
				//Sound
				player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 0.8f, 0.75f);
			}
		}
	}
}
