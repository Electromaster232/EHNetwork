package mineplex.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilTime
{
	public static final String DATE_FORMAT_NOW = "MM-dd-yyyy HH:mm:ss";
	public static final String DATE_FORMAT_DAY = "MM-dd-yyyy";
	
	public static String now() 
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	public static String when(long time) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(time);
	}

	
	public static String date() 
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
		return sdf.format(cal.getTime());
	}

	public enum TimeUnit
	{
		FIT,
		DAYS,
		HOURS,
		MINUTES,
		SECONDS,
		MILLISECONDS
	}
	
	public static String since(long epoch)
	{
		return "Took " + convertString(System.currentTimeMillis()-epoch, 1, TimeUnit.FIT) + ".";
	}
	
	public static double convert(long time, int trim, TimeUnit type)
	{
		if (type == TimeUnit.FIT)			
		{
			if (time < 60000)				type = TimeUnit.SECONDS;
			else if (time < 3600000)		type = TimeUnit.MINUTES;
			else if (time < 86400000)		type = TimeUnit.HOURS;
			else							type = TimeUnit.DAYS;
		}
		
		if (type == TimeUnit.DAYS)			return UtilMath.trim(trim, (time)/86400000d);
		if (type == TimeUnit.HOURS)			return UtilMath.trim(trim, (time)/3600000d);
		if (type == TimeUnit.MINUTES)		return UtilMath.trim(trim, (time)/60000d);
		if (type == TimeUnit.SECONDS)		return UtilMath.trim(trim, (time)/1000d);
		else								return UtilMath.trim(trim, time);
	}
	
	public static String MakeStr(long time)
	{
		return convertString(time, 1, TimeUnit.FIT);
	}
	
	public static String MakeStr(long time, int trim)
	{
		return convertString(Math.max(0, time), trim, TimeUnit.FIT);
	}
	
	public static String convertString(long time, int trim, TimeUnit type)
	{
		if (time == -1)						return "Permanent";
		
		if (type == TimeUnit.FIT)			
		{
			if (time < 60000)				type = TimeUnit.SECONDS;
			else if (time < 3600000)		type = TimeUnit.MINUTES;
			else if (time < 86400000)		type = TimeUnit.HOURS;
			else							type = TimeUnit.DAYS;
		}
		
		String text;
		double num;
		if (trim == 0)
		{
			if (type == TimeUnit.DAYS)			text = (num = UtilMath.trim(trim, time / 86400000d)) + " Day";
			else if (type == TimeUnit.HOURS)	text = (num = UtilMath.trim(trim, time / 3600000d)) + " Hour";
			else if (type == TimeUnit.MINUTES)	text = (num = UtilMath.trim(trim, time / 60000d)) + " Minute";
			else if (type == TimeUnit.SECONDS)	text = (int) (num = (int) UtilMath.trim(trim, time / 1000d)) + " Second";
			else								text = (int) (num = (int) UtilMath.trim(trim, time)) + " Millisecond";
		}
		else
		{
			if (type == TimeUnit.DAYS)			text = (num = UtilMath.trim(trim, time / 86400000d)) + " Day";
			else if (type == TimeUnit.HOURS)	text = (num = UtilMath.trim(trim, time / 3600000d)) + " Hour";
			else if (type == TimeUnit.MINUTES)	text = (num = UtilMath.trim(trim, time / 60000d)) + " Minute";
			else if (type == TimeUnit.SECONDS)	text = (num = UtilMath.trim(trim, time / 1000d)) + " Second";
			else								text = (int) (num = (int) UtilMath.trim(0, time)) + " Millisecond";
		}
		
		if (num != 1)
		    text += "s";
		
		return text;
	}
	
	public static boolean elapsed(long from, long required)
	{
		return System.currentTimeMillis() - from > required;
	}
}
