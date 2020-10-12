package ehnetwork.core.treasure.animation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.TileEntityEnderChest;

import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.hologram.Hologram;
import ehnetwork.core.hologram.HologramManager;
import ehnetwork.core.reward.RewardData;
import ehnetwork.core.treasure.ChestData;
import ehnetwork.core.treasure.Treasure;

/**
 * Created by Shaun on 8/29/2014.
 */
public class ChestOpenAnimation extends Animation
{
    private ChestData _chestData;
    private RewardData _rewardData;
    private HologramManager _hologramManager;

    private Item _itemEntity;
    private Hologram _hologram;

    public ChestOpenAnimation(Treasure treasure, ChestData chestData, RewardData rewardData, HologramManager hologramManager)
    {
        super(treasure);
        _hologramManager = hologramManager;
        _chestData = chestData;
        _rewardData = rewardData;

        // Send chest open packet
        Block block = chestData.getBlock();
        BlockPosition bl1 = new BlockPosition(block.getX(), block.getY(), block.getZ());
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(bl1,
                CraftMagicNumbers.getBlock(block), 1, 1);
        for (Player other : UtilServer.getPlayers())
        {
            ((CraftPlayer) other).getHandle().playerConnection.sendPacket(packet);

			if (block.getType() == Material.ENDER_CHEST)
			{
				// Fix for Ender Chests closing as soon as they are opened
				TileEntity tileEntity = ((CraftWorld) block.getWorld()).getTileEntityAt(block.getX(), block.getY(), block.getZ());
				if (tileEntity instanceof TileEntityEnderChest)
                {
                    ((TileEntityEnderChest) tileEntity).a = 1;
                    ((TileEntityEnderChest) tileEntity).f = 1;
                    ((TileEntityEnderChest) tileEntity).g = 1;
                }
			}

            other.playSound(block.getLocation(), Sound.CHEST_OPEN, 1, 1);
        }
    }

    @Override
    protected void tick()
    {
        if (getTicks() == 5)
        {
            Location location = _chestData.getBlock().getLocation().add(0.5, 0.8, 0.5);
            _itemEntity = location.getWorld().dropItem(location, _rewardData.getDisplayItem());
            _itemEntity.setVelocity(new Vector(0, 0, 0));
            _itemEntity.setPickupDelay(Integer.MAX_VALUE);
        }
        else if (getTicks() == 15)
        {
            _hologram = new Hologram(_hologramManager, _chestData.getBlock().getLocation().add(0.5, 1.4, 0.5),
                    _rewardData.getFriendlyName());
            _hologram.start();
        }
    }

    public void onFinish()
    {
        if (_hologram != null)
        {
            _hologram.stop();
            _itemEntity.remove();
        }
    }
}
