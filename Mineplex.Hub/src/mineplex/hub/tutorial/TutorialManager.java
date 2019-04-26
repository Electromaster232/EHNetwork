package mineplex.hub.tutorial;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mineplex.core.MiniPlugin;
import mineplex.core.common.util.C;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.F;
import mineplex.core.common.util.UtilPlayer;
import mineplex.core.donation.DonationManager;
import mineplex.core.task.TaskManager;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;
import mineplex.hub.HubManager;
import mineplex.hub.modules.TextManager;
import mineplex.hub.tutorial.types.PartyTutorial;
import mineplex.hub.tutorial.types.WelcomeTutorial;

public class TutorialManager extends MiniPlugin
{
	//Tutorials
	private HashSet<Tutorial> _tutorials;
	
	//Modules
	protected DonationManager _donationManager;
	protected TaskManager _taskManager;
	
	public TutorialManager(HubManager manager, DonationManager donation, TaskManager task, TextManager text) 
	{
		super("Tutorial Manager", manager.getPlugin());
		
		_taskManager = task;
		_donationManager = donation;
		
		_tutorials = new HashSet<Tutorial>();
		
		_tutorials.add(new WelcomeTutorial(manager, text));
		_tutorials.add(new PartyTutorial(manager));
	}
	
	@EventHandler
	public void EntityInteract(PlayerInteractEntityEvent event)
	{
		if (InTutorial(event.getPlayer()))
			return;
		
		if (!(event.getRightClicked() instanceof LivingEntity))
			return;
		
		LivingEntity ent = (LivingEntity)event.getRightClicked();
		
		String name = ent.getCustomName();
		if (name == null)
			return;
		
		for (Tutorial tut : _tutorials)
		{
			if (name.contains(tut.GetTutName()))
			{
				UtilPlayer.message(event.getPlayer(), F.main("Tutorial", "You started " + F.elem(tut.GetTutName()) + "."));
				tut.BeginTutorial(event.getPlayer());
				return;
			}
		}
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		for (Tutorial tut : _tutorials)
			tut.EndTutorial(event.getPlayer());
	}

	@EventHandler
	public void InteractCancel(PlayerInteractEvent event)
	{
		if (InTutorial(event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void MoveCancel(PlayerMoveEvent event)
	{
		if (InTutorial(event.getPlayer()))
			event.setTo(event.getFrom());
	}

	@EventHandler
	public void Update(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		for (final Tutorial tut : _tutorials)
		{
			Iterator<Player> tuteIterator = tut.GetTutorial().keySet().iterator();

			while (tuteIterator.hasNext())
			{
				final Player player = tuteIterator.next();
				TutorialData data = tut.GetTutorial().get(player);

				//Check if Phase Completed
				if (data.Update())
				{
					//Next Phase
					if (data.PhaseStep < tut.GetPhases().size())
					{
						data.SetNextPhase(tut.GetPhases().get(data.PhaseStep));
					}
					//End Tutorial
					else
					{
						tuteIterator.remove();
						
						//Inform
						UtilPlayer.message(player, F.main("Tutorial", "You completed " + F.elem(tut.GetTutName()) + "."));
						
						//Gems
						if (!_taskManager.hasCompletedTask(player, tut.GetTask()))		
						{
							_taskManager.completedTask(new Callback<Boolean>()
							{
								public void run(Boolean completed)
								{
									_donationManager.RewardGems(new Callback<Boolean>() 
									{
										public void run(Boolean completed)
										{
											if (completed)
											{
												UtilPlayer.message(player, F.main("Tutorial", "You received " + F.elem(C.cGreen + tut.GetGems() + " Gems") + "."));
												
												//Sound
												player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 1.5f);
											}
										}
									}, "Tutorial " + tut.GetTutName(), player.getName(), player.getUniqueId(), tut.GetGems());									
								}
							}, player, tut.GetTask());
						}
					}
				}
			}
		}
	}
	
	public boolean InTutorial(Player player)
	{
		for (Tutorial tut : _tutorials)
			if (tut.InTutorial(player))
				return true;
		
		return false;
	}
}
