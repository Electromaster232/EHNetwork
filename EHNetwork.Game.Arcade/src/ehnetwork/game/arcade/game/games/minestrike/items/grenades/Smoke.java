package ehnetwork.game.arcade.game.games.minestrike.items.grenades;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.games.minestrike.MineStrike;
import ehnetwork.game.arcade.game.games.minestrike.Radio;

public class Smoke extends Grenade
{
	public Smoke()
	{
		super("Smoke",  new String[] 
				{
				
				},
				300, 0, Material.POTATO_ITEM, 1);
	}

	@Override
	public boolean updateCustom(MineStrike game, Entity ent)
	{
		if (ent.getTicksLived() > 60)
		{
			UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION, ent.getLocation(), 0.3f, 0.3f, 0.3f, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
			
			ent.getWorld().playSound(ent.getLocation(), Sound.FIZZ, 0.1f, 0.1f);
			
			for (Location loc : game.Manager.GetBlockRestore().RestoreBlockAround(Material.FIRE, ent.getLocation(), 5))
			{
				loc.getWorld().playSound(loc, Sound.FIZZ, 1f, 1f);
			}
			
			return false;
		}
		
		return ent.getTicksLived() > 360;
	}
	
	@Override
	public void playSound(MineStrike game, Player player)
	{
		GameTeam team = game.GetTeam(player);
		if (team == null)
			return;
		
		game.playSound(team.GetColor() == ChatColor.RED ? Radio.T_GRENADE_SMOKE : Radio.CT_GRENADE_SMOKE, player, null);
	}
}
