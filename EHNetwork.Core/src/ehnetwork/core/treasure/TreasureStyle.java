package ehnetwork.core.treasure;

import org.bukkit.Sound;

import ehnetwork.core.common.util.UtilParticle;

public enum TreasureStyle
{
	OLD(
			UtilParticle.ParticleType.EXPLODE,
			UtilParticle.ParticleType.EXPLODE,
			UtilParticle.ParticleType.ENCHANTMENT_TABLE,
			Sound.FIZZ,
			Sound.HORSE_ARMOR),

	ANCIENT(
			UtilParticle.ParticleType.FLAME,
			UtilParticle.ParticleType.LAVA,
			UtilParticle.ParticleType.MOB_SPELL,
			Sound.LAVA_POP,
			Sound.EXPLODE),
			
	MYTHICAL(
			UtilParticle.ParticleType.HAPPY_VILLAGER,
			UtilParticle.ParticleType.LARGE_EXPLODE,
			UtilParticle.ParticleType.INSTANT_SPELL,
			Sound.PORTAL_TRAVEL,
			Sound.ANVIL_LAND);

	private UtilParticle.ParticleType _secondaryParticle;
	private UtilParticle.ParticleType _chestSpawnParticle;
	private UtilParticle.ParticleType _hoverParticle;
	private Sound _sound;
	private Sound _chestSpawnSound;
	
	TreasureStyle(UtilParticle.ParticleType secondaryParticle, UtilParticle.ParticleType chestSpawnParticle, UtilParticle.ParticleType hoverParticle, Sound sound, Sound chestSpawnSound)
	{
		_secondaryParticle = secondaryParticle;
		_chestSpawnParticle = chestSpawnParticle;
		_hoverParticle = hoverParticle;
		_sound = sound;
		_chestSpawnSound = chestSpawnSound;
	}

	public UtilParticle.ParticleType getSecondaryParticle()
	{
		return _secondaryParticle;
	}
	
	public UtilParticle.ParticleType getChestSpawnParticle()
	{
		return _chestSpawnParticle;
	}

	public UtilParticle.ParticleType getHoverParticle()
	{
		return _hoverParticle;
	}

	public Sound getSound()
	{
		return _sound;
	}

	public Sound getChestSpawnSound()
	{
		return _chestSpawnSound;
	}
}
