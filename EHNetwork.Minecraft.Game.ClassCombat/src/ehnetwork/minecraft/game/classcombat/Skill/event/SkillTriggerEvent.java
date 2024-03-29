package ehnetwork.minecraft.game.classcombat.Skill.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;

public class SkillTriggerEvent extends Event 
{
    private static final HandlerList handlers = new HandlerList();
    
    private Player _player;
    private String _skill;
    private List<Entity> _targets;
    private IPvpClass.ClassType _classType;
    private boolean _cancelled = false;

    public SkillTriggerEvent(Player player, String skill, IPvpClass.ClassType classType, List<Entity> targets)
    {
    	_player = player;
    	_skill = skill;
    	_classType = classType;
    	_targets = targets;
    }
    
    public SkillTriggerEvent(Player player, String skill, IPvpClass.ClassType classType, Entity target)
    {
    	_player = player;
    	_skill = skill;
    	_classType = classType;
    	_targets = new ArrayList<Entity>();
    	_targets.add(target);
    }
  
    public SkillTriggerEvent(Player player, String skill, IPvpClass.ClassType classType, Set<LivingEntity> targets)
    {
    	_player = player;
    	_skill = skill;
    	_classType = classType;
    	_targets = new ArrayList<Entity>();
    	for (LivingEntity ent : targets)
    		_targets.add(ent);
	}

	public SkillTriggerEvent(Player player, String skill, IPvpClass.ClassType classType)
	{
		_player = player;
    	_skill = skill;
    	_classType = classType;
	}

	public HandlerList getHandlers() 
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList() 
    {
        return handlers;
    }

	public String GetSkillName() 
	{
		return _skill;
	}

	public Player GetPlayer()
	{
		return _player;
	}

	public IPvpClass.ClassType GetClassType()
	{
		return _classType;
	}
	
	public List<Entity> GetTargets() 
	{
		return _targets;
	}

	public boolean IsCancelled() {
		return _cancelled;
	}

	public void SetCancelled(boolean cancelled) 
	{
		this._cancelled = cancelled;
	}
}