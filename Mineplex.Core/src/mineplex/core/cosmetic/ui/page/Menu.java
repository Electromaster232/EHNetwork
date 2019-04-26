package mineplex.core.cosmetic.ui.page;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.CurrencyType;
import mineplex.core.common.util.C;
import mineplex.core.cosmetic.CosmeticManager;
import mineplex.core.cosmetic.ui.CosmeticShop;
import mineplex.core.cosmetic.ui.button.OpenCostumes;
import mineplex.core.cosmetic.ui.button.OpenGadgets;
import mineplex.core.cosmetic.ui.button.OpenMorphs;
import mineplex.core.cosmetic.ui.button.OpenMounts;
import mineplex.core.cosmetic.ui.button.OpenMusic;
import mineplex.core.cosmetic.ui.button.OpenParticles;
import mineplex.core.cosmetic.ui.button.OpenPets;
import mineplex.core.donation.DonationManager;
import mineplex.core.gadget.types.Gadget;
import mineplex.core.gadget.types.GadgetType;
import mineplex.core.mount.Mount;
import mineplex.core.shop.item.IButton;
import mineplex.core.shop.item.ShopItem;
import mineplex.core.shop.page.ConfirmationPage;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.core.treasure.TreasureKey;

public class Menu extends ShopPageBase<CosmeticManager, CosmeticShop>
{
	public Menu(CosmeticManager plugin, CosmeticShop shop, CoreClientManager clientManager, DonationManager donationManager, Player player)
	{
		super(plugin, shop, clientManager, donationManager, "Inventory", player);

		buildPage();
	}

	@Override
	protected void buildPage()
	{ 
		addItem(4, new ShopItem(175, getDonationManager().Get(getPlayer().getName()).getCoins() + " Coins", new String[]
				{
						" ",
						ChatColor.RESET + C.cYellow + "Request Coins",
						ChatColor.RESET + "https://discord.gg/FttmSEQ",
						" ",
						ChatColor.RESET + C.cAqua + "Ultra Rank",
						ChatColor.RESET + "Receives 7500 Coins per Month",
						" ",
						ChatColor.RESET + C.cPurple + "Hero Rank",
						ChatColor.RESET + "Receives 15000 Coins per Month",
						" ",
						ChatColor.RESET + C.cGreen + "Legend Rank",
						ChatColor.RESET + "Recieves 30000 Coins per Month"
				}, 1, false));

		addButton(18, new ShopItem(Material.NETHER_STAR, "Particle Effects", 1, false), new OpenParticles(this));
		addButton(20, new ShopItem(Material.BOW, "Gadgets", 1, false), new OpenGadgets(this));
		addButton(22, new ShopItem(Material.LEATHER, "Morphs", 1, false), new OpenMorphs(this));
		addButton(24, new ShopItem(Material.IRON_BARDING, "Mounts", 1, false), new OpenMounts(this));
		addButton(26, new ShopItem(Material.BONE, "Pets", 1, false), new OpenPets(this));
		//
		addButton(48, new ShopItem(Material.GOLD_CHESTPLATE, "Costumes", 1, false), new OpenCostumes(this));
		addButton(50, new ShopItem(Material.GREEN_RECORD, "Music", 1, false), new OpenMusic(this));

		if (getPlugin().getGadgetManager().getActive(getPlayer(), GadgetType.Particle) != null)
		{
			final Gadget gadget = getPlugin().getGadgetManager().getActive(getPlayer(), GadgetType.Particle);

			addButton(18 + 9,
					new ShopItem(gadget.GetDisplayMaterial(), gadget.GetDisplayData(), ChatColor.RESET + C.mItem + gadget.GetName(), new String[]{}, 1, false, false),
					new IButton()
					{
						public void onClick(Player player, ClickType clickType)
						{
							playAcceptSound(player);
							gadget.Disable(player);
							refresh();
						}
					});
		}

		if (getPlugin().getGadgetManager().getActive(getPlayer(), GadgetType.Item) != null)
		{
			final Gadget gadget = getPlugin().getGadgetManager().getActive(getPlayer(), GadgetType.Item);

			addButton(20 + 9,
					new ShopItem(gadget.GetDisplayMaterial(), gadget.GetDisplayData(), ChatColor.RESET + C.mItem + gadget.GetName(), new String[]{}, 1, false, false),
					new IButton()
					{
						public void onClick(Player player, ClickType clickType)
						{
							playAcceptSound(player);
							gadget.Disable(player);
							refresh();
						}
					});
		}
		
		if (getPlugin().getGadgetManager().getActive(getPlayer(), GadgetType.Morph) != null)
		{
			final Gadget gadget = getPlugin().getGadgetManager().getActive(getPlayer(), GadgetType.Morph);

			addButton(22 + 9,
					new ShopItem(gadget.GetDisplayMaterial(), gadget.GetDisplayData(), ChatColor.RESET + C.mItem + gadget.GetName(), new String[]{}, 1, false, false),
					new IButton()
					{
						public void onClick(Player player, ClickType clickType)
						{
							playAcceptSound(player);
							gadget.Disable(player);
							refresh();
						}
					});
		}

		if (getPlugin().getMountManager().getActive(getPlayer()) != null)
		{
			final Mount<?> mount = getPlugin().getMountManager().getActive(getPlayer());

			addButton(24 + 9,
					new ShopItem(mount.GetDisplayMaterial(), mount.GetDisplayData(), ChatColor.RESET + C.mItem + mount.GetName(), new String[]{}, 1, false, false),
					new IButton()
					{
						public void onClick(Player player, ClickType clickType)
						{
							playAcceptSound(player);
							mount.Disable(player);
							refresh();
						}
					});
		}

		if (getPlugin().getPetManager().hasActivePet(getPlayer().getName()))
		{
		    Creature activePet = getPlugin().getPetManager().getActivePet(getPlayer().getName());
		    String petName = activePet.getType() == EntityType.WITHER ? "Widder" : activePet.getCustomName();
			addButton(26 + 9, new ShopItem(Material.MONSTER_EGG, (byte) getPlugin().getPetManager().getActivePet(getPlayer().getName()).getType().getTypeId(), ChatColor.RESET + C.mItem + petName, new String[]{}, 1, false, false),
					new IButton()
					{
						public void onClick(Player player, ClickType clickType)
						{
							playAcceptSound(player);
							getPlugin().getPetManager().RemovePet(player, true);
							refresh();
						}
					});
		}
	}

	public void openCostumes(Player player)
	{
		getShop().openPageForPlayer(player, new CostumePage(getPlugin(), getShop(), getClientManager(), getDonationManager(), "Costumes", player));
	}

	public void openMusic(Player player)
	{
		getShop().openPageForPlayer(player, new MusicPage(getPlugin(), getShop(), getClientManager(), getDonationManager(), "Music", player));
	}

	public void attemptPurchaseKey(Player player)
	{
		getShop().openPageForPlayer(player, new ConfirmationPage<CosmeticManager, CosmeticShop>(getPlugin(), getShop(), getClientManager(), getDonationManager(), new Runnable()
		{
			public void run()
			{
				getPlugin().getInventoryManager().addItemToInventory(getPlayer(), "Treasure", "Treasure Key", 1);
				refresh();
			}
		}, this, new TreasureKey(), CurrencyType.Coins, getPlayer()));
	}
}
