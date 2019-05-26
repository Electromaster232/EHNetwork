package nautilus.game.arcade.game.games.mineware.challenges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilEnt;
import mineplex.core.common.util.UtilMath;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilTime;
import mineplex.core.common.util.UtilTime.TimeUnit;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.minecraft.game.core.damage.CustomDamageEvent;
import nautilus.game.arcade.game.games.mineware.ChallengeSeperateRooms;
import nautilus.game.arcade.game.games.mineware.MineWare;

public class ChallengeNameThatSound extends ChallengeSeperateRooms
{
	private HashMap<EntityType, Sound[]> _sounds = new HashMap<EntityType, Sound[]>();
	private ArrayList<Entry<Entry<EntityType, Sound>, Float>> _toPlay = new ArrayList<Entry<Entry<EntityType, Sound>, Float>>();
	private HashMap<String, Integer> _currentState = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Entity>> _mobs = new HashMap<String, ArrayList<Entity>>();
	private HashMap<String, Long> _lastGuess = new HashMap<String, Long>();

	public ChallengeNameThatSound(MineWare host)
	{
		super(host, ChallengeType.FirstComplete, "Hit the creature that makes the noises");

		_sounds.put(EntityType.ZOMBIE, new Sound[]
			{
					Sound.ZOMBIE_DEATH, Sound.ZOMBIE_HURT, Sound.ZOMBIE_IDLE, Sound.ZOMBIE_INFECT, Sound.ZOMBIE_METAL,
					Sound.ZOMBIE_REMEDY, Sound.ZOMBIE_UNFECT
			});

		_sounds.put(EntityType.PIG, new Sound[]
			{
					Sound.PIG_DEATH, Sound.PIG_IDLE
			});

		_sounds.put(EntityType.CHICKEN, new Sound[]
			{
					Sound.CHICKEN_EGG_POP, Sound.CHICKEN_HURT, Sound.CHICKEN_IDLE
			});

		/*_sounds.put(EntityType.SPIDER, new Sound[]
			{
					Sound.SPIDER_DEATH, Sound.SPIDER_IDLE
			});*/

		_sounds.put(EntityType.IRON_GOLEM, new Sound[]
			{
					Sound.IRONGOLEM_DEATH, Sound.IRONGOLEM_HIT, Sound.IRONGOLEM_THROW
			});

		_sounds.put(EntityType.ENDERMAN, new Sound[]
			{
					Sound.ENDERMAN_DEATH, Sound.ENDERMAN_HIT, Sound.ENDERMAN_IDLE, Sound.ENDERMAN_SCREAM, Sound.ENDERMAN_TELEPORT
			});

		_sounds.put(EntityType.COW, new Sound[]
			{
					Sound.COW_HURT, Sound.COW_IDLE, Sound.COW_WALK
			});

		_sounds.put(EntityType.HORSE, new Sound[]
			{
					Sound.HORSE_ANGRY, Sound.HORSE_BREATHE, Sound.HORSE_DEATH, Sound.HORSE_GALLOP, Sound.HORSE_ARMOR,
					Sound.HORSE_HIT, Sound.HORSE_IDLE
			});

		_sounds.put(EntityType.OCELOT, new Sound[]
			{
					Sound.CAT_HISS, Sound.CAT_HIT, Sound.CAT_MEOW, Sound.CAT_PURR, Sound.CAT_PURREOW
			});

		_sounds.put(EntityType.VILLAGER, new Sound[]
			{
					Sound.VILLAGER_DEATH, Sound.VILLAGER_HAGGLE, Sound.VILLAGER_HIT, Sound.VILLAGER_IDLE, Sound.VILLAGER_NO,
					Sound.VILLAGER_YES
			});

		_sounds.put(EntityType.WOLF, new Sound[]
			{
					Sound.WOLF_BARK, Sound.WOLF_DEATH, Sound.WOLF_GROWL, Sound.WOLF_HURT, Sound.WOLF_PANT, Sound.WOLF_SHAKE,
					Sound.WOLF_WHINE
			});

		_sounds.put(EntityType.PIG_ZOMBIE, new Sound[]
			{
					Sound.ZOMBIE_PIG_ANGRY, Sound.ZOMBIE_PIG_DEATH, Sound.ZOMBIE_PIG_HURT, Sound.ZOMBIE_PIG_IDLE
			});

		_sounds.put(EntityType.SHEEP, new Sound[]
			{
				Sound.SHEEP_IDLE
			});

		_sounds.put(EntityType.SKELETON, new Sound[]
			{
					Sound.SKELETON_DEATH, Sound.SKELETON_HURT, Sound.SKELETON_IDLE
			});

		/*_sounds.put(EntityType.SLIME, new Sound[]
			{
					Sound.SLIME_ATTACK, Sound.SLIME_WALK, Sound.SLIME_WALK2, Sound.MAGMACUBE_JUMP, Sound.MAGMACUBE_WALK,
					Sound.MAGMACUBE_WALK2
			});*/

		while (_toPlay.size() < 3)
		{
			EntityType entityType = EntityType.values()[UtilMath.r(EntityType.values().length)];

			if (!_sounds.containsKey(entityType))
			{
				continue;
			}

			boolean allClear = true;

			for (Entry<Entry<EntityType, Sound>, Float> entry : _toPlay)
			{
				if (entry.getKey().getKey() == entityType)
				{
					allClear = false;
					break;
				}
			}

			if (!allClear)
			{
				continue;
			}

			Sound sound = _sounds.get(entityType)[UtilMath.r(_sounds.get(entityType).length)];

			_toPlay.add(new HashMap.SimpleEntry(new HashMap.SimpleEntry(entityType, sound), UtilMath.random.nextFloat() + 0.5));
		}
	}

