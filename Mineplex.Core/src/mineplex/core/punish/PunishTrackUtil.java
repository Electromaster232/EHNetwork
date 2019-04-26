package mineplex.core.punish;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import mineplex.core.common.util.TimeSpan;

public class PunishTrackUtil
{
	public static long GetPunishTime(PunishClient client, Category category, int severity)
	{
		int severityLimitOne = -1;
		int severityLimitTwo = -1;
		double algMod = 2;
		
		switch (category)
		{
			case ChatOffense:
				severityLimitOne = 24;
				severityLimitOne = 72;
				algMod = -2;
				break;
			case Advertisement:
				severityLimitOne = 48;
				severityLimitOne = 168;
				algMod = -1;
				break;
			case Exploiting:
				severityLimitOne = 48;
				severityLimitOne = 168;
				algMod = 0;
				break;
			case Hacking:
				severityLimitOne = -1;
				severityLimitOne = -1;
				algMod = 1;
				break;
			default:
				break;
		}
		
		List<Entry<Category, Punishment>> punishments = new ArrayList<Entry<Category, Punishment>>();
		
		if (client.GetPunishments().containsKey(category))
		{	
			for (Punishment punishment : client.GetPunishments().get(category))
			{
				punishments.add(new AbstractMap.SimpleEntry<Category, Punishment>(category, punishment));
			}
		}
		
		Collections.sort(punishments, new PunishmentSorter());
		
		long timeOfLastInfraction = Math.min(punishments.size() > 0 ? (System.currentTimeMillis() - punishments.get(0).getValue().GetTime()) / TimeSpan.DAY : 180, 180);

		long punishTime = (long) (Math.pow(2, algMod + ((severity - 1) * 2)) * 24) + (180 - timeOfLastInfraction) / 3;

		return severity < 3 ? Math.min(punishTime, severity == 1 ? severityLimitOne == -1 ? punishTime : severityLimitOne : severityLimitTwo == -1 ? punishTime : severityLimitTwo) : punishTime;
	}
}
