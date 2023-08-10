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
import java.util.function.Function;

public class PrivateAccess
{
	public static class Key
	{
		public final String owner;
		public Key(String owner)
		{
			this.owner = owner;
		}
		
		////////
		
		private void except()
		{
			throw new IllegalArgumentException("Cannot set PrivateAccess value without the valid key, which belongs to: " + owner);
		}
		
		private static void exceptNullOriginal()
		{
			throw new IllegalArgumentException("Cannot set original PrivateAccess value/key with a null key.");
		}

		private static Key eval(Key refKey, Key other)
		{
			if (other == null)
			{
				if (refKey != null)
					refKey.except();
				else
					Key.exceptNullOriginal();
			}
			if (refKey == null)
				return other;
			if (refKey != other)
				refKey.except();
			
			return refKey;
		}
	}
	
	////////////////////////////////////
	
	public static class PrivateRef<T>
	{
		private T ref = null;
		private Key k = null;
		
		public void set(T val, Key key)
		{
			k = Key.eval(k, key);
			ref = val;
		}
		
		public T get() { return ref; }
	}
	
	public static class PrivateUse<T>
	{
		private T ref = null;
		private Key k = null;
		
		public void set(T val, Key key)
		{
			k = Key.eval(k, key);
			ref = val;
		}
		
		public void use(Key key, Consumer<T> use)
		{
			Key.eval(k, key);
			use.accept(ref);
		}
		
		public <X> X use(Key key, Function<T, X> use)
		{
			Key.eval(k, key);
			return use.apply(ref);
		}
	}
	
	public static class PrivateBool
	{
		private boolean bool = false;
		private Key k = null;
		
		public void set(boolean val, Key key)
		{
			k = Key.eval(k, key);
			bool = val;
		}
		
		public boolean get() { return bool; }
	}
	
	public static class PrivateInt
	{
		private int i = 0;
		private Key k = null;
		
		public void set(int val, Key key)
		{
			k = Key.eval(k, key);
			i = val;
		}
		
		public int get() { return i; }
	}
	
	public static class PrivateDouble
	{
		private double d = 0;
		private Key k = null;
		
		public void set(double val, Key key)
		{
			k = Key.eval(k, key);
			d = val;
		}
		
		public double get() { return d; }
	}
	
	public static class PrivateLong
	{
		private long l = 0;
		private Key k = null;
		
		public void set(long val, Key key)
		{
			k = Key.eval(k, key);
			l = val;
		}
		
		public long get() { return l; }
	}
	
	public static class PrivateFloat
	{
		private float f = 0;
		private Key k = null;
		
		public void set(float val, Key key)
		{
			k = Key.eval(k, key);
			f = val;
		}
		
		public float get() { return f; }
	}
}