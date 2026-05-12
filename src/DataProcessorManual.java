import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class DataProcessorManual {
    private Vector<HardDisk> hardDisks;

    public DataProcessorManual(Vector<HardDisk> hardDisks) {
        if (hardDisks == null) throw new IllegalArgumentException("hardDisks is null");
        Vector<HardDisk> copy = new Vector<HardDisk>();
        for (HardDisk elem : hardDisks) {            // clone the hardDisks to not modify the original list
            copy.add(new HardDisk(elem.getDateOfEntry(), elem.getSerialNumber(), elem.getModel(), elem.getCapacityInBytes(), elem.isFailing(), elem.getSmartValues()));
        }
        this.hardDisks = copy;
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
        for (HardDisk elem : hardDisks) {
            if (predicate.test(elem)) filteredVector.add(elem);
        }
        return filteredVector;
    }


    // Returns the HardDisk with a specified maximum value
    public HardDisk max(Comparator<HardDisk> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Comparator is null");
        HardDisk maxHardDisk = hardDisks.getFirst();
        for (int i = 1; i < hardDisks.size(); i++) {
            if (comparator.compare(maxHardDisk, hardDisks.get(i)) < 0) {
                maxHardDisk = hardDisks.get(i);
            }
        }
        return maxHardDisk;
    }


    // Returns the HardDisk with a specified minimum value
    public HardDisk min(Comparator<HardDisk> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Comparator is null");
        HardDisk minHardDisk = hardDisks.getFirst();
        for (int i = 1; i < hardDisks.size(); i++) {
            if (comparator.compare(minHardDisk, hardDisks.get(i)) > 0) {
                minHardDisk = hardDisks.get(i);
            }
        }
        return minHardDisk;
    }


    // Returns a mean value specified by the function
    public double mean(Function<HardDisk, Long> function) {
        Long sum = 0L;
        if (function == null) throw new IllegalArgumentException("Function is null");
        for (HardDisk elem : hardDisks) {
            sum += function.apply(elem);
        }
        return (double)sum/hardDisks.size();
    }


    // Returns a median value specified by the function
    public long median(Comparator<HardDisk> sortingComparator, Function<HardDisk, Long> function) throws Exception {
        if (sortingComparator == null || function == null) throw new IllegalArgumentException("Comparator is null");
        if (hardDisks.isEmpty()) throw new Exception("HardDisks is empty, no such element");
        Vector<HardDisk> copy = new Vector<HardDisk>();
        for (HardDisk elem : hardDisks) {            // clone the hardDisks to not modify the original list
            copy.add(new HardDisk(elem.getDateOfEntry(), elem.getSerialNumber(), elem.getModel(), elem.getCapacityInBytes(), elem.isFailing(), elem.getSmartValues()));
        }
        Vector<Long> sortedVectorL = new Vector<Long>();
        copy.sort(sortingComparator);
        for (HardDisk elem : copy) {
            sortedVectorL.add(function.apply(elem));
        }
        int index = hardDisks.size() / 2;
        if (sortedVectorL.size() % 2 != 0) {
            return sortedVectorL.get(index);
        }
        return (sortedVectorL.get(index)+sortedVectorL.get(index-1))/2;
    }


    // Counts distinct values based on the given HardDisk-to-String mapping function
    public long countDistinctStrings(Function<HardDisk, String> function) {
        if (function == null) throw new IllegalArgumentException("Function is null");
        int count = 0;
        List<String> distinctString = new LinkedList<>();
        Iterator<HardDisk> iterator = hardDisks.iterator();
        while (iterator.hasNext()) {
            String current = function.apply(iterator.next());
            if (!distinctString.contains(current)) {
                distinctString.add(current);
                count++;
            }
        }
        return count;
    }
}
