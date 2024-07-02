import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
	private final static int PART_SIZE = 1000;

	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream("10m.txt")))) {
			long start = System.currentTimeMillis();

			List<Integer> list = new ArrayList<>();
			String line = br.readLine();
			while (line != null) {
				list.add(Integer.parseInt(line));
				line = br.readLine();
			}
			System.out.println(getStatistics(list));

			long end = System.currentTimeMillis();
			System.out.println("Time: " + (end - start) / 1000.0 + "s");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Statistics getStatistics(List<Integer> list) {
		ExecutorService executor = Executors.newCachedThreadPool();
		StatisticsWrapper resultWrapper = new StatisticsWrapper(List.of());

		List<Callable<StatisticsWrapper>> callables = new ArrayList<>();
		for (int i = 0; i < list.size(); i += PART_SIZE) {
			int index = i;
			callables.add(() -> {
				List<Integer> sublist = list.subList(index, Math.min(index + PART_SIZE, list.size()));
				StatisticsWrapper wrapper = new StatisticsWrapper(sublist);

				return wrapper;
			});
		}


		try {
			for (Callable<StatisticsWrapper> callable : callables) {
				resultWrapper.merge(callable.call());
			}
			if (resultWrapper != null) {
				return resultWrapper.getStatistics();
			}

			List<Future<StatisticsWrapper>> wrappers = executor.invokeAll(callables);

			for (Future<StatisticsWrapper> w : wrappers) {
				resultWrapper.merge(w.get());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		executor.shutdown();

		return resultWrapper.getStatistics();
	}
}
