package ehnetwork.game.arcade.game.games.hideseek.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.MobDisguise;
import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.common.util.C;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitHiderTeleporter extends Kit
{
	public KitHiderTeleporter(ArcadeManager manager)
	{
		super(manager, "Infestor Hider", KitAvailability.Achievement, 

				new String[] 
						{ 
				"Can instantly infest a target block.",
				"This is the only way you can hide."
				
						}, 

						new Perk[] 
								{
				
								}, 
								EntityType.SLIME,
								new ItemStack(Material.SLIME_BALL));
		
		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.BLOCK_HUNT_BAD_HIDER,
				Achievement.BLOCK_HUNT_HUNTER_KILLER,
				Achievement.BLOCK_HUNT_HUNTER_OF_THE_YEAR,
				Achievement.BLOCK_HUNT_MEOW,
				Achievement.BLOCK_HUNT_WINS,
				});
	}

	@Override
	public void GiveItems(Player player) 
	{
		//Swap
		player.getInventory().setItem(3, ItemStackFactory.Instance.CreateStack(Material.MAGMA_CREAM, (byte)0, 1, C.cYellow + C.Bold + "Click Block" + C.cWhite + C.Bold + " - " + C.cGreen + C.Bold + "Infest Block/Animal"));


		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.SLIME);
		MobDisguise disguise = (MobDisguise) d1;
		disguise.setCustomName(Manager.GetGame().GetTeam(player).GetColor() + player.getName());
		disguise.setCustomNameVisible(false);
		Manager.GetDisguise().applyDisguise(disguise, player);
	}
	
	@Override
	public void SpawnCustom(LivingEntity ent) 
	{
		((Slime)ent).setSize(2);
	}
}
