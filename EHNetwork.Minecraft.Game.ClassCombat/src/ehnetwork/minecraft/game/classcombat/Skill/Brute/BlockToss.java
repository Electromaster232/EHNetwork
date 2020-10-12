package ehnetwork.minecraft.game.classcombat.Skill.Brute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilEvent;
import ehnetwork.core.common.util.UtilEvent.ActionType;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.SkillCharge;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillEvent;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class BlockToss extends SkillCharge implements IThrown
{
	private HashMap<Player, FallingBlock> _holding = new HashMap<Player, FallingBlock>();
	private HashMap<FallingBlock, Player> _falling = new HashMap<FallingBlock, Player>();

	public BlockToss(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels,
				0.01f, 0.005f);

		SetDesc(new String[] 
				{
				"Hold Block to pick up a block,",
				"Release Block to throw it,",
				"dealing up to #6#1 damage.",
				"",
				GetChargeString(),
				});
	}
	
	@Override
	public String GetRechargeString()
	{
		return "Recharge: " + "#" + UtilMath.trim(1, 4000/1000d) + "#" + UtilMath.trim(1, -500/1000d) + " Seconds";
	}

	@EventHandler
	public void Grab(PlayerInteractEvent event)
	{	 	
		Player player = event.getPlayer();

		if (!UtilEvent.isAction(event, ActionType.R_BLOCK))
			return;

		if (!UtilGear.isSword(player.getItemInHand()))
			return;
		
		//Check Allowed
		SkillTriggerEvent trigger = new SkillTriggerEvent(player, GetName(), GetClassType());
		Bukkit.getServer().getPluginManager().callEvent(trigger);
		
		if (trigger.IsCancelled())
			return;
		
		if (_holding.containsKey(player))
			return;

		//Level
		int level = getLevel(player);
		if (level == 0)		return;
		
		//Water
		if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9)
		{
			UtilPlayer.message(player, F.main("Skill", "You cannot use " + F.skill(GetName()) + " in water."));
			return;
		}
	
		Block grab = event.getClickedBlock();
	
		//Recharge
		if (!Recharge.Instance.use(player, GetName(), 1000, false, false))
			return;
		
		//Door
		if (grab.getRelative(BlockFace.UP).getTypeId() == 64 || grab.getRelative(BlockFace.UP).getTypeId() == 71)
		{
			UtilPlayer.message(player, F.main(GetName(), "You cannot grab this block."));
			return;
		}

		// Ladder and beacon grabs
		if (grab.getType() == Material.LADDER || grab.getType() == Material.BEACON)
		{
			UtilPlayer.message(player, F.main(GetName(), "You cannot grab this block."));
			return;
		}

		//TrapDoor or ladder
		for (int x = -1; x <= 1; x++)
		{
		    for (int z = -1; z <= 1; z++)
		    {
		        if (x != z && (z == 0 || x == 0))
		        {
		            Block block = grab.getRelative(x, 0, z);
		            
		            if (block.getType() == Material.TRAP_DOOR || block.getType() == Material.LADDER)
		            {
		                UtilPlayer.message(player, F.main(GetName(), "You cannot grab this block."));
		                return;
		            }
		        }
		    }
		}

		//Block to Item
		FallingBlock block  = player.getWorld().spawnFallingBlock(player.getEyeLocation(), event.getClickedBlock().getType(), event.getClickedBlock().getData());

		//Action
		player.eject();
		player.setPassenger(block);
		_holding.put(player, block);
		_falling.put(block, player);

		//Effect
		player.getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, block.getMaterial().getId());
	}

	@EventHandler
	public void Throw(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		HashSet<Player> voidSet = new HashSet<Player>();
		HashSet<Player> throwSet = new HashSet<Player>();

		for (Player cur : _holding.keySet())
		{
			if (cur.getPassenger() == null)
			{
				voidSet.add(cur);
				continue;	
			}

			if (_holding.get(cur).getVehicle() == null)
			{
				voidSet.add(cur);
				continue;	
			}

			if (!_holding.get(cur).getVehicle().equals(cur))
			{
				voidSet.add(cur);
				continue;	
			}

			//Throw
			if (!cur.isBlocking())
				throwSet.add(cur);

			//Charged Tick
			Charge(cur);
		}

		for (Player cur : voidSet)
		{
			FallingBlock block = _holding.remove(cur);
			_charge.remove(cur);
			block.remove();
		}

		for (Player cur : throwSet)
		{
			FallingBlock block = _holding.remove(cur);
			float charge = _charge.remove(cur);
			
			//Throw 
			cur.eject();
			double mult = Math.max(0.4, charge * 2);

			//Action
			UtilAction.velocity(block, cur.getLocation().getDirection(), mult, false, 0, 0, 1, true);
			Factory.Projectile().AddThrow(block, cur, this, -1, true, true, true, 
					null, 0, 0, null, 0, UpdateType.FASTEST, 1.2f);
			
			//Event
			UtilServer.getServer().getPluginManager().callEvent(new SkillEvent(cur, GetName(), IPvpClass.ClassType.Brute));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void Knockback(CustomDamageEvent event)
	{
		Player damager = event.GetDamagerPlayer(true);
		if (damager == null)	return;
		
		if (event.GetReason() == null || !event.GetReason().equals(GetName()))
			return;
		
		event.AddKnockback(GetName(), 1.5d);
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		if (target == null)
			return;

		int level = getLevel(data.GetThrower());
		
		//Damage Event
		Factory.Damage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.CUSTOM, data.GetThrown().getVelocity().length() * (3 + 0.6 * level), true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());

		//Block to Item
		if (data.GetThrown() instanceof FallingBlock)
		{
			FallingBlock thrown = (FallingBlock) data.GetThrown();

			FallingBlock newThrown  = data.GetThrown().getWorld().spawnFallingBlock(data.GetThrown().getLocation(), thrown.getMaterial(), thrown.getBlockData());

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
	public void CreateBlock(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<FallingBlock> fallIter = _falling.keySet().iterator();
		
		while (fallIter.hasNext())
		{
			FallingBlock fall = fallIter.next();
			
			if (!fall.isDead() && fall.isValid())
				continue;
			
			fallIter.remove();
			
			Block block = fall.getLocation().getBlock();
			
			if (block.getType() != fall.getMaterial()) 
				continue;
			
			block.setTypeIdAndData(0, (byte)0, false);
			
			//Block Replace
			Factory.BlockRestore().Add(block, fall.getBlockId(), (byte)0, 10000);

			//Effect
			block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
		}
	}

	@EventHandler
	public void ItemSpawn(ItemSpawnEvent event)
	{
		int id = event.getEntity().getItemStack().getTypeId();

		if (
				id != 1 &&
				id != 2 &&
				id != 3 &&
				id != 4 &&
				id != 12 &&
				id != 13 &&
				id != 80) 
			return;

		for (FallingBlock block : _falling.keySet())
			if (UtilMath.offset(event.getEntity().getLocation(), block.getLocation()) < 1)
				event.setCancelled(true);	
	}

	@Override
	public void Reset(Player player) 
	{
		if (_holding.containsKey(player))
		{
			player.eject();
		}
		
		_holding.remove(player);
		_charge.remove(player);
	}
}
