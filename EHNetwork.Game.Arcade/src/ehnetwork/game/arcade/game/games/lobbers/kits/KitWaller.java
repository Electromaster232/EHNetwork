package ehnetwork.game.arcade.game.games.lobbers.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ehnetwork.core.achievement.Achievement;
import ehnetwork.core.common.util.F;
import ehnetwork.core.itemstack.ItemBuilder;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.game.games.lobbers.kits.perks.PerkCraftman;
import ehnetwork.game.arcade.game.games.lobbers.kits.perks.PerkWaller;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;

public class KitWaller extends Kit
{
	public KitWaller(ArcadeManager manager)
	{
		super(manager, "Waller", KitAvailability.Achievement, 0, new String[]
				{
				"When the times get tough,",
				"build yourself a wall!"
				}, new Perk[]
						{
				new PerkWaller(),
				new PerkCraftman()
						}, EntityType.ZOMBIE,
				new ItemBuilder(Material.SMOOTH_BRICK).setUnbreakable(true).build());
		
		this.setAchievementRequirements(new Achievement[]
				{
				Achievement.BOMB_LOBBERS_WINS,
				Achievement.BOMB_LOBBERS_ULTIMATE_KILLER,
				Achievement.BOMB_LOBBERS_SNIPER,
				Achievement.BOMB_LOBBERS_PROFESSIONAL_LOBBER,
				Achievement.BOMB_LOBBERS_EXPLOSION_PROOF,
				Achievement.BOMB_LOBBERS_BLAST_PROOF
				});
	}

	@Override
	public void GiveItems(Player player)
	{
		player.getInventory().setItem(1, new ItemBuilder(Material.STONE_SPADE).setAmount(3).setTitle(F.item("Wall Builder")).build());
	}

}

