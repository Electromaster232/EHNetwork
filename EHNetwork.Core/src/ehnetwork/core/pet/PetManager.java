package ehnetwork.core.pet;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityWither;
import net.minecraft.server.v1_7_R4.Navigation;

import ehnetwork.core.MiniClientPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.account.event.ClientWebResponseEvent;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilEnt;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.pet.repository.PetRepository;
import ehnetwork.core.pet.repository.token.ClientPetTokenWrapper;
import ehnetwork.core.pet.types.CustomWither;
import ehnetwork.core.updater.UpdateType;
import ehnetwork.core.updater.event.UpdateEvent;

public class PetManager extends MiniClientPlugin<PetClient>
{
	private static Object _petOwnerSynch = new Object();
	private static Object _petRenameSynch = new Object();
	
	private DisguiseManager _disguiseManager;
	private ehnetwork.core.creature.Creature _creatureModule;
	private PetRepository _repository;
	private PetFactory _petFactory;
	private BlockRestore _blockRestore;
	
	private NautHashMap<String, Creature> _activePetOwners;
	private NautHashMap<String, Integer> _failedAttempts;
	
	private NautHashMap<String, EntityType> _petOwnerQueue = new NautHashMap<String, EntityType>();
	private NautHashMap<String, String> _petRenameQueue = new NautHashMap<String, String>();
	private DonationManager _donationManager;
	private CoreClientManager _clientManager;
	
