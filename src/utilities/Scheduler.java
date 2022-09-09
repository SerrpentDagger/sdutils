package utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Scheduler
{
	private PriorityQueue<TickTask> queue = new PriorityQueue<TickTask>((a, b) -> { return Integer.compare(a.runTick, b.runTick); } );
	private ArrayList<TickTask> constant = new ArrayList<>();
	private int tick = 0;
	
	public void tick()
	{
		tick++;
		while (queue.peek() != null && queue.peek().runTick < tick)
			queue.poll().task.run();
		if (!constant.isEmpty())
		{
			Iterator<TickTask> it = constant.iterator();
			TickTask t;
			while (it.hasNext())
			{
				t = it.next();
				t.task.run();
				if (t.runTick < tick)
					it.remove();
			}
		}
	}
	
	public void runAll()
	{
		while (queue.peek() != null)
			queue.poll().task.run();
	}
	
	public void scheduleTask(TickTask task)
	{
		queue.add(task);
	}
	
	public void scheduleRepeated(TickTask task)
	{
		constant.add(task);
	}
	
	public boolean scheduleRefreshableRepeated(TickTask task)
	{
		if (task.key == null)
			throw new NullPointerException("Refreshable tasks must have non-null key ID.");
		boolean rep = constant.removeIf((t) -> task.key.equals(t.key));
		constant.add(task);
		return rep;
	}
	
	public boolean scheduleRefreshable(TickTask task)
	{
		if (task.key == null)
			throw new NullPointerException("Refreshable tasks must have non-null key ID.");
		boolean rep = queue.removeIf((t) -> task.key.equals(t.key));
		queue.add(task);
		return rep;
	}
	
	public void scheduleTask(int delay, Runnable task)
	{
		scheduleTask(new TickTask(tick + delay, task));
	}
	
	public void scheduleRepeated(int duration, Runnable task)
	{
		scheduleRepeated(new TickTask(tick + duration, task));
	}
	
	public boolean scheduleRefreshableRepeated(int duration, Object key, Runnable task)
	{
		return scheduleRefreshableRepeated(new TickTask(tick + duration, key, task));
	}
	
	public boolean scheduleRefreshable(int delay, Object key, Runnable task)
	{
		return scheduleRefreshable(new TickTask(tick + delay, key, task));
	}
	
	/**
	 * @param period Return the next period
	 * @param removal Return whether or not the periodic task should cease. Called <b>once</b> for each attempt to reschedule.
	 * @param task
	 */
	public void schedulePeriodic(Supplier<Integer> period, Supplier<Boolean> removal, Runnable task)
	{
		Runnable[] ref = new Runnable[1];
		Runnable per = () ->
		{
			if (!removal.get())
				scheduleTask(period.get(), () ->
				{
					task.run();
					ref[0].run();
				});
		};
		ref[0] = per;
		per.run();
	}
	
	public void schedulePeriodic(Supplier<Integer> period, int count, Runnable task)
	{
		AtomicInteger i = new AtomicInteger(0);
		schedulePeriodic(period, () -> i.getAndIncrement() >= count, task);
	}
	
	public void schedulePeriodic(int period, int count, Runnable task)
	{
		AtomicInteger i = new AtomicInteger(0);
		schedulePeriodic(() -> period, () -> i.getAndIncrement() < count, task);
	}
	
	////////////////////////////
	
	public int getTick()
	{
		return tick;
	}
	
	////////////////////////////
	
	public static class TickTask
	{
		public final int runTick;
		public final Object key;
		public final Runnable task;
		
		public TickTask(int runTick, Object key, Runnable task)
		{
			this.runTick = runTick;
			this.task = task;
			this.key = key;
		}
		
		public TickTask(int runTick, Runnable task)
		{
			this(runTick, null, task);
		}
		
		//////////////
		
		public static class KeyID
		{
			public final Object key;
			public final int id;
			
			public KeyID(Object key, int id)
			{
				this.key = key;
				this.id = id;
			}
			
			@Override
			public boolean equals(Object obj)
			{
				if (obj == null || !(obj instanceof KeyID))
					return false;
				KeyID oth = (KeyID) obj;
				return oth.key.equals(key) && oth.id == id;
			}
		}
	}
	
	public static class TickCycle
	{
		public final TickKeeper[] keepers;
		public final Runnable each;
		private int current = 0;
		
		public TickCycle(int period, Runnable... tasks)
		{
			this(null, () -> period, tasks);
		}
		
		public TickCycle(Runnable onEach, Supplier<Integer> period, Runnable... tasks)
		{
			each = onEach;
			keepers = new TickKeeper[tasks.length];
			for (int i = 0; i < tasks.length; i++)
				keepers[i] = new TickKeeper(period, tasks[i]);
		}
		
		public TickCycle(TickKeeper... keepers)
		{
			this(null, keepers);
		}
		
		public TickCycle(Runnable onEach, TickKeeper... keepers)
		{
			each = onEach;
			this.keepers = keepers;
		}
		
		public int tick(int count)
		{
			if (keepers[current].tick(count) == 0)
			{
				current = (current + 1) % keepers.length;
				if (each != null)
					each.run();
			}
			return current;
		}
		public int tick() { return tick(1); }
		
		public int randomStart(long seed)
		{
			Random r = new Random(seed);
			current = r.nextInt(keepers.length);
			keepers[current].current = r.nextInt(keepers[current].period.get());
			return current;
		}
	}
	
	public static class TickKeeper
	{
		public final Supplier<Integer> period;
		public final Runnable task;
		
		private int current = 0;
		
		public TickKeeper(int period, Runnable task)
		{
			this(() -> period, task);
		}
		
		public TickKeeper(Supplier<Integer> period, Runnable task)
		{
			this.period = period;
			this.task = task;
		}
		
		public int tick(int count)
		{
			if ((current += count) > period.get())
			{
				current = 0;
				task.run();
			}
			return current;
		}
		public int tick() { return tick(1); }
		
		//////////
		
		public int getCurrent() { return current; }
		public TickKeeper setCurrent(int current) { this.current = current; return this; }
	}
	
	public static class SparseTickKeeper extends TickKeeper
	{
		public final int runEvery;
		private int currentS = 0;
		
		public SparseTickKeeper(int period, Runnable task, int runEvery)
		{
			this(() -> period, task, runEvery);
		}
		
		public SparseTickKeeper(Supplier<Integer> period, Runnable task, int runEvery)
		{
			super(period, task);
			this.runEvery = runEvery;
		}
		
		@Override
		public int tick(int count)
		{
			setCurrent(getCurrent() + count);
			if (getCurrent() > period.get())
			{
				setCurrent(0);
				if (++currentS >= runEvery)
				{
					currentS = 0;
					task.run();
				}
			}
			return getCurrent();
		}
	}
}
