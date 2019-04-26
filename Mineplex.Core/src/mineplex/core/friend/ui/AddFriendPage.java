package mineplex.core.friend.ui;

import mineplex.core.command.CommandCenter;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.friend.FriendManager;
import mineplex.core.itemstack.ItemBuilder;
import net.minecraft.server.v1_7_R4.ContainerAnvil;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AddFriendPage implements Listener
{
    private class AnvilContainer extends ContainerAnvil
    {
        private String n;

        public AnvilContainer(EntityHuman entity)
        {
            super(entity.inventory, entity.world, 0, 0, 0, entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman)
        {
            return true;
        }

        @Override
        public void a(String origString)
        {
            n = origString;
            _itemName = origString;

            if (getSlot(2).hasItem())
            {
                net.minecraft.server.v1_7_R4.ItemStack itemstack = getSlot(2).getItem();

                if (StringUtils.isBlank(origString))
                    itemstack.t();
                else
                {
                    itemstack.c(this.n);
                }
            }

            e();
        }

    }

    private FriendManager _friends;
    private Player _player;
    private Inventory _currentInventory;
    private String _itemName = "";
    private boolean _searching;

    public AddFriendPage(FriendManager friends, Player player)
    {
        _player = player;
        _friends = friends;

        openInventory();
        friends.registerEvents(this);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (event.getPlayer() == _player)
        {
            unregisterListener();
        }
    }

    public void unregisterListener()
    {
        _currentInventory.clear();
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getRawSlot() < 3)
        {
            event.setCancelled(true);

            if (event.getRawSlot() == 2)
            {
                if (_itemName.length() > 1 && !_searching)
                {
                    _searching = true;
                    final String name = _itemName;

                    CommandCenter.Instance.GetClientManager().checkPlayerName(_player, _itemName, new Callback<String>()
                    {
                        public void run(String result)
                        {
                            _searching = false;

                            if (result != null)
                            {
                                _friends.addFriend(_player, result);
                                _player.playSound(_player.getLocation(), Sound.NOTE_PLING, 1, 1.6f);

                                unregisterListener();
                                new FriendsGUI(_friends, _player);
                            }
                            else
                            {
                                _currentInventory.setItem(
                                        2,
                                        new ItemBuilder(Material.PAPER).setTitle(
                                                C.cYellow + "0" + C.cGray + " matches for [" + C.cYellow + name + C.cGray + "]")
                                                .build());
                                _player.playSound(_player.getLocation(), Sound.ITEM_BREAK, 1, .6f);
                            }
                        }
                    });
                }
                else
                {
                    _player.playSound(_player.getLocation(), Sound.ITEM_BREAK, 1, .6f);
                }
            }
        }
        else if (event.isShiftClick())
        {
            event.setCancelled(true);
        }
    }

    public void openInventory()
    {
        _player.closeInventory();

        EntityPlayer p = ((CraftPlayer) _player).getHandle();

        AnvilContainer container = new AnvilContainer(p);
        int c = p.nextContainerCounter();

        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(c, 8, "Repairing", 0, true);

        p.playerConnection.sendPacket(packet);

        // Set their active container to the container
        p.activeContainer = container;

        // Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        // Add the slot listener
        p.activeContainer.addSlotListener(p); // Set the items to the items from the inventory given
        _currentInventory = container.getBukkitView().getTopInventory();

        _currentInventory.setItem(0, new ItemBuilder(Material.PAPER).setRawTitle("Friend's Name").build());
        _currentInventory.setItem(2, new ItemBuilder(Material.PAPER).setRawTitle("Search").build());
    }

}