	public PetManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, DisguiseManager disguiseManager, ehnetwork.core.creature.Creature creatureModule, BlockRestore restore, String webAddress)
	{
		super("Pet Manager", plugin);
		
		_creatureModule = creatureModule;		
		_disguiseManager = disguiseManager;
		_repository = new PetRepository(webAddress);
		_petFactory = new PetFactory(_repository);
		_blockRestore = restore;
        _donationManager = donationManager;
        _clientManager = clientManager;
		
		_activePetOwners = new NautHashMap<String, Creature>();
		_failedAttempts = new NautHashMap<String, Integer>();
	}
	
	public void addPetOwnerToQueue(String playerName, EntityType entityType)
	{
		synchronized (_petOwnerSynch)
		{
			_petOwnerQueue.put(playerName, entityType);
		}
	}
	
	public void addRenamePetToQueue(String playerName, String petName) 
	{
		synchronized (_petRenameSynch)
		{
			_petRenameQueue.put(playerName, petName);
		}
	}
	
	@EventHandler
	public void processQueues(UpdateEvent event)
	{
		if (event.getType() != UpdateType.TICK)
			return;
		
		synchronized (_petOwnerSynch)
		{
			for (String playerName : _petOwnerQueue.keySet())
			{
				Player player = Bukkit.getPlayerExact(playerName);
				
				if (player != null && player.isOnline())
				{
					AddPetOwner(player, _petOwnerQueue.get(playerName), player.getLocation());
				}
			}
			
			_petOwnerQueue.clear();
		}
		
		synchronized (_petRenameQueue)
		{
			for (String playerName : _petRenameQueue.keySet())
			{
				Player player = Bukkit.getPlayerExact(playerName);
				
				if (player != null && player.isOnline())
				{
					getActivePet(playerName).setCustomNameVisible(true);
					getActivePet(playerName).setCustomName(_petRenameQueue.get(playerName));
				}
			}
			
			_petRenameQueue.clear();
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
	    Player p = event.getPlayer();
	    Rank rank = _clientManager.Get(p).GetRank();
	    if (rank == Rank.LEGEND || rank == Rank.ADMIN || rank == Rank.DEVELOPER || rank == Rank.OWNER)
	    {
	        _donationManager.Get(p.getName()).AddUnknownSalesPackagesOwned("Widder");
	    }
	}
	
	public void AddPetOwner(Player player, EntityType entityType, Location location)
	{
		if (_activePetOwners.containsKey(player.getName()))
		{
			if (_activePetOwners.get(player.getName()).getType() != entityType)
			{
				RemovePet(player, true);
			}
			else
				return;
		}
		
		Creature pet;
		
		if (entityType == EntityType.WITHER)
		{ 
		    _creatureModule.SetForce(true);
		    EntityWither wither = new CustomWither(((CraftWorld) location.getWorld()).getHandle());
		    wither.Silent = true;
		    wither.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
		    ((CraftWorld) location.getWorld()).getHandle().addEntity(wither, SpawnReason.CUSTOM); 
		    pet = (Creature) wither.getBukkitEntity();
		    _creatureModule.SetForce(false);
		    
            Entity silverfish = _creatureModule.SpawnEntity(location, EntityType.SILVERFISH);
            UtilEnt.Vegetate(silverfish, true);
            ((LivingEntity) silverfish).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
            pet.setPassenger(silverfish);
		}
		else
		{
		    pet = (Creature)_creatureModule.SpawnEntity(location, entityType);
		}
		
		//Named Pet
		if (Get(player).GetPets().get(entityType) != null && Get(player).GetPets().get(entityType).length() > 0)
		{
			//pet.setCustomNameVisible(true);
			pet.setCustomName(Get(player).GetPets().get(entityType));
		}

		if (pet instanceof Zombie)
		{
			((Zombie) pet).setBaby(true);
			pet.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN));
			pet.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0));
			UtilEnt.silence(pet, true);
		}
		else if (pet instanceof Villager)
		{
			((Villager) pet).setBaby();
			((Villager) pet).setAgeLock(true);
		}
		/*
		else if (pet instanceof Sheep)
		{
			DisguiseWither disguise = new DisguiseWither(pet);
			disguise.s(350);

			_disguiseManager.disguise(disguise);
			UtilEnt.silence(pet, true);
		}
		*/
		
		_activePetOwners.put(player.getName(), pet);
		_failedAttempts.put(player.getName(), 0);
		
		if (pet instanceof Ageable)
		{
			((Ageable)pet).setBaby();
			((Ageable)pet).setAgeLock(true);
		}
		
		UtilEnt.Vegetate(pet);
	}
	
	public Creature GetPet(Player player)
	{
		return _activePetOwners.get(player.getName());
	}
	
	public void RemovePet(final Player player, boolean removeOwner)
	{
		if (_activePetOwners.containsKey(player.getName()))
		{
			final Creature pet = _activePetOwners.get(player.getName());
			pet.remove();
			
			if (removeOwner)
			{
				_activePetOwners.remove(player.getName());
			}
		}
	}

	@EventHandler
	public void preventWolfBone(PlayerInteractEntityEvent event)
	{
		if (event.getPlayer().getItemInHand().getType() == Material.BONE)
		{
			event.setCancelled(true);
			event.getPlayer().updateInventory();
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		RemovePet(event.getPlayer(), true);
	}
	
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) 
    {
    	if (event.getEntity() instanceof Creature && _activePetOwners.containsValue((Creature)event.getEntity()))
    	{
    		event.setCancelled(true);
    	}
    }
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event)
    {
    	if (event.getEntity() instanceof Creature && _activePetOwners.containsValue((Creature)event.getEntity()))
    	{
    		if (event.getCause() == DamageCause.VOID)
    		{
    			String playerName = null;
    			
    			for (Entry<String,Creature> entry : _activePetOwners.entrySet())
    			{
    				if (entry.getValue() == event.getEntity())
    					playerName = entry.getKey();
    			}
    			
    			if (playerName != null)
    			{
    				Player player = Bukkit.getPlayerExact(playerName);
    				
    				if (player != null && player.isOnline())
    				{
    					RemovePet(player, true);
    				}
    			}
    		}
    		event.setCancelled(true);
    	}
    }
    
	@EventHandler
	public void onUpdate(UpdateEvent event)
	{
		if (event.getType() != UpdateType.FAST)
			return;
		
		int xDiff;
		int yDiff;
		int zDiff;
		
		Iterator<String> ownerIterator = _activePetOwners.keySet().iterator(); 
		
		while (ownerIterator.hasNext())
		{
			String playerName = ownerIterator.next();
			Player owner = Bukkit.getPlayer(playerName);
			
			Creature pet = _activePetOwners.get(playerName);
			Location petSpot = pet.getLocation();
			Location ownerSpot = owner.getLocation();
			xDiff = Math.abs(petSpot.getBlockX() - ownerSpot.getBlockX());
			yDiff = Math.abs(petSpot.getBlockY() - ownerSpot.getBlockY());
			zDiff = Math.abs(petSpot.getBlockZ() - ownerSpot.getBlockZ());
		
			if ((xDiff + yDiff + zDiff) > 4)
			{
				EntityCreature ec = ((CraftCreature) pet).getHandle();
	            Navigation nav = ec.getNavigation();
	            
	            int xIndex = -1;
	            int zIndex = -1;
	            Block targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
	            while (targetBlock.isEmpty() || targetBlock.isLiquid())
	            {
	            	if (xIndex < 2)
	            		xIndex++;
	            	else if (zIndex < 2)
	            	{
	            		xIndex = -1;
	            		zIndex++;
	            	}
	            	else
	            		return;
	            	
	            	targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
	            }
	            
	            float speed = 0.9f;
	            if (pet instanceof Villager)
	            	speed = 0.6f;
	            
	            if (_failedAttempts.get(playerName) > 4)
	            {
	            	pet.teleport(owner);
	            	_failedAttempts.put(playerName, 0);
	            }
	            else if (!nav.a(targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ(), speed))
	            {
	            	if (pet.getFallDistance() == 0)
	            	{
	            		_failedAttempts.put(playerName, _failedAttempts.get(playerName) + 1);
	            	}
	            }
	            else
	            {
	            	_failedAttempts.put(playerName, 0);
	            }
			}
		}
	}

	@EventHandler
	public void OnClientWebResponse(ClientWebResponseEvent event)
	{		
		ClientPetTokenWrapper token = new Gson().fromJson(event.GetResponse(), ClientPetTokenWrapper.class);
	
		Get(token.Name).Load(token.DonorToken);
	}
	
	@Override
	protected PetClient AddPlayer(String player)
	{
		return new PetClient();
	}

	public PetFactory GetFactory()
	{
		return _petFactory;
	}

	public PetRepository GetRepository()
	{
		return _repository;
	}

	public boolean hasActivePet(String name)
	{
		return _activePetOwners.containsKey(name);
	}

	public Creature getActivePet(String name)
	{
		return _activePetOwners.get(name);
	}
	
	public void DisableAll()
	{
		for (Player player : UtilServer.getPlayers())
			RemovePet(player, true);
	}

    public Collection<Creature> getPets()
    {
        return _activePetOwners.values();
    }
}
