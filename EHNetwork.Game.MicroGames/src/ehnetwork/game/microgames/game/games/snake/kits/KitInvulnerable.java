package ehnetwork.game.microgames.game.games.snake.kits;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitInvulnerable extends Kit
{
	public KitInvulnerable(MicroGamesManager manager)
	{
		super(manager, "Super Snake", KitAvailability.Gem, 5000,

				new String[] 
						{ 
				"Able to temporarily travel through tails.",
				"",
				C.cYellow + "Click" + C.cGray + " with Nether Star to use " + C.cGreen + "Invulnerability",
						}, 

						new Perk[] 
								{
				
								}, 
								EntityType.SHEEP,
								new ItemStack(Material.WOOL));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 2, 
				C.cYellow + C.Bold + "Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Invulnerability"));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		((Sheep)ent).setColor(DyeColor.LIME);
	}
}
