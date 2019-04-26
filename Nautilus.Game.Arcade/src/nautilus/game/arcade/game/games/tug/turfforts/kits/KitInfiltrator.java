package nautilus.game.arcade.game.games.turfforts.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilServer;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkConstructor;
import nautilus.game.arcade.kit.perks.PerkFletcher;

public class KitInfiltrator extends Kit 
{
	public KitInfiltrator(ArcadeManager manager)
	{
		super(manager, "Infiltrator", KitAvailability.Gem, 
				new String[] 
				{
					"Able to travel into the enemies turf, but you",
					"must return to your turf fast, or receive Slow."
				}, 
				new Perk[] 
				{
				new PerkConstructor("Constructor", 4, 4, Material.WOOL, "Wool", false),
				new PerkFletcher(8, 1, false),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.IRON_SWORD));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
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
