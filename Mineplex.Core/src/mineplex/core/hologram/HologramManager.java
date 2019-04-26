package mineplex.core.hologram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import net.minecraft.server.v1_7_R4.Packet;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class HologramManager implements Listener
{
    private ArrayList<Hologram> _activeHolograms = new ArrayList<Hologram>();

    public HologramManager(JavaPlugin arcadeManager)
    {
        Bukkit.getPluginManager().registerEvents(this, arcadeManager);
    }

    void addHologram(Hologram hologram)
    {
        _activeHolograms.add(hologram);
    }

    void removeHologram(Hologram hologram)
    {
        _activeHolograms.remove(hologram);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTick(UpdateEvent event)
    {
        if (event.getType() != UpdateType.TICK || _activeHolograms.isEmpty())
            return;
        List<World> worlds = Bukkit.getWorlds();
        Iterator<Hologram> itel = _activeHolograms.iterator();
        while (itel.hasNext())
        {
            Hologram hologram = itel.next();
            if (!worlds.contains(hologram.getLocation().getWorld()))
            {
                itel.remove();
                hologram.stop();
            }
            else
            {
                if (hologram.getEntityFollowing() != null)
                {
                    Entity following = hologram.getEntityFollowing();
                    if (hologram.isRemoveOnEntityDeath() && !following.isValid())
                    {
                        itel.remove();
                        hologram.stop();
                        continue;
                    }
                    if (!hologram.relativeToEntity.equals(following.getLocation().subtract(hologram.getLocation()).toVector()))
                    {
                        // And we do this so in the rare offchance it changes by a decimal. It doesn't start turning wonky.
                        Vector vec = hologram.relativeToEntity.clone();
                        hologram.setLocation(following.getLocation().add(hologram.relativeToEntity));
                        hologram.relativeToEntity = vec;
                        continue; // No need to do the rest of the code as setLocation does it.
                    }
                }
                ArrayList<Player> canSee = hologram.getNearbyPlayers();
                Iterator<Player> itel2 = hologram.getPlayersTracking().iterator();
                while (itel2.hasNext())
                {
                    Player player = itel2.next();
                    if (!canSee.contains(player))
                    {
                        itel2.remove();
                        if (player.getWorld() == hologram.getLocation().getWorld())
                        {
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(hologram.getDestroyPacket(player));
                        }
                    }
                }
                for (Player player : canSee)
                {
                    if (!hologram.getPlayersTracking().contains(player))
                    {
                        hologram.getPlayersTracking().add(player);
                        for (Packet packet : hologram.getSpawnPackets(player))
                        {
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                }
            }
        }
    }
}
