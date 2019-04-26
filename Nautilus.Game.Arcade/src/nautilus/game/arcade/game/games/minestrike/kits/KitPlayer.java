package nautilus.game.arcade.game.games.minestrike.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.common.util.C;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;

public class KitPlayer extends Kit 
{
	public KitPlayer(ArcadeManager manager)
	{
		super(manager, "Player", KitAvailability.Free, 
				new String[] 
				{
				C.cGreen + "Right-Click" + C.cWhite + " - " + C.cYellow + "Fire Gun",
				C.cGreen + "Left-Click" + C.cWhite + " - " + C.cYellow + "Reload Gun",
				C.cGreen + "Crouch" + C.cWhite + " - " + C.cYellow + "Sniper Scope",
				"",
				C.cGreen + "Hold Right-Click with Bomb" + C.cWhite + " - " + C.cRed + "Plant Bomb",
				C.cGreen + "Hold Right-Click with Knife" + C.cWhite + " - " + C.cAqua + "Defuse Bomb",
				}, 
				new Perk[] 
				{
				
				}, 
				EntityType.PLAYER,	
				new ItemStack(Material.AIR));
 
	}

	@Override
	public void GiveItems(Player player)
	{
		
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		
	}
}
