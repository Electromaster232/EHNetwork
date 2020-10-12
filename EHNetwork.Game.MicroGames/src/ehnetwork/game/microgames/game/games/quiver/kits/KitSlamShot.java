package ehnetwork.game.microgames.game.games.quiver.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilInv;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.Game.GameState;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkSeismicSlamOITQ;

public class KitSlamShot extends Kit
{
	public KitSlamShot(MicroGamesManager manager)
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
