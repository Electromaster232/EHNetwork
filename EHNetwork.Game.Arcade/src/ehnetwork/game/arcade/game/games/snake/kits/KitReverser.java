package ehnetwork.game.arcade.game.games.snake.kits;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitReverser extends Kit
{
	public KitReverser(ArcadeManager manager)
	{
		super(manager, "Reversal Snake", KitAvailability.Achievement, 

				new String[] 
						{ 
				"Stuck in a dead end?! Change direction!",
				"",
				C.cYellow + "Click" + C.cGray + " with Cookie to use " + C.cGreen + "Reverse"
						}, 

						new Perk[] 
								{
				
								}, 
								EntityType.SHEEP,
								new ItemStack(Material.WOOL));
		
		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.SNAKE_CANNIBAL,
				Achievement.SNAKE_CHOO_CHOO,
				Achievement.SNAKE_SLIMY_SHEEP,
				Achievement.SNAKE_WINS,
				});
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.COOKIE, (byte)0, 3, 
				C.cYellow + C.Bold + "Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Reverse"));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		((Sheep)ent).setColor(DyeColor.MAGENTA);
	}
}
