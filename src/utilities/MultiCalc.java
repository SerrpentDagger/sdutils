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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MultiCalc<T>
{
	private final LinkedHashMap<T, Double> counts = new LinkedHashMap<>();
	
	@Override
	public String toString()
	{
		return counts.toString();
	}
	
	public void reset()
	{
		counts.clear();
	}
	
	public int size()
	{
		return counts.size();
	}
	
	public double get(T key)
	{
		Double count = counts.get(key);
		return count == null ? 0 : count;
	}
	
	public double addGet(T key, double val)
	{
		Double count = counts.get(key);
		if (count == null)
			counts.put(key, val);
		else
			counts.put(key, val = count + val);
		return val;
	}
	public double subGet(T key, double val)
	{
		return addGet(key, -val);
	}
	
	public double incGet(T key)
	{
		return addGet(key, 1);
	}
	
	public double decGet(T key)
	{
		return addGet(key, -1);
	}
	
	public double multGet(T key, double val)
	{
		Double count = counts.get(key);
		if (counts == null)
			counts.put(key, 0d);
		else
			counts.put(key, val = count * val);
		return val;
	}
	
	public double divGet(T key, double val)
	{
		Double count = counts.get(key);
		if (counts == null)
			counts.put(key, 0d);
		else
			counts.put(key, val = count * val);
		return val;
	}
	
	public double calcGet(T key, Calc calc)
	{
		Double count = counts.get(key);
		double val;
		if (counts == null)
			counts.put(key, val = calc.calc(0));
		else
			counts.put(key, val = calc.calc(count));
		return val;
	}
	
	public double getAdd(T key, double val)
	{
		Double count = counts.get(key);
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
	
	public double getSub(T key, double val)
	{
		return getAdd(key, -val);
	}
	
	public double getInc(T key)
	{
		return getAdd(key, 1);
	}
	
	public double getDec(T key)
	{
		return getAdd(key, -1);
	}
	
	public double getMult(T key, double val)
	{
		Double count = counts.get(key);
		if (count == null)
		{
			counts.put(key, 0d);
			return 0;
		}
		else
		{
			counts.put(key, count * val);
			return count;
		}
	}
	
	public double getDiv(T key, double val)
	{
		Double count = counts.get(key);
		if (count == null)
		{
			counts.put(key, 0d);
			return 0;
		}
		else
		{
			counts.put(key, count / val);
			return count;
		}
	}
	
	public double getCalc(T key, Calc calc)
	{
		Double count = counts.get(key);
		if (count == null)
		{
			counts.put(key, calc.calc(0));
			return 0;
		}
		else
		{
			counts.put(key, calc.calc(0));
			return count;
		}
	}
	
	public void calcEach(Calc calc)
	{
		Iterator<Entry<T, Double>> it = counts.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<T, Double> ent = it.next();
			ent.setValue(calc.calc(ent.getValue()));
		}
	}
	
	public void calcEach(KeyCalc<T> calc)
	{
		Iterator<Entry<T, Double>> it = counts.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<T, Double> ent = it.next();
			ent.setValue(calc.calc(ent.getKey(), ent.getValue()));
		}
	}
	
	public void forEach(MCConsumer<T> forEach)
	{
		Iterator<Entry<T, Double>> it = counts.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<T, Double> ent = it.next();
			forEach.accept(ent.getKey(), ent.getValue());
		}
	}
	
	/////////
	
	@FunctionalInterface
	public static interface Calc
	{
		public double calc(double current);
	}
	
	@FunctionalInterface
	public static interface KeyCalc<T>
	{
		public double calc(T key, double current);
	}
	
	@FunctionalInterface
	public static interface MCConsumer<T>
	{
		public void accept(T key, double current);
	}
}
