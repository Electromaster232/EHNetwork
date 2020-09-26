package ehnetwork.game.microgames.game.games.christmas;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAction;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.recharge.Recharge;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Sleigh 
{
	public Christmas Host;

	//This is the central entity, all other sleigh entities have location relative to this.
	private Entity CentralEntity;
	private ArrayList<SleighPart> SleighEnts;
	private ArrayList<SleighHorse> SleighHorses = new ArrayList<SleighHorse>();

	private ArrayList<SleighPart> PresentSlots;

	private ArrayList<Location> PresentsCollected = new ArrayList<Location>();;

	private Location Target;

	public void setupSleigh(Christmas host, Location loc)
	{
		Host = host;

		Host.CreatureAllowOverride = true;

		Target = loc.clone();

		CentralEntity = loc.getWorld().spawn(loc, Chicken.class);
		UtilEnt.Vegetate(CentralEntity, true);
		UtilEnt.ghost(CentralEntity, true, true);

		//Presents
		PresentSlots = new ArrayList<SleighPart>();

		PresentSlots.add(new SleighPart(2, 0, 0, loc.clone(), -1, -2));
		PresentSlots.add(new SleighPart(2, 0, 0, loc.clone(), 0, -2));
		PresentSlots.add(new SleighPart(2, 0, 0, loc.clone(), 1, -2));
		PresentSlots.add(new SleighPart(2, 0, 0, loc.clone(), -1, -1));
		PresentSlots.add(new SleighPart(2, 0, 0, loc.clone(), 0, -1));
		PresentSlots.add(new SleighPart(2, 0, 0, loc.clone(), 1, -1));

		PresentSlots.add(new SleighPart(6, 0, 0, loc.clone(), -1, -2));
		PresentSlots.add(new SleighPart(6, 0, 0, loc.clone(), 0, -2));
		PresentSlots.add(new SleighPart(6, 0, 0, loc.clone(), 1, -2));
		PresentSlots.add(new SleighPart(6, 0, 0, loc.clone(), -1, -1));
		PresentSlots.add(new SleighPart(6, 0, 0, loc.clone(), 0, -1));
		PresentSlots.add(new SleighPart(6, 0, 0, loc.clone(), 1, -1));

		//Sleigh
		SleighEnts = new ArrayList<SleighPart>();

		for (SleighPart part : PresentSlots)
			SleighEnts.add(part);

		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 0, -3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -1, -3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -2, -3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 1, -3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 2, -3));

		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -2, -2));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), -1, -2));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 0, -2));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 1, -2));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 2, -2));

		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -2, -1));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), -1, -1));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 0, -1));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 1, -1));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 2, -1));


		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -2, 0));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -1, 0));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 0, 0));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 1, 0));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 2, 0));

		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -2, 1));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), -1, 1));
		SleighEnts.add(new SleighPart(0, 159, 15, loc.clone(), 0, 1));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 1, 1));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 2, 1));

		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), -2, 2));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), -1, 2));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 0, 2));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 1, 2));
		SleighEnts.add(new SleighPart(0, 44, 7, loc.clone(), 2, 2));

		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -2, 3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), -1, 3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 0, 3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 1, 3));
		SleighEnts.add(new SleighPart(0, 159, 14, loc.clone(), 2, 3));
		
		//Santa
		SleighPart santa = new SleighPart(3, 0, 0, loc.clone(), 0, 1);
		santa.AddSanta();
		SleighEnts.add(santa);

		SleighHorses.add(new SleighHorse(loc.clone(), -1.5, 8));
		SleighHorses.add(new SleighHorse(loc.clone(), 1.5, 8));

		SleighHorses.add(new SleighHorse(loc.clone(), -1.5, 11));
		SleighHorses.add(new SleighHorse(loc.clone(), 1.5, 11));
		
		for (SleighHorse horse : SleighHorses) 
		    horse.spawnHorse();
		
		for (SleighHorse horse : SleighHorses)
			UtilEnt.Leash(horse.Ent, santa.GetTop(), false, false);
	}

	public Location GetLocation()
	{
		return CentralEntity.getLocation();
	}

	public void SetTarget(Location loc)
	{
		Target = loc;
	}

	public void Update()
	{
		Bump();

		if (Target == null)
			return;

		Move(CentralEntity, Target, 1);

		//Move Sleigh
		for (SleighPart part : SleighEnts)
		{
			part.RefreshBlocks();

			if (Move(part.Ent, CentralEntity.getLocation().add(part.OffsetX, 0, part.OffsetZ), 1.4))
				if (part.OffsetZ == -3 || Math.abs(part.OffsetX) == 2)
					if (Math.random() > 0.95)
						part.Ent.getWorld().playEffect(part.Ent.getLocation().subtract(0, 1, 0), Effect.STEP_SOUND, 80);
		}

		//Move Horses
		for (SleighHorse ent : SleighHorses)
		{
			Move(ent.Ent, CentralEntity.getLocation().add(ent.OffsetX, 0, ent.OffsetZ), 1.4);
		}
	}

	public boolean Move(Entity ent, Location target, double speed)
	{
		return UtilEnt.CreatureMoveFast(ent, target, (float)speed);
	}

	public void Bump()
	{
		for (Player player : Host.GetPlayers(true))
		{
			if (!Recharge.Instance.usable(player, "Sleigh Bump"))
				continue;

			for (SleighPart part : SleighEnts)
				if (UtilMath.offset(player, part.Ent) < 1)
				{
					UtilAction.velocity(player, UtilAlg.getTrajectory2d(CentralEntity, player), 0.4, true, 0.2, 0, 0, true);
					Recharge.Instance.useForce(player, "Sleigh Bump", 400);
				}


			for (SleighHorse part : SleighHorses)
				if (UtilMath.offset(player, part.Ent) < 1)
				{
					UtilAction.velocity(player, UtilAlg.getTrajectory2d(CentralEntity, player), 0.4, true, 0.2, 0, 0, true);
					Recharge.Instance.useForce(player, "Sleigh Bump", 400);
				}
			
			if (player.getLocation().getZ() < CentralEntity.getLocation().getZ() - 24)
			{
				player.damage(1);
				UtilPlayer.message(player, C.cRed + C.Bold + "Santa: " + ChatColor.RESET + "Careful " + player.getName() + "! Keep up with my Sleigh!");
				
				UtilAction.velocity(player, UtilAlg.getTrajectory2d(player, CentralEntity), 0.6, true, 0.2, 0, 0, true);
				Recharge.Instance.useForce(player, "Sleigh Bump", 400);
			}
		}
	}

	public boolean HasPresent(Location loc)
	{
		return PresentsCollected.contains(loc);
	}

	public void AddPresent(Location loc)
	{
		PresentsCollected.add(loc);
		loc.getBlock().setType(Material.AIR);
		loc.getBlock().getRelative(BlockFace.DOWN).setType(Material.GLASS);
		UtilFirework.launchFirework(loc.clone().add(0.5, 0.5, 0.5), FireworkEffect.builder().flicker(false).withColor(Color.YELLOW).with(Type.BALL).trail(true).build(), new Vector(0,1,0), 0);

		SleighPart part = PresentSlots.remove(0);
		if (part == null)			return;

		part.SetPresent();
	}

	public ArrayList<Location> GetPresents() 
	{
		return PresentsCollected;
	}
	
	public void Damage(CustomDamageEvent event)
	{
		if (event.GetDamageeEntity().equals(CentralEntity))
		{
			event.SetCancelled("Sleigh Damage");
			return;
		}
		
		for (SleighPart part : SleighEnts)
		{
			if (part.HasEntity(event.GetDamageeEntity()))
			{
				event.SetCancelled("Sleigh Damage");
				return;
			}	
		}
		
		for (SleighHorse part : SleighHorses)
		{
			if (part.HasEntity(event.GetDamageeEntity()))
			{
				event.SetCancelled("Sleigh Damage");
				return;
			}	
		}
	}

    public ArrayList<SleighHorse> getHorses()
    {
        return SleighHorses;
    }
}
