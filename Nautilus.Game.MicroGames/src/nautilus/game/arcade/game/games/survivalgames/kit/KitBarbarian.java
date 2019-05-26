package nautilus.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.recharge.Recharge;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitBarbarian extends Kit
{
	public KitBarbarian(ArcadeManager manager)
	{
		super(manager, "Barbarian", KitAvailability.Gem, 6000, 

				new String[] 
						{
				"Skilled at taking out teams!",
				"Abilities disabled for first 30 seconds."
						}, 

						new Perk[] 
								{ 
				
				new PerkCleave(0.75, false),
				new PerkBladeVortex()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.DIAMOND_SWORD));

	}

	@Override
	public void GiveItems(Player player) 
	{
		Recharge.Instance.useForce(player, GetName(), 45000);
	}
}
