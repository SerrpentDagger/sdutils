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

public class PrimitiveReference
{
	public static class IntRef
	{
		private int val;
		
		public IntRef()
		{ this(0); }
		public IntRef(int i)
		{ val = i; }
		
		public int set(int i) { return val = i; }
		public int get() { return val; }
		public int incrGet() { return ++val; }
		public int getIncr() { return val++; }
		public int decrGet() { return --val; }
		public int getDecr() { return val--; }
		public int add(int i) { return val += i; }
		public int sub(int i) { return val -= i; }
		public int mult(int i) { return val *= i; }
		public int divi(int i) { return val /= i; }
	}
	
	public static class LongRef
	{
		private long val;
		
		public LongRef()
		{ this(0); }
		public LongRef(long l)
		{ val = l; }
		
		public long set(long l) { return val = l; }
		public long get() { return val; }
		public long incrGet() { return ++val; }
		public long getIncr() { return val++; }
		public long decrGet() { return --val; }
		public long getDecr() { return val--; }
		public long add(long l) { return val += l; }
		public long sub(long l) { return val -= l; }
		public long mult(long l) { return val *= l; }
		public long divi(long l) { return val /= l; }
	}
	
	public static class DoubRef
	{
		private double val;
		
		public DoubRef()
		{ this(0); }
		public DoubRef(double d)
		{ val = d; }
		
		public double set(double d) { return val = d; }
		public double get() { return val; }
		public double incrGet() { return ++val; }
		public double getIncr() { return val++; }
		public double decrGet() { return --val; }
		public double getDecr() { return val--; }
		public double add(double d) { return val += d; }
		public double sub(double d) { return val -= d; }
		public double mult(double d) { return val *= d; }
		public double divi(double d) { return val /= d; }
	}
	
	public static class BoolRef
	{
		private boolean val;
		
		public BoolRef()
		{ this(false); }
		public BoolRef(boolean b)
		{ val = b; }
		
		public boolean set(boolean b) { return val = b; }
		public boolean get() { return val; }
		public boolean invertGet() { return val = !val; }
		public boolean getInvert() { boolean tmp = val; val = !val; return tmp; }
	}
}