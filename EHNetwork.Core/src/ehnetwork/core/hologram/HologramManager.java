package ehnetwork.core.hologram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.packethandler.IPacketHandler;
import ehnetwork.core.packethandler.PacketInfo;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class HologramManager extends MiniPlugin implements IPacketHandler
{

    private final List<Hologram> _activeHolograms = new ArrayList<>();

    public HologramManager(JavaPlugin arcadeManager)
    {
        super("Hologram Manager", arcadeManager);

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

        Iterator<Hologram> iterator = _activeHolograms.iterator();

        while (iterator.hasNext())
        {
            Hologram hologram = iterator.next();

            if (hologram.getMaxLifetime() != -1 && UtilTime.elapsed(hologram.getStartTime(), hologram.getMaxLifetime()))
            {
                iterator.remove();
                hologram.stop();
            }
            else if (!worlds.contains(hologram.getLocation().getWorld()))
            {
                iterator.remove();
                hologram.stop();
            }
            else
            {
                if (hologram.getEntityFollowing() != null)
                {
                    Entity following = hologram.getEntityFollowing();

                    if (hologram.isRemoveOnEntityDeath() && !following.isValid())
                    {
                        iterator.remove();
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
                            UtilPlayer.sendPacket(player, hologram.getDestroyPacket());
                        }
                    }
                }

                for (Player player : canSee)
                {
                    if (!hologram.getPlayersTracking().contains(player))
                    {
                        hologram.getPlayersTracking().add(player);

                        UtilPlayer.sendPacket(player, hologram.getSpawnPackets(player));
                    }
                }
            }
        }
    }

    @Override
    public void handle(PacketInfo packetInfo)
    {
        PacketPlayInUseEntity packetPlayIn = (PacketPlayInUseEntity) packetInfo.getPacket();

        for (Hologram hologram : _activeHolograms)
        {
            if (!hologram.isEntityId(packetPlayIn.hashCode()))
                continue;

            if (hologram.getInteraction() != null)
            {
                hologram.getInteraction().onClick(packetInfo.getPlayer(), ClickType.LEFT);
            }

            break;
        }
    }
}
