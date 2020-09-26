package ehnetwork.game.microgames.game.games.tug.turfforts.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkConstructor;
import ehnetwork.game.microgames.kit.perks.PerkFletcher;

public class KitMarksman extends Kit 
{
	public KitMarksman(MicroGamesManager manager)
	{
		super(manager, "Marksman", KitAvailability.Free, 
				new String[] 
						{
				"Unrivaled in archery. One hit kills anyone."
						}, 
						new Perk[] 
								{
				new PerkConstructor("Constructor", 4, 8, Material.WOOL, "Wool", false),
				new PerkFletcher(2, 2, false),

								}, 
								EntityType.ZOMBIE,	
								new ItemStack(Material.BOW));

	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));

		int amount = 6;
		if (!Manager.GetGame().IsLive())
			amount = 64;

		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.WOOL, Manager.GetGame().GetTeam(player).GetColorData(), amount));

		//Update
		final Player fPlayer = player;

		UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				UtilInv.Update(fPlayer);
			}
		}, 10);
	}
}
