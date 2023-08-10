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

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public abstract class DeferredFinal
{
	public abstract boolean isSet();
	
	private static void exceptSet()
	{
		throw new IllegalStateException("Attempted to set a DeferredFinal value more than once.");
	}
	
	private static void exceptGet()
	{
		throw new IllegalStateException("Attempted to get non-set DeferredFinal value.");
	}
	
	/////////////////////
	
	public static class FinalRef<T> extends DeferredFinal
	{
		private Consumer<T> onSet;
		private boolean set = false;
		private T val;
		
		public void set(T val)
		{
			if (set)
				exceptSet();
			this.val = val;
			if (onSet != null)
				onSet.accept(val);
			set = true;
		}
		public void onSet(Consumer<T> onSet) { this.onSet = onSet; }
		public T get() { if (!set) exceptGet(); return val; }
		@Override
		public boolean isSet() { return set; }
	}	
	
	public static class FinalInt extends DeferredFinal
	{
		private IntConsumer onSet;
		private boolean set = false;
		private int val;
		
		public void set(int val)
		{
			if (set)
				exceptSet();
			this.val = val;
			if (onSet != null)
				onSet.accept(val);
			set = true;
		}
		public void onSet(IntConsumer onSet) { this.onSet = onSet; }
		public int get() { if (!set) exceptGet(); return val; }
		@Override
		public boolean isSet() { return set; }
	}
	
	public static class FinalDouble extends DeferredFinal
	{
		private DoubleConsumer onSet;
		private boolean set = false;
		private double val;
		
		public void set(double val)
		{
			if (set)
				exceptSet();
			this.val = val;
			if (onSet != null)
				onSet.accept(val);
			set = true;
		}
		public void onSet(DoubleConsumer onSet) { this.onSet = onSet; }
		public double get() { if (!set) exceptGet(); return val; }
		@Override
		public boolean isSet() { return set; }
	}
	
	public static class FinalLong extends DeferredFinal
	{
		private LongConsumer onSet;
		private boolean set = false;
		private long val;
		
		public void set(long val)
		{
			if (set)
				exceptSet();
			this.val = val;
			if (onSet != null)
				onSet.accept(val);
			set = true;
		}
		public void onSet(LongConsumer onSet) { this.onSet = onSet; }
		public long get() { if (!set) exceptGet(); return val; }
		@Override
		public boolean isSet() { return set; }
	}
	
	public static class FinalFloat extends DeferredFinal
	{
		private DoubleConsumer onSet;
		private boolean set = false;
		private float val;
		
		public void set(float val)
		{
			if (set)
				exceptSet();
			this.val = val;
			if (onSet != null)
				onSet.accept(val);
			set = true;
		}
		public void onSet(DoubleConsumer onSet) { this.onSet = onSet; }
		public float get() { if (!set) exceptGet(); return val; }
		@Override
		public boolean isSet() { return set; }
	}
}