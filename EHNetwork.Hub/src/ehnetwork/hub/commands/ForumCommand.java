package ehnetwork.hub.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import ehnetwork.core.command.CommandBase;
import ehnetwork.core.common.Rank;
import ehnetwork.core.common.jsonchat.ChildJsonMessage;
import ehnetwork.core.common.jsonchat.JsonMessage;
import ehnetwork.core.common.util.C;
import ehnetwork.core.common.util.F;
import ehnetwork.core.common.util.UtilPlayer;
import ehnetwork.core.itemstack.ItemStackFactory;
import ehnetwork.hub.HubManager;

public class ForumCommand extends CommandBase<HubManager>
{
	private HubManager _manager;
	public ForumCommand(HubManager plugin)
	{
		super(plugin, Rank.ALL, "forum");
		_manager = plugin;
	}

	@Override
	public void Execute(Player caller, String[] args)
	{
		if (args[0].equalsIgnoreCase("list")){
			UtilPlayer.message(caller, F.main("Forum", "Fetching Posts..."));
			caller.playSound(caller.getLocation(), Sound.NOTE_PLING, 1f, 1f);
			ArrayList<ChildJsonMessage> pendingLines = new ArrayList<ChildJsonMessage>();
			String[] oof = _manager.GetForum().getPosts("0");
			UtilPlayer.message(caller, F.main("Forum", "Here are the posts:"));
			ChildJsonMessage message = new JsonMessage("").color("white").extra("").color("white");
			int value = 1;
			for (String oof2 : oof){
				message.add("\n" + oof2 + C.cGray + " Post ID:" + value).color("yellow").click("run_command", "/forum showpost " + value);
				value = value + 1;
			}
			pendingLines.add(message);
			for (JsonMessage oof3 : pendingLines){
				oof3.sendToPlayer(caller);
			}
		}

		if(args[0].equalsIgnoreCase("clear")){
			caller.getInventory().clear(3);
			caller.getInventory().clear(5);
		}

		if(args[0].equalsIgnoreCase("showpost")){
			UtilPlayer.message(caller, F.main("Forum", "Fetching Post..."));
			String[] posts = _manager.GetForum().getPost("0", args[1]);
			ItemStack _ruleBook = ItemStackFactory.Instance.CreateStack(Material.WRITTEN_BOOK, (byte)0, 1, ChatColor.GREEN + "Forum Post", new String[] { });
			BookMeta meta = (BookMeta)_ruleBook.getItemMeta();
			for (String oof2 : posts){
				String oof3 = oof2.replace("\\n", "\n");
				meta.addPage(oof3);
			}
			meta.setTitle("Forum Post");
			meta.setAuthor("Endless Hosting");
			_ruleBook.setItemMeta(meta);
			caller.getInventory().setItem(3, _ruleBook);
		}

		if(args[0].equalsIgnoreCase("create")){
			caller.getInventory().setItem(5, ItemStackFactory.Instance.CreateStack(Material.BOOK_AND_QUILL));
			UtilPlayer.message(caller, F.main("Forum", "Use this book and quill to write your post. Type " + C.cYellow + "/forum submit <subject>" + C.cGray + " when you are ready to submit."));
		}

		if(args[0].equalsIgnoreCase("submit")){
			String subject = "";
			for (int i = 1; i < args.length; i++)
			{
				subject += args[i] + " ";
			}
			ItemStack item = caller.getInventory().getItem(5);
			ItemMeta meta2 = item.getItemMeta();
			BookMeta meta = ((BookMeta) meta2);
			Integer oof = meta.getPageCount();
			Integer currentProcessed = 1;
			String postContent = "";
			while (currentProcessed <= oof){
				postContent = postContent + meta.getPage(currentProcessed);
				currentProcessed++;
			}
			_manager.GetForum().addPost("0", caller.getDisplayName(), subject, postContent);
			UtilPlayer.message(caller, F.main("Forum", "Your post has been submitted!"));
			caller.getInventory().clear(5);
		}

	}

}
