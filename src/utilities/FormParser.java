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

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

public class FormParser<T>
{
	public final ReadWrite<T> rw;
	private int index = 0;
	private Iterator<T> form;
	
	/////////
	
	public FormParser()
	{
		this(new ReadWrite<>());
	}
	
	public FormParser(ReadWrite<T> rw)
	{
		this.rw = rw;
	}
	
	//////////////////
	
	@SuppressWarnings("unchecked")
	public FormParser<T> reset(T... form)
	{
		return reset(MapUtils.of(form));
	}
	
	public FormParser<T> reset(Iterator<T> form)
	{
		this.form = form;
		index = 0;
		return this;
	}
	
	public FormParser<T> reset()
	{
		return reset((Iterator<T>) null);
	}
	
	private T next()
	{
		if (form == null)
			throw new IllegalStateException("Batched parsing operation attempted on null batch Iterator.");
		return form.next();
	}
	
	private void inc()
	{
		index++;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	/////////////
	
	public String get(T from)
	{
		String out = rw.read(from);
		inc();
		return out;
	}
	
	public String get()
	{
		return get(next());
	}
	
	public String[] getArray(T from)
	{
		String[] out = rw.readArray(from);
		inc();
		return out;
	}
	
	public String[] getArray()
	{
		return getArray(next());
	}
	
	public String[] getArray(T from, int minLen)
	{
		String[] out = rw.readArray(from, minLen);
		inc();
		return out;
	}
	
	public String[] getArray(int minLen)
	{
		return getArray(next(), minLen);
	}
	
	public String[] getArray(T from, int minLen, String defalt)
	{
		String[] out = rw.readArray(from, minLen, defalt);
		inc();
		return out;
	}
	
	public String[] getArray(int minLen, String defalt)
	{
		return getArray(next(), minLen, defalt);
	}
	
	public boolean getBool(T from)
	{
		boolean out = Boolean.parseBoolean(rw.read(from));
		inc();
		return out;
	}
	
	public boolean getBool()
	{
		return getBool(next());
	}
	
	public boolean[] getBoolArray(T from)
	{
		boolean[] out = toBoolArray(rw.readArray(from));
		inc();
		return out;
	}
	
	public boolean[] getBoolArray()
	{
		return getBoolArray(next());
	}
	
	public boolean[] getBoolArray(T from, int minLen)
	{
		boolean[] out = toBoolArray(rw.readArray(from, minLen));
		inc();
		return out;
	}
	
	public boolean[] getBoolArray(int minLen)
	{
		return getBoolArray(next(), minLen);
	}
	
	public boolean[] getBoolArray(T from, int minLen, boolean defalt)
	{
		boolean[] out = toBoolArray(rw.readArray(from, minLen, "" + defalt));
		inc();
		return out;
	}
	
	public boolean[] getBoolArray(int minLen, boolean defalt)
	{
		return getBoolArray(next(), minLen, defalt);
	}
	
	public int getInt(T from)
	{
		int out = Integer.parseInt(rw.read(from));
		inc();
		return out;
	}
	
	public int getInt()
	{
		return getInt(next());
	}
	
	public int[] getIntArray(T from)
	{
		int[] out = toIntArray(rw.readArray(from));
		inc();
		return out;
	}
	
	public int[] getIntArray()
	{
		return getIntArray(next());
	}
	
	public int[] getIntArray(T from, int minLen)
	{
		int[] out = toIntArray(rw.readArray(from, minLen));
		inc();
		return out;
	}
	
	public int[] getIntArray(int minLen)
	{
		return getIntArray(next(), minLen);
	}
	
	public int[] getIntArray(T from, int minLen, int defalt)
	{
		int[] out = toIntArray(rw.readArray(from, minLen, "" + defalt));
		inc();
		return out;
	}
	
	public int[] getIntArray(int minLen, int defalt)
	{
		return getIntArray(next(), minLen, defalt);
	}
	
	public double getDouble(T from)
	{
		double out = Double.parseDouble(rw.read(from));
		inc();
		return out;
	}
	
	public double getDouble()
	{
		return getDouble(next());
	}
	
	public double[] getDoubleArray(T from)
	{
		double[] out = toDoubleArray(rw.readArray(from));
		inc();
		return out;
	}
	
	public double[] getDoubleArray()
	{
		return getDoubleArray(next());
	}
	
	public double[] getDoubleArray(T from, int minLen)
	{
		double[] out = toDoubleArray(rw.readArray(from, minLen));
		inc();
		return out;
	}
	
	public double[] getDoubleArray(int minLen)
	{
		return getDoubleArray(next(), minLen);
	}
	
	public double[] getDoubleArray(T from, int minLen, double defalt)
	{
		double[] out = toDoubleArray(rw.readArray(from, minLen, "" + defalt));
		inc();
		return out;
	}
	
	public double[] getDoubleArray(int minLen, double defalt)
	{
		return getDoubleArray(next(), minLen, defalt);
	}
	
	///////////

	private boolean[] toBoolArray(String[] arr)
	{
		boolean[] out = new boolean[arr.length];
		for (int i = 0; i < out.length; i++)
			out[i] = Boolean.parseBoolean(arr[i]);
		return out;
	}
	
	private int[] toIntArray(String[] arr)
	{
		int[] out = new int[arr.length];
		for (int i = 0; i < out.length; i++)
			out[i] = Integer.parseInt(arr[i]);
		return out;
	}

	private double[] toDoubleArray(String[] arr)
	{
		double[] out = new double[arr.length];
		for (int i = 0; i < out.length; i++)
			out[i] = Double.parseDouble(arr[i]);
		return out;
	}
	
	//////////////////
	
	public FormParser<T> set(T on, String... val)
	{
		rw.writeArray(on, val);
		inc();
		return this;
	}
	
	public FormParser<T> set(String... val)
	{
		return set(next(), val);
	}
	
	public FormParser<T> setBools(T on, boolean... vals)
	{
		String[] to = new String[vals.length];
		for (int i = 0; i < to.length; i++)
			to[i] = "" + vals[i];
		return set(on, to);
	}
	
	public FormParser<T> setBools(boolean... vals)
	{
		return setBools(next(), vals);
	}
	
	public FormParser<T> setInts(T on, int... vals)
	{
		String[] to = new String[vals.length];
		for (int i = 0; i < to.length; i++)
			to[i] = rw.numFormat.format(vals[i]);
		return set(on, to);
	}
	
	public FormParser<T> setInts(int... vals)
	{
		return setInts(next(), vals);
	}
	
	public FormParser<T> setDoubles(T on, double... vals)
	{
		String[] to = new String[vals.length];
		for (int i = 0; i < to.length; i++)
			to[i] = rw.numFormat.format(vals[i]);
		return set(on, to);
	}
	
	public FormParser<T> setDoubles(double... vals)
	{
		return setDoubles(next(), vals);
	}
	
	///////////////////
	
	public static class ReadWrite<T>
	{
		public final Reader<T> reader;
		public final Writer<T> writer;
		public final NumberFormat numFormat;
		public final String readRegex, writeSpacer;
		
		///////////////
		
		public ReadWrite()
		{
			this(Object::toString);
		}
		
		public ReadWrite(Reader<T> reader)
		{
			this(reader, null);
		}
		
		public ReadWrite(Writer<T> writer)
		{
			this(Object::toString, writer);
		}
		
		public ReadWrite(Reader<T> reader, Writer<T> writer)
		{
			this(reader, writer, " ");
		}
		
		public ReadWrite(Reader<T> reader, Writer<T> writer, String arraySpacer)
		{
			this(reader, writer, NumberFormat.getInstance(), Pattern.quote(arraySpacer), arraySpacer);
		}
		
		public ReadWrite(Reader<T> reader, Writer<T> writer, String readRegex, String writeSpacer)
		{
			this(reader, writer, NumberFormat.getInstance(), readRegex, writeSpacer);
		}
		
		public ReadWrite(Reader<T> reader, Writer<T> writer, NumberFormat format)
		{
			this(reader, writer, format, " ");
		}
		
		public ReadWrite(Reader<T> reader, Writer<T> writer, NumberFormat format, String arraySpacer)
		{
			this(reader, writer, format, Pattern.quote(arraySpacer), arraySpacer);
		}
		
		public ReadWrite(Reader<T> reader, Writer<T> writer, NumberFormat format, String readRegex, String writeSpacer)
		{
			this.reader = reader;
			this.writer = writer;
			this.numFormat = format;
			this.readRegex = readRegex;
			this.writeSpacer = readRegex;
		}
		
		////////////////
		
		public String read(T t)
		{
			if (reader == null)
				throw new UnsupportedOperationException("This ReadWrite has no defined reader.");
			return reader.read(t);
		}
		
		public String[] readArray(T t)
		{
			return read(t).split(readRegex);
		}
		
		public String[] readArray(T t, int minLen)
		{
			String[] out = readArray(t);
			if (out.length < minLen)
				throw new NumberFormatException("Supplied array was too short.");
			return out;
		}
		
		public String[] readArray(T t, int minLen, String blanks)
		{
			String[] arr = readArray(t);
			String[] out = arr;
			if (out.length < minLen)
			{
				out = Arrays.copyOf(arr, minLen);
				for (int i = arr.length; i < out.length; i++)
					out[i] = blanks;
			}
			return out;
		}
		
		public ReadWrite<T> write(T t, String text)
		{
			if (writer == null)
				throw new UnsupportedOperationException("This ReadWrite has no defined writer.");
			writer.write(t, text);
			return this;
		}
		
		public ReadWrite<T> writeArray(T t, String... values)
		{
			String txt = "";
			for (int i = 0; i < values.length; i++)
				txt += values[i] + (i == values.length - 1 ? "" : writeSpacer);
			return write(t, txt);
		}
	}
	
	public static interface Reader<T>
	{
		public String read(T obj);
	}
	
	public static interface Writer<T>
	{
		public void write(T obj, String text);
	}
}
