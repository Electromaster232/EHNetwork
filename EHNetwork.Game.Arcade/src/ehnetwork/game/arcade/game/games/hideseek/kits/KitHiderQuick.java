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

public class KitHiderQuick extends KitHider
{
	public KitHiderQuick(ArcadeManager manager)
	{
		super(manager, "Instant Hider", KitAvailability.Gem, 

				new String[] 
						{ 
				"Changes into solid blocks almost instantly!"
						}, 

						new Perk[] 
								{
				
								}, 
								EntityType.SLIME,
								new ItemStack(Material.FEATHER));
	}

	@Override
	public void GiveItems(Player player) 
	{
		//Swap
		player.getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(Material.SLIME_BALL, (byte)0, 1, C.cYellow + C.Bold + "Click Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Change Form"));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		((Slime)ent).setSize(2);
		
		//Manager.GetDisguise().disguise(new DisguiseBlock(ent, 46, 0));
	}
}
