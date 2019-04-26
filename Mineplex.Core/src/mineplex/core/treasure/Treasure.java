package mineplex.core.treasure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.PacketPlayOutBlockAction;
import mineplex.core.blockrestore.BlockRestore;
import mineplex.core.common.util.C;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilParticle;
import mineplex.core.common.util.UtilServer;
import mineplex.core.common.util.UtilParticle.ViewDist;
import mineplex.core.hologram.HologramManager;
import mineplex.core.reward.Reward;
import mineplex.core.reward.RewardData;
import mineplex.core.reward.RewardRarity;
import mineplex.core.treasure.animation.Animation;
import mineplex.core.treasure.animation.BlockChangeAnimation;
import mineplex.core.treasure.animation.ChestOpenAnimation;
import mineplex.core.treasure.animation.ChestSpawnAnimation;
import mineplex.core.treasure.animation.LootLegendaryAnimation;
import mineplex.core.treasure.animation.LootMythicalAnimation;
import mineplex.core.treasure.animation.LootRareAnimation;
import mineplex.core.treasure.animation.TreasureRemoveAnimation;
import mineplex.core.treasure.animation.LootUncommonAnimation;

/**
 * Created by Shaun on 8/27/2014.
 */
public class Treasure
{
	private BlockRestore _blockRestore; 
	
	private List<BlockInfo> _chestBlockInfo = new ArrayList<BlockInfo>();
	private List<BlockInfo> _openedChestBlockInfo = new ArrayList<BlockInfo>();
	private List<BlockInfo> _otherBlockInfo = new ArrayList<BlockInfo>();

	private Player _player;
	private Random _random;
	private Block _centerBlock;
	private int _tickCount;
	private TreasureType _treasureType;

	private ChestData[] _chestData;
	private int _currentChest;

	private Reward[] _rewards;
	private int _currentReward;

	private boolean _finished;
	private int _finishedTickCount;

	private LinkedList<Animation> _animations;

	private HologramManager _hologramManager;

	public Treasure(Player player, Reward[] rewards, Block centerBlock, Block[] chestBlocks, TreasureType treasureType, BlockRestore blockRestore, HologramManager hologramManager)
	{
		this(player, new Random(), rewards, centerBlock, chestBlocks, treasureType, hologramManager);
		
		_blockRestore = blockRestore;
	}

	public Treasure(Player player, Random seed, Reward[] rewards, Block centerBlock, Block[] chestBlocks, TreasureType treasureType, HologramManager hologramManager)
	{
		_player = player;
		_random = seed;

		_treasureType = treasureType;

		_centerBlock = centerBlock;
		_animations = new LinkedList<Animation>();
		_hologramManager = hologramManager;

		_currentChest = 0;
		_currentReward = 0;
		_rewards = rewards;

		_chestData = new ChestData[chestBlocks.length];
		for (int i = 0; i < _chestData.length; i++)
		{
			_chestData[i] = new ChestData(chestBlocks[i]);
		}

		_animations.add(new BlockChangeAnimation(this, _otherBlockInfo));
	}

	public int getFinishedTickCount()
	{
		return _finishedTickCount;
	}

	public void update()
	{
		if (_finished)
		{
			_finishedTickCount++;
		}

		if (_tickCount % 10 == 0 && _currentChest < _chestData.length)
		{
			ChestSpawnAnimation chestSpawn = new ChestSpawnAnimation(this,_chestData[_currentChest].getBlock(), _chestBlockInfo, _centerBlock, _currentChest);
			_animations.add(chestSpawn);
			_currentChest++;
		}

		//Auto-open after 1 minute
		if (_tickCount == 60 * 20)
		{
			for (BlockInfo blockInfo : _chestBlockInfo)
			{
				Block block = blockInfo.getBlock();
				openChest(block, false);
			}
		}

		Block block = _player.getTargetBlock(null, 3);
		if (block.getType() == _treasureType.getMaterial())
		{
			ChestData data = getChestData(block);
			if (!isFinished() && data != null && !data.isOpened())
			{
				UtilParticle.ParticleType type = getTreasureType().getStyle().getHoverParticle();

				if (_treasureType == TreasureType.OLD)
				{
					UtilParticle.PlayParticle(type, block.getLocation().add(0.5, 0.5, 0.5), 0F, 0F, 0F, 1, 4,
							ViewDist.NORMAL, UtilServer.getPlayers());
				}
				else if (_treasureType == TreasureType.ANCIENT)
				{
					double yDif = 0.2 + 0.6 * Math.sin(Math.PI * (_tickCount / 10.0));
					double xDif = 0.7 * Math.sin(Math.PI * (_tickCount / 5.0));
					double zDif = 0.7 * Math.cos(Math.PI * (_tickCount / 5.0));
					float red = 0.1F + (float)( 0.4 * (1 + Math.cos(Math.PI * (_tickCount / 20.0))));
					UtilParticle.PlayParticle(type, block.getLocation().add(0.5 + xDif, 0.5 + yDif, 0.5 + zDif), red, 0.2F, 0.2F, 1F, 0,
							ViewDist.NORMAL, UtilServer.getPlayers());
				}
				else
				{
					UtilParticle.PlayParticle(type, block.getLocation().add(0.5, 0.5, 0.5), 0.5F, 0.5F, 0.5F, 0.2F, 0,
							ViewDist.NORMAL, UtilServer.getPlayers());
				}
			}
		}

		Iterator<Animation> taskIterator = _animations.iterator();
		while (taskIterator.hasNext())
		{
			Animation animation = taskIterator.next();

			if (animation.isRunning())
			{
				animation.run();
			}
			else
			{
				taskIterator.remove();
			}
		}

		_tickCount++;
	}

