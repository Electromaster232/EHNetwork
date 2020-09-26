package ehnetwork.game.arcade.game.games.wizards.spells;

import org.bukkit.entity.Player;

import ehnetwork.game.arcade.game.games.wizards.Spell;
import ehnetwork.game.arcade.game.games.wizards.spellinterfaces.SpellClick;

public class SpellSpeedBoost extends Spell implements SpellClick
{
	@Override
	public void castSpell(Player p)
	{
		Wizards.getArcadeManager().GetCondition().Factory().Speed("Speed Boost", p, p, 20, getSpellLevel(p), false, false, false);

		charge(p);
	}
}
