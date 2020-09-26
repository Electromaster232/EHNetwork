package ehnetwork.game.arcade.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkHiltSmash;
import ehnetwork.game.arcade.kit.perks.PerkIronSkin;

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