	public Block getCenterBlock()
	{
		return _centerBlock;
	}

	public void setBlock(Block block, Material material, byte data)
	{
		block.setType(material);
		block.setData(data);
		block.getLocation().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
	}

	public void openChest(Block block)
	{
		openChest(block, true);
	}

	public void openChest(Block block, boolean swapList)
	{
		ChestData data = getChestData(block);
		if (data != null && !data.isOpened() && _currentReward < _rewards.length)
		{
			Reward reward = _rewards[_currentReward];
			RewardData rewardData = reward.giveReward("Treasure", _player);
			_currentReward++;

			if (swapList)
			{
				BlockInfo info = getBlockInfo(block);
				_chestBlockInfo.remove(info);
				_openedChestBlockInfo.add(info);
			}

			data.setOpened(true);
			ChestOpenAnimation chestOpenTask = new ChestOpenAnimation(this, data, rewardData, _hologramManager);
			_animations.add(chestOpenTask);

			// Extra effects based off the rarity of the treasure
			if (reward.getRarity() == RewardRarity.UNCOMMON)
			{
				_animations.add(new LootUncommonAnimation(this, data.getBlock()));
			}
			else if (reward.getRarity() == RewardRarity.RARE)
			{
				_animations.add(new LootRareAnimation(this, data.getBlock().getLocation().add(0.5, 1.5, 0.5)));
				Bukkit.broadcastMessage(F.main("Treasure", F.name(_player.getName()) + " found " + C.cPurple + "Rare " + rewardData.getFriendlyName()));
			}
			else if (reward.getRarity() == RewardRarity.LEGENDARY)
			{
				_animations.add(new LootLegendaryAnimation(this, data.getBlock()));
				Bukkit.broadcastMessage(F.main("Treasure", F.name(_player.getName()) + " found " + C.cGreen + "Legendary " + rewardData.getFriendlyName()));
			}
			else if (reward.getRarity() == RewardRarity.MYTHICAL)
			{
				_animations.add(new LootMythicalAnimation(this, data.getBlock()));
				Bukkit.broadcastMessage(F.main("Treasure", F.name(_player.getName()) + " found " + C.cRed + "Mythical " + rewardData.getFriendlyName()));
			}

			if (isFinished())
			{
				TreasureRemoveAnimation animation = new TreasureRemoveAnimation(this, _openedChestBlockInfo, _chestBlockInfo);
				_animations.add(animation);
				_finished = true;
			}
		}
	}

	public BlockInfo getBlockInfo(Block block)
	{
		for (BlockInfo blockInfo : _chestBlockInfo)
		{
			if (blockInfo.getBlock().equals(block))
				return blockInfo;
		}
		return null;
	}

	public void sendChestOpenPackets(Player... players)
	{
		for (ChestData data : _chestData)
		{
			if (data.isOpened())
			{
				Block block = data.getBlock();
				PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(block.getX(), block.getY(), block.getZ(), CraftMagicNumbers.getBlock(block), 1, 1);

				for (Player player : players)
				{
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
				}

			}
		}

	}

	public ChestData getChestData(Block block)
	{
		for (ChestData data : _chestData)
		{
			if (data.getBlock().equals(block))
			{
				return data;
			}
		}
		return null;
	}

	public Player getPlayer()
	{
		return _player;
	}

	public boolean isFinished()
	{
		return _currentReward == _rewards.length;
	}

	public void cleanup()
	{

		for (int i = _currentReward; i < _rewards.length; i++)
		{
			_rewards[_currentReward].giveReward("Treasure", _player);
		}

		_currentReward = _rewards.length;

		// Remove any extra blocks
		resetBlockInfo(_chestBlockInfo);
		resetBlockInfo(_openedChestBlockInfo);
		resetBlockInfo(_otherBlockInfo);

		for (Animation animation : _animations)
		{
			animation.finish();
		}
		_animations.clear();
	}

	public void resetBlockInfo(List<BlockInfo> blockInfoSet)
	{
		for (BlockInfo blockInfo : blockInfoSet)
		{
			resetBlockInfo(blockInfo);
		}

		blockInfoSet.clear();
	}

	public void resetBlockInfo(BlockInfo blockInfo)
	{
		if (blockInfo == null)
			return;

		Block block = blockInfo.getBlock();

		if (block.getType().equals(Material.CHEST))
		{
			UtilParticle.PlayParticle(UtilParticle.ParticleType.LARGE_SMOKE, block.getLocation().add(0.5, 0.5, 0.5), 0.5F, 0.5F, 0.5F, 0.1F, 10,
					ViewDist.NORMAL, UtilServer.getPlayers());
//				block.getLocation().getWorld().createExplosion(block.getLocation().add(0.5, 0.5, 0.5), 0F);
		}
		block.getLocation().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());

		block.setTypeId(blockInfo.getId());
		block.setData(blockInfo.getData());
	}

	public boolean containsBlock(Block block)
	{
		for (BlockInfo info : _chestBlockInfo)
			if (info.getBlock().equals(block))
				return true;

		return false;
	}

	public TreasureType getTreasureType()
	{
		return _treasureType;
	}
}
