package ehnetwork.game.microgames.game.games.dragonriders;

import ehnetwork.game.microgames.GameType;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.game.SoloGame;
import ehnetwork.game.microgames.game.games.dragonriders.kits.KitRider;
import ehnetwork.game.microgames.kit.Kit;

public class DragonRiders extends SoloGame
{
	public DragonRiders(MicroGamesManager manager)
	{
		super(manager, GameType.DragonRiders,

				new Kit[]
						{
				new KitRider(manager)
						},

						new String[]
								{
				
								});
		
		this.Damage = false;
		this.HungerSet = 20;
	}
}
