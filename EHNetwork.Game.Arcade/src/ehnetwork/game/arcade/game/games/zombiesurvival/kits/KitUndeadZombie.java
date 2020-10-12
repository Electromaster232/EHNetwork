package ehnetwork.game.arcade.game.games.zombiesurvival.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.disguise.disguises.DisguiseSkeleton;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkIronSkin;
import ehnetwork.game.arcade.kit.perks.PerkLeap;
import ehnetwork.game.arcade.kit.perks.PerkRegeneration;
import ehnetwork.game.arcade.kit.perks.PerkStrength;

public class KitUndeadZombie extends Kit
{
	public KitUndeadZombie(ArcadeManager manager)
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
