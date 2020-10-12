package ehnetwork.game.microgames.kit.perks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.disguise.disguises.DisguiseBase;
import ehnetwork.core.disguise.disguises.DisguiseSheep;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.projectile.IThrown;
import ehnetwork.core.projectile.ProjectileUser;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.recharge.RechargedEvent;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.data.WoolBombData;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class PerkWoolBomb extends Perk implements IThrown
{
	private HashMap<Player, Item> _thrown = new HashMap<Player, Item>();
	private HashMap<Player, WoolBombData> _active = new HashMap<Player, WoolBombData>();
	
	public PerkWoolBomb() 
	{
		super("Wool Mine", new String[] 
				{
				C.cYellow + "Right-Click" + C.cGray + " with Axe to " + C.cGreen + "Wool Mine"
				});
	}

	@EventHandler
	public void skill(PlayerInteractEvent event)
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

		if (!Kit.HasKit(player))
			return;
		
		if (!Recharge.Instance.usable(player, GetName() + " Rate"))
			return;
		
		if (_active.containsKey(player))
		{
			if (detonate(player, true))
				return;
		}
		
		if (_thrown.containsKey(player))
		{
			if (solidify(player, true))
				return;
		}
		
		launch(player);

		event.setCancelled(true);
	}

	private void launch(Player player)
	{
		if (!Recharge.Instance.use(player, GetName(), 8000, true, true))
			return;

		org.bukkit.entity.Item ent = player.getWorld().dropItem(player.getEyeLocation(), ItemStackFactory.Instance.CreateStack(Material.WOOL, (byte)0));

		UtilAction.velocity(ent, player.getLocation().getDirection(), 1, false, 0, 0.2, 10, false);	

		Manager.GetProjectile().AddThrow(ent, player, this, -1, true, true, true, 
				null, 1f, 1f, 
				null, 1, UpdateType.SLOW, 
				0.5f);
		
		_thrown.put(player, ent);

		//Inform
		UtilPlayer.message(player, F.main("Game", "You launched " + F.skill(GetName()) + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.SHEEP_IDLE, 2f, 1.5f);
		
		//Rate
		Recharge.Instance.useForce(player, GetName() + " Rate", 800);
		
		//Disguise
		DisguiseBase disguise = Manager.GetDisguise().getDisguise(player);
		if (disguise != null && disguise instanceof DisguiseSheep)
		{
			DisguiseSheep sheep = (DisguiseSheep)disguise;
			sheep.setSheared(true);
			
			sheep.UpdateDataWatcher();
			Manager.GetDisguise().updateDisguise(disguise);
		}
	}
	
	@EventHandler
	public void rechargeWool(RechargedEvent event)
	{
		if (event.GetAbility().equals(GetName()))
		{
			DisguiseBase disguise = Manager.GetDisguise().getDisguise(event.GetPlayer());
			if (disguise != null && disguise instanceof DisguiseSheep)
			{
				DisguiseSheep sheep = (DisguiseSheep)disguise;
				sheep.setSheared(false);
				
				sheep.UpdateDataWatcher();
				Manager.GetDisguise().updateDisguise(disguise);
			}
		}
	}
	
	private boolean solidify(LivingEntity ent, boolean inform)
	{
		if (!(ent instanceof Player))
			return false;
		
		Player player = (Player)ent;
		
		Item thrown = _thrown.remove(player);
		if (thrown == null)
			return false;
		
		//Make Block
		Block block = thrown.getLocation().getBlock();
		
		Manager.GetBlockRestore().Restore(block);
		
		_active.put(player, new WoolBombData(block));
		
		block.setTypeIdAndData(35, (byte)0, true);
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
		
		//Clean
		thrown.remove();
		
		//Rate
		Recharge.Instance.useForce(player, GetName() + " Rate", 1000);
		
		//Inform
		if (inform)
		{
			player.getWorld().playSound(player.getLocation(), Sound.SHEEP_IDLE, 2f, 1.5f);
			
			UtilPlayer.message(player, F.main("Game", "You armed " + F.skill(GetName()) + "."));
		}
		
		return true;
	}

	private boolean detonate(Player player, boolean inform)
	{
		WoolBombData data = _active.remove(player);
		
		if (data == null)
			return false;
		
		//Restore
		data.restore();
		
		//Explode
		UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, data.Block.getLocation().add(0.5, 0.5, 0.5), 0, 0, 0, 0, 1,
				ViewDist.MAX, UtilServer.getPlayers());
		data.Block.getWorld().playSound(data.Block.getLocation(), Sound.EXPLODE, 3f, 0.8f);
		
		//Damage
		HashMap<LivingEntity, Double> targets = UtilEnt.getInRadius(data.Block.getLocation().add(0.5, 0.5, 0.5), 9);
		for (LivingEntity cur : targets.keySet())
		{
			//Damage Event
			Manager.GetDamage().NewDamageEvent(cur, player, null, 
					DamageCause.CUSTOM, 14 * targets.get(cur) + 0.5, false, true, false,
					player.getName(), GetName());	
			
			//Condition
			Manager.GetCondition().Factory().Falling(GetName(), cur, player, 10, false, true);
			
			//Knockback
			UtilAction.velocity(cur, UtilAlg.getTrajectory2d(data.Block.getLocation().add(0.5, 0.5, 0.5), cur.getEyeLocation()), 0.5 + 2.5 * targets.get(cur), true, 0.8, 0, 10, true);

			//Inform
			if (cur instanceof Player && !player.equals(cur))
				UtilPlayer.message((Player)cur, F.main("Game", F.name(player.getName()) +" hit you with " + F.skill(GetName()) + "."));	
		}
		
		//Rate
		Recharge.Instance.useForce(player, GetName() + " Rate", 800);
		
		//Inform
		if (inform)
		{
			player.getWorld().playSound(player.getLocation(), Sound.SHEEP_IDLE, 2f, 1.5f);
			
			UtilPlayer.message(player, F.main("Game", "You detonated " + F.skill(GetName()) + "."));
		}

		return true;
	}

	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data) 
	{
		solidify(data.GetThrower(), false);

		if (target == null)
			return;

		//Damage Event
		Manager.GetDamage().NewDamageEvent(target, data.GetThrower(), null, 
				DamageCause.PROJECTILE, 4, true, true, false,
				UtilEnt.getName(data.GetThrower()), GetName());
	}

	@Override
	public void Idle(ProjectileUser data) 
	{
		solidify(data.GetThrower(), false);
	}

	@Override
	public void Expire(ProjectileUser data) 
	{
		solidify(data.GetThrower(), false);
	}
	
	@EventHandler
	public void colorExpireUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTER)
			return;
		
		HashSet<Player> detonate = new HashSet<Player>();
		
		Iterator<Player> playerIterator = _active.keySet().iterator();
		
		while (playerIterator.hasNext())
		{
			Player player = playerIterator.next();
			WoolBombData data = _active.get(player);
			
			if (UtilTime.elapsed(data.Time, 8000))
			{
				detonate.add(player);
				continue;
			}
			
			if (Recharge.Instance.usable(player, GetName() + " Rate"))
			{
				if (data.Block.getData() == 14)
				{
					data.Block.setData((byte) 0);
				}
				else
				{
					data.Block.setData((byte) 14);
				}
			}
		}
		
		for (Player player : detonate)
		{
			detonate(player, false);
		}
	}

	@EventHandler
	public void knockback(CustomDamageEvent event)
	{
		if (event.GetReason() == null || !event.GetReason().contains(GetName()))
			return;

		event.AddKnockback(GetName(), 2);
	}
}
