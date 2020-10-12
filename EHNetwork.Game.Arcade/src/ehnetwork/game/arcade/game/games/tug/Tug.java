package ehnetwork.game.arcade.game.games.tug;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.UtilAlg;
import ehnetwork.core.common.util.UtilFirework;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;
import ehnetwork.game.arcade.ArcadeManager;
import ehnetwork.game.arcade.GameType;
import ehnetwork.game.arcade.game.TeamGame;
import ehnetwork.game.arcade.game.games.tug.kits.KitArcher;
import ehnetwork.game.arcade.game.games.tug.kits.KitSmasher;
import ehnetwork.game.arcade.kit.Kit;

public class Tug extends TeamGame
{
	private ArrayList<Location> _redMobs;
	private ArrayList<Location> _blueMobs;

	private ArrayList<Block> _redLives = new ArrayList<Block>();
	private ArrayList<Block> _blueLives = new ArrayList<Block>();

	private ArrayList<TugCreature> _redCreatures = new ArrayList<TugCreature>();
	private ArrayList<TugCreature> _blueCreatures = new ArrayList<TugCreature>();

	public Tug(ArcadeManager manager)
	{
		super(manager, GameType.Tug,

				new Kit[] 
						{ 

				new KitSmasher(manager),
				new KitArcher(manager),
						},

						new String[]
								{
				"Your animals are hungry",
				"Guide them to enemy crops",
				"Eat all enemy crops to win!"

								});

		this.HungerSet = 20;
		this.DeathOut = false;
		
		this.DeathSpectateSecs = 20;
	}

	@Override
	public void ParseData()
	{
		_redMobs = WorldData.GetDataLocs("RED");
		for (Location loc : WorldData.GetDataLocs("PINK"))
		{
			_redLives.add(loc.getBlock());
			loc.getBlock().getRelative(BlockFace.DOWN).setTypeIdAndData(60, (byte)0, true);
			loc.getBlock().setTypeIdAndData(59, (byte)7, true);
		}


		_blueMobs = WorldData.GetDataLocs("BLUE");
		for (Location loc : WorldData.GetDataLocs("LIGHT_BLUE"))
		{
			_blueLives.add(loc.getBlock());
			loc.getBlock().getRelative(BlockFace.DOWN).setTypeIdAndData(60, (byte)0, true);
			loc.getBlock().setTypeIdAndData(141, (byte)7, true);
		}
	}

	@EventHandler
	public void CreatureTarget(EntityTargetEvent event)
	{
		event.setCancelled(true);
	}

	/*
	@EventHandler
	public void LivesFix(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() != UpdateType.TICK)
			return;

		for (Block block : _redLives)
		{
			if (block.getTypeId() != 59)
			{
				block.getRelative(BlockFace.DOWN).setTypeIdAndData(60, (byte)0, true);
				block.setTypeIdAndData(59, (byte)7, true);
			}
		}

		for (Block block : _blueLives)
		{
			if (block.getTypeId() != 141)
			{
				block.getRelative(BlockFace.DOWN).setTypeIdAndData(60, (byte)0, true);
				block.setTypeIdAndData(141, (byte)7, true);
			}
		}
	}
	*/

