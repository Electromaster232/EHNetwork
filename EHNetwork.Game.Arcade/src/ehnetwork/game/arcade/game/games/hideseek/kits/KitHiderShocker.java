package ehnetwork.game.arcade.game.games.hideseek.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkShockingStrike;

public class KitHiderShocker extends KitHider
{
	public KitHiderShocker(ArcadeManager manager)
	{
		super(manager, "Shocking Hider", KitAvailability.Gem, 5000,

				new String[] 
						{ 
				"Shock and stun seekers!"
						}, 

						new Perk[] 
								{
				new PerkShockingStrike()
								}, 
								EntityType.SLIME,
								new ItemStack(Material.REDSTONE_LAMP_OFF));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(Material.SLIME_BALL, (byte)0, 1, C.cYellow + C.Bold + "Click Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Change Form"));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		((Slime)ent).setSize(2);
		
		//Manager.GetDisguise().disguise(new DisguiseBlock(ent, 61, 0));
	}
}
