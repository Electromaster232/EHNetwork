package nautilus.game.arcade.game.games.snake.kits;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.disguise.disguises.DisguiseSheep;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

public class KitSpeed extends Kit
{
	public KitSpeed(ArcadeManager manager)
	{
		super(manager, "Speedy Snake", KitAvailability.Free, 

				new String[] 
						{ 
				"Can quickly speed up to take out other snakes.",
				"",
				C.cYellow + "Click" + C.cGray + " with Feather to use " + C.cGreen + "Speed Boost"
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
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.FEATHER, (byte)0, 5, 
				C.cYellow + C.Bold + "Click" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Speed Boost"));
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		((Sheep)ent).setColor(DyeColor.YELLOW);
	}
}
