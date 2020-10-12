package ehnetwork.game.arcade.game.games.minestrike.items.grenades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ehnetwork.core.common.util.UtilParticle;
import ehnetwork.core.common.util.UtilParticle.ParticleType;
import ehnetwork.core.common.util.UtilParticle.ViewDist;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.game.arcade.game.GameTeam;
import ehnetwork.game.arcade.game.games.minestrike.MineStrike;
import ehnetwork.game.arcade.game.games.minestrike.Radio;

public class HighExplosive extends Grenade
{
	public HighExplosive()
	{
		super("High Explosive",  new String[] 
				{
				
				},
				300, 0, Material.APPLE, 1);
	}

	@Override
	public boolean updateCustom(MineStrike game, Entity ent)
	{
		if (ent.getTicksLived() > 40)
		{
			UtilParticle.PlayParticle(ParticleType.HUGE_EXPLOSION,
					ent.getLocation(), 0, 0, 0, 0, 1,
					ViewDist.MAX, UtilServer.getPlayers());
			ent.getWorld().playSound(ent.getLocation(),
					Sound.EXPLODE, 3f, 0.8f);

			HashMap<Player, Double> players = UtilPlayer.getInRadius(ent.getLocation(), 10);
			List<Player> damagedPlayers = new ArrayList<>();
			for (Player player : players.keySet())
			{
				if (!game.IsAlive(player))
					continue;
				
				// Damage Event
				game.Manager.GetDamage().NewDamageEvent(player, _thrower, null,
						DamageCause.CUSTOM, 1 + (players.get(player) * 18),
						true, true, false, _thrower.getName(), getName());

				damagedPlayers.add(player);
			}

			Bukkit.getPluginManager().callEvent(new GrenadeExplodeEvent(this, _thrower, damagedPlayers));

			return true;
		}

		return false;
	}
	
	@Override
	public void playSound(MineStrike game, Player player)
	{
		GameTeam team = game.GetTeam(player);
		if (team == null)
			return;
		
		game.playSound(team.GetColor() == ChatColor.RED ? Radio.T_GRENADE_HE : Radio.CT_GRENADE_HE, player, null);
	}
}
