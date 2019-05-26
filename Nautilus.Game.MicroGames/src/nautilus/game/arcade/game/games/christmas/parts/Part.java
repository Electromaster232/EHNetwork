package nautilus.game.arcade.game.games.christmas.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilAlg;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilTextTop;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import nautilus.game.arcade.game.games.christmas.Christmas;
import nautilus.game.arcade.game.games.christmas.ChristmasAudio;
import net.minecraft.server.v1_7_R4.EntityCreature;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Part implements Listener
{	
	public Christmas Host;
	protected Location _sleigh;
	protected Location[] _presents;

	protected boolean _presentsAnnounce = false;

	protected String _objective = "Follow Santa";
	protected double _objectiveHealth = 1;

	protected HashMap<Creature, Player> _creatures = new HashMap<Creature, Player>();

	public Part(Christmas host, Location sleigh, Location[] presents)
	{
		Host = host;
		_sleigh = sleigh;
		_presents = presents;
		
		for (Location loc : _presents)
			loc.getBlock().setType(Material.AIR);
	}

	public void Prepare()
	{
		Activate();

		for (Location loc : _presents)
		{
			Block present = loc.getBlock();
			present.setTypeIdAndData(Material.SKULL.getId(), (byte)1, true);

			Skull skull = (Skull) present.getState();
			skull.setSkullType(SkullType.PLAYER);
			
			//Present Type
			double r = Math.random();
			if (r > 0.75)		skull.setOwner("CruXXx");
			else if (r > 0.5)	skull.setOwner("CruXXx");
			else if (r > 0.25)	skull.setOwner("CruXXx");
			else 				skull.setOwner("CruXXx");
			
			//Angle
			BlockFace face = BlockFace.UP;
			while (face == BlockFace.UP || face == BlockFace.DOWN || face == BlockFace.SELF)
				face = BlockFace.values()[UtilMath.r(BlockFace.values().length)];
			skull.setRotation(face);

			skull.update();
			
			//Beacon
			present.getRelative(BlockFace.DOWN).setType(Material.BEACON);
			for (int x=-1 ; x<=1 ; x++)
				for (int z=-1 ; z<=1 ; z++)
					present.getRelative(x, -2, z).setType(Material.IRON_BLOCK);
		}
			
	}

	public abstract void Activate();

	public boolean IsDone() 
	{
		if (CanFinish())
		{
			if (HasPresents())
			{
				//Deregister
				HandlerList.unregisterAll(this);

				//Clean
				KillCreatures();

				SetObjectiveText("Follow Santa", 1);
				
				if (!(this instanceof Part5))
				{
					if (Math.random() > 0.5)
						Host.SantaSay("Let's go!", ChristmasAudio.GENERAL_LETS_GO);
					else
						Host.SantaSay("Follow me!", ChristmasAudio.GENERAL_FOLLOW_ME);
				}
				
				return true;
			}
			else if (!_presentsAnnounce)
			{
				SetObjectivePresents();
				
				if (!(this instanceof Part5))
				{
					Host.SantaSay("Collect the presents!", ChristmasAudio.GENERAL_COLLECT_PRESENTS);
				}
				
				_presentsAnnounce = true;
			}
		}

		return false;
	}

	public void SetObjectivePresents()
	{
		SetObjectiveText("Collect the two Presents", 1);
	}

	public abstract boolean CanFinish();

	public Location GetSleighWaypoint() 
	{
		return _sleigh;
	}

	public Location[] GetPresents()
	{
		return _presents;
	}

	public boolean HasPresents()
	{
		for (Location loc : _presents)
			if (!Host.GetSleigh().HasPresent(loc.getBlock().getLocation()))
				return false;

		return true;
	}

	@EventHandler
	public void PresentCollect(PlayerInteractEvent event)
	{
		if (event.getClickedBlock() == null)
			return;

		boolean contains = false;
		for (Location loc : _presents)
			if (loc.getBlock().equals(event.getClickedBlock()))
				contains = true;

		if (!contains)
			return;

		event.setCancelled(true);

		if (!Host.IsLive())
			return;

		if (!Host.IsAlive(event.getPlayer()))
			return;

		if (Host.GetSleigh().HasPresent(event.getClickedBlock().getLocation()))
			return;
		
		if (UtilMath.offset(event.getPlayer().getLocation(), event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5)) > 2)
			return;

		Host.GetSleigh().AddPresent(event.getClickedBlock().getLocation());

		Host.SantaSay("Well done " + event.getPlayer().getName() + "! You collected a present!", null);
	}

	public void SetObjectiveText(String text, double percent)
	{
		_objective = text;
		_objectiveHealth = percent;
	}

	@EventHandler
	public void ObjectiveDisplay(UpdateEvent event)
	{
		if (!Host.InProgress())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		UtilTextTop.displayProgress(C.cYellow + C.Bold + _objective, _objectiveHealth, UtilServer.getPlayers());
	}

	public void AddCreature(Creature ent)
	{
		_creatures.put(ent, null);
	}
	
	public HashMap<Creature, Player> GetCreatures()
	{
		return _creatures;
	}

	@EventHandler
	public void UpdateCreatures(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;

		Iterator<Creature> entIterator = _creatures.keySet().iterator();

		//Move & Die
		while (entIterator.hasNext())
		{
			Creature ent = entIterator.next();

			//Get Target
			Player target = _creatures.get(ent);
			if (target == null || !target.isValid() || !Host.IsAlive(target))
			{
				if (Host.GetPlayers(true).size() > 0)
				{
					target = UtilAlg.Random(Host.GetPlayers(true));
					_creatures.put(ent, target);
				}
				else
				{
					continue;
				}
			}
				
			//Speed
			float speed = 1f;
			if (ent instanceof Ageable)
			{
				if (!((Ageable)ent).isAdult())
					speed = 0.6f;
			}
			
			//Move
			EntityCreature ec = ((CraftCreature)ent).getHandle();
			
			ec.getControllerMove().a(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), speed);

			//Remove
			if (!ent.isValid())
			{
				ent.remove();
				entIterator.remove();
			}
		}
	}

	public void KillCreatures()
	{
		//for (Creature ent : _creatures.keySet())
		//	ent.damage(5000);

		_creatures.clear();
	}

}
