package mineplex.core.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.server.v1_7_R4.EntityBat;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityEnderDragon;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.Navigation;
import net.minecraft.server.v1_7_R4.PathfinderGoal;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

public class UtilEnt
{

	//Custom Entity Names
	private static HashMap<Entity, String> _nameMap = new HashMap<Entity, String>();
	private static HashMap<String, EntityType> creatureMap = new HashMap<String, EntityType>();
	
	private static Field _goalSelector;
	private static Field _targetSelector;
	private static Field _bsRestrictionGoal;
	private static Field _pathfinderBList;
	private static Field _pathfinderCList;
	
	public static HashMap<Entity, String> GetEntityNames() 
	{
		return _nameMap;
	}
	
	public static void silence(Entity entity, boolean silence)
	{
		((CraftEntity)entity).getHandle().Silent = silence;
	}
	
	public static void ghost(Entity entity, boolean ghost, boolean invisible)
	{
		if (entity instanceof LivingEntity)
		{
			((CraftLivingEntity)entity).getHandle().ghost = ghost;
		}
		
		((CraftEntity)entity).getHandle().Invisible = invisible;
		((CraftEntity)entity).getHandle().setInvisible(invisible);
	}
	
	public static void Leash(LivingEntity leashed, Entity holder, boolean pull, boolean breakable)
	{
		if (((CraftEntity)leashed).getHandle() instanceof EntityInsentient)
		{
			EntityInsentient creature = (EntityInsentient)((CraftEntity)leashed).getHandle();
			
			creature.PullWhileLeashed = pull;
			creature.BreakLeash = breakable;
		}

		leashed.setLeashHolder(holder);
	}

	public static void addLookAtPlayerAI(Entity entity, float dist)
	{
		if (((CraftEntity) entity).getHandle() instanceof EntityInsentient)
		{
			addAI(entity, 7, new PathfinderGoalLookAtPlayer(((EntityInsentient) ((CraftEntity) entity).getHandle()), EntityHuman.class, dist));
			addAI(entity, 8, new PathfinderGoalRandomLookaround(((EntityInsentient) ((CraftEntity) entity).getHandle())));
		}
	}

