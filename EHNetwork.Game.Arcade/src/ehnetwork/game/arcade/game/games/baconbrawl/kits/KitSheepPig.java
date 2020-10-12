package ehnetwork.game.arcade.game.games.baconbrawl.kits;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.SheepDisguise;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.kit.Kit;
import ehnetwork.game.arcade.kit.KitAvailability;
import ehnetwork.game.arcade.kit.Perk;
import ehnetwork.game.arcade.kit.perks.PerkBackstabKnockback;
import ehnetwork.game.arcade.kit.perks.PerkPigCloak;

public class KitSheepPig extends Kit
{
	public KitSheepPig(ArcadeManager manager)
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
		Disguise d1 = Manager.GetDisguise().createDisguise(EntityType.SHEEP);
		SheepDisguise d2 = (SheepDisguise) d1;
		d2.setColor(DyeColor.PINK);
		d2.setCustomNameVisible(true);
		d2.setCustomName(C.cYellow + player.getName());
		Manager.GetDisguise().applyDisguise(d2, player);

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
