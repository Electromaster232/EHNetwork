package ehnetwork.core.gadget;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.blockrestore.BlockRestore;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.common.util.UtilMath;
import ehnetwork.core.common.util.UtilServer;
import ehnetwork.core.common.util.UtilTime;
import ehnetwork.core.disguise.DisguiseManager;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.gadget.event.GadgetCollideEntityEvent;
import ehnetwork.core.gadget.gadgets.*;
import ehnetwork.core.gadget.types.Gadget;
import ehnetwork.core.gadget.types.GadgetType;
import ehnetwork.core.gadget.types.ItemGadget;
import ehnetwork.core.gadget.types.MorphGadget;
import ehnetwork.core.gadget.types.MusicGadget;
import ehnetwork.core.gadget.types.OutfitGadget;
import ehnetwork.core.gadget.types.OutfitGadget.ArmorSlot;
import ehnetwork.core.gadget.types.ParticleGadget;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.mount.MountManager;
import ehnetwork.core.pet.PetManager;
import ehnetwork.core.preferences.PreferencesManager;
import ehnetwork.core.projectile.ProjectileManager;

public class GadgetManager extends MiniPlugin
{
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private InventoryManager _inventoryManager;
	private PetManager _petManager;
	private PreferencesManager _preferencesManager;
	private DisguiseManager _disguiseManager;
	private BlockRestore _blockRestore;
	private ProjectileManager _projectileManager;

	private NautHashMap<GadgetType, List<Gadget>> _gadgets;

	private NautHashMap<Player, Long> _lastMove = new NautHashMap<Player, Long>();
	private NautHashMap<Player, NautHashMap<GadgetType, Gadget>> _playerActiveGadgetMap = new NautHashMap<Player, NautHashMap<GadgetType, Gadget>>();
	
	private boolean _hideParticles = false;
	private int _activeItemSlot = 3;

