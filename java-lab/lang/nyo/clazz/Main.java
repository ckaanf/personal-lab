package nyo.clazz;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {

		List<Object> list = new ArrayList<>();

		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		executor.scheduleAtFixedRate(() -> {
			MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

			System.out.printf("memory : %d\n", heapMemoryUsage.getUsed());

			// list.add(new Outer(100_000).getInnerObject());
			System.out.println(list.get(0));

			System.gc();

		},0,1000, TimeUnit.MILLISECONDS);
	}
}
