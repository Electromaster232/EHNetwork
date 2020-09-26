package ehnetwork.game.arcade.game.games.wizards.spells;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftFireball;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_7_R4.EntityFireball;

import ehnetwork.game.arcade.game.games.wizards.Spell;
import ehnetwork.game.arcade.game.games.wizards.spellinterfaces.SpellClick;
import ehnetwork.minecraft.game.core.explosion.CustomExplosion;

public class SpellFireball extends Spell implements SpellClick
{

	@EventHandler
	public void onHit(ProjectileHitEvent event)
	{
		Projectile projectile = event.getEntity();

		if (projectile.hasMetadata("FireballSpell"))
		{
			projectile.remove();

			int spellLevel = projectile.getMetadata("SpellLevel").get(0).asInt();

			CustomExplosion explosion = new CustomExplosion(Wizards.getArcadeManager().GetDamage(), Wizards.getArcadeManager()
					.GetExplosion(), projectile.getLocation(), (spellLevel * 0.3F) + 1F, "Fireball");

			explosion.setPlayer((Player) projectile.getMetadata("FireballSpell").get(0).value(), true);

			explosion.setFallingBlockExplosion(true);

			explosion.setDropItems(false);

			explosion.setMaxDamage(spellLevel + 6);

			explosion.explode();
		}
	}

	@Override
	public void castSpell(Player p)
	{
		org.bukkit.entity.Fireball fireball = (org.bukkit.entity.Fireball) p.getWorld().spawnEntity(p.getEyeLocation(),
				EntityType.FIREBALL);

		Vector vector = p.getEyeLocation().getDirection().normalize().multiply(0.14);

		// We can't call the bukkit methods because for some weird reason, it enforces a certain speed.
		EntityFireball eFireball = ((CraftFireball) fireball).getHandle();
		eFireball.dirX = vector.getX();
		eFireball.dirY = vector.getY();
		eFireball.dirZ = vector.getZ();

		fireball.setBounce(false);
		fireball.setShooter(p);
		fireball.setYield(0);
		fireball.setMetadata("FireballSpell", new FixedMetadataValue(Wizards.getArcadeManager().getPlugin(), p));
		fireball.setMetadata("SpellLevel", new FixedMetadataValue(Wizards.getArcadeManager().getPlugin(), getSpellLevel(p)));

		p.getWorld().playSound(p.getLocation(), Sound.BLAZE_BREATH, 0.5F, 5F);
		charge(p);
	}
}
