package ehnetwork.game.arcade.game.games.deathtag.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkDamageSet;
import ehnetwork.game.arcade.kit.perks.PerkIronSkin;
import ehnetwork.game.arcade.kit.perks.PerkKnockbackMultiplier;

public class KitAlphaChaser extends AbstractKitChaser
{
	public KitAlphaChaser(ArcadeManager manager)
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
		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.SKELETON);
		MobDisguise disguise = (MobDisguise) d1;
		disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
	}
}
