package ehnetwork.core.memory;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import net.minecraft.server.v1_7_R4.CraftingManager;
import net.minecraft.server.v1_7_R4.IInventory;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class MemoryFix extends MiniPlugin
{
	private static Field _intHashMap;
	
	public MemoryFix(JavaPlugin plugin)
	{
		super("Memory Fix", plugin);
		
		//_intHashMap = IntHashMap.class.	
	}
	
	@EventHandler
	public void fixInventoryLeaks(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		for (World world : Bukkit.getWorlds())
		{
			for (Object tileEntity : ((CraftWorld)world).getHandle().tileEntityList)
			{
				if (tileEntity instanceof IInventory)
				{
					Iterator<HumanEntity> entityIterator = ((IInventory)tileEntity).getViewers().iterator();
					
					while (entityIterator.hasNext())
					{
						HumanEntity entity = entityIterator.next();
						
						if (entity instanceof CraftPlayer && !((CraftPlayer)entity).isOnline())
						{
							entityIterator.remove();
						}
					}
				}
			}
		}
		
		CraftingManager.getInstance().lastCraftView = null;
		CraftingManager.getInstance().lastRecipe = null;
	}
	
	@EventHandler
	public void fixEntityTrackerLeak(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SLOW)
			return;
		
		// NEED TO FIX STUCK NETWORKMANAGERS.....
		/*
		for (World world : Bukkit.getWorlds())
		{
			EntityTracker tracker = ((CraftWorld)world).getHandle().getTracker();
			
	        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) tracker.trackedEntities.d(entity.getId());

	        if (entitytrackerentry1 != null) {
	            this.c.remove(entitytrackerentry1);
	            entitytrackerentry1.a();
	        }
		}		
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entity;
            

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                entitytrackerentry.a(entityplayer);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) this.trackedEntities.d(entity.getId());

        if (entitytrackerentry1 != null) {
            this.c.remove(entitytrackerentry1);
            entitytrackerentry1.a();
        }
        */
	}
}
