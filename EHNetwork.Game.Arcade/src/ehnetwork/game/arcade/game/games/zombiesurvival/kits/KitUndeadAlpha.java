package ehnetwork.game.arcade.game.games.zombiesurvival.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkIronSkin;
import ehnetwork.game.arcade.kit.perks.PerkLeap;
import ehnetwork.game.arcade.kit.perks.PerkRegeneration;
import ehnetwork.game.arcade.kit.perks.PerkStrength;
 
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

		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.SKELETON);
		MobDisguise disguise = (MobDisguise) d1;

			disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor()
					+ player.getName());

		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
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
