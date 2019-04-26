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
import nautilus.game.arcade.kit.perks.PerkFletcher;
import nautilus.game.arcade.kit.perks.PerkKnockbackArrow;

public class KitRunnerArcher extends AbstractKitRunner
{
	public KitRunnerArcher(ArcadeManager manager)
	{
		super(manager, "Runner Archer", KitAvailability.Gem, 
				new String[] 
				{
					"Fight off the Chasers with Arrows!"
				}, 
				new Perk[] 
				{
					new PerkKnockbackArrow(3),
					new PerkFletcher(2, 2, true),
				}, 
				EntityType.ZOMBIE,	
				new ItemStack(Material.BOW));

	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
	}
}
