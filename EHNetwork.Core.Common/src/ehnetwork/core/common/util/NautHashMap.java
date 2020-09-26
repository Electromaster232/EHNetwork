package ehnetwork.core.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class NautHashMap<KeyType, ValueType>
{
	private HashMap<KeyType, ValueType> _wrappedHashMap = new HashMap<KeyType, ValueType>();
	
	public boolean containsKey(KeyType key)
	{
		return _wrappedHashMap.containsKey(key);
	}
	
	public boolean containsValue(ValueType key)
	{
		return _wrappedHashMap.containsValue(key);
	}
	
	public Set<Entry<KeyType, ValueType>> entrySet()
	{
		return _wrappedHashMap.entrySet();
	}
	
	public Set<KeyType> keySet()
	{
		return _wrappedHashMap.keySet();
	}
	
	public Collection<ValueType> values()
	{
		return _wrappedHashMap.values();
	}
	
	public ValueType get(KeyType key)
	{
		return _wrappedHashMap.get(key);
	}
	
	public ValueType remove(KeyType key)
	{
		return _wrappedHashMap.remove(key);
	}
	
	public ValueType put(KeyType key, ValueType value)
	{
		return _wrappedHashMap.put(key, value);
	}
	
	public void clear()
	{
		_wrappedHashMap.clear();
	}
	
	public int size()
	{
		return _wrappedHashMap.size();
	}

	public boolean isEmpty()
	{
		return _wrappedHashMap.isEmpty();
	}
}
