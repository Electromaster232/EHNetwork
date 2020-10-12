package ehnetwork.game.arcade.game.games.dragonriders;

import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.SoloGame;
import ehnetwork.game.arcade.game.games.dragonriders.kits.KitRider;
import ehnetwork.game.arcade.kit.Kit;

public class DragonRiders extends SoloGame
{
	public DragonRiders(ArcadeManager manager) 
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
