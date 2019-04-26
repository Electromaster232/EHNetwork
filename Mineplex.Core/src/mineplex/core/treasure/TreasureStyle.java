package mineplex.core.treasure;

import org.bukkit.Sound;

import mineplex.core.common.util.UtilParticle.ParticleType;

public enum TreasureStyle
{
	OLD(
			ParticleType.EXPLODE,
			ParticleType.EXPLODE,
			ParticleType.ENCHANTMENT_TABLE,
			Sound.FIZZ,
			Sound.HORSE_ARMOR),

	ANCIENT(
			ParticleType.FLAME,
			ParticleType.LAVA,
			ParticleType.MOB_SPELL,
			Sound.LAVA_POP,
			Sound.EXPLODE),
			
	MYTHICAL(
			ParticleType.HAPPY_VILLAGER,
			ParticleType.LARGE_EXPLODE,
			ParticleType.INSTANT_SPELL,
			Sound.PORTAL_TRAVEL,
			Sound.ANVIL_LAND);

	private ParticleType _secondaryParticle;
	private ParticleType _chestSpawnParticle;
	private ParticleType _hoverParticle;
	private Sound _sound;
	private Sound _chestSpawnSound;
	
	TreasureStyle(ParticleType secondaryParticle, ParticleType chestSpawnParticle, ParticleType hoverParticle,  Sound sound, Sound chestSpawnSound)
	{
		_secondaryParticle = secondaryParticle;
		_chestSpawnParticle = chestSpawnParticle;
		_hoverParticle = hoverParticle;
		_sound = sound;
		_chestSpawnSound = chestSpawnSound;
	}

	public ParticleType getSecondaryParticle()
	{
		return _secondaryParticle;
	}
	
	public ParticleType getChestSpawnParticle()
	{
		return _chestSpawnParticle;
	}

	public ParticleType getHoverParticle()
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
