package ehnetwork.game.arcade.game.games.hideseek.forms;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.MapUtil;
import ehnetwork.core.common.util.UtilBlock;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.arcade.game.games.hideseek.HideSeek;
import ehnetwork.game.arcade.game.games.hideseek.kits.KitHiderQuick;

public class BlockForm extends Form
{
    private Material _mat;

    private Block _block;
    private int _entityId;

    private Location _loc;
    private int _selfEntityId1;
    private int _selfEntityId2;
    private Vector _lastSaw;
    private int _fakeSilverfishId;
    private Vector _sawDiff = new Vector();
    private boolean _is18;

    public BlockForm(HideSeek host, Player player, Material mat)
    {
        super(host, player);

        _is18 = UtilPlayer.is1_8(player);
        _mat = mat;
        _loc = player.getLocation();
        _fakeSilverfishId = UtilEnt.getNewEntityId();
        _selfEntityId1 = UtilEnt.getNewEntityId();
        _selfEntityId2 = UtilEnt.getNewEntityId();
        System.out.println("Block Form: " + _mat + " " + _mat.getId());
    }

    @Override
    public void Apply()
    {
        // Remove Old
        if (Player.getPassenger() != null)
        {
            Recharge.Instance.useForce(Player, "PassengerChange", 100);

            Player.getPassenger().remove();
            Player.eject();
        }

        EntityPlayer player = ((CraftPlayer) Player).getHandle();
        player.getDataWatcher().watch(0, (byte) 32, Entity.META_ENTITYDATA, (byte) 32);

        // Player > Chicken
        //DisguiseChicken disguise = new DisguiseChicken(Player);
        //disguise.setBaby();
        //disguise.setInvisible(true);
        //disguise.setSoundDisguise(new DisguiseCat(Player));
        //Host.Manager.GetDisguise().disguise(disguise);

        Disguise d1 = Host.Manager.GetDisguise().createDisguise(EntityType.CHICKEN);
        MobDisguise d2 = (MobDisguise) d1;
        d2.setVisibility(Disguise.Visibility.ONLY_LIST);
        Host.Manager.GetDisguise().applyDisguise(d2, Player);

        // Apply Falling Block
        FallingBlockCheck();

        // Spawn the falling block that's visible to the disguised player only.
        
        Packet[] packets = new Packet[3];

        if (_is18)
        {
            PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving();
            packet1.a = _selfEntityId1;
            packet1.b = EntityType.SILVERFISH.getTypeId();
            packet1.c = (int) Math.floor(_lastSaw.getX() * 32);
            packet1.d = (int) Math.floor(_lastSaw.getY() * 32);
            packet1.e = (int) Math.floor(_lastSaw.getZ() * 32);
            DataWatcher watcher = new DataWatcher(null);
            watcher.a(0, (byte) 32, Entity.META_ENTITYDATA, (byte) 32);
            watcher.a(1, 0, Entity.META_AIR, 0);
            packet1.l = watcher;
            packets[0] = packet1;

            PacketPlayOutAttachEntity packet3 = new PacketPlayOutAttachEntity();

            packet3.b = _selfEntityId2;
            packet3.c = _selfEntityId1;
            packets[2] = packet3;
        }

        PacketPlayOutSpawnEntity packet2 = new PacketPlayOutSpawnEntity(player, 70, _mat.getId());
        packet2.a = _is18 ? _selfEntityId2 : _selfEntityId1;
        packets[1] = packet2;

        for (Packet packet : packets)
        {
            if (packet != null)
            {
                player.playerConnection.sendPacket(packet);
            }
        }

        // Inform
        String blockName = F.elem(ItemStackFactory.Instance.GetName(_mat, (byte) 0, false));
        if (!blockName.contains("Block"))
            UtilPlayer.message(
                    Player,
                    F.main("Game",
                            C.cWhite + "You are now a "
                                    + F.elem(ItemStackFactory.Instance.GetName(_mat, (byte) 0, false) + " Block") + "!"));
        else
            UtilPlayer.message(
                    Player,
                    F.main("Game", C.cWhite + "You are now a " + F.elem(ItemStackFactory.Instance.GetName(_mat, (byte) 0, false))
                            + "!"));

        // Give Item
        Player.getInventory().setItem(8, new ItemStack(Host.GetItemEquivilent(_mat)));
        UtilInv.Update(Player);

        // Sound
        Player.playSound(Player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
    }

    @Override
    public void Remove()
    {
        SolidifyRemove();

        Host.Manager.GetDisguise().undisguise(Player);

        // Remove FB
        if (Player.getPassenger() != null)
        {
            Recharge.Instance.useForce(Player, "PassengerChange", 100);

            Player.getPassenger().remove();
            Player.eject();

            ((CraftPlayer) Player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[]
                {
                        _selfEntityId1, _selfEntityId2
                }));
        }

        ((CraftEntity) Player).getHandle().getDataWatcher().watch(0, (byte) 0, Entity.META_ENTITYDATA, (byte) 0);
    }