	@EventHandler
	public void CreatureUpdate(UpdateEvent event)
	{
		if (!IsLive())
			return;

		if (event.getType() == UpdateType.FAST)
		{
			if (_redCreatures.size() < 30)
			{
				this.CreatureAllowOverride = true;
				Location loc = UtilAlg.Random(_redMobs);
				Creature ent = loc.getWorld().spawn(loc, Pig.class);
				ent.setMaxHealth(10);
				ent.setHealth(10);
				this.CreatureAllowOverride = false;

				_redCreatures.add(new TugCreature(this, ent, _blueLives));
			}

			if (_blueCreatures.size() < 30)
			{
				this.CreatureAllowOverride = true;
				Location loc = UtilAlg.Random(_blueMobs);
				Creature ent = loc.getWorld().spawn(loc, Sheep.class);
				ent.setMaxHealth(10);
				ent.setHealth(10);
				this.CreatureAllowOverride = false;

				_blueCreatures.add(new TugCreature(this, ent, _redLives));
			}
		}

		
		if (event.getType() == UpdateType.TICK)
		{
			//TimingManager.start("Creature Move");
			
			//Target
			for (TugCreature ent : _redCreatures)
			{
				ent.Move(_blueLives);
			}

			//Target
			for (TugCreature ent : _blueCreatures)
			{
				ent.Move(_redLives);
			}
			
			//TimingManager.stop("Creature Move");
		}
		

		else if (event.getType() == UpdateType.FAST)
		{
			//Target
			for (TugCreature ent : _redCreatures)
			{
				ent.FindTarget(_blueCreatures, GetTeam(ChatColor.AQUA).GetPlayers(true));
			}

			//Target
			for (TugCreature ent : _blueCreatures)
			{
				ent.FindTarget(_redCreatures, GetTeam(ChatColor.RED).GetPlayers(true));
			}

			//Eat 
			Eat(_redCreatures, _blueLives);
			Eat(_blueCreatures, _redLives);
		}
		
		
	}

	public void Eat(ArrayList<TugCreature> ents, ArrayList<Block> lives)
	{
		Iterator<TugCreature> entIterator = ents.iterator();

		while (entIterator.hasNext())
		{
			TugCreature ent = entIterator.next();

			if (!ent.Entity.isValid())
			{
				ent.Entity.remove();
				entIterator.remove();
				continue;
			}

			Iterator<Block> blockIterator = lives.iterator();

			while (blockIterator.hasNext())
			{
				Block block = blockIterator.next();

				if (UtilMath.offset(ent.Entity.getLocation(), block.getLocation().add(0.5, 0, 0.5)) < 1)
				{
					blockIterator.remove();
					entIterator.remove();

					//Effect
					ent.Entity.getWorld().playSound(ent.Entity.getLocation(), Sound.EAT, 2f, 1f);
					ent.Entity.getWorld().playSound(ent.Entity.getLocation(), Sound.EAT, 2f, 1f);
					ent.Entity.remove();

					block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
					block.setType(Material.AIR);

					//Firework
					if (ent instanceof Pig)
						UtilFirework.playFirework(block.getLocation().add(0.5, 0.5, 0.5), 
								FireworkEffect.builder().flicker(false).withColor(Color.AQUA).with(Type.BURST).trail(true).build());
					else
						UtilFirework.playFirework(block.getLocation().add(0.5, 0.5, 0.5), 
								FireworkEffect.builder().flicker(false).withColor(Color.RED).with(Type.BURST).trail(true).build());

					EndLivesCheck();
					
					break;
				}
			}
		}
	}

	public void EndLivesCheck()
	{
		if (_redLives.isEmpty())
		{
			AnnounceEnd(GetTeam(ChatColor.AQUA));
			SetState(GameState.End);	
		}

		else if (_blueLives.isEmpty())
		{
			AnnounceEnd(GetTeam(ChatColor.RED));
			SetState(GameState.End);	
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void CropTrample(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.PHYSICAL)
			return;

		if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.SOIL)
			return;
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void CropTrample(EntityInteractEvent event)
	{
		if (event.getBlock() == null)
			return;
		
		System.out.println(event.getBlock().getType());

		if (event.getBlock().getType() != Material.SOIL)
			return;

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void CropTrample(EntityChangeBlockEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void CreatureDeath(EntityDeathEvent event)
	{
		event.getDrops().clear();
	}

	@Override
	@EventHandler
	public void ScoreboardUpdate(UpdateEvent event)
	{
		if (event != null && event.getType() != UpdateType.FAST)
			return;

		ScoreboardWrite();
	}

	public void ScoreboardWrite()
	{
		Scoreboard.Reset();


		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cRed + "Pig Team");
		Scoreboard.Write(_redLives.size() + " Wheat");

		Scoreboard.WriteBlank();
		Scoreboard.Write(C.cAqua + "Sheep Team");
		Scoreboard.Write(_blueLives.size() + " Carrots");

		Scoreboard.Draw();
	}
}
