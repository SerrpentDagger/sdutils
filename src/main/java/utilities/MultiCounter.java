/**
 * This file is part of SDUtils, which is a library of useful classes and functionality.
 * Copyright (c) 2023, SerpentDagger (MRRH) <serpentdagger.contact@gmail.com>.
 * 
 * SDUtils is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * SDUtils is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with SDUtils.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