	private void spawnMobs(Player player, int stage)
	{
		if (!_mobs.containsKey(player.getName()))
		{
			return;
		}

		for (Entity entity : _mobs.get(player.getName()))
		{
			entity.remove();
		}

		if (stage > 2)
			return;

		ArrayList<EntityType> entityType = new ArrayList<EntityType>();

		for (EntityType type : _sounds.keySet())
		{
			entityType.add(type);
		}

		entityType.remove(_toPlay.get(stage).getKey().getKey());

		while (entityType.size() > 8)
		{
			entityType.remove(UtilMath.r(entityType.size()));
		}

		entityType.add(_toPlay.get(stage).getKey().getKey());

		Collections.shuffle(entityType);

		Host.CreatureAllowOverride = true;

		int i = 0;

		for (int x = 1; x <= 9; x++)
		{
			for (int z = 1; z <= 9; z++)
			{
				if ((x == 3 && z == 3) || (x % 3 != 0 || z % 3 != 0))
					continue;

				Location loc = getRoom(player).add(x, 1.1, z);

				Entity entity = loc.getWorld().spawnEntity(loc, entityType.get(i++));

				UtilEnt.Vegetate(entity, true);

				_mobs.get(player.getName()).add(entity);
			}
		}

		Host.CreatureAllowOverride = false;
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event)
	{
		event.setCancelled(true);

		Player player = event.getPlayer();

		if (UtilPlayer.isSpectator(player))
			return;

		if (_lastGuess.containsKey(player.getName()) && !UtilTime.elapsed(_lastGuess.get(player.getName()), 3000))
		{
			player.sendMessage(F.main(
					"Guess",
					"Wait "
							+ UtilTime.convertString((_lastGuess.get(player.getName()) + 3000) - System.currentTimeMillis(), 1,
									TimeUnit.SECONDS) + " before next guess"));
			return;
		}

		EntityType entityType = event.getRightClicked().getType();

		int stage = _currentState.get(player.getName());

		if (_toPlay.get(stage).getKey().getKey() == entityType)
		{
			stage++;
			displayCount(player, event.getRightClicked().getLocation(), stage == 1 ? C.cRed : stage == 2 ? C.cGreen : C.cDGreen);
			spawnMobs(player, stage);

			if (stage < 3)
			{
				player.playSound(player.getLocation(), _toPlay.get(stage).getKey().getValue(), 5, _toPlay.get(stage).getValue());
			}
			else
			{
				SetCompleted(player);
			}
		}
		else
		{
			_lastGuess.put(player.getName(), System.currentTimeMillis());
			player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 2, 0);
			player.sendMessage(F.main("Name that sound", "Bad guess!"));
		}
	}

	@EventHandler
	public void onTwoSeconds(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TWOSEC)
		{
			return;
		}

		for (Player player : getChallengers())
		{
			int stage = _currentState.get(player.getName());

			if (stage < 3)
			{
				player.playSound(player.getLocation(), _toPlay.get(stage).getKey().getValue(), 5, _toPlay.get(stage).getValue());
			}
		}
	}

	@EventHandler
	public void onAttack(CustomDamageEvent event)
	{
		if (event.GetDamagerPlayer(true) == null)
		{
			return;
		}

		onInteract(new PlayerInteractEntityEvent(event.GetDamagerPlayer(true), event.GetDamageeEntity()));

		event.SetCancelled("No damage");
		event.GetDamageeEntity().setFireTicks(0);
	}

	@Override
	public void generateRoom(Location loc)
	{
		for (int x = 0; x <= 10; x++)
		{
			for (int z = 0; z <= 10; z++)
			{
				if (x == 0 || x == 10 || z == 0 || z == 10)
				{
					for (int y = 1; y <= 5; y++)
					{
						Block b = loc.getBlock().getRelative(x, y, z);
						b.setType(Material.COAL_BLOCK);
						addBlock(b);
					}
				}

				Block b = loc.getBlock().getRelative(x, 0, z);
				b.setType(Material.WOOL);
				addBlock(b);
			}
		}
	}

	@Override
	public int getBorderX()
	{
		return 10;
	}

	@Override
	public int getBorderY()
	{
		return 10;
	}

	@Override
	public int getBorderZ()
	{
		return 10;
	}

	@Override
	public int getDividersX()
	{
		return 5;
	}

	@Override
	public int getDividersZ()
	{
		return 5;
	}

	@Override
	public void cleanupRoom()
	{
		for (ArrayList<Entity> entityList : _mobs.values())
		{
			for (Entity entity : entityList)
			{
				entity.remove();
			}

		}
	}

	@Override
	public void setupPlayers()
	{
		for (Player player : getChallengers())
		{
			_currentState.put(player.getName(), 0);
			_mobs.put(player.getName(), new ArrayList<Entity>());

			spawnMobs(player, 0);
			player.playSound(player.getLocation(), _toPlay.get(0).getKey().getValue(), 5, _toPlay.get(0).getValue());
		}
	}

}
