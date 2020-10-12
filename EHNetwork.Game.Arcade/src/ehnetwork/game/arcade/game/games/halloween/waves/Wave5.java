package ehnetwork.game.arcade.game.games.halloween.waves;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.game.arcade.game.games.halloween.Halloween;
import ehnetwork.game.arcade.game.games.halloween.HalloweenAudio;
import ehnetwork.game.arcade.game.games.halloween.creatures.MobCreeper;
import ehnetwork.game.arcade.game.games.halloween.creatures.MobGiant;
import ehnetwork.game.arcade.game.games.halloween.creatures.MobZombie;

public class Wave5 extends WaveBase
{
	public Wave5(Halloween host) 
	{
		super(host, "Double the Giants! Double the fun!", 90000, host.GetSpawnSet(1), HalloweenAudio.WAVE_5);
	}

	@Override
	public void Spawn(int tick) 
	{
		if (UtilTime.elapsed(_start, 30000))
			return;
		
		if (tick == 0)
			SpawnBeacons(Host.GetSpawnSet(2));
		
		if (tick == 0)
		{
			Host.AddCreature(new MobGiant(Host, Host.GetSpawnSet(1).get(UtilMath.r(Host.GetSpawnSet(1).size()))));
			Host.AddCreature(new MobGiant(Host, Host.GetSpawnSet(2).get(UtilMath.r(Host.GetSpawnSet(2).size()))));
		}
			
		if (Host.GetCreatures().size() > Host.GetMaxMobs())
			return;
		
		if (tick % 20 == 0)
		{
			Host.AddCreature(new MobZombie(Host, Host.GetSpawnSet(1).get(UtilMath.r(Host.GetSpawnSet(1).size()))));
			Host.AddCreature(new MobZombie(Host, Host.GetSpawnSet(2).get(UtilMath.r(Host.GetSpawnSet(2).size()))));
		}

		if (tick % 60 == 0)
		{
			Host.AddCreature(new MobCreeper(Host, Host.GetSpawnSet(1).get(UtilMath.r(Host.GetSpawnSet(1).size()))));
			Host.AddCreature(new MobCreeper(Host, Host.GetSpawnSet(2).get(UtilMath.r(Host.GetSpawnSet(2).size()))));
		}
	}
}
