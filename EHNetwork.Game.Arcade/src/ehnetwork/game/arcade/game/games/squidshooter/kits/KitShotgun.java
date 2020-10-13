package ehnetwork.game.arcade.game.games.squidshooter.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkSquidShotgun;
import ehnetwork.game.arcade.kit.perks.PerkSquidSwim;

public class KitShotgun extends Kit
{
	public KitShotgun(ArcadeManager manager)
	{
		super(manager, "Squid Blaster", KitAvailability.Gem, 

				new String[] 
						{ 
				"Fires many slow projectiles."
						}, 

						new Perk[] 
								{
				new PerkSquidSwim(),
				new PerkSquidShotgun()
								}, 
								EntityType.SQUID,
								new ItemStack(Material.INK_SACK));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));
		
		ItemStack helm = ItemStackFactory.Instance.CreateStack(Material.DIAMOND_HELMET);
		helm.addEnchantment(Enchantment.OXYGEN, 3);
		player.getInventory().setHelmet(helm);

		//Disguise
		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.SQUID);
		MobDisguise disguise = (MobDisguise) d1;
		disguise.setCustomName(player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().applyDisguise(disguise, player);
	}
}
