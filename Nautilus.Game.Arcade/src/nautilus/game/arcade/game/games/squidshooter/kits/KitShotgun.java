package nautilus.game.arcade.game.games.squidshooter.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.disguise.disguises.DisguiseSquid;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.PerkSquidShotgun;
import nautilus.game.arcade.kit.perks.PerkSquidSwim;

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
		DisguiseSquid disguise = new DisguiseSquid(player);
		disguise.setName(C.cWhite + player.getName());
		disguise.setCustomNameVisible(true);
		Manager.GetDisguise().disguise(disguise);
	}
}
