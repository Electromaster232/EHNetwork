package nautilus.game.arcade.game.games.zombiesurvival.kits;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import mineplex.core.disguise.disguises.DisguiseSkeleton;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;
 
public class KitUndeadAlpha extends Kit
{
	public KitUndeadAlpha(ArcadeManager manager)
	{
		super(manager, "Alpha Undead", KitAvailability.Free, 

				new String[] 
						{
				"Leap at those undead"
						}, 

						new Perk[] 
								{ 
				new PerkLeap("Leap", 1, 1, 8000),
				new PerkStrength(2),
				new PerkIronSkin(2),
				new PerkRegeneration(1)
								}, 
								EntityType.SKELETON,
								new ItemStack(Material.STONE_AXE));

	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.STONE_AXE));
		
		DisguiseSkeleton disguise = new DisguiseSkeleton(player);
		disguise.SetSkeletonType(SkeletonType.WITHER);
		disguise.setName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{ 
		if (ent instanceof Skeleton)
		{
			Skeleton skel =  (Skeleton)ent;
			skel.setSkeletonType(SkeletonType.WITHER);
		}
	}
}
