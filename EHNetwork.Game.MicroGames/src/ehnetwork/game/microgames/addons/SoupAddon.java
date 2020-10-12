package ehnetwork.game.microgames.addons;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.minecraft.game.core.condition.Condition.ConditionType;

public class SoupAddon extends MiniPlugin
{
	public MicroGamesManager Manager;
	
	public SoupAddon(JavaPlugin plugin, MicroGamesManager manager)
	{
		super("Soup Addon", plugin);
		
		Manager = manager;
	}

	@EventHandler
	public void EatSoup(PlayerInteractEvent event)
	{
		if (Manager.GetGame() == null)
			return;
				
		if (!Manager.GetGame().IsLive())
			return;
		
		if (!Manager.GetGame().SoupEnabled)
			return;
		
		Player player = event.getPlayer();
		
		if (!Manager.GetGame().IsAlive(player))
			return;
		
		if (!UtilGear.isMat(player.getItemInHand(), Material.MUSHROOM_SOUP))
			return;
		
		if (UtilBlock.usable(event.getClickedBlock()))
			return;
		
		//Effect
		player.getWorld().playSound(player.getLocation(), Sound.EAT, 2f, 1f);
		player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, 39);
		player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, 40);
		
		//Healing
		Manager.GetCondition().Factory().Custom("Mushroom Soup", player, player, ConditionType.REGENERATION, 4, 1, false, Material.MUSHROOM_SOUP, (byte)0, true);
		
		//Food
		UtilPlayer.hunger(player, 3);
		
		event.setCancelled(true);
		player.setItemInHand(null);
	}
}
