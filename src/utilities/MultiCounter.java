package utilities;

import java.util.HashMap;

public class MultiCounter<T>
{
	private final HashMap<T, Integer> counts = new HashMap<>();
	
	@Override
	public String toString()
	{
		return counts.toString();
/*		Iterator<Entry<T, Integer>> it = counts.entrySet().iterator();
		String out = "{";
		Entry<T, Integer> n;
		while (it.hasNext())
			out += (n = it.next()).getKey() + ":" + n.getValue() + (it.hasNext() ? ", " : "");
		out += "}";
		return out;*/
	}
	
	public void reset()
	{
		counts.clear();
	}
	
	public int get(T key)
	{
		Integer count = counts.get(key);
		return count == null ? 0 : count;
	}
	
	public int addGet(T key, int val)
	{
		Integer count = counts.get(key);
		if (count == null)
			counts.put(key, val);
		else
			counts.put(key, val = count + val);
		return val;
	}
	
	public int subGet(T key, int val)
	{
		return addGet(key, -val);
	}
	
	public int incGet(T key)
	{
		return addGet(key, 1);
	}
	
	public int decGet(T key)
	{
		return addGet(key, -1);
	}
	
	public int getAdd(T key, int val)
	{
		Integer count = counts.get(key);
		if (count == null)
		{
			counts.put(key, val);
			return 0;
		}
		else
		{
			counts.put(key, count + val);
			return count;
		}
	}
	
	public int getSub(T key, int val)
	{
		return getAdd(key, -val);
	}
	
	public int getInc(T key)
	{
		return getAdd(key, 1);
	}
	
	public int getDec(T key)
	{
		return getAdd(key, -1);
	}
}
