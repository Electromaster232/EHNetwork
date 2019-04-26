package nautilus.game.arcade.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkCripple;

public class KitRunnerBasher extends AbstractKitRunner
{
	public KitRunnerBasher(ArcadeManager manager)
	{
		super(manager, "Runner Basher", KitAvailability.Free, 
				new String[] 
				{
					"Your attacks cripple Chasers breifly!"
				}, 
				new Perk[] 
				{
					new PerkCripple(3, 2),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.IRON_SWORD));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD));
	}
}
