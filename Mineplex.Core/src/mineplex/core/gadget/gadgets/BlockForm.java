package mineplex.core.gadget.gadgets;

import java.util.ArrayList;
import java.util.List;

import mineplex.core.common.util.F;
import mineplex.core.common.util.MapUtil;
import mineplex.core.common.util.UtilBlock;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.common.util.UtilServer;
import mineplex.core.disguise.disguises.DisguiseCat;
import mineplex.core.disguise.disguises.DisguiseChicken;
import mineplex.core.gadget.event.GadgetBlockEvent;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.recharge.Recharge;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftFallingSand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class BlockForm
{
	private MorphBlock _host;
	private Player _player;
	
	private Material _mat;
	private Block _block;
	private Location _loc;

	public BlockForm(MorphBlock host, Player player, Material mat) 
	{
		_host = host;
		_player = player;
		
		_mat = mat;
		_loc = player.getLocation();
		
		Apply();
	}
 
	public void Apply() 
	{
		//Remove Old
		if (_player.getPassenger() != null)
		{
			Recharge.Instance.useForce(_player, "PassengerChange", 100);

			_player.getPassenger().remove();
			_player.eject();
		}

		((CraftEntity)_player).getHandle().getDataWatcher().watch(0, Byte.valueOf((byte) 32));

		//Player > Chicken
		DisguiseChicken disguise = new DisguiseChicken(_player);
		disguise.setBaby(); 
		disguise.setSoundDisguise(new DisguiseCat(_player));
		disguise.setInvisible(true);
		_host.Manager.getDisguiseManager().disguise(disguise);

		//Apply Falling Block
		FallingBlockCheck();

		//Inform
		String blockName = F.elem(ItemStackFactory.Instance.GetName(_mat, (byte)0, false));
		if (!blockName.contains("Block"))
			UtilPlayer.message(_player, F.main("Morph", "You are now a " + F.elem(ItemStackFactory.Instance.GetName(_mat, (byte)0, false) + " Block") + "!"));
		else
			UtilPlayer.message(_player, F.main("Morph", "You are now a " + F.elem(ItemStackFactory.Instance.GetName(_mat, (byte)0, false)) + "!"));

		//Sound
		_player.playSound(_player.getLocation(), Sound.ZOMBIE_UNFECT, 2f, 2f);
	}

	public void Remove() 
	{
		SolidifyRemove();

		_host.Manager.getDisguiseManager().undisguise(_player);

		//Remove FB
		if (_player.getPassenger() != null)
		{
			Recharge.Instance.useForce(_player, "PassengerChange", 100);

			_player.getPassenger().remove();
			_player.eject();
		}

		((CraftEntity)_player).getHandle().getDataWatcher().watch(0, Byte.valueOf((byte) 0));
	}

	public void SolidifyUpdate()
	{
		if (!_player.isSprinting())
			((CraftEntity)_player).getHandle().getDataWatcher().watch(0, Byte.valueOf((byte) 32));
		
		//Not a Block
		if (_block == null)
		{
			//Moved
			if (!_loc.getBlock().equals(_player.getLocation().getBlock()))
			{
				_player.setExp(0);
				_loc = _player.getLocation();
			}
			//Unmoved
			else
			{
				double hideBoost = 0.025;

				_player.setExp((float) Math.min(0.999f, _player.getExp() + hideBoost));

				//Set Block
				if (_player.getExp() >= 0.999f)
				{
					Block block = _player.getLocation().getBlock();

					List<Block> blockList = new ArrayList<Block>();
					blockList.add(block);
					
					GadgetBlockEvent event = new GadgetBlockEvent(_host, blockList);
					
					Bukkit.getServer().getPluginManager().callEvent(event);
					
					//Not Able
					if (block.getType() != Material.AIR || !UtilBlock.solid(block.getRelative(BlockFace.DOWN)) || event.getBlocks().isEmpty() || event.isCancelled())
					{
						UtilPlayer.message(_player, F.main("Morph", "You cannot become a Solid Block here."));
						_player.setExp(0f);
						return;
					}

					//Set Block
					_block = block;

					//Effect
					_player.playEffect(_player.getLocation(), Effect.STEP_SOUND, _mat);
					//block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, _mat);

					//Display
					SolidifyVisual();

					//Invisible
					//Host.Manager.GetCondition().Factory().Cloak("Disguised as Block", Player, Player, 60000, false, false);

					//Sound 
					_player.playSound(_player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
				}
			}
		}
		//Is a Block
		else
		{
			//Moved
			if (!_loc.getBlock().equals(_player.getLocation().getBlock()))
			{
				SolidifyRemove();
			}
			//Send Packets
			else
			{
				SolidifyVisual();
			}
		}	
	}

	public void SolidifyRemove()
	{
		if (_block != null)
		{
			MapUtil.QuickChangeBlockAt(_block.getLocation(), 0, (byte)0);
			_block = null;
		}

		_player.setExp(0f);

		//Host.Manager.GetCondition().EndCondition(Player, null, "Disguised as Block");

		//Inform
		_player.playSound(_player.getLocation(), Sound.NOTE_PLING, 1f, 0.5f);

		FallingBlockCheck();
	}

	@SuppressWarnings("deprecation")
	public void SolidifyVisual()
	{
		if (_block == null)
			return;

		//Remove Old
		if (_player.getPassenger() != null)
		{
			Recharge.Instance.useForce(_player, "PassengerChange", 100);

			_player.getPassenger().remove();
			_player.eject();
		}

		//Others
		for (Player other : UtilServer.getPlayers())
			other.sendBlockChange(_player.getLocation(), _mat, (byte)0);

		//Self
		_player.sendBlockChange(_player.getLocation(), 36, (byte)0);

		FallingBlockCheck();
	}

	public void FallingBlockCheck() 
	{
		//Block Form (Hide Falling)
		if (_block != null)
			return;

		//Recreate Falling
		if (_player.getPassenger() == null || !_player.getPassenger().isValid())
		{
			if (!Recharge.Instance.use(_player, "PassengerChange", 100, false, false))
				return;

			//Falling Block
			FallingBlock block = _player.getWorld().spawnFallingBlock(_player.getEyeLocation(), _mat, (byte)0);
			
			//No Arrow Collision
			((CraftFallingSand)block).getHandle().spectating = true;
			
			_player.setPassenger(block);
			
			_host.fallingBlockRegister(block);
		}

		//Ensure Falling doesnt Despawn
		else
		{
			((CraftFallingSand)_player.getPassenger()).getHandle().ticksLived = 1;
			_player.getPassenger().setTicksLived(1);
		}

	}

	public Block GetBlock() 
	{
		return _block;
	}
}
