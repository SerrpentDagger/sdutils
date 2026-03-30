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

import java.util.function.Function;

public class StringUtils
{
	public static <T> String toString(T[] array, String pre, String mid, String post)
	{
		return toString(array, (t) -> t.toString(), pre, mid, post);
	}
	public static <T> String toString(T[] array, Function<T, String> printer, String pre, String mid, String post)
	{
		String out = pre;
		for (int i = 0; i < array.length; i++)
		{
			String in = printer.apply(array[i]);
			out += in + (i == array.length - 1 || in.isEmpty() ? "" : mid);
		}
		return out + post;
	}
	public static String toString(int[] array, String pre, String mid, String post)
	{
		return toString(array, (t) -> t.toString(), pre, mid, post);
	}
	public static String toString(int[] array, Function<Integer, String> printer, String pre, String mid, String post)
	{
		String out = pre;
		for (int i = 0; i < array.length; i++)
		{
			String in = printer.apply(array[i]);
			out += in + (i == array.length - 1 || in.isEmpty() ? "" : mid);
		}
		return out + post;
	}
	public static String toString(double[] array, String pre, String mid, String post)
	{
		return toString(array, (t) -> t.toString(), pre, mid, post);
	}
	public static String toString(double[] array, Function<Double, String> printer, String pre, String mid, String post)
	{
		String out = pre;
		for (int i = 0; i < array.length; i++)
		{
			String in = printer.apply(array[i]);
			out += in + (i == array.length - 1 || in.isEmpty() ? "" : mid);
		}
		return out + post;
	}
	
	public static String padTo(String str, int len)
	{
		if (str.length() >= len)
			return str;
		return str + mult(" ", len - str.length());
	}
	
	public static String mult(String str, double by)
	{
		boolean neg = by < 0;
		if (neg)
		{
			str = flip(str);
			by *= -1;
		}
		
		String out = "";
		boolean first = true;
		while (by != 0)
		{
			if (!(neg && first) && by >= 1)
			{
				out += str;
				by--;
			}
			else if (by > 0)
			{
				int split = Math.round(Math.round((by % 1) * str.length()));
				if (!neg)
					out += str.substring(0, split);
				else
					out += str.substring(str.length() - split, str.length());
				by = (int) by;
			}
			first = false;
		}
		return out;
	}
	
	public static String divi(String str, double by)
	{
		return mult(str, 1d / by);
	}
	
	public static String flip(String str)
	{
		String out = "";
		for (int i = str.length() - 1; i >= 0; i--)
			out += str.charAt(i);
		return out;
	}
	
	public static String endStartWithout(String str, String other)
	{
		String s = str;
		if ((s = endWithout(str, other)) != str)
			return s;
		if ((s = startWithout(str, other)) != str)
			return s;
		return s;
	}
	
	public static String startWith(String str, String prefix)
	{
		return str.startsWith(prefix) ? str : prefix + str;
	}
	
	public static String startWithout(String str, String prefix)
	{
		return str.startsWith(prefix) ? str.substring(prefix.length()) : str;
	}
	
	public static String endWith(String str, String suffix)
	{
		return str.endsWith(suffix) ? str : str + suffix;
	}
	
	public static String endWithout(String str, String suffix)
	{
		return str.endsWith(suffix) ? str.substring(0, str.length() - suffix.length()) : str;
	}
	
	public static boolean matchingFileNames(String a, String b, String extension)
	{
		return endWith(a, extension).equals(endWith(b, extension));
	}
}
