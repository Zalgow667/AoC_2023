package solve;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class day6 {
	public static void main(String[] args) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get("src\\input.txt"))).trim();
		System.out.println(solve1(content));
		System.out.println(solve2(content));
	}
	
	static int solve1(String races) {
		String[] lines = races.split("\n");
		
	    String dur = lines[0].split(":")[1].trim();
	    String distance = lines[1].split(":")[1].trim();

	    int[] time = Arrays.stream(dur.split("\\s+"))
	            .filter(s -> !s.isEmpty())
	            .mapToInt(Integer::parseInt)
	            .toArray();

	    int[] dst = Arrays.stream(distance.split("\\s+"))
	            .filter(s -> !s.isEmpty())
	            .mapToInt(Integer::parseInt)
	            .toArray();
		
		int total = 1;
		
		for(int i = 0; i < time.length; i++) {
			int cmpt = 0;
			for(int timePressed = 0; timePressed <= time[i]; timePressed++) {
				int dstParcouru = timePressed * (time[i]-timePressed);
				if(dstParcouru > dst[i]) {
					cmpt++;
				}
			}
			total *= cmpt;
		}
		return total;
	}
	
	static int solve2(String races) {
		String[] lines = races.split("\n");

	    long time = Long.valueOf(lines[0].split(":")[1].trim().replaceAll("\\s", ""));
	    long dst = Long.valueOf(lines[1].split(":")[1].trim().replaceAll("\\s", ""));
	    
	    int total = 0;
	    
		for(int timePressed = 0; timePressed <= time; timePressed++) {
			long dstParcouru = timePressed * (time-timePressed);
			if(dstParcouru > dst) {
				total++;
			}
		}
		return total;
	}
}