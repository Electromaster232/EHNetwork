package ehnetwork.game.microgames.game.games.survivalgames.kit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.recharge.Recharge;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkBladeVortex;
import ehnetwork.game.microgames.kit.perks.PerkCleave;

public class KitBarbarian extends Kit
{
	public KitBarbarian(MicroGamesManager manager)
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
