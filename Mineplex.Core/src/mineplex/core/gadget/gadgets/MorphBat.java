package mineplex.core.gadget.gadgets;

import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilAction;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilEvent;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilEvent.ActionType;
import mineplex.core.disguise.disguises.DisguiseBat;
import mineplex.core.projectile.IThrown;
import mineplex.core.projectile.ProjectileUser;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.MorphGadget;
import mineplex.core.itemstack.ItemStackFactory;

public class MorphBat extends MorphGadget implements IThrown
{
	public MorphBat(GadgetManager manager)
	{
		super(manager, "Bat Morph", new String[] 
				{
				C.cWhite + "Flap around and annoy people by",
				C.cWhite + "screeching loudly into their ears!",
				" ",
				C.cYellow + "Left Click" + C.cGray + " to use " + C.cGreen + "Screech",
				C.cYellow + "Double Jump" + C.cGray + " to use " + C.cGreen + "Flap",
				C.cYellow + "Tap Sneak" + C.cGray + " to use " + C.cGreen + "Poop",
				},
				40000,
				Material.SKULL_ITEM, (byte)1);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguiseBat disguise = new DisguiseBat(player);
		disguise.setName(player.getName(), Manager.getClientManager().Get(player).GetRank());
		disguise.setCustomNameVisible(true);
		Manager.getDisguiseManager().disguise(disguise);
	}

	@Override
	public void DisableCustom(Player player) 
	{
		this.RemoveArmor(player);
		Manager.getDisguiseManager().undisguise(player);

		player.setAllowFlight(false);
		player.setFlying(false);
	}

	@EventHandler
	public void Screech(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!IsActive(player))
			return;

		if (!UtilEvent.isAction(event, ActionType.L))
			return;

		if (!Recharge.Instance.use(player, GetName(), 100, false, false))
			return;

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.BAT_HURT, 1f, 1f);
	}

	@EventHandler
	public void Poop(PlayerToggleSneakEvent event)
	{
		Player player = event.getPlayer();

		if (player.isSneaking())
			return;

		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		if (!IsActive(player))
			return;

		if (!Recharge.Instance.use(player, "Poop", 4000, true, false))
			return;

		//Action
		Item item = player.getWorld().dropItem(player.getEyeLocation().add(player.getLocation().getDirection()), ItemStackFactory.Instance.CreateStack(Material.MELON_SEEDS));
		UtilAction.velocity(item, player.getLocation().getDirection(), 
				0.01, true, -0.3, 0, 10, false);

		Manager.getProjectileManager().AddThrow(item, player, this, -1, true, true, true, 
				null, 1f, 1f, null, null, 0, UpdateType.TICK, 0.5f);
		
		//Inform
		UtilPlayer.message(player, F.main("Skill", "You used " + F.skill("Poop") + "."));
		
		player.getWorld().playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 0.1f);
	}
	
	@Override
	public void Collide(LivingEntity target, Block block, ProjectileUser data)
	{
		if (target != null)
		{
			//Effect
			target.playEffect(EntityEffect.HURT);
			
			target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1), true);
			
			//Inform
			UtilPlayer.message(target, F.main("Skill", F.name(UtilEnt.getName(data.GetThrower())) + " hit you with " + F.skill("Bat Poop") + "."));
			
			UtilPlayer.message(data.GetThrower(), F.main("Skill", "You hit " + F.name(UtilEnt.getName(target)) + " with " + F.skill("Bat Poop") + "."));
		}
		
		data.GetThrown().remove();
	}

	@Override
	public void Idle(ProjectileUser data)
	{
		data.GetThrown().remove();
	}

	@Override
	public void Expire(ProjectileUser data)
	{
		data.GetThrown().remove();
	}

	@EventHandler
	public void Flap(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();

		if (player.getGameMode() == GameMode.CREATIVE)
			return;

		if (!IsActive(player))
			return;

		event.setCancelled(true);
		player.setFlying(false);

		//Disable Flight
		player.setAllowFlight(false);

		//Velocity
		UtilAction.velocity(player, player.getLocation().getDirection(), 0.8, false, 0, 0.5, 0.8, true);

		//Sound
		player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, (float)(0.3 + player.getExp()), (float)(Math.random()/2+0.5));

		//Set Recharge
		Recharge.Instance.use(player, GetName(), 40, false, false);
	}

	@EventHandler
	public void FlapUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetActive())
		{
			if (player.getGameMode() == GameMode.CREATIVE)
				continue;

			if (UtilEnt.isGrounded(player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) 
			{
				player.setAllowFlight(true);
			}
			else if (Recharge.Instance.usable(player, GetName()))
			{
				player.setAllowFlight(true);
			}
		}
	}
}
