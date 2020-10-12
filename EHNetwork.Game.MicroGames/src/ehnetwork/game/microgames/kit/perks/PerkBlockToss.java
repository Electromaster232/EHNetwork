package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.SmashPerk;
import ehnetwork.game.microgames.kit.perks.data.BlockTossData;
import ehnetwork.game.microgames.kit.perks.event.PerkBlockGrabEvent;
import ehnetwork.game.microgames.kit.perks.event.PerkBlockThrowEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkBlockToss extends SmashPerk implements IThrown
{
	private HashMap<Player, BlockTossData> _hold = new HashMap<Player, BlockTossData>();
	private HashMap<Player, Long> _charge = new HashMap<Player, Long>();
	private HashSet<Player> _charged = new HashSet<Player>();
	private HashMap<FallingBlock, Player> _falling = new HashMap<FallingBlock, Player>();

	public PerkBlockToss() 
	{
		super("Block Toss", new String[] 
				{ 
				C.cYellow + "Hold Block" + C.cGray + " to " + C.cGreen + "Grab Block",
				C.cYellow + "Release Block" + C.cGray + " to " + C.cGreen + "Throw Block"
				});
	}

	@EventHandler
	public void Grab(PlayerInteractEvent event)
	{	 	
		Player player = event.getPlayer();

		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;
		
		if (isSuperActive(player))
			return;

		if (!Kit.HasKit(player))
			return;

		if (!UtilGear.isSword(player.getItemInHand()))
			return;

		if (_hold.containsKey(player))
			return;

		Block grab = event.getClickedBlock();

		if (UtilBlock.usable(grab))
			return;

		if (!UtilBlock.airFoliage(grab.getRelative(BlockFace.UP)) || Manager.GetBlockRestore().Contains(grab.getRelative(BlockFace.UP)))
		{
			UtilPlayer.message(player, F.main("Game", "You can only pick up blocks with Air above them."));
			return;
		}

		//Event
		PerkBlockGrabEvent blockEvent = new PerkBlockGrabEvent(player, grab.getTypeId(), grab.getData());
		UtilServer.getServer().getPluginManager().callEvent(blockEvent);

		//Block to Data
		int id = grab.getTypeId();
		byte data = grab.getData();
		
		//Remove Block
		//Manager.GetBlockRestore().Add(event.getClickedBlock(), 0, (byte)0, 10000);
		event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, event.getClickedBlock().getType());
		
		_hold.put(player, new BlockTossData(id, data));
		
		_charge.put(player, System.currentTimeMillis());

		//Effect
		player.getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, id);
	}

	@EventHandler
	public void Throw(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		HashSet<Player> throwSet = new HashSet<Player>();

		for (Player cur : _hold.keySet())
		{
			//Throw
			if (!cur.isBlocking())
				throwSet.add(cur);

			//Charged Tick
			if (!_charged.contains(cur))
				if (System.currentTimeMillis() - _charge.get(cur) > 1200)
				{
					_charged.add(cur);
					cur.playEffect(cur.getLocation(), Effect.CLICK1, 0);
				}
		}

		for (Player cur : throwSet)
		{
			BlockTossData data = _hold.remove(cur);
			
			FallingBlock block  = cur.getWorld().spawnFallingBlock(cur.getEyeLocation().add(cur.getLocation().getDirection()), data.Type, data.Data);
			
			_falling.put(block, cur);
			
			_charged.remove(cur);
			
			long charge = System.currentTimeMillis() - _charge.remove(cur);

			//Throw 
			double mult = 1.4;
			if (charge < 1200)
				mult = mult * ((double)charge/1200d);
			
			//Action
			UtilAction.velocity(block, cur.getLocation().getDirection(), mult, false, 0.2, 0, 1, true);
			Manager.GetProjectile().AddThrow(block, cur, this, -1, true, true, true, 
					null, 0, 0, null, 0, UpdateType.FASTEST, 1f);

			//Event
			PerkBlockThrowEvent blockEvent = new PerkBlockThrowEvent(cur);
			UtilServer.getServer().getPluginManager().callEvent(blockEvent);
		}
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		if (target == null)
			return;

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.PROJECTILE, data.GetThrown().getVelocity().length() * 10, true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());

		//Block to Item
		if (data.GetThrown() instanceof FallingBlock)
		{
			FallingBlock thrown = (FallingBlock) data.GetThrown();

			FallingBlock newThrown  = data.GetThrown().getWorld().spawnFallingBlock(data.GetThrown().getLocation(), thrown.getMaterial(), (byte)0);

			//Remove Old
			_falling.remove(thrown);
			thrown.remove();

			//Add New
			if (data.GetThrower() instanceof Player)
				_falling.put(newThrown, (Player)data.GetThrower());
		}
	}

	@Override
	public void Idle(ProjectileUser data) 
	{

	}

	@Override
	public void Expire(ProjectileUser data) 
	{

	}	


	@EventHandler
	public void BlockForm(EntityChangeBlockEvent event)
	{
		if (!(event.getEntity() instanceof FallingBlock))
			return;

		FallingBlock falling = (FallingBlock)event.getEntity();	

		falling.getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, falling.getBlockId());

		_falling.remove(falling);
		falling.remove();

		event.setCancelled(true);
	}

	@EventHandler
	public void Knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		event.AddKnockback(GetName(), 2.5);
	}
}
