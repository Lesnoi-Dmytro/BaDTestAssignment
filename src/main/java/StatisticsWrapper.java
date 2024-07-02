import java.util.ArrayList;
import java.util.List;

public class StatisticsWrapper {
	private Statistics statistics;
	private List<Integer> startAscSequence;
	private List<Integer> endAscSequence;
	private List<Integer> startDescSequence;
	private List<Integer> endDescSequence;

	StatisticsWrapper(List<Integer> list) {
		if (!list.isEmpty()) {
			startAscSequence = new ArrayList<>();
			startAscSequence.add(list.get(0));
			startDescSequence = new ArrayList<>();
			startDescSequence.add(list.get(0));
			statistics = calculateStatistics(list);
		}
	}

	private Statistics calculateStatistics(List<Integer> list) {
		if (list.isEmpty()) {
			return null;
		}

		Statistics statistics = new Statistics(list.get(0), list.size());
		int last = list.get(0);
		List<Integer> ascSequence = new ArrayList<>();
		List<Integer> descSequence = new ArrayList<>();
		for (int i = 1; i < list.size(); i++) {
			int current = list.get(i);
			statistics.calculate(current);

			if (current > last) {
				if (descSequence.size() > 1) {
					statistics.tryMaxDescSequence(descSequence);
					descSequence.clear();
				}
				if (ascSequence.isEmpty()) {
					ascSequence.add(last);
				}
				ascSequence.add(current);
			} else if (current < last) {
				if (ascSequence.size() > 1) {
					statistics.tryMaxAscSequence(ascSequence);
					ascSequence.clear();
				}
				if (descSequence.isEmpty()) {
					descSequence.add(last);
				}
				descSequence.add(current);
			} else {
				ascSequence.clear();
				descSequence.clear();
			}
			last = current;
		}

		if (ascSequence.size() > 1) {
			statistics.tryMaxAscSequence(ascSequence);
			endAscSequence = ascSequence;
		} else {
			endAscSequence = new ArrayList<>();
			endAscSequence.add(last);
		}
		if (descSequence.size() > 1) {
			statistics.tryMaxDescSequence(descSequence);
			endDescSequence = descSequence;
		} else {
			endDescSequence = new ArrayList<>();
			endDescSequence.add(last);
		}

		for (int i = 1; i < list.size() && list.get(i) >
				startAscSequence.get(startAscSequence.size() - 1); i++) {
			startAscSequence.add(list.get(i));
		}
		for (int i = 1; i < list.size() && list.get(i) <
				startDescSequence.get(startDescSequence.size() - 1); i++) {
			startDescSequence.add(list.get(i));
		}

		return statistics;
	}

	public void merge(StatisticsWrapper wrapper) {
		if (statistics == null) {
			statistics = wrapper.statistics;
			endAscSequence = wrapper.endAscSequence;
			endDescSequence = wrapper.endDescSequence;
			return;
		}

		statistics.merge(wrapper.statistics);

		if (wrapper.startAscSequence.get(0) >
				endAscSequence.get(endAscSequence.size() - 1)) {
			List<Integer> newAscSequence = new ArrayList<>(endAscSequence);
			newAscSequence.addAll(wrapper.startAscSequence);
			statistics.tryMaxAscSequence(newAscSequence);
			if (wrapper.startAscSequence.size() ==
					wrapper.getStatistics().getCount()) {
				endAscSequence = newAscSequence;
			} else {
				endAscSequence = wrapper.endAscSequence;
			}
		} else {
			endAscSequence = wrapper.endAscSequence;
		}

		if (wrapper.startDescSequence.get(0) <
				endDescSequence.get(endDescSequence.size() - 1)) {
			List<Integer> newDescSequence = new ArrayList<>(endDescSequence);
			newDescSequence.addAll(wrapper.startDescSequence);
			statistics.tryMaxDescSequence(newDescSequence);
			if (wrapper.startDescSequence.size() ==
					wrapper.getStatistics().getCount()) {
				endDescSequence = newDescSequence;
			} else {
				endDescSequence = wrapper.endDescSequence;
			}
		} else {
			endDescSequence = wrapper.endDescSequence;
		}
	}

	public Statistics getStatistics() {
		return statistics;
	}
}
