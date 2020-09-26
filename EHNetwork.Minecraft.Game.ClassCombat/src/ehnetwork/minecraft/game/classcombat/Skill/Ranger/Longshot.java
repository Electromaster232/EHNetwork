package ehnetwork.minecraft.game.classcombat.Skill.Ranger;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import ehnetwork.core.common.util.UtilMath;
import ehnetwork.minecraft.game.classcombat.Class.IPvpClass;
import ehnetwork.minecraft.game.classcombat.Skill.Skill;
import ehnetwork.minecraft.game.classcombat.Skill.SkillFactory;
import ehnetwork.minecraft.game.core.damage.CustomDamageEvent;

public class Longshot extends Skill
{

    public Longshot(SkillFactory skills, String name, IPvpClass.ClassType classType, SkillType skillType, int cost, int levels)
    {
        super(skills, name, classType, skillType, cost, levels);

        SetDesc(new String[]
            {
                    "Arrows do an additional 1 damage", 
                    "for every #4#-0.5 Blocks they travelled,",
                    "however, their base damage is", 
                    "reduced by 5.", 
                    "", 
                    "Maximum of #6#6 additional damage."
            });
    }

    @EventHandler
    public void ShootBow(EntityShootBowEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        int level = getLevel((Player) event.getEntity());
        if (level == 0)
            return;

        // Save
        event.getProjectile().setMetadata("ShotFrom",
                new FixedMetadataValue(this.Factory.getPlugin(), event.getProjectile().getLocation()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void Damage(CustomDamageEvent event)
    {
        if (event.IsCancelled())
            return;

        if (event.GetCause() != DamageCause.PROJECTILE)
            return;

        Projectile projectile = event.GetProjectile();
        if (projectile == null)
            return;

        if (!projectile.hasMetadata("ShotFrom"))
            return;

        Player damager = event.GetDamagerPlayer(true);
        if (damager == null)
            return;

        int level = getLevel(damager);

        Location loc = (Location) projectile.getMetadata("ShotFrom").get(0).value();
        double length = UtilMath.offset(loc, projectile.getLocation());

        // Damage
        double damage = Math.min(6 + 6 * level, (length / (4 - 0.5 * level)) - 5);

        if (damage < 0)
        	damage = 0;
        
        event.AddMod(damager.getName(), GetName(), damage, damage > 0);
    }

    @Override
    public void Reset(Player player)
    {

    }
}
