package ehnetwork.game.microgames.game.games.lobbers.kits.perks;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.game.microgames.game.games.lobbers.BombLobbers;
import ehnetwork.game.microgames.game.games.lobbers.events.TNTPreExplodeEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkMortar extends Perk
{
	private Set<TNTPrimed> _scatter = new HashSet<TNTPrimed>();
	
	public PerkMortar()
	{
		super("Mortar", new String[]
				{
				C.cYellow + "Right Click" + C.cGray + " Fireball to " + C.cGreen + "Fire Mortar",
				"Mortars will explode after " + C.cYellow + "2 Seconds",
				"releasing 3 more TNT, each with a 3 second fuse.",
				"Your TNT will not scatter if it is grounded."
				});
	}
	
	@EventHandler
	public void throwTNT(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.PHYSICAL)
			return;
		
		if (!Manager.GetGame().IsLive())
			return;

		if (!(Manager.GetGame() instanceof BombLobbers))
			return;
			
		BombLobbers l = (BombLobbers) Manager.GetGame();
		
		Player player = event.getPlayer();

		if (!Manager.IsAlive(player))
			return;

		if (!UtilInv.IsItem(player.getItemInHand(), Material.FIREBALL, (byte) 0))
			return;
		
		event.setCancelled(true);

		UtilInv.remove(player, Material.FIREBALL, (byte) 0, 1);
		UtilInv.Update(player);

		TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), TNTPrimed.class);
		tnt.setMetadata("owner", new FixedMetadataValue(Manager.getPlugin(), player.getUniqueId()));
		tnt.setFuseTicks(40);
		
		UtilAction.velocity(tnt, player.getLocation().getDirection(), 2.0D, false, 0.0D, 0.1D, 10.0D, false);
		Manager.GetProjectile().AddThrow(tnt, player, l, -1L, true, false, false, .2F);

		player.playSound(player.getLocation(), Sound.CREEPER_HISS, 3.0F, 1.0F);
		
//		l.addThrower(player, tnt);
	}
	
	@EventHandler
	public void onExplode(TNTPreExplodeEvent event)
	{
		if (!Manager.GetGame().IsLive())
			return;
		
		if (!Kit.HasKit(event.getPlayer()))
			return;
		
		if (event.getTNT().isOnGround() || UtilEnt.isGrounded(event.getTNT()))
			return;
		
		if (!(Manager.GetGame() instanceof BombLobbers))
			return;
		
		BombLobbers l = (BombLobbers) Manager.GetGame();

		event.setCancelled(true);
		
		if (_scatter.contains(event.getTNT()))
		{
			_scatter.remove(event.getTNT());
			return;
		}
		
		for (int i = -1 ; i < 2 ; i++)
		{
			TNTPrimed tnt = (TNTPrimed) event.getTNT().getWorld().spawn(event.getTNT().getLocation(), TNTPrimed.class);
			tnt.setMetadata("owner", new FixedMetadataValue(Manager.getPlugin(), event.getPlayer().getUniqueId()));
			tnt.setFuseTicks(60);
			
			UtilAction.velocity(tnt, event.getTNT().getVelocity().add(new Vector(i / 5, 0, i / 5)).normalize(), 2.0D, false, 0.0D, 0.1D, 10.0D, false);
			Manager.GetProjectile().AddThrow(tnt, event.getPlayer(), l, -1L, true, false, false, .2F);
			
//			l.addThrower(event.getPlayer(), tnt);
			_scatter.add(tnt);
		}
	}
}
