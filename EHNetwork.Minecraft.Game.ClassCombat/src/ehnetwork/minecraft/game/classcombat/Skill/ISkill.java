package ehnetwork.minecraft.game.classcombat.Skill;

import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;

public interface ISkill
{
	public enum SkillType
	{ 
		Axe,
		Bow,
		Sword,
		PassiveA,
		PassiveB,
		GlobalPassive,
		Class,
	}
	
    String GetName();
    int getLevel(Entity ent);
    IPvpClass.ClassType GetClassType();
    SkillType GetSkillType();
    int GetGemCost();
    int GetTokenCost();
    boolean IsFree();
    void setFree(boolean free);
    String[] GetDesc(int level);
    void Reset(Player player);
    
    Set<Player> GetUsers();
    void AddUser(Player player, int level);
    void RemoveUser(Player player);
    
	Integer GetSalesPackageId();
	int getMaxLevel();
	
	boolean isAchievementSkill();
}
