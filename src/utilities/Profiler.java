package utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import utilities.ArrayUtils.Ind;

public class Profiler
{
	public static final NumberFormat PERCENT_FORMAT = NumberFormat.getInstance();
	static
	{ PERCENT_FORMAT.setMaximumFractionDigits(2); }
	
	/////////////////
	
	private Mark root;
	private Mark[] last;
	private final int smoothness;
	
	public Profiler()
	{
		this(1);
	}
	
	public Profiler(int smoothness)
	{
		this.smoothness = Math.max(1, smoothness);
		last = new Mark[this.smoothness];
	}
	
	public Profiler reset()
	{
		ArrayUtils.shift(last, 1);
		last[0] = root;
		root = null;
		return this;
	}

	public Profiler push(String name)
	{
		if (root == null)
			root = new Mark(name);
		else
			root.push(name);
		return this;
	}

	public Profiler pop()
	{
		if (root == null)
			throw new IllegalStateException("You must push a Profiler at least once before popping.");
		root.pop();
		return this;
	}
	
	public Mark[] getLasts()
	{
		return ArrayUtils.removeIf(last, (m) -> m == null);
	}
	
	public Mark getLast()
	{
		return last[0];
	}
	
	public Mark getSmooth()
	{
		Mark[] l = getLasts();
		if (l.length == 0)
			return root;
		if (l.length == 1)
			return l[0];
		return getSmooth(l);
	}
	
	private static Mark getSmooth(Mark[] layer)
	{
		String layerName = layer[0].name;
		MultiCalc<String> subDurs = new MultiCalc<>();
		int dur = 0;
		for (Mark l : layer)
		{
			l.finished.forEach((m) -> subDurs.addGet(m.name, m.duration()));
			dur += l.duration();
		}
		dur /= layer.length;
		subDurs.calcEach((cur) -> cur / layer.length);
		Mark[] outArr = new Mark[subDurs.size()];
		Ind i = new Ind(0);
		subDurs.forEach((str, val) ->
		{
			Mark[] subs = new Mark[layer.length];
			for (int j = 0; j < layer.length; j++)
			{
				Mark sub = layer[j].getSubMark(str);
				if (sub == null)
					sub = new Mark(0, str, new Mark[0]);
				subs[j] = sub;
			}
			outArr[i.get()] = getSmooth(subs);
			i.inc();
		});
		return new Mark(dur, layerName, outArr);
	}
	
	public int getDepth()
	{
		return root == null ? 0 : root.getDepth();
	}
	
	@Override
	public String toString()
	{
		if (last[0] == null)
			return root == null ? super.toString() : root.toString();
		return last[0].toString();
	}
	
	public static class Mark
	{
		private final String name;
		private final long stamp;
		private final ArrayList<Mark> finished;
		private Mark curSub;
		private int duration;
		private boolean popped;
		
		protected Mark(int duration, String name, Mark[] finished)
		{
			stamp = -1;
			popped = true;
			this.name = name;
			this.finished = new ArrayList<>(Arrays.asList(finished));
			this.duration = duration;
		}
		
		public Mark(String name)
		{
			stamp = System.currentTimeMillis();
			popped = false;
			this.name = name;
			finished = new ArrayList<>(5);
		}
		
		public Mark push(String name)
		{
			if (popped)
				throw new IllegalStateException("The Profiler Mark '" + this.name + "' is already popped.");
			if (curSub == null)
				curSub = new Mark(name);
			else
				curSub.push(name);
			return this;
		}
		
		public Mark pop()
		{
			if (popped)
				throw new IllegalStateException("The Profiler Mark '" + name + "' is already popped.");
			if (curSub == null)
			{
				duration = (int) (System.currentTimeMillis() - stamp);
				popped = true;
			}
			else
			{
				curSub.pop();
				if (curSub.popped)
				{
					finished.add(curSub);
					duration += curSub.duration;
					curSub = null;
				}
			}
			return this;
		}
		
		public int duration()
		{
			if (popped)
				return duration;
			return (int) (System.currentTimeMillis() - stamp);
		}
		
		public int subDuration()
		{
			if (finished.size() == 0)
				return duration();
			int d = 0, size = finished.size();
			for (int i = 0; i < size; i++)
				d += finished.get(i).subDuration();
			return d;
		}
		
		public Mark getSubMark(String name)
		{
			if (curSub != null && curSub.name.equals(name))
				return curSub;
			for (int i = 0; i < finished.size(); i++)
				if (finished.get(i).name.equals(name))
					return finished.get(i);
			return null;
		}
		
		public int getDepth()
		{
			if (curSub == null)
				return 1;
			return curSub.getDepth() + 1;
		}
		
		@Override
		public String toString()
		{
			return toString(0, duration()).trim();
		}
		
		private String toString(int depth, int total)
		{
			String out = StringUtils.mult("|  ", depth) + "- " + name + ": " + duration() +
					" (" + PERCENT_FORMAT.format((double) duration() / (double) total) + "%)\n";
			for (int i = 0; i < finished.size(); i++)
				out += finished.get(i).toString(depth + 1, total);
			return out;
		}
	}
}
