import java.util.List;

public class Statistics {
	private int max;
	private int min;
	private long sum;
	private long sumOdd;
	private long sumEven;
	private List<Integer> maxAscSequence;
	private List<Integer> maxDescSequence;
	private int count;
	private boolean odd;

	Statistics(int num, int count) {
		max = num;
		min = num;
		sumOdd = num;
		odd = false;
		maxAscSequence = List.of(num);
		maxDescSequence = List.of(num);
		this.count = count;
	}

	public void calculate(int num) {
		tryMax(num);
		tryMin(num);
		addSum(num);
	}

	public void merge(Statistics other) {
		tryMax(other.max);
		tryMin(other.min);
		tryMaxAscSequence(other.maxAscSequence);
		tryMaxDescSequence(other.maxDescSequence);
		sum += other.sum;
		if (odd) {
			sumOdd += other.sumEven;
			sumEven += other.sumOdd;
		} else {
			sumOdd += other.sumOdd;
			sumEven += other.sumEven;
		}
		odd = odd != other.odd;
		count += other.count;
	}

	public void tryMax(int max) {
		if (max > this.max) {
			this.max = max;
		}
	}

	public void tryMin(int min) {
		if (min < this.min) {
			this.min = min;
		}
	}

	public void tryMaxAscSequence(List<Integer> maxAscSequence) {
		if (maxAscSequence.size() > this.maxAscSequence.size()) {
			this.maxAscSequence = maxAscSequence;
		}
	}

	public void tryMaxDescSequence(List<Integer> maxDescSequence) {
		if (maxDescSequence.size() > this.maxDescSequence.size()) {
			this.maxDescSequence = maxDescSequence;
		}
	}

	public void addSum(long num) {
		sum += num;
		if (odd) {
			sumOdd += num;
		} else {
			sumEven += num;
		}
		odd = !odd;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public long getSum() {
		return sum;
	}

	public double getAvg() {
		return (double) sum / count;
	}

	public long getMedian() {
		return (Math.round(sumEven / (double) (count / 2)) +
				Math.round(sumOdd / Math.ceil(count / 2.0))) / 2;
	}

	public int getCount() {
		return count;
	}

	public List<Integer> getMaxAscSequence() {
		return maxAscSequence;
	}

	public List<Integer> getMaxDescSequence() {
		return maxDescSequence;
	}

	@Override
	public String toString() {
		return "1. " + max +
				"\n2. " + min +
				"\n3. " + getMedian() +
				"\n4. " + getAvg() +
				"\n5. " + maxAscSequence +
				"\n6. " + maxDescSequence;
	}
}
