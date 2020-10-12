package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkHiltSmash;
import ehnetwork.game.microgames.kit.perks.PerkIronSkin;

public class KitKnight extends Kit
{
	public KitKnight(MicroGamesManager manager)
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
