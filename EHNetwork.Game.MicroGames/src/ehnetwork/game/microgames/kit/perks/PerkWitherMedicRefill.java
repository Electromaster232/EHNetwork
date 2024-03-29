package ehnetwork.game.microgames.kit.perks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.microgames.kit.Perk;

public class PerkWitherMedicRefill extends Perk
{
	private int _max = 0;
	private int _time = 0;

	public PerkWitherMedicRefill(int timeInSeconds, int max)
	{
		super("Healing Hands", new String[]
				{
				C.cGray + "Receive 1 healing bottle every " + timeInSeconds + " seconds if you're inventory is clearned from a bottle.",
				});
		
		this._time = timeInSeconds;
		this._max = max;
		
	}
	
	@EventHandler
	public void bottleRefill(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;

		for (Player player : UtilServer.getPlayers())
		{
			if (Manager.isSpectator(player))
				continue;
			
			if (!Kit.HasKit(player))
				continue;

			if (!Manager.GetGame().IsAlive(player))
				continue;

			if (!Recharge.Instance.use(player, GetName(), _time * 1000, false, false))
				continue;

			//Add
			ItemStack potion = new ItemStack(Material.POTION, 1, (short)16429); // 16422
			PotionMeta potionMeta = (PotionMeta)potion.getItemMeta();
			potionMeta.setDisplayName(ChatColor.RESET + "Revival Potion");
			potion.setItemMeta(potionMeta);
			
			if (UtilInv.contains(player, "Revival Potion", Material.POTION, potion.getData().getData(), _max))
				continue;

			player.getInventory().addItem(potion);

			player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 2f, 1f);
		}
	}

}
