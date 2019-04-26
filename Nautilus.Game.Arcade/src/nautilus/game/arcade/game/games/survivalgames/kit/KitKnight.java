package nautilus.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitKnight extends Kit
{
	public KitKnight(ArcadeManager manager)
	{
		super(manager, "Knight", KitAvailability.Free, 

				new String[] 
						{
				
						}, 

						new Perk[] 
								{
				new PerkIronSkin(0.5),
				new PerkHiltSmash()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_SWORD));

	}

	@Override
	public void GiveItems(Player player) 
	{
		
	}
}
