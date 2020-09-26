package ehnetwork.minecraft.game.classcombat.Skill.Mage;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilGear;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.classcombat.Skill.event.SkillTriggerEvent;

public class LifeBonds extends Skill
{
	private HashSet<Player> _active = new HashSet<Player>();
	private HashSet<Item> _items = new HashSet<Item>();
	
	private HashSet<LifeBondsData> _hearts = new HashSet<LifeBondsData>();

	public LifeBonds(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
	{
		super(skills, name, classType, skillType, cost, levels);

		SetDesc(new String[] 
				{
				"Drop Axe/Sword to Toggle.",
				"",
				"Transfers life from healthy allies",
				"to nearby allies with less health.",
				"",
				"Transfers #0.5#0.5 health every second.",
				"Maximum range of #3#3 Blocks from user."
				});
	}

	@Override
	public String GetEnergyString()
	{
		return "Energy: #4.5#-0.5 per Second";
	}

	@EventHandler
	public void Toggle(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();

		if (getLevel(player) == 0)				
			return;

		if (!UtilGear.isWeapon(event.getItemDrop().getItemStack()))
			return;

		event.setCancelled(true);

		//Check Allowed
		SkillTriggerEvent trigger = new SkillTriggerEvent(player, GetName(), GetClassType());
		UtilServer.getServer().getPluginManager().callEvent(trigger);
		if (trigger.IsCancelled())
			return;

		if (_active.contains(player))
		{
			Remove(player);	
		}
		else
		{
			if (!Factory.Energy().Use(player, "Enable " + GetName(), 10, true, true))
				return;

			Add(player);
		}
	}

	public void Add(Player player)
	{
		_active.add(player);
		UtilPlayer.message(player, F.main(GetClassType().name(), GetName() + ": " + F.oo("Enabled", true)));
	}

	public void Remove(Player player)
	{
		_active.remove(player);
		UtilPlayer.message(player, F.main(GetClassType().name(), GetName() + ": " + F.oo("Disabled", false)));
	}

	@EventHandler
	public void Energy(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		for (Player cur : GetUsers())
		{	
			if (!_active.contains(cur))
				continue;

			//Level
			int level = getLevel(cur);
			if (level == 0)
			{
				Remove(cur);	
				continue;
			}

			//Check Allowed
			SkillTriggerEvent trigger = new SkillTriggerEvent(cur, GetName(), GetClassType());
			UtilServer.getServer().getPluginManager().callEvent(trigger);
			if (trigger.IsCancelled())
			{
				Remove(cur);
				continue;
			}

			//Energy
			if (!Factory.Energy().Use(cur, GetName(), 0.225 - (level * 0.025), true, true))
			{
				_active.remove(cur);
				UtilPlayer.message(cur, F.main(GetClassType().name(), GetName() + ": " + F.oo("Disabled", false)));
				continue;
			}
		}
	}

	@EventHandler
	public void Plants(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;

		for (Player cur : GetUsers())
		{	
			if (!_active.contains(cur))
				continue;

			for (Player other : UtilPlayer.getNearby(cur.getLocation(), 8))
			{
				if (Factory.Relation().canHurt(cur, other) && !other.equals(cur))
					continue;

				//Plants
				ItemStack stack;

				double r = Math.random();

				if (r > 0.4)		stack = ItemStackFactory.Instance.CreateStack(31, (byte)1);
				else if (r > 0.11)	stack = ItemStackFactory.Instance.CreateStack(31, (byte)2);
				else if (r > 0.05)	stack = ItemStackFactory.Instance.CreateStack(37, (byte)0);
				else				stack = ItemStackFactory.Instance.CreateStack(38, (byte)0);

				Item item = other.getWorld().dropItem(other.getLocation().add(0, 0.4, 0), stack);
				_items.add(item);

				Vector vec = new Vector(Math.random() - 0.5, Math.random()/2 + 0.2, Math.random() - 0.5).normalize();
				vec.multiply(0.1 + Math.random()/8);
				item.setVelocity(vec);
			}
		}
	}

	@EventHandler
	public void LifeTransfer(UpdateEvent event)
	{
		if (event.getType() != UpdateType.SEC)
			return;

		for (Player cur : GetUsers())
		{	
			if (!_active.contains(cur))
				continue;
			
			int level = getLevel(cur);
			
			//Bonds
			Player highest = null;
			double highestHp = 0;

			Player lowest = null;
			double lowestHp = 20;

			for (Player other : UtilPlayer.getNearby(cur.getLocation(), 6 + (2 * level)))
			{
				if (Factory.Relation().canHurt(cur, other) && !other.equals(cur))
					continue;

				if (highest == null || other.getHealth() > highestHp)
				{
					highest = other;
					highestHp = other.getHealth();
				}

				if (lowest == null || other.getHealth() < lowestHp)
				{
					lowest = other;
					lowestHp = other.getHealth();
				}
			}
			
			//Nothing to Transfer
			if (highest == null || lowest == null || highest.equals(lowest) || highestHp - lowestHp < 2)
				continue;

			double amount = 0.5 + (0.5 * level);
			
			amount = Math.min((double)(highestHp - lowestHp) / 2d, amount);
			
			//Steal
			UtilPlayer.health(highest, -amount);

			//Hearts
			_hearts.add(new LifeBondsData(highest.getLocation().add(0, 0.8, 0), lowest, amount));
			
			//Effect
			//highest.getWorld().playEffect(highest.getLocation(), Effect.STEP_SOUND, 18);
			//lowest.getWorld().playEffect(lowest.getLocation(), Effect.STEP_SOUND, 18);
		}
	}
	
	@EventHandler
	public void Hearts(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FASTEST)
			return;
		
		Iterator<LifeBondsData> heartIterator = _hearts.iterator();
		
		while (heartIterator.hasNext())
		{
			LifeBondsData data = heartIterator.next();
			
			if (data.Update())
				heartIterator.remove();
		}
	}

	@EventHandler
	public void ItemPickup(PlayerPickupItemEvent event)
	{
		if (_items.contains(event.getItem()))
			event.setCancelled(true);
	}

	@EventHandler
	public void HopperPickup(InventoryPickupItemEvent event)
	{
		if (_items.contains(event.getItem()))
			event.setCancelled(true);
	}

	@EventHandler
	public void ItemDestroy(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		if (_items.isEmpty())
			return;

		HashSet<Item> remove = new HashSet<Item>();

		for (Item cur : _items)
			if (UtilEnt.isGrounded(cur) || cur.getTicksLived() > 400 || cur.isDead() || !cur.isValid())
				remove.add(cur);

		for (Item cur : remove)
		{
			Block block = cur.getLocation().getBlock();
			if (block.getTypeId() == 0)
			{
				int below = block.getRelative(BlockFace.DOWN).getTypeId();
				if (below == 2 || below == 3)
				{
					byte data = 0;
					if (cur.getItemStack().getData() != null)
						data = cur.getItemStack().getData().getData();

					Factory.BlockRestore().Add(block, cur.getItemStack().getTypeId(), data, 2000);
				}
			}

			_items.remove(cur);
			cur.remove();
		}
	}

	@Override
	public void Reset(Player player) 
	{
		_active.remove(player);
	}
}
