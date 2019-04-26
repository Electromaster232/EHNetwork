package nautilus.game.minekart.item.use_default;

import java.util.ArrayList;

import nautilus.game.minekart.item.KartItemEntity;
import nautilus.game.minekart.item.KartItemManager;
import nautilus.game.minekart.item.use_active.ActiveStandard;
import nautilus.game.minekart.item.world_items_default.Banana;
import nautilus.game.minekart.kart.Kart;
import nautilus.game.minekart.kart.KartUtil;

public class UseBanana extends ItemUse
{
	@Override
	public void Use(KartItemManager manager, Kart kart) 
	{
		//Auto-Trail = Dont do this 
		//kart.SetItemStored(null);
		
		ArrayList<KartItemEntity> ents = new ArrayList<KartItemEntity>();
		
		ents.add(new Banana(manager, kart, KartUtil.GetBehind(kart)));
		
		new ActiveStandard(manager, kart, ents);	
	}
}
