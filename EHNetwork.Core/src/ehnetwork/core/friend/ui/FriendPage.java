package ehnetwork.core.friend.ui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemBuilder;

public enum FriendPage
{
    FRIENDS(new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setTitle("Friends").build(), "Friends"),

    FRIEND_REQUESTS(new ItemBuilder(Material.RED_ROSE).setTitle("Friend Requests").build(), "Friend Requests"),

    DELETE_FRIENDS(new ItemBuilder(Material.TNT).setTitle("Delete Friends").build(), "Delete Friends"),

    SEND_REQUEST(new ItemBuilder(Material.BOOK_AND_QUILL).setTitle("Send Friend Request").build(), "Send Friend Request"),

    TOGGLE_DISPLAY(new ItemBuilder(Material.SIGN).setTitle(C.cGray + "Toggle friends to display in chat").build(),
            "Toggle Display");

    private ItemStack _icon;
    private String _name;

    private FriendPage(ItemStack item, String name)
    {
        _icon = item;
        _name = name;
    }

    public String getName()
    {
        return _name;
    }

    public ItemStack getIcon()
    {
        return _icon;
    }
}
