package utilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * A class for simple implementation of parallel processing.
 * <p>Each instance of this class produces and maintains its own pool of worker threads through {@linkplain Executors#newFixedThreadPool(int)}. The pool will be shutdown through
 * {@linkplain Object#finalize()} when the instance leaves scope. You can shutdown the instance and its pool early by using {@linkplain Parallelizer#shutdown()}.
 * <p>Use of this class follows the flow of instantiation, adding of tasks through the task methods, and then running those tasks through
 * the run methods. You can block the managing thread until completion of the currently running tasks by using {@linkplain Parallelizer#await()}.
 * <p>You should not share and use one instance of this class among multiple threads.
 * 
 * @author SerpentDagger
 *
 */
public class Parallelizer
{
	private int threadCount;
	private ExecutorService threads;
	private ArrayList<Callable<?>> tasks = new ArrayList<>();
	private RunContext current;
	
	/**
	 * Create a new {@linkplain Parallelizer} with a number of threads equal to the number of available processors of the runtime environment.
	 */
	public Parallelizer()
	{
		this(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Create a new {@linkplain Parallelizer} with a given number of threads.
	 * @param threadCount
	 */
	public Parallelizer(int threadCount)
	{
		this.threadCount = threadCount;
		threads = Executors.newFixedThreadPool(threadCount);
	}
	
	/**
	 * Updates the number of threads to the number of available processors of the runtime environment, by using {@linkplain Parallelizer#updateThreadCount(int)}.
	 * @return this
	 */
	public Parallelizer updateThreadCount()
	{
		return updateThreadCount(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Updates the number of threads being used by shutting down the current pool and starting a new one.
	 * <p>The {@linkplain Parallelizer} must be empty of tasks in order to do this.
	 * @param threadCount
	 * @return this
	 */
	public Parallelizer updateThreadCount(int threadCount)
	{
		assertEmpty();
		this.threadCount = threadCount;
		threads.shutdown();
		threads = Executors.newFixedThreadPool(threadCount);
		return this;
	}
	
	/**
	 * Add a Runnable task to the list. The corresponding {@linkplain Future} will return null on succesful completion.
	 * @param task
	 * @return this
	 */
	public Parallelizer task(Runnable task)
	{
		tasks.add(Executors.callable(task));
		return this;
	}
	
	/**
	 * Adds the multiple Runnable tasks to the list. The corresponding {@linkplain Future}s will return null on succesful completion.
	 * @param tasks
	 * @return this
	 */
	public Parallelizer tasks(Runnable... tasks)
	{
		for (Runnable r : tasks)
			task(r);
		return this;
	}
	
	/**
	 * Adds a {@linkplain ByThread} task to be executed for each thread. The task will recieve the index of the thread on which it is run. The corresponding {@linkplain Future}s will return null on succesful completion.
	 * <p>Useful for alloting threads to chunks of an array.
	 * @param task
	 * @return this
	 */
	public Parallelizer taskEach(ByThread task)
	{
		for (int i = 0; i < threadCount; i++)
		{
			final int ii = i;
			tasks.add(() -> { task.run(ii); return null; });
		}
		return this;
	}
	
	/**
	 * Runs all tasks in the list.
	 * @return An array of {@linkplain Future}s as returned from {@linkplain ExecutorService#submit(Callable)}.
	 */
	public Future<?>[] outputRunAll()
	{
		return outputRun(tasks.size());
	}
	
	/**
	 * Runs all tasks in the list. Latest output can be retrieved with {@linkplain Parallelizer#getLatestOutput()}.
	 * @return this
	 */
	public Parallelizer runAll()
	{
		outputRunAll();
		return this;
	}
	
	/**
	 * Runs the latest countToRun tasks in the list.
	 * @param countToRun
	 * @return An array of {@linkplain Future}s as returned from {@linkplain ExecutorService#submit(Callable)}.
	 */
	public Future<?>[] outputRun(int countToRun)
	{
		if (countToRun > tasks.size())
			throw new IllegalArgumentException("Parallelizer cannot run more tasks than are in its list.");
		current = new RunContext(countToRun);
		final RunContext here = current;
		Iterator<Callable<?>> it = tasks.iterator();
		for (int i = tasks.size() - countToRun; i < countToRun && it.hasNext(); i++)
		{
			final Callable<?> task = it.next();
			here.output[i] = threads.submit(() ->
			{
				try
				{
					return task.call();
				}
				finally
				{
					here.latch.countDown();
					if (here.latch.getCount() == 0)
						here.runDone();
				}
			});
			it.remove();
		}
		return here.output;
	}
	
	/**
	 * Runs the latest countToRun tasks in the list. Latest output can be retrieved with {@linkplain Parallelizer#getLatestOutput()}.
	 * @param countToRun
	 * @return this
	 */
	public Parallelizer run(int countToRun)
	{
		outputRun(countToRun);
		return this;
	}
	
	/**
	 * Await execution of the tasks started with the most recent call of {@linkplain Parallelizer#outputRun(int)} (or equivelant).
	 * <p>This method will block until those tasks have finished.
	 * @return this
	 */
	public Parallelizer await()
	{
		buryInterrupted(() -> current.latch.await());
		return this;
	}
	
	/**
	 * Defines a Runnable to be run when the most recently launched execution of tasks finishes. The Runnable will be called on whichever worker thread finishes its task last.
	 * <p>This method is designed to be called after {@linkplain Parallelizer#outputRun(int)} (or equivelant), and before {@linkplain Parallelizer#await()}.
	 * <p>This method can be called for any number of individual asynchronous run methods. Example:<p>
	 * <pre>{@code
...
Parallelizer par;
par.tasks(tasks);
par.run(4).whenDone(done1);
par.run(6).whenDone(done2);
...}</pre>
	 * @param whenDone Runnable to be run when execution finishes.
	 * @return this
	 */
	public Parallelizer whenDone(Runnable whenDone)
	{
		if (current != null)
			current.whenDone.set(whenDone);
		return this;
	}
	
	/**
	 * Casts the output {@linkplain Future}s of the most recent call of {@linkplain Parallelizer#outputRun(int)} (or equivelant) to an array of the desired type.
	 * <p>If the tasks have not finished, the corresponding slots in the array will be null. If no task has finished, the array itself will be null.
	 * @param <T> The type of output expected from <b>each</b> Future.
	 * @return An array T[] of the results of each Future.
	 */
	public <T> T[] castOutput()
	{
		return castOutput(current.output);
	}
	
	/**
	 * @return The output {@linkplain Future}s of the most recent call of {@linkplain Parallelizer#outputRun(int)} (or equivelant).
	 */
	public Future<?>[] getLatestOutput()
	{
		return current == null ? null : current.output;
	}
	
	/**
	 * Asserts that there are no tasks un-started, and no tasks running.
	 * @return this
	 */
	public Parallelizer assertEmpty()
	{
		assert tasks.isEmpty() && (current == null || current.latch == null || current.latch.getCount() == 0);
		return this;
	}
	
	/**
	 * Uses {@linkplain ExecutorService#shutdown()} to shut down the thread pool.
	 */
	public void shutdown()
	{
		threads.shutdown();
	}
	
	@Override
	public void finalize() throws Throwable
	{
		shutdown();
	}
	
	////////////////////
	
	/**
	 * @return The number of threads managed by this {@linkplain Parallelizer}.
	 */
	public int threads()
	{
		return threadCount;
	}
	
	/**
	 * @return The number of un-started tasks stored in this {@linkplain Parallelizer}.
	 */
	public int tasks()
	{
		return tasks.size();
	}
	
	/**
	 * Calculates and returns an {@linkplain IterationSpecs} with the specified start and end, and the number of threads of this {@linkplain Parallelizer}.
	 * @param start The starting index of the proposed iteration operation.
	 * @param end The ending index of the proposed iteration operation.
	 * @return The calculated IterationSpecs
	 */
	public IterationSpecs specsOf(int start, int end)
	{
		return new IterationSpecs(start, end, threadCount);
	}
	
	/**
	 * Fill the supplied array with the results of the filler, called for each index of the array or thread (whichever is fewer).
	 * <p>Useful for allocating resources to different threads.
	 * @param <T>
	 * @param toFill The array to be filled
	 * @param filler The {@linkplain ThreadFill} to be used on each index.
	 */
	public <T> void fillByThread(T[] toFill, ThreadFill<T> filler)
	{
		for (int i = 0; i < Math.min(toFill.length, threadCount); i++)
			toFill[i] = filler.fill(i);
	}
	
	/**
	 * Fill a new array with the results of the nonNullFiller, called for each thread index.
	 * @param <T>
	 * @param nonNullFiller A {@linkplain ThreadFill} to be used on each index, which must not return null for index 0.
	 * @return A new array T[] containing the results of the nonNullFiller.
	 */
	public <T> T[] fillByThread(ThreadFill<T> nonNullFiller)
	{
		T first = nonNullFiller.fill(0);
		@SuppressWarnings("unchecked")
		T[] out = (T[]) Array.newInstance(first.getClass(), threadCount);
		out[0] = first;
		for (int i = 1; i < threadCount; i++)
			out[i] = nonNullFiller.fill(i);
		return out;
	}
	
	////////////////////
	
	/**
	 * Cast the supplied array of {@linkplain Future}s to a new array T[] by filling that array with the results of {@linkplain Future#get()}.
	 * <p>Unfinished Futures will be skipped (corresponding slot in the array left null). If each Future is unfinished, the returned array will be null.
	 * @param <T>
	 * @param output The array of Futures whose results are to be fetched.
	 * @return A new array T[] containing the results of the supplied Futures.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] castOutput(Future<?>[] output)
	{
		try
		{
			T first = null;
			int i = 0;
			while (i < output.length)
			{
				if (output[i].isDone())
					first = (T) output[i].get();
				if (first == null)
					i++;
				else
					break;
			}
			if (first == null)
				return null;
			T[] out = (T[]) Array.newInstance(first.getClass(), output.length);
			for (; i < out.length; i++)
				if (output[i].isDone())
					out[i] = (T) output[i].get();
			return out;
		}
		catch (Exception e)
		{}
		return null; // No exception should ever be thrown, because only done futures are considered for the output.
	}
	
	private static void buryInterrupted(Interruptable from)
	{
		try
		{
			from.run();
		}
		catch (InterruptedException e)
		{}
	}
	
	///////////////////
	
	@FunctionalInterface
	private static interface Interruptable
	{
		public void run() throws InterruptedException;
	}
	
	/**
	 * A functional interface designed to return a resource for a given thread index, thus allocating distinct resources among threads.
	 * 
	 * @author SerpentDagger
	 *
	 * @param <T> The type of the returned Object resource.
	 */
	@FunctionalInterface
	public static interface ThreadFill<T>
	{
		/**
		 * Called to fill the given thread index with the output value.
		 * @param thread
		 * @return The T resource for that thread index.
		 */
		public T fill(int thread);
	}
	
	/**
	 * 
	 * A function which should be run on a thread-by-thread basis, distinguished by the supplied thread index.
	 * 
	 * @author SerpentDagger
	 *
	 */
	@FunctionalInterface
	public static interface ByThread
	{
		/**
		 * The method to be run on each thread, distinguished by the supplied thread index.
		 * @param thread The index of the thread on which the code is being run.
		 */
		public void run(int thread);
	}
	
	//////////////////////
	
	private static class RunContext
	{
		private CountDownLatch latch;
		private Future<?>[] output;
		private AtomicReference<Runnable> whenDone;
		
		private RunContext(int toRun)
		{
			latch = new CountDownLatch(toRun);
			output = new Future<?>[toRun];
		}
		
		private void runDone()
		{
			Runnable current = whenDone.get();
			if (current != null)
				current.run();
		}
	}
	
	/////////////////////
	
	/**
	 * 
	 * A data-calculating class that represents the relevant specifications of a parallel iteration across a range of indices.
	 * 
	 * <p>Example Usage:<p>
	 * <pre>{@code 
Parallelizer par = new Parallelizer();
IterationSpecs specs = par.specsOf(0, 100);
par.taskEach((th) ->
{
	for (int i = specs.starts[th]; i < specs.ends[th]; i++)
		out.println("Thread " + th + ": " + i);
});
par.runAll().await().shutdown();}</pre>
	 * 
	 * @author SerpentDagger
	 *
	 */
	public static class IterationSpecs
	{
		/** The overal starting index (inclusive). */
		public final int start;
		/** The overal ending index (exclusive). */
		public final int end;
		/** The starting indices (inclusive) of each thread. Use these for iteration. */
		public final int[] starts;
		/** The ending indices (exclusive) of each thread. Use these for iteration. */
		public final int[] ends;
		/** The overal span of indices. (end - start)*/
		public final int span;
		/** The minimum number of indices that each thread will get. */
		public final int each;
		/** The number of indices left over after even distribution among threads, to be given one-each to the first threads. */
		public final int extra;
		/** The number of threads involved in the parallel iteration. */
		public final int threadCount;
		
		/**
		 * Calculate a new {@linkplain IterationSpecs} representing the desired parallel iteration from start (inclusive) to end (exclusive), as divided among threadCount threads.
		 * @param start
		 * @param end
		 * @param threadCount
		 */
		public IterationSpecs(int start, int end, int threadCount)
		{
			this.start = start;
			this.end = end;
			this.threadCount = threadCount;
			span = end - start;
			each = span / threadCount;
			extra = span - (each * threadCount);
			starts = new int[threadCount];
			ends = new int[threadCount];
			int s = start;
			for (int i = 0; i < threadCount; i++)
			{
				starts[i] = s;
				s += each + (i < extra ? 1 : 0);
				ends[i] = s;
			}
			assert s == end;
		}
		
		/**
		 * @param index The index of the relevant thread.
		 * @return Whether or not that thread is the last in the iteration.
		 */
		public boolean lastThread(int index)
		{
			return index == threadCount - 1;
		}
		
		@Override
		public String toString()
		{
			String out = "" + start + "->" + end + " in " + threadCount + " threads (" + each + " each): " + "[";
			for (int i = 0; i < threadCount; i++)
				out += starts[i] + "->" + ends[i] + (i == threadCount - 1 ? "]" : ", ");
			return out;
		}
	}
}
