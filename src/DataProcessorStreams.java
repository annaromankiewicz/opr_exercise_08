import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Comparator;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Predicate;


public class DataProcessorStreams {
    private Vector<HardDisk> hardDisks;

    public DataProcessorStreams(Vector<HardDisk> hardDisks) {
        if (hardDisks == null) throw new IllegalArgumentException("hardDisks is null");
        this.hardDisks = hardDisks;
    }


    // Can use Vector.sort internally, sorts the internal Vector
    public void sort(Comparator<HardDisk> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Comparator is null");
        hardDisks.sort(comparator);
    }


    // Returns size of the internal vector
    public long count() {
        return hardDisks.size();
    }


    // Returns a Vector of HardDisks filtered by predicate
    public Vector<HardDisk> filter(Predicate<HardDisk> predicate) {
        if (predicate == null) throw new IllegalArgumentException("Predicate is null");
        Vector<HardDisk> filteredVector = new Vector<HardDisk>();
        hardDisks.stream().filter(predicate).forEach(filteredVector::add);          // filteredVector::add is the same like x -> filterdVector.add(x)
        return filteredVector;
    }


    // Returns the HardDisk with a specified maximum value
    public HardDisk max(Comparator<HardDisk> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Comparator is null");
        return hardDisks.stream().max(comparator).orElseThrow();
    }


    // Returns the HardDisk with a specified minimum value
    public HardDisk min(Comparator<HardDisk> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Comparator is null");
        return hardDisks.stream().min(comparator).orElseThrow();
    }


    // Returns a mean value specified by the function
    public double mean(Function<HardDisk, Long> function) {
        if (function == null) throw new IllegalArgumentException("Comparator is null");
        return hardDisks.stream().map(function).mapToLong(x->x).average().orElseThrow(); // converts Stream<Long> into a LongStream: necessary to use function average
    }


    // Returns a median value specified by the function
    public long median(Comparator<HardDisk> sortingComparator, Function<HardDisk, Long> function) throws Exception {
        if (sortingComparator == null || function == null) throw new IllegalArgumentException("Comparator is null");
        if (hardDisks.isEmpty()) throw new Exception("HardDisks is empty, no such element");
        Vector<Long> sortedVector = new Vector<Long>();
        hardDisks.stream().sorted(sortingComparator).map(function).forEach(sortedVector::add);
        int index = hardDisks.size()/2;
        if (sortedVector.size() % 2 != 0) {
            return sortedVector.get(index);
        }
        return index;
    }


// Counts distinct values based on the given HardDisk-to-String mapping function
    public long countDistinctStrings(Function<HardDisk, String> function) {
        if (function == null) throw new IllegalArgumentException("Function is null");
        return hardDisks.stream().map(function).distinct().count();
    }
}