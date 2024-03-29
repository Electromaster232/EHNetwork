package ehnetwork.game.microgames.game.games.baconbrawl.kits;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.disguise.disguises.DisguiseSheep;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.microgames.MicroGamesManager;
import ehnetwork.game.microgames.kit.Kit;
import ehnetwork.game.microgames.kit.KitAvailability;
import ehnetwork.game.microgames.kit.Perk;
import ehnetwork.game.microgames.kit.perks.PerkBackstabKnockback;
import ehnetwork.game.microgames.kit.perks.PerkPigCloak;

public class KitSheepPig extends Kit
{
	public KitSheepPig(MicroGamesManager manager)
	{
		super(manager, "Pig", KitAvailability.Gem, 5000,

				new String[] 
						{
				"\"...Oink?\""
						}, 

						new Perk[] 
								{
				new PerkPigCloak(),
				new PerkBackstabKnockback()
								}, 
								EntityType.SHEEP,
								new ItemStack(Material.WOOL));
	}

	@Override
	public void GiveItems(Player player) 
	{
		player.getInventory().addItem(ItemStackFactory.Instance.CreateStack(Material.IRON_AXE));

		//Disguise
		DisguiseSheep disguise = new DisguiseSheep(player);
		disguise.setName(C.cYellow + player.getName());
		disguise.setCustomNameVisible(false);
		disguise.setColor(DyeColor.PINK);
		Manager.GetDisguise().disguise(disguise);
	}
	
	@Override
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

		if (type == EntityType.SHEEP)
		{
			Sheep sheep = (Sheep)entity;
			sheep.setColor(DyeColor.PINK);
		}

		UtilEnt.Vegetate(entity);

		SpawnCustom(entity); 

		return entity;
	}
}
