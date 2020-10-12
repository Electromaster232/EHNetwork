package ehnetwork.game.arcade.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.disguise.disguises.DisguiseSkeleton;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkDamageSet;
import ehnetwork.game.arcade.kit.perks.PerkIronSkin;

public class KitChaser extends AbstractKitChaser
{
	public KitChaser(ArcadeManager manager)
	{
		super(manager, "Chaser", KitAvailability.Hide, 

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkDamageSet(4),
				new PerkIronSkin(2)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.IRON_AXE));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		
		//Disguise
		DisguiseSkeleton disguise = new DisguiseSkeleton(player);
		disguise.setName(C.cRed + player.getName());
		disguise.setCustomNameVisible(true);
		disguise.hideArmor();
		Manager.GetDisguise().disguise(disguise);
	}
}
