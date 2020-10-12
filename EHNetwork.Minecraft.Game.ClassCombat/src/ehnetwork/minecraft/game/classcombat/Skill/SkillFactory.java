package ehnetwork.minecraft.game.classcombat.Skill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.donation.repository.GameSalesPackageToken;
import ehnetwork.core.energy.Energy;
import ehnetwork.core.movement.Movement;
import ehnetwork.core.projectile.ProjectileManager;
import ehnetwork.core.teleport.Teleport;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.Assassin;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.BackStab;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.Blink;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.ComboAttack;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.Evade;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.Flash;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.Leap;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.MarkedForDeath;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.Recall;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.ShockingStrikes;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.SilencingArrow;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.SmokeArrow;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.SmokeBomb;
import ehnetwork.minecraft.game.classcombat.Skill.Assassin.ViperStrikes;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.BlockToss;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.Bloodlust;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.Brute;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.Colossus;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.CripplingBlow;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.DwarfToss;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.FleshHook;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.Intimidation;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.Overwhelm;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.SeismicSlam;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.Stampede;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.Takedown;
import ehnetwork.minecraft.game.classcombat.Skill.Brute.WhirlwindAxe;
import ehnetwork.minecraft.game.classcombat.Skill.Global.BreakFall;
import ehnetwork.minecraft.game.classcombat.Skill.Global.Fitness;
import ehnetwork.minecraft.game.classcombat.Skill.Global.Recharge;
import ehnetwork.minecraft.game.classcombat.Skill.Global.Resistance;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.AxeThrow;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.BullsCharge;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.Cleave;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.DefensiveStance;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.Deflection;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.Fortitude;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.HiltSmash;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.HoldPosition;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.Knight;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.LevelField;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.Riposte;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.ShieldSmash;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.Swordsmanship;
import ehnetwork.minecraft.game.classcombat.Skill.Knight.Vengeance;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.ArcticArmor;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.Blizzard;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.FireBlast;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.Fissure;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.GlacialBlade;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.IcePrison;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.Immolate;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.Inferno;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.LifeBonds;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.LightningOrb;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.Mage;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.MagmaBlade;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.NullBlade;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.Rupture;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.StaticLazer;
import ehnetwork.minecraft.game.classcombat.Skill.Mage.Void;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.Agility;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.BarbedArrows;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.Barrage;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.Disengage;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.ExplosiveShot;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.HealingShot;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.HeavyArrows;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.IncendiaryShot;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.Longshot;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.NapalmShot;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.Overcharge;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.PinDown;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.Ranger;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.RopedArrow;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.Sharpshooter;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.VitalitySpores;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.WolfsFury;
import ehnetwork.minecraft.game.classcombat.Skill.Ranger.WolfsPounce;
import ehnetwork.minecraft.game.classcombat.Skill.repository.SkillRepository;
import ehnetwork.minecraft.game.classcombat.Skill.repository.token.SkillToken;
import ehnetwork.minecraft.game.core.IRelation;
import ehnetwork.minecraft.game.core.combat.CombatManager;
import ehnetwork.minecraft.game.core.condition.ConditionManager;
import ehnetwork.minecraft.game.core.damage.DamageManager;
import ehnetwork.minecraft.game.core.fire.Fire;

public class SkillFactory extends MiniPlugin implements ISkillFactory
{
	private DamageManager _damageManager;
	private IRelation _relation;
	private CombatManager _combatManager;
	private ConditionManager _conditionManager;
	private ProjectileManager _projectileManager;
	private DisguiseManager _disguiseManager;
	private BlockRestore _blockRestore;
	private Fire _fire;
	private Movement _movement;
	private Teleport _teleport;
	private Energy _energy;
	private SkillRepository _repository;
	private HashMap<String, Skill> _skillMap;
	private HashMap<Integer, ISkill> _skillSalesPackageMap;

