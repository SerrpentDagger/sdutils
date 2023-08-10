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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MapUtils
{
	public static <K, V> V getPutIfAbsent(Map<K, V> map, K key, Supplier<V> ifAbsent)
	{
		V old = map.get(key);
		if (old == null)
		{
			V now = ifAbsent.get();
			map.put(key, now);
			return now;
		}
		return old;
	}
	
	public static <V> V getRandom(Map<?, V> map, long seed)
	{
		Random ran = new Random(seed);
		return map.get(map.keySet().toArray()[ran.nextInt(map.size())]);
	}
	
	@SuppressWarnings("unchecked")
	public static <V> V getRandom(Map<?, V> map, Random ran, Predicate<V> from)
	{
		Object[] vals = new Object[map.size()];
		int matched = 0;
		Iterator<V> it = map.values().iterator();
		while (it.hasNext())
		{
			V n = it.next();
			if (from.test(n))
				vals[matched++] = n;
		}
		if (matched > 0)
			return (V) vals[ran.nextInt(matched)];
		return null;
	}
	
	public static <T> T getLargest(Iterator<T> it, Predicate<T> valid, Comparator<T> comp)
	{
		T t = null;
		while (it.hasNext())
		{
			T n = it.next();
			if ((valid == null || valid.test(n)) && (t == null || comp.compare(n, t) == 1))
				t = n;
		}
		return t;
	}
	
	public static <T> T getSmallest(Iterator<T> it, Predicate<T> valid, Comparator<T> comp)
	{
		return getLargest(it, valid, comp.reversed());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] getLargest(Iterator<T> it, int count, Predicate<T> valid, Comparator<T> comp)
	{
		T[] t = null;
		int i = 0;
		while (it.hasNext())
		{
			T n = it.next();
			if ((valid == null || valid.test(n)))
			{
				if (t == null)
					t = (T[]) Array.newInstance(n.getClass(), count);
				if (i < count)
					t[i++] = n;
				else
				{
					for (int j = 0; j < t.length; j++)
					{
						if (comp.compare(n, t[j]) == 1)
						{
							t[j] = n;
							break;
						}
					}
				}
			}
		}
		Arrays.sort(t, comp);
		return t;
	}
	
	public static <T> T[] getSmallest(Iterator<T> it, int count, Predicate<T> valid, Comparator<T> comp)
	{
		return getLargest(it, count, valid, comp.reversed());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] getIf(Iterator<T> it, Predicate<T> valid)
	{
		T ref = null;
		ArrayList<T> got = new ArrayList<T>();
		while (it.hasNext())
		{
			T n = it.next();
			if (!valid.test(n))
				continue;
			if (ref == null)
				ref = n;
			got.add(n);
		}
		if (ref != null)
			return got.toArray((T[]) Array.newInstance(ref.getClass(), got.size()));
		return null;
	}
	
	public static <T> T getFirst(Iterator<T> it, Predicate<T> valid)
	{
		T ref = null;
		while (it.hasNext())
			if (valid.test(ref = it.next()))
				return ref;
		return null;
	}
	
	public static <T> boolean removeFirst(Iterator<T> it, Function<T, Boolean> iff)
	{
		while (it.hasNext())
		{
			if (iff.apply(it.next()))
			{
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	@SafeVarargs
	public static <T> Iterator<T> of(T... array)
	{
		return Arrays.asList(array).iterator();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] keySetArray(Map<T, ?> map, Class<T> type)
	{
		return map.keySet().toArray((T[]) Array.newInstance(type, map.keySet().size()));
	}
	
	public static String[] keySetArray(Map<String, ?> map)
	{
		return map.keySet().toArray(new String[map.keySet().size()]);
	}
	
	public static <K, V> V[] getNonNull(Map<K, V> map, K[] keys)
	{
		return get(map, keys, (v) -> v != null);
	}
	
	public static <P, K, V> V[] getNonNull(Map<K, V> map, P[] preKeys, Function<P, K> toKey)
	{
		return get(map, preKeys, toKey, (v) -> v != null);
	}
	
	public static <K, V> V[] get(Map<K, V> map, K[] keys, Predicate<V> valid)
	{
		V v = map.values().parallelStream().findAny().get();
		
		@SuppressWarnings("unchecked")
		V[] out = (V[]) Array.newInstance(v.getClass(), keys.length);
		
		for (int i = 0; i < keys.length; i++)
		{
			v = map.get(keys[i]);
			if (valid.test(v))
				out[i] = v;
			else
				return null;
		}
		return out;
	}
	
	public static <P, K, V> V[] get(Map<K, V> map, P[] preKeys, Function<P, K> toKey, Predicate<V> valid)
	{
		V v = map.values().parallelStream().findAny().get();
		
		@SuppressWarnings("unchecked")
		V[] out = (V[]) Array.newInstance(v.getClass(), preKeys.length);
		
		for (int i = 0; i < preKeys.length; i++)
		{
			v = map.get(toKey.apply(preKeys[i]));
			if (valid.test(v))
				out[i] = v;
			else
				return null;
		}
		return out;
	}
}
