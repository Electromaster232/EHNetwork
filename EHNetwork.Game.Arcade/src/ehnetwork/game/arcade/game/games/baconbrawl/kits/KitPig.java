package ehnetwork.game.arcade.game.games.baconbrawl.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.PigDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBodySlam;

public class KitPig extends Kit
{
	public KitPig(ArcadeManager manager)
	{
		super(manager, "El Muchacho Pigo", KitAvailability.Free, 

				new String[] 
						{
				"Such a fat pig. Oink."
						}, 

						new Perk[] 
								{
				new PerkBodySlam(6, 2),
				//new PerkJump(1), MAC 
								}, 
								EntityType.PIG,
								new ItemStack(Material.PORK));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));

		//Disguise
		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.PIG);
		PigDisguise d2 = (PigDisguise) d1;
		d2.setCustomNameVisible(true);
		d2.setCustomName(C.cYellow + player.getName());
		Manager.GetDisguise().applyDisguise(d2, player);
	}
}
