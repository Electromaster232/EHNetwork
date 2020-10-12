package ehnetwork.game.microgames.game.games.zombiesurvival.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.disguise.disguises.DisguiseSkeleton;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkIronSkin;
import ehnetwork.game.microgames.kit.perks.PerkLeap;
import ehnetwork.game.microgames.kit.perks.PerkRegeneration;
import ehnetwork.game.microgames.kit.perks.PerkStrength;

public class KitUndeadZombie extends Kit
{
	public KitUndeadZombie(MicroGamesManager manager)
	{
		super(manager, "Undead", KitAvailability.Hide, 

				new String[] 
						{
				"Just a standard Zombie..."
						}, 

						new Perk[] 
								{ 
				new PerkLeap("Leap", 1, 1, 8000),
				new PerkStrength(1),
				new PerkIronSkin(1),
				new PerkRegeneration(0)
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.STONE_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_AXE));
		
		DisguiseSkeleton disguise = new DisguiseSkeleton(player);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
