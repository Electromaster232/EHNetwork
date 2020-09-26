package ehnetwork.game.microgames.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.data.IBlockRestorer;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkWitherAttack extends Perk
{
	private ArrayList<WitherSkull> _active = new ArrayList<WitherSkull>();
	
	public PerkWitherAttack() 
	{
		super("Wither Skull", new String[] 
				{ 
				C.cYellow + "Left-Click" + C.cGray + " with Gold Sword to use " + C.cGreen + "Wither Skull"
				});
	}
	
	
	@EventHandler
	public void Activate(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;
		
		if (!UtilEvent.isAction(event, ActionType.L))
			return;
		
		Player player = event.getPlayer();
		
		if (!Kit.HasKit(player))
			return;
		
		if (!UtilGear.isMat(event.getPlayer().getItemInHand(), Material.GOLD_SWORD))
			return;
		
		if (!Recharge.Instance.use(player, GetName(), 2000, true, true))
			return;
		
		//Fire
		_active.add(player.launchProjectile(WitherSkull.class));
		
		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 1f, 1f);
		
		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill(GetName()) + "."));
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent event)
	{
		if (!_active.contains(event.getEntity()))
			return;
		
		event.setCancelled(true);
		
		WitherSkull skull = (WitherSkull)event.getEntity();
		
		UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, skull.getLocation(), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		
		explode(skull);
	}
	
	@EventHandler
	public void clean(UpdateEvent event)  
	{
		if (event.getType() != UpdateType.FAST)
			return;

		Iterator<WitherSkull> skullIterator = _active.iterator();
		
		while (skullIterator.hasNext())
		{
			WitherSkull skull = skullIterator.next();
			
			if (!skull.isValid())
			{
				skullIterator.remove();
				skull.remove();
				continue;
			}
		}
	}
		
	@EventHandler(priority = EventPriority.LOWEST)
	public void ExplodeDamage(CustomDamageEvent event)
	{
		if (event.IsCancelled())
			return;
		
		if (event.GetProjectile() != null && event.GetProjectile() instanceof WitherSkull)
			event.SetCancelled("Wither Skull Cancel");
	}
	
	private void explode(WitherSkull skull) 
	{	
		double scale = 0.4 + 0.6 * Math.min(1, skull.getTicksLived()/20d);
		
		//Players
		HashMap<Player, Double> players = UtilPlayer.getInRadius(skull.getLocation(), 7);
		for (Player player : players.keySet())
		{
			if (!Manager.GetGame().IsAlive(player))
				continue;

			//Damage Event
			Manager.GetDamage().NewDamageEvent(player, (LivingEntity)skull.getShooter(), null, 
					DamageCause.CUSTOM, 2 + 10 * players.get(player) * scale, true, true, false,
					UtilEnt.getName((LivingEntity)skull.getShooter()), GetName());
		}
		
		//Blocks
		Set<Block> blocks = UtilBlock.getInRadius(skull.getLocation(), 4d).keySet();
		
		Iterator<Block> blockIterator = blocks.iterator();
		while (blockIterator.hasNext())
		{
			Block block = blockIterator.next();
			
			if (block.isLiquid())
				blockIterator.remove();
			
			else if (block.getRelative(BlockFace.UP).isLiquid())
				blockIterator.remove();
		}

		if (Manager.GetGame() != null && Manager.GetGame() instanceof IBlockRestorer)
		{
			((IBlockRestorer)Manager.GetGame()).addBlocks(blocks);
		}
		
		
		Manager.GetExplosion().BlockExplosion(blocks, skull.getLocation(), false);
	}
}
