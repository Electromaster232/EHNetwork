package ehnetwork.game.microgames.game.games.halloween.waves;

import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.game.microgames.game.games.halloween.Halloween;
import ehnetwork.game.microgames.game.games.halloween.creatures.CreatureBase;

public class WaveVictory extends WaveBase
{
	public WaveVictory(Halloween host) 
	{
		super(host, "Celebration!", 15000, host.GetSpawnSet(3), null);
	}

	@Override
	public void Spawn(int tick) 
	{
		if (UtilTime.elapsed(_start, 20000))
			return;
		
		//Play
		if (tick == 0)
			for (Player player : UtilServer.getPlayers())
				player.playEffect(Host.WorldData.GetDataLocs("BLACK").get(0), Effect.RECORD_PLAY, 2259);
			
		//Mobs
		for (CreatureBase<LivingEntity> mob : Host.GetCreatures())
			mob.GetEntity().damage(5);
		
		//Time
		if (Host.WorldTimeSet != 6000)
		{
			Host.WorldTimeSet = (Host.WorldTimeSet + 50)%24000;
			Host.WorldData.World.setTime(Host.WorldTimeSet);
		}
	}
}
