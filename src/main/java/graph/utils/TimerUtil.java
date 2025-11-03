package graph.utils;

/**
 * Simple timer utility using System.nanoTime()
 */
public class TimerUtil {
    public static class Timer {
        private final long start;
        public Timer(long start) { this.start = start; }
        public long stop() { return System.nanoTime() - start; }
    }

    public static Timer start() { return new Timer(System.nanoTime()); }
}
