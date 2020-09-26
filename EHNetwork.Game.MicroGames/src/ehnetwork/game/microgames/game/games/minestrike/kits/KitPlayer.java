package ehnetwork.game.microgames.game.games.minestrike.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;

public class KitPlayer extends Kit 
{
	public KitPlayer(MicroGamesManager manager)
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
