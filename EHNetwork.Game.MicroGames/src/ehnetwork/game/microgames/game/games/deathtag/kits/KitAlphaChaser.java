package ehnetwork.game.microgames.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.disguise.disguises.DisguiseSkeleton;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkDamageSet;
import ehnetwork.game.microgames.kit.perks.PerkIronSkin;
import ehnetwork.game.microgames.kit.perks.PerkKnockbackMultiplier;

public class KitAlphaChaser extends AbstractKitChaser
{
	public KitAlphaChaser(MicroGamesManager manager)
	{
		super(manager, "Alpha Chaser", KitAvailability.Free, 

				new String[] 
						{
						}, 

						new Perk[] 
								{
				new PerkDamageSet(6),
				new PerkKnockbackMultiplier(0.5),
				new PerkIronSkin(4)
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
		disguise.SetSkeletonType(SkeletonType.WITHER);
		Manager.GetDisguise().disguise(disguise);
	}
}