    public void SolidifyUpdate()
    {
        if (!Player.isSprinting())
            ((CraftEntity) Player).getHandle().getDataWatcher().watch(0, (byte) 32, Entity.META_ENTITYDATA,
                    (byte) 32);

        // Not a Block
        if (_block == null)
        {
            // Moved
            if (!_loc.getBlock().equals(Player.getLocation().getBlock()))
            {
                Player.setExp(0);
                _loc = Player.getLocation();
            }
            // Unmoved
            else
            {
                double hideBoost = 0.025;
                if (Host.GetKit(Player) instanceof KitHiderQuick)
                    hideBoost = 0.1;

                Player.setExp((float) Math.min(0.999f, Player.getExp() + hideBoost));

                // Set Block
                if (Player.getExp() >= 0.999f)
                {
                    Block block = Player.getLocation().getBlock();

                    // Not Able
                    if (block.getType() != Material.AIR || !UtilBlock.solid(block.getRelative(BlockFace.DOWN)))
                    {
                        UtilPlayer.message(Player, F.main("Game", "You cannot become a Solid Block here."));
                        Player.setExp(0f);
                        return;
                    }

                    Bukkit.getPluginManager().callEvent(new HideSeek.PlayerSolidifyEvent(Player));

                    // Set Block
                    _block = block;

                    // Effect
                    Player.playEffect(Player.getLocation(), Effect.STEP_SOUND, _mat);
                    // block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, _mat);

                    // Display
                    SolidifyVisual();

                    // Invisible
                    // Host.Manager.GetCondition().Factory().Cloak("Disguised as Block", Player, Player, 60000, false, false);

                    // Sound
                    Player.playSound(Player.getLocation(), Sound.NOTE_PLING, 1f, 2f);

                    // Teleport falling block to the position.
                    Vector blockLoc = _block.getLocation().add(0.5, _is18 ? -.21875 : 0, 0.5).toVector();

                    _sawDiff.add(blockLoc.clone().subtract(_lastSaw));

                    Packet[] packet = this.getPacket(_sawDiff, blockLoc);

                    _lastSaw = Player.getLocation().toVector().subtract(new Vector(0, _is18 ? 0.15625 : 0, 0));
                    _sawDiff = _lastSaw.clone().subtract(blockLoc);

                    if (packet != null)
                    {
                        if (packet[0] instanceof   PacketPlayOutEntityTeleport)
                        {
                            _sawDiff = new Vector();
                        }

                        ((CraftPlayer) Player).getHandle().playerConnection.sendPacket(packet[0]);
                    }
                }
            }
        }
        // Is a Block
        else
        {
            // Moved
            if (!_loc.getBlock().equals(Player.getLocation().getBlock()))
            {
                SolidifyRemove();
            }
            // Send Packets
            else
            {
                SolidifyVisual();
            }
        }
    }

    public void SolidifyRemove()
    {
        if (_block != null)
        {
            MapUtil.QuickChangeBlockAt(_block.getLocation(), 0, (byte) 0);
            _block = null;
        }

        Player.setExp(0f);

        // Host.Manager.GetCondition().EndCondition(Player, null, "Disguised as Block");

        // Inform
        Player.playSound(Player.getLocation(), Sound.NOTE_PLING, 1f, 0.5f);

        FallingBlockCheck();
    }

    @SuppressWarnings("deprecation")
    public void SolidifyVisual()
    {
        if (_block == null)
            return;

        // Remove Old
        if (Player.getPassenger() != null)
        {
            Recharge.Instance.useForce(Player, "PassengerChange", 100);

            Player.getPassenger().remove();
            Player.eject();
        }

        // Others
        for (Player other : UtilServer.getPlayers())
            if (!other.equals(Player))
                other.sendBlockChange(Player.getLocation(), _mat, (byte) 0);

        // Self
        Player.sendBlockChange(Player.getLocation(), 36, (byte) 0);

    }
    public void FallingBlockCheck()
    {
        // Block Form (Hide Falling)
        if (_block == null)
        {
            // Tell falling block to move around

            if (_lastSaw != null)
            {
                this._sawDiff.add(Player.getLocation().subtract(0, 0.15625, 0).toVector().subtract(_lastSaw));
            }

            _lastSaw = Player.getLocation().subtract(0, 0.15625, 0).toVector();

            Packet[] packet = this.getPacket(_sawDiff, _lastSaw);

            if (packet != null)
            {

                    _sawDiff = new Vector();


                UtilPlayer.sendPacket(Player, packet[0]);
            }
        }
    }


    private Packet[] getPacket(Vector blocksFromNewPosition, Vector newPosition)
    {
        int x = (int) Math.floor(blocksFromNewPosition.getX() * 32);
        int y = (int) Math.floor(blocksFromNewPosition.getY() * 32);
        int z = (int) Math.floor(blocksFromNewPosition.getZ() * 32);

        if (x != 0 || y != 0 || z != 0)
        {
            Packet[] packets = new Packet[2];

            if (x >= -128 && x <= 127 && y >= -128 && y <= 127 && z >= -128 && z <= 127)
            {
                PacketPlayOutRelEntityMove relMove = new PacketPlayOutRelEntityMove();
                relMove.a = this._fakeSilverfishId;
                relMove.b = (byte) x;
                relMove.c = (byte) y;
                relMove.d = (byte) z;

                packets[0] = relMove;
            }

            {
                PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport();
                teleportPacket.a = _fakeSilverfishId;
                teleportPacket.b = (int) Math.floor(32 * newPosition.getX());
                teleportPacket.c = (int) Math.floor(32 * newPosition.getY());
                teleportPacket.d = (int) Math.floor(32 * newPosition.getZ());

                if (packets[0] == null)
                    packets[0] = teleportPacket;

                packets[1] = teleportPacket;
            }

            return packets;
        }

        return null;
    }


    public Block GetBlock()
    {
        return _block;
    }

    public int getEntityId()
    {
        return _entityId;
    }
}
