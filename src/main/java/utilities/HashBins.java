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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class HashBins<K, V>
{
	private final HashMap<K, HashSet<V>> bins;
	private final int binCap;
	private final float binLF;
	
	public HashBins()
	{
		this(16);
	}
	
	/**
	 * {@linkplain HashMap#HashMap(int)}
	 * @param capacity
	 */
	public HashBins(int capacity)
	{
		this(capacity, 0.75f);
	}
	
	/**
	 * {@linkplain HashMap#HashMap(int, float)}
	 * @param capacity
	 * @param loadFactor
	 */
	public HashBins(int capacity, float loadFactor)
	{
		this(capacity, loadFactor, 16, 0.75f);
	}
	
	public HashBins(int capacity, float loadFactor, int binCapacity, float binLoadFactor)
	{
		bins = new HashMap<>(capacity, loadFactor);
		binCap = binCapacity;
		binLF = binLoadFactor;
	}
	
	private HashSet<V> binSupplier()
	{
		return new HashSet<V>(binCap, binLF);
	}
	
	public void add(K key, V val)
	{
		MapUtils.getPutIfAbsent(bins, key, this::binSupplier).add(val);
	}
	
	public HashSet<V> getBin(K key)
	{
		return bins.get(key);
	}
	
	public boolean contains(K key, V value)
	{
		HashSet<V> bin = bins.get(key);
		return bin != null && bin.contains(value); 
	}
	
	public void clear()
	{
		bins.clear();
	}
	
	public void clearBins()
	{
		bins.forEach((k, b) -> b.clear());
	}
	
	public boolean isEmpty()
	{
		if (bins.isEmpty())
			return true;
		boolean emp = true;
		Iterator<Entry<K, HashSet<V>>> binIt = bins.entrySet().iterator();
		while (emp && binIt.hasNext())
			emp = binIt.next().getValue().isEmpty();
		return emp;
	}
	
	public HashSet<V> remove(K key)
	{
		return bins.remove(key);
	}
	
	public boolean remove(K key, V val)
	{
		HashSet<V> bin = bins.get(key);
		return bin != null && bin.remove(val);
	}
}
