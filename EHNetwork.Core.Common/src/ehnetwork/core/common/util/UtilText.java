package ehnetwork.core.common.util;

import java.util.Collection;

public class UtilText {
	public static <T> String listToString(Collection<T> inputList, boolean comma) {
		String out = "";

		for (T cur : inputList) {
			out += cur.toString() + (comma ? ", " : " ");
		}

		if (out.length() > 0) {
			out = out.substring(0, out.length() - (comma ? 2 : 1));
		}

		return out;
	}
	
	public static int upperCaseCount(String input) {
		int count = 0;
		
		for (int k = 0; k < input.length(); k++) {
			
			
			char ch = input.charAt(k);
			if (Character.isUpperCase(ch)) 
				count++;
		
		}
		
		return count;
	}
	public static int lowerCaseCount(String input) {
		int count = 0;
		
		for (int k = 0; k < input.length(); k++) {
			
			
			char ch = input.charAt(k);
			if (Character.isLowerCase(ch)) 
				count++;
		
		}
		
		return count;
	}

	public static boolean isStringSimilar(String newString, String oldString, float matchRequirement)
	{
		if (newString.length() <= 3)
		{
			return newString.toLowerCase().equals(oldString.toLowerCase());
		}
		
		for (int i=0 ; i < newString.length() * matchRequirement ; i++)
		{
			int matchFromIndex = 0;
			
			//Look for substrings starting at i
			for (int j=0 ; j < oldString.length() ; j++)
			{
				//End of newString
				if (i+j >= newString.length())
				{
					break;
				}
				
				//Matched
				if (newString.charAt(i+j) == oldString.charAt(j))
				{
					matchFromIndex++;
					
					if (matchFromIndex >= newString.length() * matchRequirement)
						return true;
				}
				//No Match > Reset
				else
				{
					break;
				}
			}
		}
		
		return false;
	}
}
