package nautilus.game.arcade.kit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import mineplex.core.achievement.Achievement;
import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilInv;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import nautilus.game.arcade.ArcadeFormat;
import nautilus.game.arcade.ArcadeManager;
import nautilus.game.arcade.events.PlayerKitGiveEvent;

public abstract class Kit implements Listener
{
	public ArcadeManager Manager;

	private String _kitName;
	private String[] _kitDesc;
	private KitAvailability _kitAvailability;
	private int _cost;
	
	private Perk[] _kitPerks;
	
	protected EntityType _entityType;
	protected ItemStack _itemInHand;
	
	protected Material _displayItem;
	
	protected Achievement[] _achivementCategory;

	public Kit(ArcadeManager manager, String name, KitAvailability kitAvailability, String[] kitDesc, Perk[] kitPerks, EntityType entityType, ItemStack itemInHand)
	{
		this(manager, name, kitAvailability, 2000, kitDesc, kitPerks, entityType, itemInHand);
	}

	public Kit(ArcadeManager manager, String name, KitAvailability kitAvailability, int cost, String[] kitDesc, Perk[] kitPerks, EntityType entityType, ItemStack itemInHand)
	{
		Manager = manager;

		_kitName = name;
		_kitDesc = kitDesc;
		_kitPerks = kitPerks;

		for (Perk perk : _kitPerks)
			perk.SetHost(this);
		
		_kitAvailability = kitAvailability;
		_cost = cost;
		
		_entityType = entityType;
		_itemInHand = itemInHand;
		
		_displayItem = Material.BOOK;
		if (itemInHand != null)
			_displayItem = itemInHand.getType();
	}

	public String GetFormattedName()
	{
		return GetAvailability().GetColor() + "§l" + _kitName;
	}
	
	public String GetName()
	{	
		return _kitName;
	}

	public ItemStack GetItemInHand()
	{
		return _itemInHand;
	}
	
	public KitAvailability GetAvailability()
	{
		return _kitAvailability;
	}
	
	public String[] GetDesc()
	{
		return _kitDesc;
	}
	
	public Perk[] GetPerks()
	{
		return _kitPerks;
	}

	public boolean HasKit(Player player)
	{
		if (Manager.GetGame() == null)
			return false;

		return Manager.GetGame().HasKit(player, this);
	}	
	
	public void ApplyKit(Player player)
	{
		UtilInv.Clear(player);
		
		for (Perk perk : _kitPerks)
			perk.Apply(player);
		
		GiveItemsCall(player);
		
		UtilInv.Update(player);
	}
	
	public void GiveItemsCall(Player player)
	{
		GiveItems(player);
		
		//Event
		PlayerKitGiveEvent kitEvent = new PlayerKitGiveEvent(Manager.GetGame(), this, player);
		UtilServer.getServer().getPluginManager().callEvent(kitEvent);
	}
	
	public abstract void GiveItems(Player player);
	
	public Entity SpawnEntity(Location loc)
	{
		EntityType type = _entityType;
		if (type == EntityType.PLAYER)
			type = EntityType.ZOMBIE;
			
		
		LivingEntity entity = (LivingEntity) Manager.GetCreature().SpawnEntity(loc, type);

		entity.setRemoveWhenFarAway(false);
		entity.setCustomName(GetAvailability().GetColor() + GetName() + " Kit");
		entity.setCustomNameVisible(true);
		entity.getEquipment().setItemInHand(_itemInHand);
		
		if (type == EntityType.SKELETON && (GetName().contains("Wither") || GetName().contains("Alpha")))
		{
			Skeleton skel = (Skeleton)entity;
			skel.setSkeletonType(SkeletonType.WITHER);
		}

		UtilEnt.Vegetate(entity);
		UtilEnt.silence(entity, true);
		UtilEnt.ghost(entity, true, false);

		SpawnCustom(entity); 

		return entity;
	}

	public void SpawnCustom(LivingEntity ent) { }

	public void DisplayDesc(Player player) 
	{
		for (int i=0 ; i<3 ; i++)
			UtilPlayer.message(player, "");
		
		UtilPlayer.message(player, ArcadeFormat.Line);

		UtilPlayer.message(player, "§aKit - §f§l" + GetName());
		
		//Desc
		for (String line : GetDesc())
		{
			UtilPlayer.message(player, C.cGray + "  " + line);
		}

		//Perk Descs
		for (Perk perk : GetPerks())
		{
			if (!perk.IsVisible())
				continue;
			
			UtilPlayer.message(player, "");
			UtilPlayer.message(player, C.cWhite + C.Bold + perk.GetName());
			for (String line : perk.GetDesc())
			{
				UtilPlayer.message(player, C.cGray + "  " + line);
			}
		}
		
		UtilPlayer.message(player, ArcadeFormat.Line);

	}

	public int GetCost() 
	{
		return _cost;
	}

	public Material getDisplayMaterial()
	{
		return _displayItem;
	}

	public void Deselected(Player player) { }
	
	public void Selected(Player player) { }

	public void setEntityType(EntityType entityType)
	{
		_entityType = entityType;
	}
	
	public void setAchievementRequirements(Achievement[] category)
	{
		_achivementCategory = category;
	}
	
	public Achievement[] getAchievementRequirement()
	{
		return _achivementCategory;
	}
	
	public boolean hasKitsUnlocked(Player player)
	{
		return Manager.hasKitsUnlocked(player);
	}
}
