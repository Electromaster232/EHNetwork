package nautilus.game.arcade.game.games.quiver.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mineplex.core.achievement.Achievement;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilServer;
import mineplex.core.itemstack.ItemStackFactory;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.game.Game.GameState;
import nautilus.game.arcade.kit.Kit;
import nautilus.game.arcade.kit.KitAvailability;
import nautilus.game.arcade.kit.Perk;
import nautilus.game.arcade.kit.perks.*;

public class KitSlamShot extends Kit
{
	public KitSlamShot(ArcadeManager manager)
	{
		super(manager, "Slam Shooter", KitAvailability.Achievement, 

				new String[] 
						{
				"Gets 2 arrows for killing slammed players!"
						}, 

						new Perk[] 
								{ 
				new PerkSeismicSlamOITQ()
								}, 
								EntityType.ZOMBIE,
								new ItemStack(Material.IRON_SPADE));

		this.setAchievementRequirements(new Achievement[] 
				{
				Achievement.OITQ_PERFECTIONIST,
				Achievement.OITQ_SHARPSHOOTER,
				Achievement.OITQ_WHATS_A_BOW,
				Achievement.OITQ_WINS,
				});
	}
	
	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_SPADE));
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.BOW));
		
		if (Manager.GetGame().GetState() == GameState.Live)
		{
			player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(262, (byte)0, 1, F.item("Super Arrow")));
			
			final Player fPlayer = player;
			
			UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					UtilInv.Update(fPlayer);
				}
			}, 10);
		}
	}
}
