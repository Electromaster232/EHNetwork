package nautilus.game.arcade.kit.perks;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.recharge.Recharge;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.game.games.dragonriders.DragonData;
import nautilus.game.arcade.kit.Perk;

public class PerkDragonRider extends Perk
{
	public HashMap<Player, DragonData> _dragons = new HashMap<Player, DragonData>();
	
	public PerkDragonRider() 
	{
		super("Dragon Rider", new String[] 
				{
				C.cGray + "You ride a dragon!",
				});
	}

	@EventHandler
	public void DragonSpawn(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		if (Manager.GetGame().GetState() != GameState.Live)
			return;
		
		for (Player player : Manager.GetGame().GetPlayers(true))
		{
			if (!Kit.HasKit(player))
					continue;
			
			if (!_dragons.containsKey(player))
				_dragons.put(player, new DragonData(Manager, player));
		}
	}
	
	@EventHandler
	public void DragonLocation(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		if (Manager.GetGame().GetState() != GameState.Live)
			return;
		
		//Dragon Update!
		for (DragonData data : _dragons.values())
			data.Move();
	}
	
	@EventHandler
	public void DragonTargetCancel(EntityTargetEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void ShootWeb(PlayerInteractEvent event)
	{
		if (event.isCancelled())
			return;

		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		if (UtilBlock.usable(event.getClickedBlock()))
			return;

		if (event.getPlayer().getItemInHand() == null)
			return;

		if (!event.getPlayer().getItemInHand().getType().toString().contains("BOW"))
			return;

		Player player = event.getPlayer();

		if (!Kit.HasKit(player))
			return;

		if (!Recharge.Instance.use(player, GetName(), 1000, true, false))
			return;

		event.setCancelled(true);

		UtilInv.Update(player);

		Fireball ball = _dragons.get(player).Dragon.launchProjectile(Fireball.class);

		//Inform
		UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Dragon Blast") + "."));

		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 2f, 1f);
	}
}
