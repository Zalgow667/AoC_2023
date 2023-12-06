package solve;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class day5 {
	public static void main(String[] args) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get("src\\input.txt"))).trim();
		String[] lines = content.split("\n");

		String[] parts = content.split("\n\n");
		String seedString = parts[0];
		String[] others = Arrays.copyOfRange(parts, 1, parts.length);

		long[] seed = Arrays.stream(seedString.split(":")[1]
				.split("\\s+"))
				.filter(s -> !s.isEmpty())
				.mapToLong(Long::parseLong).toArray();

		List<Function> functions = new ArrayList<>();
		for (String functionString : others) {
			functions.add(new Function(functionString));
		}
		

		List<Long> P1 = new ArrayList<>();
		for (long x : seed) {
			for (Function f : functions) {
				x = f.applyOne(x);
			}
			P1.add(x);
		}
		System.out.println(min(P1));

		List<Long> P2 = new ArrayList<>();
		for (int i = 0; i < seed.length; i += 2) {
			long st = seed[i];
			long sz = seed[i + 1];
			List<Range> ranges = new ArrayList<>();
			ranges.add(new Range(st, st + sz));

			for (Function f : functions) {
				ranges = f.applyRange(ranges);
			}

			P2.add(minRange(ranges).start);
		}
		System.out.println(min(P2));
	}

	static Long min(List<Long> list) {
		return list.stream().min(Long::compareTo).orElseThrow();
	}

	static Range minRange(List<Range> ranges) {
		return ranges.stream().min(Range::compareTo).orElseThrow();
	}

	static class Function {
		List<Tuple> tuples;

		public Function(String S) {
			String[] lines = S.split("\n");
			tuples = new ArrayList<>();
			for (int i = 1; i < lines.length; i++) {
				String[] parts = lines[i].split("\\s+");
				tuples.add(new Tuple(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2])));
			}
		}

		long applyOne(long x) {
			for (Tuple tuple : tuples) {
				if (tuple.src <= x && x < tuple.src + tuple.sz) {
					return x + tuple.dst - tuple.src;
				}
			}
			return x;
		}

		List<Range> applyRange(List<Range> R) {
			List<Range> A = new ArrayList<>();
			List<Range> NR = new ArrayList<>();

			for (Tuple tuple : tuples) {
				long srcEnd = tuple.src + tuple.sz;
				while (!R.isEmpty()) {
					Range range = R.remove(R.size() - 1);

					long st = range.start;
					long ed = range.end;

					Range before = new Range(st, Math.min(ed, tuple.src));
					Range inter = new Range(Math.max(st, tuple.src), Math.min(srcEnd, ed));
					Range after = new Range(Math.max(srcEnd, st), ed);

					if (before.end > before.start) {
						NR.add(before);
					}
					if (inter.end > inter.start) {
						A.add(new Range(inter.start - tuple.src + tuple.dst, inter.end - tuple.src + tuple.dst));
					}
					if (after.end > after.start) {
						NR.add(after);
					}
				}
				R = NR;
			}
			A.addAll(R);
			return A;
		}
	}

	static class Tuple {
		long dst;
		long src;
		long sz;

		public Tuple(long dst, long src, long sz) {
			this.dst = dst;
			this.src = src;
			this.sz = sz;
		}
	}

	static class Range implements Comparable<Range> {
		long start;
		long end;

		public Range(long start, long end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int compareTo(Range o) {
			return Long.compare(this.start, o.start);
		}
	}

}