	public SkillFactory(JavaPlugin plugin, DamageManager damageManager, IRelation relation, 
			CombatManager combatManager, ConditionManager conditionManager, ProjectileManager projectileManager, DisguiseManager disguiseManager,
			BlockRestore blockRestore, Fire fire, Movement movement, Teleport teleport, Energy energy, String webAddress) 
	{
		super("Skill Factory", plugin);
		
		_repository = new SkillRepository(webAddress);
		_damageManager = damageManager;
		_relation = relation;
		_combatManager = combatManager;
		_conditionManager = conditionManager;
		_projectileManager = projectileManager;
		_blockRestore = blockRestore;
		_disguiseManager = disguiseManager;
		_fire = fire;
		_movement = movement;
		_teleport = teleport;
		_energy = energy;
		_skillMap = new HashMap<String, Skill>();
		_skillSalesPackageMap = new HashMap<Integer, ISkill>();

		PopulateSkills();
	}
	
	public ConditionManager Condition()
	{
		return _conditionManager;
	}
	
	public Teleport Teleport()
	{
		return _teleport;
	}
	
	public Energy Energy()
	{
		return _energy;
	}

	private void PopulateSkills()
	{
		_skillMap.clear();

		AddAssassin();
		AddBrute();
		AddKnight();
		AddMage();
		AddRanger();
		//AddShifter();
		AddGlobal();

		for (Skill skill : _skillMap.values())
			getPlugin().getServer().getPluginManager().registerEvents(skill, getPlugin());

		List<SkillToken> skillTokens = new ArrayList<SkillToken>();

		for (Skill skill : _skillMap.values())
		{
			for (int i=0; i < 1; i++)
			{
				SkillToken skillToken = new SkillToken();

				skillToken.Name = skill.GetName();				
				skillToken.Level = i + 1;
				skillToken.SalesPackage = new GameSalesPackageToken();
				skillToken.SalesPackage.Gems = 2000;

				skillTokens.add(skillToken);
			}
		}

		for (SkillToken skillToken : _repository.GetSkills(skillTokens))
		{
			if (_skillMap.containsKey(skillToken.Name))
			{
				Skill skill = _skillMap.get(skillToken.Name);
				_skillSalesPackageMap.put(skillToken.SalesPackage.GameSalesPackageId, skill);
				_skillMap.get(skillToken.Name).Update(skillToken);
			}
		}
	}

	public void AddGlobal()
	{
		//Passive C
		AddSkill(new BreakFall(this, "Break Fall", IPvpClass.ClassType.Global, ISkill.SkillType.GlobalPassive, 1, 3));
		AddSkill(new Resistance(this, "Resistance", IPvpClass.ClassType.Global, ISkill.SkillType.GlobalPassive, 1, 3));
		//AddSkill(new Cooldown(this, "Quick Recovery", ClassType.Global, SkillType.GlobalPassive, 1, 3));
		//AddSkill(new Rations(this, "Rations", ClassType.Global, SkillType.GlobalPassive, 1, 2));
		
		AddSkill(new Fitness(this, "Mana Pool", IPvpClass.ClassType.Mage, ISkill.SkillType.GlobalPassive, 1, 3));
		AddSkill(new Recharge(this, "Mana Regeneration", IPvpClass.ClassType.Mage, ISkill.SkillType.GlobalPassive, 1, 3));
		
		AddSkill(new Fitness(this, "Fitness", IPvpClass.ClassType.Assassin, ISkill.SkillType.GlobalPassive, 1, 3));
		AddSkill(new Recharge(this, "Rest", IPvpClass.ClassType.Assassin, ISkill.SkillType.GlobalPassive, 1, 3));
		
		
		//AddSkill(new Stamina(this, "Stamina", ClassType.Global, SkillType.GlobalPassive, 1, 1));
		//AddSkill(new Swim(this, "Swim", ClassType.Global, SkillType.GlobalPassive, 1, 1));
	}

