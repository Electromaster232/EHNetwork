package ehnetwork.game.arcade.game.games.baconbrawl.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.disguise.disguises.DisguisePig;
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
		DisguisePig disguise = new DisguisePig(player);
		disguise.setName(C.cYellow + player.getName());
		disguise.setCustomNameVisible(false);
		Manager.GetDisguise().disguise(disguise);
	}
}
