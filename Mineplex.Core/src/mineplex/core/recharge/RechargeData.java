package mineplex.core.recharge;

import mineplex.core.common.util.C;

import mineplex.core.common.util.UtilGear;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextBottom;
import mineplex.core.common.util.UtilTime;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class RechargeData
{
	public Recharge Host;

	public long Time;
	public long Recharge;

	public Player Player;
	public String Name;

	public ItemStack Item;
	
	public boolean DisplayForce = false;
	public boolean Countdown = false; 		//This will make the output a countdown, instead of a recharge.
	public boolean AttachItem;
	public boolean AttachDurability;

	public RechargeData(Recharge host, Player player, String name, ItemStack stack, long rechargeTime, boolean attachitem, boolean attachDurability)
	{
		Host = host;

		Player = player;
		Name = name;
		Item = player.getItemInHand();
		Time = System.currentTimeMillis();
		Recharge = rechargeTime;
		
		AttachItem = attachitem;
		AttachDurability = attachDurability;
	}

	public boolean Update()
	{
		if ((DisplayForce || Item != null) && Name != null && Player != null)
		{
			//Holding Recharge Item
			double percent = (double)(System.currentTimeMillis() - Time)/(double)Recharge;
			
			if (DisplayForce || AttachItem)
			{
				try
				{
					if (DisplayForce || (Item != null && UtilGear.isMat(Player.getItemInHand(), Item.getType())))
					{
						if (!UtilTime.elapsed(Time, Recharge))
						{
							UtilTextBottom.displayProgress(C.Bold + Name, percent, UtilTime.MakeStr(Recharge - (System.currentTimeMillis() - Time)), Countdown, Player);
						}
						else
						{
							
							if (!Countdown)
								UtilTextBottom.display(C.cGreen + C.Bold + Name + " Recharged", Player);
							else
								UtilTextBottom.display(C.cRed + C.Bold + Name + " Ended", Player);
							
							//PLING!
							if (Recharge > 4000)
								Player.playSound(Player.getLocation(), Sound.NOTE_PLING, 0.4f, 3f);
						}
					}
				}
				catch (Exception e)
				{
					System.out.println("Recharge Indicator Error!");
					e.printStackTrace();
				}
			}
			
			if (AttachDurability && Item != null)
			{
				Item.setDurability((short) (Item.getType().getMaxDurability() - (Item.getType().getMaxDurability() * percent)));
			}
		}


		return UtilTime.elapsed(Time, Recharge);
	}

	public long GetRemaining()
	{
		return Recharge - (System.currentTimeMillis() - Time);
	}

	public void debug(Player player)
	{
		player.sendMessage("Recharge: " + Recharge);
		player.sendMessage("Time: " + Time);
		player.sendMessage("Elapsed: " + (System.currentTimeMillis() - Time));
		player.sendMessage("Remaining: " + GetRemaining());
	}
}
