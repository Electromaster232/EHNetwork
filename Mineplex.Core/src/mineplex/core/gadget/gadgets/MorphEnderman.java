package mineplex.core.gadget.gadgets;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilFirework;
import mineplex.core.disguise.disguises.DisguiseEnderman;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.MorphGadget;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class MorphEnderman extends MorphGadget
{
	public MorphEnderman(GadgetManager manager)
	{
		super(manager, "Enderman Morph", new String[] 
				{
				C.cWhite + "Transforms the wearer into an Enderman!",
				" ",
				C.cYellow + "Double Jump" + C.cGray + " to use " + C.cGreen + "Blink",
				},
				30000,
				Material.ENDER_PEARL, (byte)0);
	}

	@Override
	public void EnableCustom(final Player player) 
	{
		this.ApplyArmor(player);

		DisguiseEnderman disguise = new DisguiseEnderman(player);
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
	public void teleport(PlayerToggleFlightEvent event)
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

		//Set Recharge
		Recharge.Instance.use(player, GetName(), 2000, false, false);

		//Smoke Trail
		Block lastSmoke = player.getLocation().getBlock();

		double curRange = 0;
		while (curRange <= 16)
		{
			Location newTarget = player.getLocation().add(new Vector(0,0.2,0)).add(player.getLocation().getDirection().multiply(curRange));

			if (!UtilBlock.airFoliage(newTarget.getBlock()) || 
					!UtilBlock.airFoliage(newTarget.getBlock().getRelative(BlockFace.UP)))
				break;

			//Progress Forwards
			curRange += 0.2;

			//Smoke Trail
			if (!lastSmoke.equals(newTarget.getBlock()))
			{
				lastSmoke.getWorld().playEffect(lastSmoke.getLocation(), Effect.SMOKE, 4);
			}

			lastSmoke = newTarget.getBlock();
		}

		//Modify Range
		curRange -= 0.4;
		if (curRange < 0)
			curRange = 0;

		//Destination
		Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(curRange).add(new Vector(0, 0.4, 0)));

		if (curRange > 0)
		{
			//Firework
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.BLACK).with(Type.BALL).trail(false).build();

			try 
			{
				UtilFirework.playFirework(player.getEyeLocation(), effect);
				player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}

			player.teleport(loc);

			//Firework
			try 
			{
				UtilFirework.playFirework(player.getEyeLocation(), effect);
				player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}


		player.setFallDistance(0);
	}

	@EventHandler
	public void teleportUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : GetActive())
		{
			if (player.getGameMode() == GameMode.CREATIVE)
				continue;

			if (Recharge.Instance.usable(player, GetName()))
			{
				player.setAllowFlight(true);
			}
		}
	}
}
