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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class ArrayUtils
{
	private static final Random RNG = new Random();
	private static int decPlaces = 3;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void putAll(HashMap map, String[] keys, Object[] values)
	{
		for (int i = 0; i < keys.length; i++)
		{
			map.put(keys[i], values.length > i ? values[i] : null);
		}
	}
	
	public static int sizeOf(Object[][] array)
	{
		int size = 0;
		for (int i = 0; i < array.length; i++)
		{
			size += array[i].length;
		}
		return size;
	}

	public static boolean containsNot(Object[] array, Object object)
	{
		for (Object obj : array)
		{
			if (!obj.equals(object))
				return true;
		}
		return false;
	}
	
	public static boolean contains(Object[] array, Object object)
	{
		for (Object obj : array)
		{
			if (obj == object || obj.equals(object))
				return true;
		}
		return false;
	}
	
	public static boolean containsFile(String[] files, String file, String ext)
	{
		for (String str : files)
		{
			if (StringUtils.endWith(str, ext).equals(StringUtils.endWith(file, ext)))
				return true;
		}
		return false;
	}
	
	public static boolean containsNot(double[] array, double number)
	{
		for (double num : array)
		{
			if (num != number)
				return true;
		}
		return false;
	}
	
	public static boolean contains(double[] array, double number)
	{
		for (double num : array)
		{
			if (num == number)
				return true;
		}
		return false;
	}
	
	public static boolean contains(int[] array, int number)
	{
		for (int num : array)
			if (num == number)
				return true;
		return false;
	}
	
	public static int countOf(Object[] array, Object object)
	{
		int count = 0;
		for (Object obj : array)
		{
			if (obj == object || obj.equals(object))
				count++;
		}
		return count;
	}
	
	public static int indexOf(Object[] array, Object object, int estimate)
	{
		int found = -1;
		int h = estimate, l = estimate;
		int searched = 0;
		while (found == -1 && searched < array.length)
		{
			if (h < array.length)
			{
				if (array[h] == object || (object != null && object.equals(array[h])))
					found = h;
				else
					h++;
			}
			if (found == -1 && l >= 0)
			{
				if (array[l] == object || (object != null && object.equals(array[l])))
					found = l;
				else
					l--;
			}
			searched += 2;
		}
		return found;
	}
	
	public static int indexOf(IntFunction<Object> haystack, Object needle, int estimate, int startIncl, int length)
	{
		int found = -1;
		int h = estimate, l = estimate;
		int searched = 0;
		while(found == -1 && searched < length)
		{
			Object obj;
			if (h < length)
			{
				obj = haystack.apply(h);
				if (obj == needle || (needle != null && needle.equals(obj)))
					found = h;
				else
					h++;
			}
			if (found == -1 && l >= 0)
			{
				obj = haystack.apply(l);
				if (obj == needle || (needle != null && needle.equals(obj)))
					found = l;
				else
					l--;
			}
			searched += 2;
		}
		return found;
	}
	
	public static <T> void replace(T[] array, ArrayReplacer<T> replacer)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = replacer.replace(i, array[i]);
	}

	public static String toString(double... vals)
	{
		String str = "";
		for (int i = 0; i < vals.length; i++)
				str += "" + vals[i] + (i == vals.length - 1 ? "" : " ");
		return str;
	}
	
	public static String toFormattedString(double... vals)
	{
		String str = "";	
		NumberFormat f = NumberFormat.getNumberInstance(Locale.US);
		f.setMaximumFractionDigits(decPlaces);
		for (int i = 0; i < vals.length; i++)
			str += f.format(vals[i]) + (i == vals.length - 1 ? "" : " ");
		return str;
	}
	
	public static String toFormattedString(int... vals)
	{
		NumberFormat f = NumberFormat.getNumberInstance(Locale.US);
		f.setMaximumFractionDigits(decPlaces);
		return StringUtils.toString(vals, (s) -> f.format(s), "", " ", "");
	}
	
	/**
	 * For mixes of double, int, long, float, String and/or boolean vals.
	 * @param vals
	 * @return
	 */
	public static String toFormattedString(Object... vals)
	{
		String str = "";
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(decPlaces);
		for (int i = 0; i < vals.length; i++)
		{
			Object obj = vals[i];
			if (obj instanceof Double)
				str += f.format((Double) obj);
			else if (obj instanceof Integer)
				str += f.format((Integer) obj);
			else if (obj instanceof Long)
				str += f.format((Long) obj);
			else if (obj instanceof Boolean)
				str += obj;
			else if (obj instanceof Float)
				str += f.format((Float) obj);
			else if (obj instanceof String)
				str += obj;
			str += i == vals.length - 1 ? "" : " ";
		}
		return str;
	}
	
	/**
	 * For retrieving mixes of double, int, long, boolean, float, and/or String vals from a String.
	 * @param str String containing values.
	 * @param typeOrder Order of types in which values should be parsed.
	 * @return
	 */
	public static Object[] fromString(String str, ValType... typeOrder)
	{
		Object[] objs = new Object[typeOrder.length];
		
		Scanner scan = new Scanner(str.replaceAll(",", ""));
		ValType type;
		for (int i = 0; i < objs.length && scan.hasNext(); i++)
		{
			type = typeOrder[i];
			if (type == ValType.DOUBLE)
				objs[i] = scan.nextDouble();
			else if (type == ValType.INT)
				objs[i] = scan.nextInt();
			else if (type == ValType.LONG)
				objs[i] = scan.nextLong();
			else if (type == ValType.BOOLEAN)
				objs[i] = scan.nextBoolean();
			else if (type == ValType.FLOAT)
				objs[i] = scan.nextFloat();
			else if (type == ValType.STRING)
				objs[i] = scan.next();
		}
		scan.close();
		
		return objs;
	}
	
	public static double[] fromString(String str)
	{
		Scanner scan = new Scanner(str);
		double[] vals = new double[(int) (str.length() / 2) + 1];
		int i = 0;
		while (scan.hasNextDouble())
		{
			vals[i] = scan.nextDouble();
			i++;
		}
		double[] vals2 = new double[i];
		for (int j = 0; j < vals2.length; j++)
			vals2[j] = vals[j];
		scan.close();
		return vals2;
	}
	
	public static double[] fromString(String str, int minL)
	{
		double[] from = fromString(str);
		if (from.length < minL)
			from = Arrays.copyOf(from, minL);
		return from;
	}
	
	public static int[] fromStringInt(String str)
	{
		Scanner scan = new Scanner(str);
		int[] vals = new int[(int) (str.length() / 2) + 1];
		int i = 0;
		while (scan.hasNextInt())
		{
			vals[i] = scan.nextInt();
			i++;
		}
		int[] vals2 = new int[i];
		for (int j = 0; j < vals2.length; j++)
			vals2[j] = vals[j];
		scan.close();
		return vals2;
	}
	
	public static double[] toArrayD(double... vals)
	{
		return vals;
	}
	
	public static boolean[] toArrayB(boolean... vals)
	{
		return vals;
	}
	
	public static int[] toArrayI(int... vals)
	{
		return vals;
	}
	
	public static Object[] toArrayO(Object... vals)
	{
		return vals;
	}
	
	@SafeVarargs
	public static <T> T[] of(T... vals)
	{
		return vals;
	}
	
	public static float[] toArrayF(float... vals)
	{
		return vals;
	}
	
	public static char[] toArrayC(char... vals)
	{
		return vals;
	}
	
	public static double[] toRadians(double... degrees)
	{
		for (int i = 0; i < degrees.length; i++)
		{
			degrees[i] = Math.toRadians(degrees[i]);
		}
		return degrees;
	}
	
	public static double[] toDegrees(double... radians)
	{
		for (int i = 0; i < radians.length; i++)
			radians[i] = Math.toDegrees(radians[i]);
		return radians;
	}
	
	public static double[] append(double[] array, double newVal)
	{
		double[] arr = Arrays.copyOf(array, array.length + 1);
		arr[arr.length - 1] = newVal;
		return arr;
	}
	
	public static <T> T[] concat(T[] array, T[] other)
	{
		T[] out = Arrays.copyOf(array, array.length + other.length);
		for (int i = array.length; i < out.length; i++)
			out[i] = other[i - array.length];
		return out;
	}
	
	public static int[] append(int[] array, int newVal)
	{
		int[] arr = Arrays.copyOf(array, array.length + 1);
		arr[arr.length - 1] = newVal;
		return arr;
	}
	
	public static <O extends Object> O[] append(O[] array, O obj)
	{
		O[] arr = Arrays.copyOf(array, array.length + 1);
		arr[arr.length - 1] = obj;
		return arr;
	}
	
	public static <T> T[] extend(T[] array, int newLength, IntFunction<T> filler)
	{
		T[] arr = Arrays.copyOf(array, newLength);
		for (int i = array.length; i < arr.length; i++)
			arr[i] = filler.apply(i);
		return arr;
	}
	
	public static <T> void fillFrom(T[] to, T[] from, int toInd, int fromInd, int length)
	{
		for (int i = 0; i < length; i++)
			to[toInd + i] = from[fromInd + i];
	}
	
	public static <T> void fillFrom(T[] to, T[] from)
	{
		fillFrom(to, from, 0, 0, Math.min(to.length, from.length));
	}
	
	public static void fillFrom(double[] to, double[] from, int toInd, int fromInd, int length)
	{
		for (int i = 0; i < length; i++)
			to[toInd + i] = from[fromInd + i];
	}
	
	public static void fillFrom(double[] to, double[] from)
	{
		fillFrom(to, from, 0, 0, Math.min(to.length, from.length));
	}
	
	public static void fillFrom(int[] to, int[] from, int toInd, int fromInd, int length)
	{
		for (int i = 0; i < length; i++)
			to[toInd + i] = from[fromInd + i];
	}
	
	public static void fillFrom(int[] to, int[] from)
	{
		fillFrom(to, from, 0, 0, Math.min(to.length, from.length));
	}
	
	public static double[] removeDuplicates(double[] array, double margin)
	{
		if (array.length == 0 || array.length == 1)
			return array;
		Arrays.sort(array);
		ArrayList<Double> uniq = new ArrayList<>();
		uniq.add(array[0]);
		for (int i = 1; i < array.length; i++)
			if (Math.abs(array[i] - array[i - 1]) > margin)
				uniq.add(array[i]);
		double[] out = new double[uniq.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = uniq.get(i);
		return out;
	}
	
	public static <O extends Object> O[] remove(O[] array, O obj)
	{
		@SuppressWarnings("unchecked")
		O[] arr = (O[]) Array.newInstance(obj.getClass(), array.length - 1);
		int i = 0;
		for (O o : array)
		{
			if (o != obj)
			{
				if (i < arr.length)
				{
					arr[i] = o;
					i++;
				}
				else
				{
					return array;
				}
			}
		}
		return arr;
	}
	
	public static <O extends Object> O[] insert(O[] array, int index, O obj)
	{
		@SuppressWarnings("unchecked")
		O[] arr = (O[]) Array.newInstance(obj.getClass(), array.length + 1);
		for (int i = 0; i < arr.length; i++)
		{
			if (i < index)
				arr[i] = array[i];
			else if (i == index)
				arr[i] = obj;
			else
				arr[i] = array[i - 1];
		}
		return arr;
	}
	
	@SuppressWarnings("unchecked")
	public static <O extends Object> O[] subtract(O[] array, O[] sub)
	{
		ArrayList<O> out = new ArrayList<>();
		out.addAll(Arrays.asList(array));
		out.removeAll(Arrays.asList(sub));
		return (O[]) out.toArray();
	}
	
	public static <T> T[] randomFetch(T[] from, T[] fill)
	{
		for (int i = 0; i < fill.length; i++)
			fill[i] = from[RNG.nextInt(from.length)];
		return fill;
	}
	
	public static <T> T[] randomFetch(T[] from, T[] fill, Function<T, T> process)
	{
		for (int i = 0; i < fill.length; i++)
			fill[i] = process.apply(from[RNG.nextInt(from.length)]);
		return fill;
	}
	
	public static int wrappedIndex(int unwrapped, int length)
	{
		int mod = unwrapped % length;
		if (mod < 0)
			mod += length;
		return mod;
	}
	
	public static int multiIndex(int... indOfPairs)
	{
		int i = indOfPairs[0];
		int of = 1;
		for (int j = 2; j < indOfPairs.length; j += 2)
		{
			of *= indOfPairs[j - 1];
			i += indOfPairs[j] * of;
		}
		return i;
	}
	
	/////////////////////
	
	@SuppressWarnings("unchecked")
	public static <T> T[] newEmpty(T[] type, int length)
	{
		return (T[]) Array.newInstance(type.getClass().getComponentType(), length);
	}
	
	public static <T> T[] fill(T[] array, int start, int end, IntFunction<T> indFill)
	{
		for (int i = start; i < end; i++)
			array[i] = indFill.apply(i);
		return array;
	}
	public static <T> T[] fill(T[] array, IntFunction<T> indFill)
	{
		return fill(array, 0, array.length - 1, indFill);
	}
	
	public static <T> T[] extendPre(T[] array, int extension)
	{
		return extendPre(array, extension, (i) -> null);
	}
	public static <T> T[] extendPre(T[] array, int extension, IntFunction<T> indFill)
	{
		T[] out = newEmpty(array, array.length + extension);
		
		for (int i = 0; i < extension; i++)
			out[i] = indFill.apply(i);
		for (int i = extension; i < out.length; i++)
			out[i] = array[i - extension];
		
		return out;
	}
	
	public static <T> T[] extendPost(T[] array, int extension)
	{
		return extendPost(array, extension, (i) -> null);
	}
	public static <T> T[] extendPost(T[] array, int extension, IntFunction<T> indFill)
	{
		T[] out = Arrays.copyOf(array, array.length + extension);
		for (int i = array.length; i < out.length; i++)
			out[i] = indFill.apply(i);
		return out;
	}
	
	public static <T> String toString(T[] array, String pre, String mid, String post)
	{
		return toString(array, (t) -> t.toString(), pre, mid, post);
	}
	public static <T> String toString(T[] array, Function<T, String> printer, String pre, String mid, String post)
	{
		String out = pre;
		for (int i = 0; i < array.length; i++)
			out += printer.apply(array[i]) + (i == array.length - 1 ? "" : mid);
		return out + post;
	}
	
	public static <T> T[] replace(T[] array, T[] with, int index, Ind newInd)
	{
		return replace(array, with, index, newInd, false);
	}
	public static <T> T[] replace(T[] array, T[] with, int index, Ind newInd, boolean recursive)
	{
		if (with.length != 1)
		{
			T[] out = newEmpty(array, array.length + with.length - 1);
			for (int i = 0; i < index; i++)
				out[i] = array[i];
			for (int i = 0; i < with.length; i++)
				out[index + i] = with[i];
			for (int i = index + 1; i < array.length; i++)
				out[i + with.length - 1] = array[i];
			if (newInd != null && !recursive)
				newInd.set(index + with.length);
			return out;
		}
		array[index] = with[0];
		if (newInd != null && !recursive)
			newInd.set(index + 1);
		return array;
	}
	public static <T> int replaceFirst(T[] array, ArrayReplacer<T> arrayReplacer)
	{
		for (int i = 0; i < array.length; i++)
		{
			T rep = arrayReplacer.replace(i, array[i]);
			if (rep != array[i])
			{
				array[i] = rep;
				return i;
			}
		}
		return -1;
	}
	@SuppressWarnings("unchecked")
	public static <T> T[] removeIf(T[] array, Predicate<T> remIf)
	{
		ArrayList<T> out = new ArrayList<>(array.length);
		for (T t : array)
			if (!remIf.test(t))
				out.add(t);
		return out.toArray((T[]) Array.newInstance(array.getClass().getComponentType(), out.size()));
	}
	public static <T> T[] trim(T[] array)
	{
		int trimF = 0, trimE = 0;
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] == null)
				trimF++;
			else
				break;
		}
		for (int i = array.length - 1; i >= 0; i--)
		{
			if (array[i] == null)
				trimE++;
			else
				break;
		}
		@SuppressWarnings("unchecked")
		T[] out = (T[]) Array.newInstance(array.getClass().componentType(), array.length - trimF - trimE);
		for (int i = 0; i < out.length; i++)
			out[i] = array[i + trimF];
		return out;
	}
	
	public static <T> boolean containsAll(T[] arr, T[] contains, BiFunction<T, T, Boolean> test)
	{
		boolean[] found = new boolean[contains.length];
		int foundI = 0;
		for (int i = 0; i < arr.length; i++)
		{
			for (int j = 0; j < found.length; j++)
			{
				if (!found[j] && test.apply(arr[i], contains[j]))
				{
					found[j] = true;
					foundI++;
				}
				if (foundI == found.length - 1)
					return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static <A, B> B[] transform(A[] arr, Function<A, B> transformer, boolean keepNull)
	{
		ArrayList<B> B = new ArrayList<>(arr.length);
		Class<B> clsB = null;
		for (A a : arr)
		{
			if (a == null && !keepNull)
				continue;
			B b = transformer.apply(a);
			if (b == null)
			{
				if (keepNull)
					B.add(b);
			}
			else
			{
				B.add(b);
				if (clsB == null)
					clsB = (Class<B>) b.getClass();
			}
		}
		if (clsB == null)
			return null;
		return B.toArray((B[]) Array.newInstance(clsB, B.size()));
	}
	public static <A, B> B[] transform(A[] arr, Function<A, B> transformer)
	{
		return transform(arr, transformer, false);
	}
	
	public static <T> boolean forCombos(T[] arr, int r, Predicate<T[]> onEach)
	{
		int n = arr.length;
		if (r > n)
			return false;
		@SuppressWarnings("unchecked")
		T[] combo = (T[]) Array.newInstance(arr.getClass().getComponentType(), r);
		int[] comboI = new int[combo.length];
		for (int i = 0; i < combo.length; i++)
		{
			comboI[i] = i;
			combo[i] = arr[i];
		}
		
		while (comboI[r - 1] < n)
		{
			if (onEach.test(combo))
				return true;
			int t = r - 1;
			while (t != 0 && comboI[t] == n - r + t)
				t--;
			comboI[t]++;
			combo[t] = arr[comboI[t]];
			for (int i = t + 1; i < r; i++)
			{
				comboI[i] = comboI[i - 1] + 1;
				if (comboI[i] < arr.length)
					combo[i] = arr[comboI[i]];
				else
					return false;
			}
		}
		return false;
	}
	
	public static <T> T[] shift(T[] arr, int by)
	{
		if (by == 0)
			return arr;
		if (by > 0)
		{
			for (int i = arr.length - 1 - by; i >= 0; i--)
			{
				arr[i + by] = arr[i];
				if (i < by)
					arr[i] = null;
			}
		}
		else
		{
			by = -by;
			for (int i = by; i < arr.length; i++)
			{
				arr[i - by] = arr[i];
				if (i >= arr.length - by);
					arr[i] = null;
			}
		}
		return arr;
	}
	
/*	@SuppressWarnings("unchecked")
	public static <SUB> Object cast(Object arr, Class<SUB> toOfType)
	{
		Class<?> arrType = arr.getClass();
		if (!arrType.isArray())
			throw new IllegalArgumentException("Array to cast must be an array.");
		
		Object out;
		for (int d = 0; d < outTypes.length; d++)
		{
			
		}
	}*/
	
	@SuppressWarnings("unchecked")
	public static <SUP, SUB extends SUP> SUB[] cast(SUP[] arr, Class<SUB> to)
	{
		SUB[] out = (SUB[]) Array.newInstance(to, arr.length);
		for (int i = 0; i < out.length; i++)
			out[i] = to.cast(arr[i]);
		return out;
	}
	
	public static <FROM, TO> Object castA(Object arr, Class<TO> toComponent, Function<FROM, TO> cast)
	{
		Class<?> arrType = arr.getClass();
		ArrayList<Class<?>> arrTypes = new ArrayList<>(5);
		Class<?> comp = arrType;
		while (comp.isArray())
		{
			arrTypes.add(comp);
			comp = comp.getComponentType();
		}
		arrTypes.add(comp);
		
		Class<?>[] outTypes = new Class<?>[arrTypes.size()];
		Class<?> outType = toComponent;
		for (int i = outTypes.length - 1; i >= 0; i--)
		{
			outTypes[i] = outType;
			outType = outType.arrayType();
		}

		return cast(arr, arrTypes.toArray(new Class<?>[arrTypes.size()]), outTypes, 0, cast);
	}
	@SuppressWarnings("unchecked")
	private static <FROM, TO> Object cast(Object arr, Class<?>[] arrTypes, Class<?>[] outTypes, int depth, Function<FROM, TO> cast)
	{
		if (depth < outTypes.length - 1)
		{
			int len = Array.getLength(arr);
			Object out = Array.newInstance(outTypes[depth + 1], len);
			for (int i = 0; i < len; i++)
				Array.set(out, i, cast(Array.get(arr, i), arrTypes, outTypes, depth + 1, cast));
			return out;
		}
		else
			return cast.apply((FROM) arr);
	}
	
	@SuppressWarnings("unchecked")
	public static <FROM, TO> TO[] cast(FROM[] arr, Class<TO> to, Function<FROM, TO> cast)
	{
		TO[] out = (TO[]) Array.newInstance(to, arr.length);
		for (int i = 0; i < out.length; i++)
			out[i] = cast.apply(arr[i]);
		return out;
	}
	
	public static float[] cast(double[] arr)
	{
		float[] out = new float[arr.length];
		for (int i = 0; i < out.length; i++)
			out[i] = (float) arr[i];
		return out;
	}
	
	public static <T> double sum(T[] arr, ToDoubleFunction<T> valOf)
	{
		double out = 0;
		for (T t : arr)
			out += valOf.applyAsDouble(t);
		return out;
	}
	
	//////////////////////

	public static class Ind
	{
		private int i;
		public Ind(int i)
		{
			this.i = i;
		}
		
		public int get()
		{
			return i;
		}
		
		public void set(int i)
		{
			this.i = i;
		}
		
		public void inc()
		{
			i++;
		}
	}
	
	public static class MultiIndex
	{
		private final int[] inds;
		private int count;
		private final int[] maxInds;
		private boolean stop;
		private final int last;
		
		public MultiIndex(int... maxInds)
		{
			inds = new int[maxInds.length];
			this.maxInds = maxInds;
			last = maxInds.length - 1;
			count = 0;
		}
		
		public void setMax(int i, int max)
		{
			maxInds[i] = max;
		}
		
		public void inc()
		{
			inds[0] = (inds[0] + 1) % maxInds[0];
			for (int i = 1; i < inds.length; i++)
			{
				if (inds[i - 1] == 0)
				{
					if (i == last && inds[last] == maxInds[last] - 1)
						stop = true;
					inds[i] = (inds[i] + 1) % maxInds[i];
				}
				else
					break;
			}
			count++;
		}
		
		public int get(int i)
		{
			return inds[i];
		}
		
		public int getCount()
		{
			return count;
		}
		
		public boolean stop()
		{
			return stop || last == -1;
		}
		
		public void reset()
		{
			count = 0;
			stop = false;
			Arrays.fill(inds, 0);
		}
	}
	
	@FunctionalInterface
	public static interface ArrayReplacer<T>
	{
		public T replace(int index, T current);
	}
	
	public static enum ValType
	{
		DOUBLE,
		INT,
		LONG,
		BOOLEAN,
		STRING,
		FLOAT,
	}

	///////////////////////////
	
	public static int getDecPlaces()
	{
		return decPlaces;
	}

	public static void setDecPlaces(int decPlaces)
	{
		ArrayUtils.decPlaces = decPlaces;
	}
}