	public static void addAI(Entity entity, int value, PathfinderGoal ai)
	{
		if (((CraftEntity) entity).getHandle() instanceof EntityInsentient)
		{
			EntityInsentient ei = ((EntityInsentient) ((CraftEntity) entity).getHandle());

			if (_goalSelector == null)
			{
				try
				{
					_goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
				}
				catch (NoSuchFieldException e)
				{
					e.printStackTrace();
					return;
				}
				_goalSelector.setAccessible(true);
			}

			try
			{
				((PathfinderGoalSelector) _goalSelector.get(ei)).a(value, ai);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

	}
	
	public static void Vegetate(Entity entity)
	{
		Vegetate(entity, false);
	}
	
	public static void Vegetate(Entity entity, boolean mute)
	{
    	try
		{
    		if (_goalSelector == null)
    		{
				_goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
				_goalSelector.setAccessible(true);
    		}
    		
    		if (_targetSelector == null)
    		{
				_targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
				_targetSelector.setAccessible(true);
    		}
    		
    		if (_pathfinderBList == null)
    		{
    			_pathfinderBList = PathfinderGoalSelector.class.getDeclaredField("b");
    			_pathfinderBList.setAccessible(true);
    		}
    		
    		if (_pathfinderCList == null)
    		{
    			_pathfinderCList = PathfinderGoalSelector.class.getDeclaredField("c");
    			_pathfinderCList.setAccessible(true);
    		}
    		
    		if (entity instanceof CraftCreature)
    		{
    			EntityCreature creature = ((CraftCreature)entity).getHandle();
    			
	    		if (_bsRestrictionGoal == null)
	    		{
					_bsRestrictionGoal = EntityCreature.class.getDeclaredField("bs");
					_bsRestrictionGoal.setAccessible(true);
	    		}
	    		
	    		_bsRestrictionGoal.set(creature, new PathfinderGoalMoveTowardsRestriction(creature, 0D));
    		}
        	
    		if (((CraftEntity)entity).getHandle() instanceof EntityInsentient)
    		{
    			EntityInsentient creature = (EntityInsentient)((CraftEntity)entity).getHandle();
		        
    			creature.Vegetated = true;
    			creature.Silent = mute;
    			
    			((List)_pathfinderBList.get(((PathfinderGoalSelector)_goalSelector.get(creature)))).clear();
    			((List)_pathfinderCList.get(((PathfinderGoalSelector)_goalSelector.get(creature)))).clear();
    			
    			((List)_pathfinderBList.get(((PathfinderGoalSelector)_targetSelector.get(creature)))).clear();
    			((List)_pathfinderCList.get(((PathfinderGoalSelector)_targetSelector.get(creature)))).clear();
    		}
    		
    		if (((CraftEntity)entity).getHandle() instanceof EntityBat)
    		{
    			((EntityBat)((CraftEntity)entity).getHandle()).Vegetated = true;
    		}
	    	
    		if (((CraftEntity)entity).getHandle() instanceof EntityEnderDragon)
    		{
    			EntityEnderDragon creature = (EntityEnderDragon)((CraftEntity)entity).getHandle();
		        
    			creature.Vegetated = true;
    		}
		} 
    	catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} 
    	catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} 
    	catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} 
    	catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void removeGoalSelectors(Entity entity)
	{
    	try
		{
    		if (_goalSelector == null)
    		{
				_goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
				_goalSelector.setAccessible(true);
    		}
        	
    		if (((CraftEntity)entity).getHandle() instanceof EntityInsentient)
    		{
    			EntityInsentient creature = (EntityInsentient)((CraftEntity)entity).getHandle();
		        
		    	PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(((CraftWorld)entity.getWorld()).getHandle().methodProfiler);
		    	
		    	_goalSelector.set(creature, goalSelector);
    		}
		} 
    	catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} 
    	catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} 
    	catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} 
    	catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void populate()
	{
		if (creatureMap.isEmpty())
		{
			creatureMap.put("Bat", EntityType.BAT);
			creatureMap.put("Blaze", EntityType.BLAZE);
			creatureMap.put("Arrow", EntityType.ARROW);
			creatureMap.put("Cave Spider", EntityType.CAVE_SPIDER);
			creatureMap.put("Chicken", EntityType.CHICKEN);
			creatureMap.put("Cow", EntityType.COW);
			creatureMap.put("Creeper", EntityType.CREEPER);
			creatureMap.put("Ender Dragon", EntityType.ENDER_DRAGON);
			creatureMap.put("Enderman", EntityType.ENDERMAN);
			creatureMap.put("Ghast", EntityType.GHAST);
			creatureMap.put("Giant", EntityType.GIANT);
			creatureMap.put("Horse", EntityType.HORSE);
			creatureMap.put("Iron Golem", EntityType.IRON_GOLEM);
			creatureMap.put("Item", EntityType.DROPPED_ITEM);
			creatureMap.put("Magma Cube", EntityType.MAGMA_CUBE);
			creatureMap.put("Mooshroom", EntityType.MUSHROOM_COW);
			creatureMap.put("Ocelot", EntityType.OCELOT);
			creatureMap.put("Pig", EntityType.PIG);
			creatureMap.put("Pig Zombie", EntityType.PIG_ZOMBIE);
			creatureMap.put("Sheep", EntityType.SHEEP);
			creatureMap.put("Silverfish", EntityType.SILVERFISH);
			creatureMap.put("Skeleton", EntityType.SKELETON);
			creatureMap.put("Slime", EntityType.SLIME);
			creatureMap.put("Snowman", EntityType.SNOWMAN);
			creatureMap.put("Spider", EntityType.SPIDER);
			creatureMap.put("Squid", EntityType.SQUID);
			creatureMap.put("Villager", EntityType.VILLAGER);
			creatureMap.put("Witch", EntityType.WITCH);
			creatureMap.put("Wither", EntityType.WITHER);
			creatureMap.put("WitherSkull", EntityType.WITHER_SKULL);
			creatureMap.put("Wolf", EntityType.WOLF);
			creatureMap.put("Zombie", EntityType.ZOMBIE);
			
			creatureMap.put("Item", EntityType.DROPPED_ITEM);
		}
	}
	
	public static String getName(Entity ent)
	{
		if (ent == null)
			return "Null";
		
		if (ent.getType() == EntityType.PLAYER)
			return ((Player)ent).getName();
		
		if (GetEntityNames().containsKey(ent))
			return GetEntityNames().get(ent);
		
		if (ent instanceof LivingEntity)
		{
			LivingEntity le = (LivingEntity)ent;
			if (le.getCustomName() != null)
				return le.getCustomName();
		}
		
		return getName(ent.getType());  
	}

	public static String getName(EntityType type)
	{
		for (String cur : creatureMap.keySet())
			if (creatureMap.get(cur) == type)
				return cur;

		return type.getName();
	}

	public static String searchName(Player caller, String arg, boolean inform)
	{
		populate();

		arg = arg.toLowerCase().replaceAll("_", " ");
		LinkedList<String> matchList = new LinkedList<String>();
		for (String cur : creatureMap.keySet())
		{
			if (cur.equalsIgnoreCase(arg))
				return cur;
			
			if (cur.toLowerCase().contains(arg))
				matchList.add(cur);
		}
			

		//No / Non-Unique
		if (matchList.size() != 1)
		{
			if (!inform)
				return null;

			//Inform
			UtilPlayer.message(caller, F.main("Creature Search", "" +
					C.mCount + matchList.size() +
					C.mBody + " matches for [" +
					C.mElem + arg +
					C.mBody + "]."));

			if (matchList.size() > 0)
			{
				String matchString = "";
				for (String cur : matchList)
					matchString += F.elem(cur) + ", ";
				if (matchString.length() > 1)
					matchString = matchString.substring(0 , matchString.length() - 2);

				UtilPlayer.message(caller, F.main("Creature Search", "" +
						C.mBody + "Matches [" +
						C.mElem + matchString +
						C.mBody + "]."));
			}

			return null;
		}

		return matchList.get(0);
	}

	public static EntityType searchEntity(Player caller, String arg, boolean inform)
	{
		populate();

		arg = arg.toLowerCase();
		LinkedList<EntityType> matchList = new LinkedList<EntityType>();
		for (String cur : creatureMap.keySet())
		{
			if (cur.equalsIgnoreCase(arg))
				return creatureMap.get(cur);
			
			if (cur.toLowerCase().contains(arg))
				matchList.add(creatureMap.get(cur));
		}
			

		//No / Non-Unique
		if (matchList.size() != 1)
		{
			if (!inform)
				return null;

			//Inform
			UtilPlayer.message(caller, F.main("Creature Search", "" +
					C.mCount + matchList.size() +
					C.mBody + " matches for [" +
					C.mElem + arg +
					C.mBody + "]."));

			if (matchList.size() > 0)
			{
				String matchString = "";
				for (EntityType cur : matchList)
					matchString += F.elem(cur.getName()) + ", ";
				if (matchString.length() > 1)
					matchString = matchString.substring(0 , matchString.length() - 2);

				UtilPlayer.message(caller, F.main("Creature Search", "" +
						C.mBody + "Matches [" +
						C.mElem + matchString +
						C.mBody + "]."));
			}

			return null;
		}

		return matchList.get(0);
	}
	
	public static HashMap<LivingEntity, Double> getInRadius(Location loc,	double dR) 
	{
		HashMap<LivingEntity, Double> ents = new HashMap<LivingEntity, Double>();

		for (Entity cur : loc.getWorld().getEntities())
		{
			if (!(cur instanceof LivingEntity) || UtilPlayer.isSpectator(cur))
				continue;
			
			LivingEntity ent = (LivingEntity)cur;
			
			double offset = UtilMath.offset(loc, ent.getLocation());
			
			if (offset < dR)
				ents.put(ent, 1 - (offset/dR));
		}

		return ents;
	}
	
	public static boolean hitBox(Location loc, LivingEntity ent, double mult, EntityType disguise)
	{
		if (disguise != null)
		{
			if (disguise == EntityType.SQUID)
			{
				if (UtilMath.offset(loc, ent.getLocation().add(0, 0.4, 0)) < 0.6 * mult)
						return true;
				
				return false;
			}
		}
		
		if (ent instanceof Player)
		{
			Player player = (Player)ent;
			
			if (UtilMath.offset(loc, player.getEyeLocation()) < 0.4 * mult)
			{
				return true;
			}
			else if (UtilMath.offset2d(loc, player.getLocation()) < 0.6 * mult)
			{
				if (loc.getY() > player.getLocation().getY() && loc.getY() < player.getEyeLocation().getY())
				{
					return true;
				}		
			}
		}
		else
		{
			if (ent instanceof Giant)
			{
				if (loc.getY() > ent.getLocation().getY() && loc.getY() < ent.getLocation().getY() + 12)
					if (UtilMath.offset2d(loc, ent.getLocation()) < 4)
						return true;
			}
			else
			{
				if (loc.getY() > ent.getLocation().getY() && loc.getY() < ent.getLocation().getY() + 2)
					if (UtilMath.offset2d(loc, ent.getLocation()) < 0.5 * mult)
						return true;
			}
		}
			
		

		return false;
	}
	
	public static boolean isGrounded(Entity ent) 
	{ 
		if (ent instanceof CraftEntity)
			return ((CraftEntity)ent).getHandle().onGround;

		return UtilBlock.solid(ent.getLocation().getBlock().getRelative(BlockFace.DOWN));
	}

	public static void PlayDamageSound(LivingEntity damagee) 
	{
		Sound sound = Sound.HURT_FLESH;
		
		if (damagee.getType() == EntityType.BAT)				sound = Sound.BAT_HURT;
		else if (damagee.getType() == EntityType.BLAZE)			sound = Sound.BLAZE_HIT;
		else if (damagee.getType() == EntityType.CAVE_SPIDER)	sound = Sound.SPIDER_IDLE;
		else if (damagee.getType() == EntityType.CHICKEN)		sound = Sound.CHICKEN_HURT;
		else if (damagee.getType() == EntityType.COW)			sound = Sound.COW_HURT;
		else if (damagee.getType() == EntityType.CREEPER)		sound = Sound.CREEPER_HISS;
		else if (damagee.getType() == EntityType.ENDER_DRAGON)	sound = Sound.ENDERDRAGON_GROWL;
		else if (damagee.getType() == EntityType.ENDERMAN)		sound = Sound.ENDERMAN_HIT;
		else if (damagee.getType() == EntityType.GHAST)			sound = Sound.GHAST_SCREAM;
		else if (damagee.getType() == EntityType.GIANT)			sound = Sound.ZOMBIE_HURT;
		//else if (damagee.getType() == EntityType.HORSE)		sound = Sound.
		else if (damagee.getType() == EntityType.IRON_GOLEM)	sound = Sound.IRONGOLEM_HIT;
		else if (damagee.getType() == EntityType.MAGMA_CUBE)	sound = Sound.MAGMACUBE_JUMP;
		else if (damagee.getType() == EntityType.MUSHROOM_COW)	sound = Sound.COW_HURT;
		else if (damagee.getType() == EntityType.OCELOT)		sound = Sound.CAT_MEOW;
		else if (damagee.getType() == EntityType.PIG)			sound = Sound.PIG_IDLE;
		else if (damagee.getType() == EntityType.PIG_ZOMBIE)	sound = Sound.ZOMBIE_HURT;
		else if (damagee.getType() == EntityType.SHEEP)			sound = Sound.SHEEP_IDLE;
		else if (damagee.getType() == EntityType.SILVERFISH)	sound = Sound.SILVERFISH_HIT;
		else if (damagee.getType() == EntityType.SKELETON)		sound = Sound.SKELETON_HURT;
		else if (damagee.getType() == EntityType.SLIME)			sound = Sound.SLIME_ATTACK;
		else if (damagee.getType() == EntityType.SNOWMAN)		sound = Sound.STEP_SNOW;
		else if (damagee.getType() == EntityType.SPIDER)		sound = Sound.SPIDER_IDLE;
		//else if (damagee.getType() == EntityType.SQUID)		sound = Sound;
		//else if (damagee.getType() == EntityType.VILLAGER)	sound = Sound;
		//else if (damagee.getType() == EntityType.WITCH)		sound = Sound.;
		else if (damagee.getType() == EntityType.WITHER)		sound = Sound.WITHER_HURT;
		else if (damagee.getType() == EntityType.WOLF)			sound = Sound.WOLF_HURT;
		else if (damagee.getType() == EntityType.ZOMBIE)		sound = Sound.ZOMBIE_HURT;	

		damagee.getWorld().playSound(damagee.getLocation(), sound, 1.5f + (float)(0.5f * Math.random()), 0.8f + (float)(0.4f * Math.random()));
	}

	public static boolean onBlock(Player player) 
	{
		//Side Standing
		double xMod = player.getLocation().getX() % 1;
		if (player.getLocation().getX() < 0)
			xMod += 1;
		
		double zMod = player.getLocation().getZ() % 1;
		if (player.getLocation().getZ() < 0)
			zMod += 1;

		int xMin = 0;
		int xMax = 0;
		int zMin = 0;
		int zMax = 0;
		
		if (xMod < 0.3)	xMin = -1;
		if (xMod > 0.7)	xMax = 1;
		
		if (zMod < 0.3)	zMin = -1;
		if (zMod > 0.7)	zMax = 1;

		for (int x=xMin ; x<=xMax ; x++)
		{
			for (int z=zMin ; z<=zMax ; z++)
			{				
				//Standing on SOMETHING
				if (player.getLocation().add(x, -0.5, z).getBlock().getType() != Material.AIR && !player.getLocation().add(x, -0.5, z).getBlock().isLiquid())
					return true;
				
				//Inside a Lillypad
				if (player.getLocation().add(x, 0, z).getBlock().getType() == Material.WATER_LILY)
					return true;
				
				//Fences/Walls
				Material beneath = player.getLocation().add(x, -1.5, z).getBlock().getType();
				if (player.getLocation().getY() % 0.5 == 0 &&
						(beneath == Material.FENCE || 
						beneath == Material.FENCE_GATE || 
						beneath == Material.NETHER_FENCE || 
						beneath == Material.COBBLE_WALL))
					return true;
			}	
		}
		
		return false;
	}

	public static void CreatureMove(Entity ent, Location target, float speed) 
	{
		if (!(ent instanceof Creature))
			return;
		
		if (UtilMath.offset(ent.getLocation(), target) < 0.1)
			return;
		
		EntityCreature ec = ((CraftCreature)ent).getHandle();
		Navigation nav = ec.getNavigation();
		
		if (UtilMath.offset(ent.getLocation(), target) > 16)
		{
			Location newTarget = ent.getLocation();

			newTarget.add(UtilAlg.getTrajectory(ent.getLocation(), target).multiply(16));

			nav.a(newTarget.getX(), newTarget.getY(), newTarget.getZ(), speed);
		}
		else
		{
			nav.a(target.getX(), target.getY(), target.getZ(), speed);
		}
	}
	
	public static boolean CreatureMoveFast(Entity ent, Location target, float speed) 
	{
		return CreatureMoveFast(ent, target, speed, true);
	}
	
	public static boolean CreatureMoveFast(Entity ent, Location target, float speed, boolean slow) 
	{
		if (!(ent instanceof Creature))
			return false;
		
		if (UtilMath.offset(ent.getLocation(), target) < 0.1)
			return false;
		
		if (UtilMath.offset(ent.getLocation(), target) < 2)
			speed = Math.min(speed, 1f);
		
		EntityCreature ec = ((CraftCreature)ent).getHandle();
		ec.getControllerMove().a(target.getX(), target.getY(), target.getZ(), speed);
		
		return true;
	}

	public static int getNewEntityId()
	{
		return getNewEntityId(true);
	}
	
	/**
	 * Use false if you don't want to modify the next entityid to be used.
	 * 
	 * Normally you want true if you want a unique entityid to use.
	 **/
	public static int getNewEntityId(boolean modifynumber)
    {
	    try
        {
            Field field = net.minecraft.server.v1_7_R4.Entity.class.getDeclaredField("entityCount");
            field.setAccessible(true);
            int entityId = field.getInt(null);
            if (modifynumber) {
                field.set(null, entityId+1);
            }
            return entityId;
       }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	    return -1;
    }
	
	public static Entity getEntityById(int entityId)
	{
		for (World world : Bukkit.getWorlds())
		{
			for (Entity entity : world.getEntities())
			{
				if (entity.getEntityId() == entityId)
				{
					return entity;
				}
			}
		}
		
		return null;
	}

	public static boolean inWater(LivingEntity ent) 
	{
		return ent.getLocation().getBlock().getTypeId() == 8 || ent.getLocation().getBlock().getTypeId() == 9;
	}
}