	public GadgetManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, InventoryManager inventoryManager,
			MountManager mountManager, PetManager petManager, PreferencesManager preferencesManager,
			DisguiseManager disguiseManager, BlockRestore blockRestore, ProjectileManager projectileManager)
	{
		super("Gadget Manager", plugin);

		_clientManager = clientManager;
		_donationManager = donationManager;
		_inventoryManager = inventoryManager;
		_petManager = petManager;
		_preferencesManager = preferencesManager;
		_disguiseManager = disguiseManager;
		_blockRestore = blockRestore;
		_projectileManager = projectileManager;

		CreateGadgets();
	}

	private void CreateGadgets()
	{
		_gadgets = new NautHashMap<GadgetType, List<Gadget>>();
 
		// Items
		addGadget(new ItemEtherealPearl(this));
		addGadget(new ItemFirework(this));
		addGadget(new ItemTNT(this));
		addGadget(new ItemMelonLauncher(this));
		addGadget(new ItemFleshHook(this));
		addGadget(new ItemPaintballGun(this));
		addGadget(new ItemBatGun(this));
		addGadget(new ItemCoinBomb(this));
		addGadget(new ItemGemBomb(this));
		addGadget(new ItemFootball(this));
		addGadget(new ItemDuelingSword(this));
		
		// Costume
		addGadget(new OutfitRaveSuit(this, "Rave Hat", -2, ArmorSlot.Helmet, Material.LEATHER_HELMET, (byte)0));
		addGadget(new OutfitRaveSuit(this, "Rave Shirt", -2, ArmorSlot.Chest, Material.LEATHER_CHESTPLATE, (byte)0));
		addGadget(new OutfitRaveSuit(this, "Rave Pants", -2, ArmorSlot.Legs, Material.LEATHER_LEGGINGS, (byte)0));
		addGadget(new OutfitRaveSuit(this, "Rave Boots", -2, ArmorSlot.Boots, Material.LEATHER_BOOTS, (byte)0));
		
		addGadget(new OutfitSpaceSuit(this, "Space Helmet", -2, ArmorSlot.Helmet, Material.GLASS, (byte)0));
		addGadget(new OutfitSpaceSuit(this, "Space Jacket", -2, ArmorSlot.Chest, Material.GOLD_CHESTPLATE, (byte)0));
		addGadget(new OutfitSpaceSuit(this, "Space Pants", -2, ArmorSlot.Legs, Material.GOLD_LEGGINGS, (byte)0));
		addGadget(new OutfitSpaceSuit(this, "Space Boots", -2, ArmorSlot.Boots, Material.GOLD_BOOTS, (byte)0));
		
		// Morphs
		addGadget(new MorphVillager(this));
		addGadget(new MorphCow(this));
		addGadget(new MorphChicken(this));
		addGadget(new MorphBlock(this));
		addGadget(new MorphEnderman(this));
		addGadget(new MorphBat(this));
		//addGadget(new MorphNotch(this));
		addGadget(new MorphPumpkinKing(this));
		addGadget(new MorphPig(this));
		addGadget(new MorphCreeper(this));
		addGadget(new MorphBlaze(this));
		//addGadget(new MorphGeno(this));
		addGadget(new MorphWither(this));
		addGadget(new MorphBunny(this));
		
		// Particles
		addGadget(new ParticleFoot(this));
		addGadget(new ParticleEnchant(this));
		addGadget(new ParticleFireRings(this));
		addGadget(new ParticleRain(this)); 
		addGadget(new ParticleHelix(this));
		addGadget(new ParticleGreen(this));
		addGadget(new ParticleHeart(this));
		addGadget(new ParticleFairy(this));
		addGadget(new ParticleLegend(this));
		addGadget(new ParticleBlizzard(this));
		
		// Music
		addGadget(new MusicGadget(this, "13 Disc", new String[] {""}, -2, 2256, 178000));
		addGadget(new MusicGadget(this, "Cat Disc", new String[] {""}, -2, 2257, 185000));
		addGadget(new MusicGadget(this, "Blocks Disc", new String[] {""}, -2, 2258, 345000));
		addGadget(new MusicGadget(this, "Chirp Disc", new String[] {""}, -2, 2259, 185000));
		addGadget(new MusicGadget(this, "Far Disc", new String[] {""}, -2, 2260, 174000));
		addGadget(new MusicGadget(this, "Mall Disc", new String[] {""}, -2, 2261, 197000));
		addGadget(new MusicGadget(this, "Mellohi Disc", new String[] {""}, -2, 2262, 96000));
		addGadget(new MusicGadget(this, "Stal Disc", new String[] {""}, -2, 2263, 150000));
		addGadget(new MusicGadget(this, "Strad Disc", new String[] {""}, -2, 2264, 188000));
		addGadget(new MusicGadget(this, "Ward Disc", new String[] {""}, -2, 2265, 251000));
		//addGadget(new MusicGadget(this, "11 Disc", new String[] {""}, -2, 2266, 71000));
		addGadget(new MusicGadget(this, "Wait Disc", new String[] {""}, -2, 2267, 238000));
	}

	private void addGadget(Gadget gadget)
	{
		if (!_gadgets.containsKey(gadget.getGadgetType()))
			_gadgets.put(gadget.getGadgetType(), new ArrayList<Gadget>());

		_gadgets.get(gadget.getGadgetType()).add(gadget);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if (_clientManager.Get(event.getPlayer()).GetRank().Has(Rank.MODERATOR))
		{
			for (GadgetType gadgetType : _gadgets.keySet())
			{
				if (gadgetType == GadgetType.Particle && _clientManager.Get(event.getPlayer()).GetRank().Has(Rank.ADMIN))
				{
					for (Gadget gadget : _gadgets.get(gadgetType))
					{
						_donationManager.Get(event.getPlayer().getName()).AddUnknownSalesPackagesOwned(gadget.GetName());
					}
				}
			}
		}
	}

	public List<Gadget> getGadgets(GadgetType gadgetType)
	{
		return _gadgets.get(gadgetType);
	}

	// Disallows two armor gadgets in same slot.
	public void RemoveOutfit(Player player, ArmorSlot slot)
	{
		for (GadgetType gadgetType : _gadgets.keySet())
		{
			for (Gadget gadget : _gadgets.get(gadgetType))
			{
				if (gadget instanceof OutfitGadget)
				{
					OutfitGadget armor = (OutfitGadget) gadget;

					if (armor.GetSlot() == slot)
					{
						armor.RemoveArmor(player);
					}
				}
			}
		}
	}

	public void RemoveItem(Player player)
	{
		for (GadgetType gadgetType : _gadgets.keySet())
		{
			for (Gadget gadget : _gadgets.get(gadgetType))
			{
				if (gadget instanceof ItemGadget)
				{
					ItemGadget item = (ItemGadget) gadget;

					item.RemoveItem(player);
				}
			}
		}
	}

	public void RemoveParticle(Player player)
	{
		for (GadgetType gadgetType : _gadgets.keySet())
		{
			for (Gadget gadget : _gadgets.get(gadgetType))
			{
				if (gadget instanceof ParticleGadget)
				{
					ParticleGadget part = (ParticleGadget) gadget;

					part.Disable(player);
				}
			}
		}
	}
	
	public void RemoveMorph(Player player)
	{
		for (GadgetType gadgetType : _gadgets.keySet())
		{
			for (Gadget gadget : _gadgets.get(gadgetType))
			{
				if (gadget instanceof MorphGadget)
				{
					MorphGadget part = (MorphGadget) gadget;

					part.Disable(player);
				}
			}
		}
	}
	
	public void DisableAll()
	{
		for (GadgetType gadgetType : _gadgets.keySet())
		{
			for (Gadget gadget : _gadgets.get(gadgetType))
			{
				if (gadget instanceof ParticleGadget)
					continue;
				
				for (Player player : UtilServer.getPlayers())
					gadget.Disable(player);
			}
		}
	}
	
	public void DisableAll(Player player)
	{
		for (GadgetType gadgetType : _gadgets.keySet())
		{
			for (Gadget gadget : _gadgets.get(gadgetType))
			{
				gadget.Disable(player);
			}
		}
	}

	public void EnableAll(Player player){

		for (GadgetType gadgetType : _gadgets.keySet())
		{
			for (Gadget gadget : _gadgets.get(gadgetType))
			{
				gadget.Enable(player);
			}
		}
	}

	public PetManager getPetManager()
	{
		return _petManager;
	}
	
	public CoreClientManager getClientManager()
	{
		return _clientManager;
	}

	public DonationManager getDonationManager()
	{
		return _donationManager;
	}

	public PreferencesManager getPreferencesManager()
	{
		return _preferencesManager;
	}
	
	public ProjectileManager getProjectileManager()
	{
		return _projectileManager;
	}

	public DisguiseManager getDisguiseManager()
	{
		return _disguiseManager;
	}
	
	public InventoryManager getInventoryManager()
	{
		return _inventoryManager;
	}

	public boolean collideEvent(Gadget gadget, Player other)
	{
		GadgetCollideEntityEvent collideEvent = new GadgetCollideEntityEvent(gadget, other);

		Bukkit.getServer().getPluginManager().callEvent(collideEvent);

		return collideEvent.isCancelled();
	}

	public BlockRestore getBlockRestore()
	{
		return _blockRestore;
	}

	@EventHandler
	public void setMoving(PlayerMoveEvent event)
	{
		if (UtilMath.offset(event.getFrom(), event.getTo()) <= 0)
			return;

		_lastMove.put(event.getPlayer(), System.currentTimeMillis());
	}

	public boolean isMoving(Player player)
	{
		if (!_lastMove.containsKey(player))
			return false;

		return !UtilTime.elapsed(_lastMove.get(player), 500);
	}

	@EventHandler
	public void quit(PlayerQuitEvent event)
	{
		DisableAll(event.getPlayer());
		
		_lastMove.remove(event.getPlayer());

		_playerActiveGadgetMap.remove(event.getPlayer());
	}
	
	@EventHandler
	public void death(PlayerDeathEvent event)
	{
		_lastMove.remove(event.getEntity());

		_playerActiveGadgetMap.remove(event.getEntity());
	}

	public void setActive(Player player, Gadget gadget)
	{
		if (!_playerActiveGadgetMap.containsKey(player))
			_playerActiveGadgetMap.put(player, new NautHashMap<GadgetType, Gadget>());

		_playerActiveGadgetMap.get(player).put(gadget.getGadgetType(), gadget);
	}

	public Gadget getActive(Player player, GadgetType gadgetType)
	{
		if (!_playerActiveGadgetMap.containsKey(player))
			_playerActiveGadgetMap.put(player, new NautHashMap<GadgetType, Gadget>());

		return _playerActiveGadgetMap.get(player).get(gadgetType);
	}

	public void removeActive(Player player, Gadget gadget)
	{
		if (!_playerActiveGadgetMap.containsKey(player))
			_playerActiveGadgetMap.put(player, new NautHashMap<GadgetType, Gadget>());

		_playerActiveGadgetMap.get(player).remove(gadget.getGadgetType());
	}
	
	public void setHideParticles(boolean b)
	{
		_hideParticles = b;
	}

	public boolean hideParticles()
	{
		return _hideParticles;
	}

	public void setActiveItemSlot(int i)
	{
		_activeItemSlot = i;
	}
	
	public int getActiveItemSlot()
	{
		return _activeItemSlot;
	}

	public void redisplayActiveItem(Player player)
	{
		for (Gadget gadget : _gadgets.get(GadgetType.Item))
		{
			if (gadget instanceof ItemGadget)
			{
				if (gadget.IsActive(player))
				{
					((ItemGadget)gadget).ApplyItem(player, false);
				}
			}
		}
	}

	public boolean canPlaySongAt(Location location)
	{
		for (Gadget gadget : _gadgets.get(GadgetType.MusicDisc))
		{
			if (gadget instanceof MusicGadget)
			{
				if (!((MusicGadget)gadget).canPlayAt(location))
				{
					return false;
				}
			}	
		}
		
		return true;
	}
	
	@EventHandler
	public void chissMeow(PlayerToggleSneakEvent event)
	{
		if (event.getPlayer().getName().equals("Chiss"))
		{
			if (!event.getPlayer().isSneaking())
			{
				event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.CAT_MEOW, 1f, 1f);
			}
		}
	}
}
