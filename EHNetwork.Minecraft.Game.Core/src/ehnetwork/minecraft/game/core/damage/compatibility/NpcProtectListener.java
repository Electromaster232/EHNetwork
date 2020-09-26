package ehnetwork.minecraft.game.core.damage.compatibility;

import ehnetwork.core.npc.NpcManager;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NpcProtectListener implements Listener
{
	private NpcManager _npcManager;
	
	public NpcProtectListener(NpcManager npcManager)
	{
		_npcManager = npcManager;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void CustomDamage(CustomDamageEvent event)
	{
    	if (event.GetDamageeEntity() != null && _npcManager.getNpcByEntity(event.GetDamageeEntity()) != null)
    		event.SetCancelled("NPC");
	}
}
