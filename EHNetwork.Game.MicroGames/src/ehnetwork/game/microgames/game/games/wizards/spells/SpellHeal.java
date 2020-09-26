package ehnetwork.game.microgames.game.games.wizards.spells;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

import ehnetwork.game.microgames.game.games.wizards.Spell;
import ehnetwork.game.microgames.game.games.wizards.spellinterfaces.SpellClick;

public class SpellHeal extends Spell implements SpellClick
{

	@Override
	public void castSpell(Player p)
	{
		if (p.getHealth() < p.getMaxHealth())
		{
			double health = p.getHealth() + (3 + getSpellLevel(p));

			if (health > p.getMaxHealth())
				health = p.getMaxHealth();

			p.setHealth(health);

			p.getWorld().spigot().playEffect(p.getEyeLocation(), Effect.HEART, 0, 0, 0.8F, 0.4F, 0.8F, 0, 6, 30);

			charge(p);
		}
	}
}