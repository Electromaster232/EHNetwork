package mineplex.core.gadget.gadgets;

import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilServer;
import mineplex.core.gadget.GadgetManager;
import mineplex.core.gadget.types.OutfitGadget;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class OutfitRaveSuit extends OutfitGadget
{

	private HashMap<String, Integer> _colorPhase = new HashMap<String, Integer>();

	public OutfitRaveSuit(GadgetManager manager, String name,
			int cost, ArmorSlot slot, Material mat, byte data)
	{
		super(manager, name, new String[] {"Wear the complete set for","awesome bonus effects!", "Bonus coming soon..."}, cost, slot, mat, data);
	}

	@Override
	public void EnableCustom(Player player)
	{
		ApplyArmor(player);
		_colorPhase.put(player.getName(), -1);
	}

	@Override
	public void DisableCustom(Player player)
	{
		RemoveArmor(player);
		_colorPhase.remove(player.getName());
	}

	@EventHandler
	public void updateColor(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (!IsActive(player))
				continue;

			//Get Item
			ItemStack stack;

			if (GetSlot() == ArmorSlot.Helmet)
			{
				stack = player.getInventory().getHelmet();

				if (!UtilGear.isMat(stack, GetDisplayMaterial()))
				{
					Disable(player);
					continue;
				}
			}
			else if (GetSlot() == ArmorSlot.Chest)
			{
				stack = player.getInventory().getChestplate();

				if (!UtilGear.isMat(stack, GetDisplayMaterial()))
				{
					Disable(player);
					continue;
				}
			}
			else if (GetSlot() == ArmorSlot.Legs)
			{
				stack = player.getInventory().getLeggings();

				if (!UtilGear.isMat(stack, GetDisplayMaterial()))
				{
					Disable(player);
					continue;
				}
			}
			else if (GetSlot() == ArmorSlot.Boots)
			{
				stack = player.getInventory().getBoots();

				if (!UtilGear.isMat(stack, GetDisplayMaterial()))
				{
					Disable(player);
					continue;
				}
			}
			else
			{
				continue;
			}

			//Rainbow
			int phase = _colorPhase.get(player.getName());

			LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();

			if (phase == -1)
			{
				meta.setColor(Color.fromRGB(250, 0, 0));
				_colorPhase.put(player.getName(), 0);
			}
			//Red > Yellow
			else if (phase == 0)
			{
				meta.setColor(Color.fromRGB(250, Math.min(250, meta.getColor().getGreen() + 25), 0));

				if (meta.getColor().getGreen() >= 250)
					_colorPhase.put(player.getName(), 1);
			}
			//Yellow > Green
			else if (phase == 1)
			{
				meta.setColor(Color.fromRGB(Math.max(0, meta.getColor().getRed() - 25), 250, 0));

				if (meta.getColor().getRed() <= 0)
					_colorPhase.put(player.getName(), 2);
			}
			//Green > Blue
			else if (phase == 2)
			{
				meta.setColor(Color.fromRGB(0, Math.max(0, meta.getColor().getGreen() - 25), Math.min(250, meta.getColor().getBlue() + 25)));

				if (meta.getColor().getGreen() <= 0)
					_colorPhase.put(player.getName(), 3);
			}
			//Blue > Red
			else if (phase == 3)
			{
				meta.setColor(Color.fromRGB(Math.min(250, meta.getColor().getRed() + 25), 0, Math.max(0, meta.getColor().getBlue() - 25)));

				if (meta.getColor().getBlue() <= 0)
					_colorPhase.put(player.getName(), 0);
			}

			stack.setItemMeta(meta);
		}
	}
}
