package utilities;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;

public class MultiArray<T>
{
	private final T[] array;
	private final int[] dims, offs;
	private final int depth, size;
	private final MultiIndex mInd;
	
	@SuppressWarnings("unchecked")
	public MultiArray(Class<T> clazz, int... dimensions)
	{
		dims = dimensions;
		depth = dims.length;
		if (depth < 1)
			throw new IllegalArgumentException("MultiArray must have depth at least 1.");
		size = (int) ArrayUtils.computeOver(dims, (a, b) -> a * b);
		offs = new int[depth - 1];
		offs[0] = dims[0];
		for (int i = 1; i < depth - 1; i++)
			offs[i] = dims[i] * offs[i - 1];
		ArrayUtils.flip(offs);
		array = (T[]) Array.newInstance(clazz, size);
		mInd = new MultiIndex(dims);
	}
	
	public MultiArray(Class<T> clazz, MultiIndex dimensions)
	{
		this(clazz, dimensions.maxInds);
	}
	
	//////////////////////////
	
	public T get(int... dind)
	{
		return array[lind(dind)];
	}
	
	public T get(int lind)
	{
		return array[lind];
	}
	
	public T get(MultiIndex mInd)
	{
		return array[mInd.count()];
	}
	
	public int lind(int... dind)
	{
		if (dind.length != depth)
			throw new ArrayIndexOutOfBoundsException("The dindex does not match dimensions of the MultiArray. dindex: " + Arrays.toString(dind));
		int out = 0;
		for (int i = 0; i < depth - 1; i++)
		{
			if (dind[i] >= dims[i] || dind[i] < 0)
				throw new ArrayIndexOutOfBoundsException(dind[i]);
			out += offs[i] * dind[i];
		}
		return out;
	}
	
	public int[] dind(int lind)
	{
		int[] out = new int[depth];
		out[0] = lind / offs[0];
		for (int i = 1; i < depth; i++)
		{
			lind -= out[i - 1] * offs[i - 1];
			out[i] = i == depth - 1 ? lind : lind / offs[i];
		}
		return out;
	}
	
	public void fillD(Function<int[], T> dindFiller)
	{
		for (int i = 0; i < size; i++)
			array[i] = dindFiller.apply(dind(i));
	}
	
	public void fillL(IntFunction<T> lindFiller)
	{
		for (int i = 0; i < size; i++)
			array[i] = lindFiller.apply(i);
	}
	
	public void forEach(Function<T, Boolean> opBreak)
	{
		mInd.reset();
		while (!mInd.stop())
		{
			Boolean b = opBreak.apply(get(mInd));
			if (b == null || !b)
				return;
		}
	}
	
	////////////////////
	
	public int depth()
	{
		return depth;
	}
	
	public int size()
	{
		return size;
	}
	
	public MultiIndex mInd()
	{
		return new MultiIndex(dims);
	}
	
	///////////////////////
	
	@Override
	public String toString()
	{
		return ArrayUtils.toString(array, (t) -> t == null ? "null" : t.toString(), "[", ", ", "]");
	}
	
	////////////////////////////////////////////////////
	
	public static class MultiIndex
	{
		private final int[] inds;
		private int count, dMark;
		private final int[] maxInds;
		private boolean stop;
		private final int last;
		
		public MultiIndex(int... maxInds)
		{
			inds = new int[maxInds.length];
			this.maxInds = maxInds;
			last = maxInds.length - 1;
			count = 0;
			dMark = last;
		}
		
		public void setMax(int i, int max)
		{
			maxInds[i] = max;
		}
		
		public void inc()
		{
			inds[last] = (inds[last] + 1) % maxInds[last];
			dMark = last;
			for (int i = last - 1; i >= 0; i--)
			{
				if (inds[i + 1] == 0)
				{
					if (i == 0 && inds[0] == maxInds[0] - 1)
						stop = true;
					inds[i] = (inds[i] + 1) % maxInds[i];
					dMark = i;
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
		
		public int[] get()
		{
			return inds;
		}
		
		public int depth()
		{
			return dMark;
		}
		
		public int climb()
		{
			return last - dMark;
		}
		
		public int count()
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
			dMark = last;
			stop = false;
			Arrays.fill(inds, 0);
		}
	}
}
