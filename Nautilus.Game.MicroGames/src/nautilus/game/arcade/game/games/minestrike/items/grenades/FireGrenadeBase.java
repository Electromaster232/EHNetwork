package nautilus.game.arcade.game.games.minestrike.items.grenades;

import java.util.HashMap;

import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ParticleType;
import mineplex.core.common.util.UtilParticle.ViewDist;
import nautilus.game.arcade.game.GameTeam;
import nautilus.game.arcade.game.games.minestrike.MineStrike;
import nautilus.game.arcade.game.games.minestrike.Radio;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class FireGrenadeBase extends Grenade
{
	private long _baseTime;
	
	public FireGrenadeBase(String name, int price, int gemCost, Material skin, long burnTime)
	{
		super(name,  new String[] 
				{
				
				},
				price, gemCost, skin, 1);
		
		_baseTime = burnTime;
	}

	@Override
	public boolean updateCustom(MineStrike game, Entity ent)
	{
		if (UtilEnt.isGrounded(ent))
		{
			createFire(game, ent.getLocation());

			return true;
		}

		return false;
	}

	private void createFire(final MineStrike game, final Location loc)
	{
		//Sound
		loc.getWorld().playSound(loc, Sound.IRONGOLEM_THROW, 1f, 1f);
		
		//Particle
		UtilParticle.PlayParticle(ParticleType.LAVA, loc.add(0, 0.2, 0), 0.3f, 0f, 0.3f, 0, 30,
				ViewDist.LONG, UtilServer.getPlayers());
		
		//Fire Blocks
		final HashMap<Block, Double> blocks = UtilBlock.getInRadius(loc, 3.5d);
		final int round = game.getRound();
		for (final Block block : blocks.keySet())
		{
			
			//Edited by TheMineBench, to keep the two-half-slabs from burning
			if (block.getType() != Material.AIR && !block.getType().name().toLowerCase().contains("step") || block.getType().name().toLowerCase().contains("double"))
				continue;
			
			if (!UtilBlock.solid(block.getRelative(BlockFace.DOWN)))
				continue;
			
			UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(game.Manager.getPlugin(), new Runnable()
			{
				public void run()
				{
					if (round == game.getRound() && !game.isFreezeTime())
						game.Manager.GetBlockRestore().Add(block, 51, (byte)0, (long) (_baseTime + blocks.get(block) * _baseTime));
				}
			}, 60 - (int)(60d * blocks.get(block)));
		}
		
		//Initial Burn Sound
		UtilServer.getServer().getScheduler().scheduleSyncDelayedTask(game.Manager.getPlugin(), new Runnable()
		{
			public void run()
			{
				loc.getWorld().playSound(loc, Sound.PIG_DEATH, 1f, 1f);
			}
		}, 20);
		
		//Register
		game.registerIncendiary(loc, System.currentTimeMillis() + (_baseTime*2-4000));
	}
	
	@Override
	public void playSound(MineStrike game, Player player)
	{
		GameTeam team = game.GetTeam(player);
		if (team == null)
			return;
		
		game.playSound(team.GetColor() == ChatColor.RED ? Radio.T_GRENADE_FIRE : Radio.CT_GRENADE_FIRE, player, null);
	}
}
