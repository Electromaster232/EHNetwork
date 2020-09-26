package ehnetwork.core.common.util;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UtilItem 
{
	public static LinkedList<Entry<Material, Byte>> matchItem(Player caller, String items, boolean inform)
	{
		LinkedList<Entry<Material, Byte>> matchList = new LinkedList<Entry<Material, Byte>>();

		String failList = "";

		//Mass Search
		for (String cur : items.split(","))
		{
			Entry<Material, Byte> match = searchItem(caller, cur, inform);

			if (match != null)
				matchList.add(match);

			else
				failList += cur + " " ;
		}

		if (inform && failList.length() > 0)
		{
			failList = failList.substring(0, failList.length() - 1);
			UtilPlayer.message(caller, F.main("Item(s) Search", "" +
					C.mBody + " Invalid [" +
					C.mElem + failList +
					C.mBody + "]."));
		}

		return matchList;
	}
	
	public static Entry<Material, Byte> searchItem(Player caller, String args, boolean inform)
	{
		LinkedList<Entry<Material, Byte>> matchList = new LinkedList<Entry<Material, Byte>>();
		
		for (Material cur : Material.values())
		{
			//By Name
			if (cur.toString().equalsIgnoreCase(args))
				return new AbstractMap.SimpleEntry<Material, Byte>(cur, (byte)0);

			if (cur.toString().toLowerCase().contains(args.toLowerCase()))
				matchList.add(new AbstractMap.SimpleEntry<Material, Byte>(cur, (byte)0));
			
			//By ID:Data
			String[] arg = args.split(":");
			
			//ID
			int id = 0;
			try
			{
				if (arg.length > 0)	
					id = Integer.parseInt(arg[0]);
			}
			catch (Exception e)	
			{
				continue;
			}
			
			if (id != cur.getId())
				continue;
			
			//Data
			byte data = 0;
			try
			{
				if (arg.length > 1)	
					data = Byte.parseByte(arg[1]);
			}
			catch (Exception e)	
			{
				continue;
			}
			
			return new AbstractMap.SimpleEntry<Material, Byte>(cur, data);
		}

		//No / Non-Unique
		if (matchList.size() != 1)
		{
			if (!inform)
				return null;
			
			//Inform
			UtilPlayer.message(caller, F.main("Item Search", "" +
					C.mCount + matchList.size() +
					C.mBody + " matches for [" +
					C.mElem + args +
					C.mBody + "]."));

			if (matchList.size() > 0)
			{
				String matchString = "";
				for (Entry<Material, Byte> cur : matchList)
					matchString += F.elem(cur.getKey().toString()) + ", ";
				if (matchString.length() > 1)
					matchString = matchString.substring(0 , matchString.length() - 2);

				UtilPlayer.message(caller, F.main("Item Search", "" +
						C.mBody + "Matches [" +
						C.mElem + matchString +
						C.mBody + "]."));
			}
			
			return null;
		}
		
		return matchList.get(0);
	}
	
	public static String itemToStr(ItemStack item)
	{
		String data = "0";
		if (item.getData() != null)
			data = item.getData().getData() + "";
		
		return item.getType() + ":" + item.getAmount() + ":" + item.getDurability() + ":" + data;
	}
}
