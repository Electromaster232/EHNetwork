package ehnetwork.game.arcade.game.games.tug.turfforts.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBarrage;
import ehnetwork.game.arcade.kit.perks.PerkConstructor;
import ehnetwork.game.arcade.kit.perks.PerkFletcher;

public class KitShredder extends Kit 
{
	public KitShredder(ArcadeManager manager)
	{
		super(manager, "Shredder", KitAvailability.Gem, 5000,
				new String[] 
						{
				"Arrows are weaker, but shred through forts."
						}, 
						new Perk[] 
								{
				new PerkConstructor("Constructor", 4, 6, Material.WOOL, "Wool", false),
				new PerkFletcher(4, 2, false),
				new PerkBarrage(5, 250, false, false),
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
