package mineplex.core.itemstack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

public class ItemBuilder
{

    private static ArrayList<String> split(String string, int maxLength)
    {
        String[] split = string.split(" ");
        string = "";
        ArrayList<String> newString = new ArrayList<String>();
        for (int i = 0; i < split.length; i++)
        {
            string += (string.length() == 0 ? "" : " ") + split[i];
            if (ChatColor.stripColor(string).length() > maxLength)
            {
                newString
                        .add((newString.size() > 0 ? ChatColor.getLastColors(newString.get(newString.size() - 1)) : "") + string);
                string = "";
            }
        }
        if (string.length() > 0)
            newString.add((newString.size() > 0 ? ChatColor.getLastColors(newString.get(newString.size() - 1)) : "") + string);
        return newString;
    }

    private int _amount;
    private Color _color;
    private short _data;
    private final HashMap<Enchantment, Integer> _enchants = new HashMap<Enchantment, Integer>();
    private final List<String> _lore = new ArrayList<String>();
    private Material _mat;
    // private Potion potion;
    private String _title = null;
    private boolean _unbreakable;
	private String _playerHeadName = null;

    public ItemBuilder(ItemStack item)
    {
        this(item.getType(), item.getDurability());
        this._amount = item.getAmount();
        this._enchants.putAll(item.getEnchantments());
        if (item.getType() == Material.POTION)
        {
            // setPotion(Potion.fromItemStack(item));
        }
        if (item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName())
            {
                this._title = meta.getDisplayName();
            }
            if (meta.hasLore())
            {
                this._lore.addAll(meta.getLore());
            }
            if (meta instanceof LeatherArmorMeta)
            {
                this.setColor(((LeatherArmorMeta) meta).getColor());
            }
            this._unbreakable = meta.spigot().isUnbreakable();
        }
    }

    public ItemBuilder(Material mat)
    {
        this(mat, 1);
    }

    public ItemBuilder(Material mat, int amount)
    {
        this(mat, amount, (short) 0);
    }

    public ItemBuilder(Material mat, int amount, short data)
    {
        this._mat = mat;
        this._amount = amount;
        this._data = data;
    }

    public ItemBuilder(Material mat, short data)
    {
        this(mat, 1, data);
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level)
    {
        if (_enchants.containsKey(enchant))
        {
            _enchants.remove(enchant);
        }
        _enchants.put(enchant, level);
        return this;
    }

    public ItemBuilder addLore(String... lores)
    {
        for (String lore : lores)
        {
            this._lore.add(ChatColor.GRAY + lore);
        }
        return this;
    }

    public ItemBuilder addLore(String lore, int maxLength)
    {
        this._lore.addAll(split(lore, maxLength));
        return this;
    }

    public ItemBuilder addLores(List<String> lores)
    {
        this._lore.addAll(lores);
        return this;
    }

    public ItemBuilder addLores(List<String> lores, int maxLength)
    {
        for (String lore : lores)
        {
            addLore(lore, maxLength);
        }
        return this;
    }

    public ItemBuilder addLores(String[] description, int maxLength)
    {
        return addLores(Arrays.asList(description), maxLength);
    }

    public ItemStack build()
    {
        Material mat = this._mat;
        if (mat == null)
        {
            mat = Material.AIR;
            Bukkit.getLogger().warning("Null material!");
        }
        else if (mat == Material.AIR)
        {
            Bukkit.getLogger().warning("Air material!");
        }
        ItemStack item = new ItemStack(mat, this._amount, this._data);
        ItemMeta meta = item.getItemMeta();
        if (meta != null)
        {
            if (this._title != null)
            {
                meta.setDisplayName(this._title);
            }
            if (!this._lore.isEmpty())
            {
                meta.setLore(this._lore);
            }
            if (meta instanceof LeatherArmorMeta)
            {
                ((LeatherArmorMeta) meta).setColor(this._color);
            }
			else if (meta instanceof SkullMeta && _playerHeadName != null)
			{
				((SkullMeta) meta).setOwner(_playerHeadName);
			}
            meta.spigot().setUnbreakable(isUnbreakable());
            item.setItemMeta(meta);
        }
        item.addUnsafeEnchantments(this._enchants);
        // if (this.potion != null) {
        // this.potion.apply(item);
        // }
        return item;
    }

    @Override
    public ItemBuilder clone()
    {
        ItemBuilder newBuilder = new ItemBuilder(this._mat);

        newBuilder.setTitle(this._title);
        for (String lore : this._lore)
        {
            newBuilder.addLore(lore);
        }
        for (Map.Entry<Enchantment, Integer> entry : this._enchants.entrySet())
        {
            newBuilder.addEnchantment(entry.getKey(), entry.getValue());
        }
        newBuilder.setColor(this._color);
        // newBuilder.potion = this.potion;

        return newBuilder;
    }

    public HashMap<Enchantment, Integer> getAllEnchantments()
    {
        return this._enchants;
    }

    public Color getColor()
    {
        return this._color;
    }

    public short getData()
    {
        return this._data;
    }

    public int getEnchantmentLevel(Enchantment enchant)
    {
        return this._enchants.get(enchant);
    }

    public List<String> getLore()
    {
        return this._lore;
    }

    public String getTitle()
    {
        return this._title;
    }

    public Material getType()
    {
        return this._mat;
    }

    public boolean hasEnchantment(Enchantment enchant)
    {
        return this._enchants.containsKey(enchant);
    }

    public boolean isItem(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != this.getType())
        {
            return false;
        }
        if (!meta.hasDisplayName() && this.getTitle() != null)
        {
            return false;
        }
        if (!meta.getDisplayName().equals(this.getTitle()))
        {
            return false;
        }
        if (!meta.hasLore() && !this.getLore().isEmpty())
        {
            return false;
        }
        if (meta.hasLore())
        {
            for (String lore : meta.getLore())
            {
                if (!this.getLore().contains(lore))
                {
                    return false;
                }
            }
        }
        for (Enchantment enchant : item.getEnchantments().keySet())
        {
            if (!this.hasEnchantment(enchant))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isUnbreakable()
    {
        return this._unbreakable;
    }

    public ItemBuilder setAmount(int amount)
    {
        this._amount = amount;
        return this;
    }

    public ItemBuilder setColor(Color color)
    {
        if (!this._mat.name().contains("LEATHER_"))
        {
            throw new IllegalArgumentException("Can only dye leather armor!");
        }
        this._color = color;
        return this;
    }

    public void setData(short newData)
    {
        this._data = newData;
    }

    public ItemBuilder setPotion(Potion potion)
    {
        if (this._mat != Material.POTION)
        {
            this._mat = Material.POTION;
        }
        // this.potion = potion;
        return this;
    }

    public ItemBuilder setRawTitle(String title)
    {
        this._title = title;
        return this;
    }

    public ItemBuilder setTitle(String title)
    {
        this._title = (title == null ? null
                : (title.length() > 2 && ChatColor.getLastColors(title.substring(0, 2)).length() == 0 ? ChatColor.WHITE : ""))
                + title;
        return this;
    }

    public ItemBuilder setTitle(String title, int maxLength)
    {
        if (title != null && ChatColor.stripColor(title).length() > maxLength)
        {
            ArrayList<String> lores = split(title, maxLength);
            for (int i = 1; i < lores.size(); i++)
            {
                this._lore.add(lores.get(i));
            }
            title = lores.get(0);
        }
        setTitle(title);
        return this;
    }

    public ItemBuilder setType(Material mat)
    {
        this._mat = mat;
        return this;
    }

    public ItemBuilder setUnbreakable(boolean setUnbreakable)
    {
        this._unbreakable = setUnbreakable;return this;
    }

	public ItemBuilder setPlayerHead(String playerName)
	{
		_playerHeadName = playerName;
		return this;
	}

}