	public void AddAssassin()
	{
		AddSkill(new Assassin(this, "Assassin Class", IPvpClass.ClassType.Assassin, ISkill.SkillType.Class, 0, 1));

		//Sword
		AddSkill(new Evade(this, "Evade", IPvpClass.ClassType.Assassin, ISkill.SkillType.Sword,
				1, 4, 
				26, -2, 
				0, 0, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));


		//Axe
		AddSkill(new Blink(this, "Blink", IPvpClass.ClassType.Assassin, ISkill.SkillType.Axe,
				1, 4, 
				64, -4, 
				12000, 0, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new Flash(this, "Flash", IPvpClass.ClassType.Assassin, ISkill.SkillType.Axe,
				1, 4, 
				30, -2, 
				0, 0, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new Leap(this, "Leap", IPvpClass.ClassType.Assassin, ISkill.SkillType.Axe,
				1, 4,
				36, -3, 
				10500, -1500, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		//Bow
		AddSkill(new MarkedForDeath(this, "Marked for Death", IPvpClass.ClassType.Assassin, ISkill.SkillType.Bow,
				1, 4,
				40, 0, 
				20000, 0, true,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		AddSkill(new SmokeArrow(this, "Smoke Arrow", IPvpClass.ClassType.Assassin, ISkill.SkillType.Bow,
				1, 4,
				40, 0, 
				20000, 0, true,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		AddSkill(new SilencingArrow(this, "Silencing Arrow", IPvpClass.ClassType.Assassin, ISkill.SkillType.Bow,
				1, 4,
				40, 0,
				20000, 0, true,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));


		//Passive A
		AddSkill(new SmokeBomb(this, "Smoke Bomb", IPvpClass.ClassType.Assassin, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new Recall(this, "Recall", IPvpClass.ClassType.Assassin, ISkill.SkillType.PassiveA, 1, 3));
		//AddSkill(new Stealth(this, "Stealth", ClassType.Assassin, SkillType.PassiveA, 5, 3));

		//Passive B
		AddSkill(new ShockingStrikes(this, "Shocking Strikes", IPvpClass.ClassType.Assassin, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new ComboAttack(this, "Combo Attack", IPvpClass.ClassType.Assassin, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new ViperStrikes(this, "Viper Strikes", IPvpClass.ClassType.Assassin, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new BackStab(this, "Backstab", IPvpClass.ClassType.Assassin, ISkill.SkillType.PassiveB, 1, 3));
	}

	public void AddBrute()
	{
		AddSkill(new Brute(this, "Brute Class", IPvpClass.ClassType.Brute, ISkill.SkillType.Class, 0, 1));

		//Sword
		AddSkill(new DwarfToss(this, "Dwarf Toss", IPvpClass.ClassType.Brute, ISkill.SkillType.Sword,
				1, 5,
				0, 0, 
				20000, -2000, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new FleshHook(this, "Flesh Hook", IPvpClass.ClassType.Brute, ISkill.SkillType.Sword,
				1, 5,
				0, 0, 
				15000, -1000, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new BlockToss(this, "Block Toss", IPvpClass.ClassType.Brute, ISkill.SkillType.Sword, 1, 5));

		//Axe
		AddSkill(new SeismicSlam(this, "Seismic Slam", IPvpClass.ClassType.Brute, ISkill.SkillType.Axe,
				1, 5,
				0, 0, 
				30000, -2000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new Takedown(this, "Takedown", IPvpClass.ClassType.Brute, ISkill.SkillType.Axe,
				1, 5,
				0, 0, 
				30000, -3000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));
		
		AddSkill(new WhirlwindAxe(this, "Whirlwind Axe", IPvpClass.ClassType.Brute, ISkill.SkillType.Axe,
				1, 5,
				0, 0, 
				30000, -3000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		//Passive A
		AddSkill(new Stampede(this, "Stampede", IPvpClass.ClassType.Brute, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new Bloodlust(this, "Bloodlust", IPvpClass.ClassType.Brute, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new Intimidation(this, "Intimidation", IPvpClass.ClassType.Brute, ISkill.SkillType.PassiveA, 1, 3));

		//Passive B
		AddSkill(new CripplingBlow(this, "Crippling Blow", IPvpClass.ClassType.Brute, ISkill.SkillType.PassiveB, 2, 1));
		AddSkill(new Colossus(this, "Colossus", IPvpClass.ClassType.Brute, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new Overwhelm(this, "Overwhelm", IPvpClass.ClassType.Brute, ISkill.SkillType.PassiveB, 1, 3));
	}

	public void AddKnight()
	{
		AddSkill(new Knight(this, "Knight Class", IPvpClass.ClassType.Knight, ISkill.SkillType.Class, 0, 1));

		//Sword
		AddSkill(new HiltSmash(this, "Hilt Smash", IPvpClass.ClassType.Knight, ISkill.SkillType.Sword,
				1, 5,
				0, 0, 
				15000, -1000, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new Riposte(this, "Riposte", IPvpClass.ClassType.Knight, ISkill.SkillType.Sword,
				1, 5,
				0, 0, 
				11000, -1000, false,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new DefensiveStance(this, "Defensive Stance", IPvpClass.ClassType.Knight, ISkill.SkillType.Sword,
				2, 1,
				0, 0, 
				0, 0, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));


		//Axe
		AddSkill(new BullsCharge(this, "Bulls Charge", IPvpClass.ClassType.Knight, ISkill.SkillType.Axe,
				1, 5,
				0, 0, 
				10000, 1000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new HoldPosition(this, "Hold Position", IPvpClass.ClassType.Knight, ISkill.SkillType.Axe,
				1, 5,
				0, 0, 
				16000, 2000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));
		
		AddSkill(new ShieldSmash(this, "Shield Smash", IPvpClass.ClassType.Knight, ISkill.SkillType.Axe,
				1, 5,
				0, 0, 
				10000, -1000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));
		
		AddSkill(new AxeThrow(this, "Roped Axe Throw", IPvpClass.ClassType.Knight, ISkill.SkillType.Axe,
				1, 5,
				0, 0, 
				2200, -200, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));


		//Passive A
		AddSkill(new Cleave(this, "Cleave", IPvpClass.ClassType.Knight, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new Swordsmanship(this, "Swordsmanship", IPvpClass.ClassType.Knight, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new Deflection(this, "Deflection", IPvpClass.ClassType.Knight, ISkill.SkillType.PassiveA, 1, 3));

		//Passive B
		AddSkill(new Vengeance(this, "Vengeance", IPvpClass.ClassType.Knight, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new Fortitude(this, "Fortitude", IPvpClass.ClassType.Knight, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new LevelField(this, "Level Field", IPvpClass.ClassType.Knight, ISkill.SkillType.PassiveB, 1, 3));
	}

	public void AddMage()
	{
		AddSkill(new Mage(this, "Mage Class", IPvpClass.ClassType.Mage, ISkill.SkillType.Class, 0, 1));

		//Sword
		AddSkill(new Blizzard(this, "Blizzard", IPvpClass.ClassType.Mage, ISkill.SkillType.Sword,
				1, 5,
				0, 0, 
				0, 0, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new Inferno(this, "Inferno", IPvpClass.ClassType.Mage, ISkill.SkillType.Sword,
				1, 5,
				0, 0, 
				0, 0, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new Rupture(this, "Rupture", IPvpClass.ClassType.Mage, ISkill.SkillType.Sword,
				1, 5,
				0, 0, 
				0, 0, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new StaticLazer(this, "Static Lazer", IPvpClass.ClassType.Mage, ISkill.SkillType.Sword, 1, 5));

		//Axe
		AddSkill(new FireBlast(this, "Fire Blast", IPvpClass.ClassType.Mage, ISkill.SkillType.Axe,
				1, 5,
				60, -3, 
				13000, -1000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new IcePrison(this, "Ice Prison", IPvpClass.ClassType.Mage, ISkill.SkillType.Axe,
				1, 5,
				60, -3, 
				21000, -1000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));
		
		AddSkill(new LightningOrb(this, "Lightning Orb", IPvpClass.ClassType.Mage, ISkill.SkillType.Axe,
				1, 5,
				60, -2, 
				11000, -1000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		
		AddSkill(new Fissure(this, "Fissure", IPvpClass.ClassType.Mage, ISkill.SkillType.Axe,
				1, 5,
				60, -3, 
				11000, -1000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		/*
		AddSkill(new FreezingBlast(this, "Freezing Blast", ClassType.Mage, SkillType.Axe, 
				5, 5,
				40, -2, 
				4000, 0, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));
		 */
		
		// AddSkill(new Tundra(this, "Tundra", ClassType.Mage, SkillType.Axe, 830, 200, 1));


		//Passive A
		AddSkill(new ArcticArmor(this, "Arctic Armor", IPvpClass.ClassType.Mage, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new Immolate(this, "Immolate", IPvpClass.ClassType.Mage, ISkill.SkillType.PassiveA, 2, 1));
		AddSkill(new Void(this, "Void", IPvpClass.ClassType.Mage, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new LifeBonds(this, "Life Bonds", IPvpClass.ClassType.Mage, ISkill.SkillType.PassiveA, 1, 3));

		//Passive B
		AddSkill(new GlacialBlade(this, "Glacial Blade", IPvpClass.ClassType.Mage, ISkill.SkillType.PassiveB,
				1, 3,
				16, -2, 
				1200, -200, false,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		AddSkill(new MagmaBlade(this, "Magma Blade", IPvpClass.ClassType.Mage, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new NullBlade(this, "Null Blade", IPvpClass.ClassType.Mage, ISkill.SkillType.PassiveB, 1, 3));
		//AddSkill(new RootingAxe(this, "Rooting Axe", ClassType.Mage, SkillType.PassiveB, 5, 3));
	}

	public void AddRanger()
	{
		AddSkill(new Ranger(this, "Ranger Class", IPvpClass.ClassType.Ranger, ISkill.SkillType.Class, 0, 1));

		//Sword
		AddSkill(new Disengage(this, "Disengage", IPvpClass.ClassType.Ranger, ISkill.SkillType.Sword,
				1, 4,
				0, 0, 
				18000, -2000, true,
				new Material[] {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new WolfsPounce(this, "Wolfs Pounce", IPvpClass.ClassType.Ranger, ISkill.SkillType.Sword, 1, 4));

		//Axe
		AddSkill(new Agility(this, "Agility", IPvpClass.ClassType.Ranger, ISkill.SkillType.Axe,
				1, 4,
				0, 0, 
				14000, 1000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		AddSkill(new WolfsFury(this, "Wolfs Fury", IPvpClass.ClassType.Ranger, ISkill.SkillType.Axe,
				1, 4,
				0, 0, 
				18000, 2000, true,
				new Material[] {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE}, 
				new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}));

		//Bow
		AddSkill(new HealingShot(this, "Healing Shot", IPvpClass.ClassType.Ranger, ISkill.SkillType.Bow,
				1, 4,
				0, 0, 
				20000, -3000, true,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		AddSkill(new IncendiaryShot(this, "Incendiary Shot", IPvpClass.ClassType.Ranger, ISkill.SkillType.Bow,
				1, 4,
				0, 0,
				20000, -2000, true,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		AddSkill(new NapalmShot(this, "Napalm Shot", IPvpClass.ClassType.Ranger, ISkill.SkillType.Bow,
				1, 4,
				0, 0, 
				30000, -2000, true,
				new Material[] {Material.BOW},  
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		AddSkill(new PinDown(this, "Pin Down", IPvpClass.ClassType.Ranger, ISkill.SkillType.Bow,
				1, 4,
				0, 0, 
				13000, -1000, true,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		AddSkill(new RopedArrow(this, "Roped Arrow", IPvpClass.ClassType.Ranger, ISkill.SkillType.Bow,
				1, 4,
				0, 0, 
				9000, -1000, false,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));
		
		AddSkill(new ExplosiveShot(this, "Explosive Arrow", IPvpClass.ClassType.Ranger, ISkill.SkillType.Bow,
				1, 4,
				0, 0, 
				20000, -2000, false,
				new Material[] {Material.BOW}, 
				new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}));

		//Passive A
		AddSkill(new Barrage(this, "Barrage", IPvpClass.ClassType.Ranger, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new Overcharge(this, "Overcharge", IPvpClass.ClassType.Ranger, ISkill.SkillType.PassiveA, 1, 3));
		AddSkill(new VitalitySpores(this, "Vitality Spores", IPvpClass.ClassType.Ranger, ISkill.SkillType.PassiveA, 1, 3));

		//Passive B	
		AddSkill(new BarbedArrows(this, "Barbed Arrows", IPvpClass.ClassType.Ranger, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new HeavyArrows(this, "Heavy Arrows", IPvpClass.ClassType.Ranger, ISkill.SkillType.PassiveB, 1, 3));
		//AddSkill(new Shadowmeld(this, "Shadowmeld", ClassType.Ranger, SkillType.PassiveB, 5, 3));
		AddSkill(new Longshot(this, "Longshot", IPvpClass.ClassType.Ranger, ISkill.SkillType.PassiveB, 1, 3));
		AddSkill(new Sharpshooter(this, "Sharpshooter", IPvpClass.ClassType.Ranger, ISkill.SkillType.PassiveB, 1, 3));
		//AddSkill(new Fletcher(this, "Fletcher", ClassType.Ranger, SkillType.PassiveB, 5, 3));
	}

	public ISkill GetSkillBySalesPackageId(int id)
	{
		return _skillSalesPackageMap.get(id);
	}

	public Skill GetSkill(String skillName)
	{
		return _skillMap.get(skillName);
	}

	public Collection<Skill> GetAllSkills()
	{
		return _skillMap.values();
	}

	public void AddSkill(Skill skill)
	{
		_skillMap.put(skill.GetName(), skill);
	}

	public void RemoveSkill(String skillName, String defaultReplacement)
	{
		if (skillName == null)
		{
			System.out.println("[Skill Factory] Remove Skill: Remove Skill NULL [" + skillName + "].");
			return;
		}

		Skill remove = _skillMap.get(skillName);
		if (remove == null)
		{
			System.out.println("[Skill Factory] Remove Skill: Remove Skill NULL [" + skillName + "].");
			return;
		}

		Skill replacement = null;
		if (defaultReplacement != null)
		{
			replacement = _skillMap.get(defaultReplacement);
			if (replacement == null)
			{
				System.out.println("[Skill Factory] Remove Skill: Replacement Skill NULL [" + defaultReplacement + "].");
				return;
			}
		}

		//Remove
		_skillMap.remove(remove.GetName());
		HandlerList.unregisterAll(remove);

		System.out.println("Skill Factory: Removed " + remove.GetName() + " from SkillMap.");
	}

	@Override
	public List<ISkill> GetGlobalSkillsFor(IPvpClass gameClass) 
	{
		List<ISkill> skills = new LinkedList<ISkill>();

		for (ISkill cur : _skillMap.values())
		{
			if (cur.GetSkillType() == ISkill.SkillType.GlobalPassive && (cur.GetClassType() == IPvpClass.ClassType.Global || (gameClass != null && cur.GetClassType() == gameClass.GetType())))
			{
				skills.add(cur);
			}
		}

		return skills;
	}

	@Override
	public List<ISkill> GetSkillsFor(IPvpClass gameClass) 
	{
		List<ISkill> skills = new LinkedList<ISkill>();

		for (ISkill cur : _skillMap.values())
		{
			if (cur.GetClassType() == gameClass.GetType() && cur.GetSkillType() != ISkill.SkillType.GlobalPassive)
			{
				skills.add(cur);
			}
		}

		return skills;
	}

	//Called once, upon Class creation.
	@Override
	public HashMap<ISkill, Integer> GetDefaultSkillsFor(IPvpClass classType) 
	{
		HashMap<ISkill, Integer> skills = new HashMap<ISkill, Integer>();
		if (classType.GetType() == IPvpClass.ClassType.Knight)
		{
			AddSkill(skills, "Knight Class", 1);         	 //Class

			AddSkill(skills, "Bulls Charge", 1);			//Axe
			AddSkill(skills, "Riposte", 1);					//Sword
			AddSkill(skills, "Deflection", 1);				//Passive A
			AddSkill(skills, "Vengeance", 1);				//Passive B

			AddSkill(skills, "Resistance", 1);				//Passive C
		}

		else if (classType.GetType() == IPvpClass.ClassType.Ranger)
		{
			AddSkill(skills, "Ranger Class", 1);          	//Class

			AddSkill(skills, "Napalm Shot", 1);				//Bow
			AddSkill(skills, "Agility", 1);					//Axe
			AddSkill(skills, "Disengage", 1);				//Sword
			AddSkill(skills, "Barrage", 1);					//Passive A
			AddSkill(skills, "Barbed Arrows", 1);			//Passive B

			AddSkill(skills, "Quick Recovery", 1);			//Passive D
		}

		else if (classType.GetType() == IPvpClass.ClassType.Brute)
		{
			AddSkill(skills, "Brute Class", 1);              //Class

			AddSkill(skills, "Seismic Slam", 1);			//Axe
			AddSkill(skills, "Dwarf Toss", 1);				//Sword
			AddSkill(skills, "Stampede", 1);				//Passive A
			AddSkill(skills, "Crippling Blow", 1);			//Passive B

			AddSkill(skills, "Resistance", 1);				//Passive C
		}

		else if (classType.GetType() == IPvpClass.ClassType.Assassin)
		{
			AddSkill(skills, "Assassin Class", 1);          //Class

			AddSkill(skills, "Blink", 1);					//Axe
			AddSkill(skills, "Evade", 1);					//Sword
			AddSkill(skills, "Toxic Arrow", 1);				//Bow
			AddSkill(skills, "Smoke Bomb", 1);				//Passive A
			AddSkill(skills, "Repeated Strikes", 1);		//Passive B

			AddSkill(skills, "Break Fall", 1);				//Passive C
		}

		else if (classType.GetType() == IPvpClass.ClassType.Mage)
		{
			AddSkill(skills, "Mage Class", 1);              //Class

			AddSkill(skills, "Freezing Blast", 1);			//Axe
			AddSkill(skills, "Blizzard", 1);				//Sword
			AddSkill(skills, "Arctic Armor", 1);			//Passive A
			AddSkill(skills, "Glacial Blade", 1);			//Passive B

			AddSkill(skills, "Fitness", 1);					//Passive D
		}

		else if (classType.GetType() == IPvpClass.ClassType.Shifter)
		{
			AddSkill(skills, "Shifter Class", 1);      		//Class

			AddSkill(skills, "Tree Shift", 1);				//Axe
			AddSkill(skills, "Polysmash", 1);				//Sword
			AddSkill(skills, "Golem Form", 1);				//Passive A
			AddSkill(skills, "Chicken Form", 1);			//Passive B

			AddSkill(skills, "Quick Recovery", 1);			//Passive D
		}

		skills.remove(null);

		return skills;
	}

	public void AddSkill(HashMap<ISkill, Integer> skills, String skillName, int level)
	{
		ISkill skill = GetSkill(skillName);

		if (skill == null)
			return;

		skills.put(skill, level);
	}

	public Movement Movement()
	{
		return _movement;
	}
	
	public DamageManager Damage()
	{
		return _damageManager;
	}
	
	public CombatManager Combat()
	{
		return _combatManager;
	}
	
	public ProjectileManager Projectile()
	{
		return _projectileManager;
	}
	
	public BlockRestore BlockRestore()
	{
		return _blockRestore;
	}
	
	public DisguiseManager Disguise()
	{
		return _disguiseManager;
	}
	
	public Fire Fire()
	{
		return _fire;
	}
	
	public IRelation Relation()
	{
		return _relation;
	}
	
	public void ResetAll()
	{
		for (ISkill skill : _skillMap.values())
			for (Player player : skill.GetUsers())
				skill.Reset(player);
	}
	
	@Override
	public void registerSelf()
	{
		registerEvents(this);
		
		for (Skill skill : _skillMap.values())
			registerEvents(skill);
	}
	
	@Override
	public void deregisterSelf()
	{
		HandlerList.unregisterAll(this);
		
		for (Skill skill : _skillMap.values())
			HandlerList.unregisterAll(skill);
	}
}
