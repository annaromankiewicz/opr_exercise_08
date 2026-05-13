import at.fhhgb.mc.opr.backblazedata.model.HardDisk;
import at.fhhgb.mc.opr.backblazedata.model.SMARTValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class DataProcessorManualTest {

    private Vector<HardDisk> disksOdd;   // 5 disks: capacities 100, 200, 300, 400, 500
    private Vector<HardDisk> disksEven;  // 4 disks: capacities 100, 200, 300, 400
    private Vector<HardDisk> disksOne;   // 1 disk:  capacity 42
    private Vector<HardDisk> disksEmpty; // 0 disks

    private static HardDisk disk(String sn, String model, long cap, boolean failing, int smartCount) {
        LinkedList<SMARTValue> smart = new LinkedList<>();
        for (int i = 0; i < smartCount; i++) smart.add(new SMARTValue(i, (long) i, (long) i));
        return new HardDisk(new Date(), sn, model, cap, failing, smart);
    }

    @BeforeEach
    void setUp() {
        disksOdd = new Vector<>(List.of(
                disk("SN1", "ModelA", 300L, false, 1),
                disk("SN2", "ModelB", 100L, true,  2),
                disk("SN3", "ModelA", 500L, false, 3),
                disk("SN4", "ModelC", 200L, true,  1),
                disk("SN5", "ModelA", 400L, false, 2)
        ));
        disksEven = new Vector<>(List.of(
                disk("SN1", "ModelA", 300L, false, 1),
                disk("SN2", "ModelB", 100L, true,  2),
                disk("SN3", "ModelA", 400L, false, 3),
                disk("SN4", "ModelC", 200L, true,  1)
        ));
        disksOne = new Vector<>(List.of(disk("SN1", "ModelA", 42L, false, 1)));
        disksEmpty = new Vector<>();
    }

    // -------- Constructor --------

    @Test
    void constructor_nullVector_throws() {
        assertThrows(IllegalArgumentException.class, () -> new DataProcessorManual(null));
    }

    @Test
    void constructor_emptyVector_ok() {
        assertDoesNotThrow(() -> new DataProcessorManual(disksEmpty));
    }

    // -------- sort --------

    @Test
    void sort_nullComparator_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertThrows(IllegalArgumentException.class, () -> p.sort(null));
    }

    @Test
    void sort_byCapacityAscending_sortsInternalOnly() {
        List<Long> externalBefore = disksOdd.stream().map(HardDisk::getCapacityInBytes).toList();
        DataProcessorManual p = new DataProcessorManual(disksOdd);

        p.sort(Comparator.comparingLong(HardDisk::getCapacityInBytes));

        // external vector must be untouched (constructor deep-copies)
        assertEquals(externalBefore,
                disksOdd.stream().map(HardDisk::getCapacityInBytes).toList(),
                "sort() must not modify the caller's vector");

        // internal vector must be sorted ascending — peek via filter(x -> true)
        List<Long> internalAfter = p.filter(x -> true).stream()
                .map(HardDisk::getCapacityInBytes).toList();
        assertEquals(List.of(100L, 200L, 300L, 400L, 500L), internalAfter);
    }

    @Test
    void sort_descendingByCapacity_sortsInternalOnly() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        p.sort(Comparator.comparingLong(HardDisk::getCapacityInBytes).reversed());
        List<Long> internalAfter = p.filter(x -> true).stream()
                .map(HardDisk::getCapacityInBytes).toList();
        assertEquals(List.of(500L, 400L, 300L, 200L, 100L), internalAfter);
    }

    @Test
    void sort_emptyVector_noException() {
        DataProcessorManual p = new DataProcessorManual(disksEmpty);
        assertDoesNotThrow(() -> p.sort(Comparator.comparingLong(HardDisk::getCapacityInBytes)));
    }

    @Test
    void constructor_deepCopiesInput_externalMutationDoesNotAffectProcessor() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        long sizeBefore = p.count();
        disksOdd.clear();
        assertEquals(sizeBefore, p.count(),
                "clearing the caller's vector must not affect the processor's internal state");
    }

    // -------- count --------

    @Test
    void count_returnsSize() {
        assertEquals(5L, new DataProcessorManual(disksOdd).count());
        assertEquals(4L, new DataProcessorManual(disksEven).count());
        assertEquals(1L, new DataProcessorManual(disksOne).count());
    }

    @Test
    void count_emptyVector_returnsZero() {
        assertEquals(0L, new DataProcessorManual(disksEmpty).count());
    }

    // -------- filter --------

    @Test
    void filter_nullPredicate_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertThrows(IllegalArgumentException.class, () -> p.filter(null));
    }

    @Test
    void filter_someMatch() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        Vector<HardDisk> failing = p.filter(HardDisk::isFailing);
        assertEquals(2, failing.size());
        assertTrue(failing.stream().allMatch(HardDisk::isFailing));
    }

    @Test
    void filter_noneMatch_returnsEmpty() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertTrue(p.filter(x -> false).isEmpty());
    }

    @Test
    void filter_allMatch_returnsAll() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertEquals(5, p.filter(x -> true).size());
    }

    @Test
    void filter_emptyInput_returnsEmpty() {
        DataProcessorManual p = new DataProcessorManual(disksEmpty);
        assertTrue(p.filter(x -> true).isEmpty());
    }

    // -------- max --------

    @Test
    void max_nullComparator_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertThrows(IllegalArgumentException.class, () -> p.max(null));
    }

    @Test
    void max_returnsHardDiskWithMaxCapacity() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        HardDisk maxHd = p.max(Comparator.comparingLong(HardDisk::getCapacityInBytes));
        assertEquals("SN3", maxHd.getSerialNumber());
        assertEquals(500L, maxHd.getCapacityInBytes());
    }

    @Test
    void max_singleElement_returnsThatElement() {
        DataProcessorManual p = new DataProcessorManual(disksOne);
        HardDisk only = p.max(Comparator.comparingLong(HardDisk::getCapacityInBytes));
        assertEquals("SN1", only.getSerialNumber());
    }

    @Test
    void max_emptyVector_throws() {
        DataProcessorManual p = new DataProcessorManual(disksEmpty);
        assertThrows(NoSuchElementException.class,
                () -> p.max(Comparator.comparingLong(HardDisk::getCapacityInBytes)));
    }

    // -------- min --------

    @Test
    void min_nullComparator_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertThrows(IllegalArgumentException.class, () -> p.min(null));
    }

    @Test
    void min_returnsHardDiskWithMinCapacity() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        HardDisk minHd = p.min(Comparator.comparingLong(HardDisk::getCapacityInBytes));
        assertEquals("SN2", minHd.getSerialNumber());
        assertEquals(100L, minHd.getCapacityInBytes());
    }

    @Test
    void min_singleElement_returnsThatElement() {
        DataProcessorManual p = new DataProcessorManual(disksOne);
        HardDisk only = p.min(Comparator.comparingLong(HardDisk::getCapacityInBytes));
        assertEquals("SN1", only.getSerialNumber());
    }

    @Test
    void min_emptyVector_throws() {
        DataProcessorManual p = new DataProcessorManual(disksEmpty);
        assertThrows(NoSuchElementException.class,
                () -> p.min(Comparator.comparingLong(HardDisk::getCapacityInBytes)));
    }

    // -------- mean --------

    @Test
    void mean_nullFunction_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        // NOTE: current implementation has wrong message "Comparator is null" — should say "Function is null"
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> p.mean(null));
        assertTrue(ex.getMessage() != null && ex.getMessage().toLowerCase().contains("function"),
                "Expected message to mention 'Function', was: " + ex.getMessage());
    }

    @Test
    void mean_capacityAverage() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        // (100+200+300+400+500)/5 = 300
        assertEquals(300.0, p.mean(HardDisk::getCapacityInBytes), 1e-9);
    }

    @Test
    void mean_singleElement_returnsThatValue() {
        DataProcessorManual p = new DataProcessorManual(disksOne);
        assertEquals(42.0, p.mean(HardDisk::getCapacityInBytes), 1e-9);
    }

    // Empty: current implementation produces NaN (0 / 0). Spec-wise it should throw — left as a known issue.
    @Test
    void mean_emptyVector_currentlyReturnsNaN() {
        DataProcessorManual p = new DataProcessorManual(disksEmpty);
        double result = p.mean(HardDisk::getCapacityInBytes);
        assertTrue(Double.isNaN(result), "Empty mean is NaN today; consider throwing instead");
    }

    // -------- median --------

    @Test
    void median_nullSortingComparator_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertThrows(IllegalArgumentException.class,
                () -> p.median(null, HardDisk::getCapacityInBytes));
    }

    @Test
    void median_nullFunction_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertThrows(IllegalArgumentException.class,
                () -> p.median(Comparator.comparingLong(HardDisk::getCapacityInBytes), null));
    }

    @Test
    void median_emptyVector_throws() {
        DataProcessorManual p = new DataProcessorManual(disksEmpty);
        assertThrows(Exception.class,
                () -> p.median(Comparator.comparingLong(HardDisk::getCapacityInBytes),
                        HardDisk::getCapacityInBytes));
    }

    @Test
    void median_oddSize_returnsMiddleValue() throws Exception {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        // sorted capacities: 100, 200, 300, 400, 500 → median = 300
        assertEquals(300L,
                p.median(Comparator.comparingLong(HardDisk::getCapacityInBytes),
                        HardDisk::getCapacityInBytes));
    }

    @Test
    void median_singleElement_returnsThatValue() throws Exception {
        DataProcessorManual p = new DataProcessorManual(disksOne);
        assertEquals(42L,
                p.median(Comparator.comparingLong(HardDisk::getCapacityInBytes),
                        HardDisk::getCapacityInBytes));
    }

    // Will currently FAIL — bug at DataProcessorManual.java:94 returns `index` instead of average.
    @Test
    void median_evenSize_returnsAverageOfTwoMiddle() throws Exception {
        DataProcessorManual p = new DataProcessorManual(disksEven);
        // sorted capacities: 100, 200, 300, 400 → median = (200 + 300) / 2 = 250
        assertEquals(250L,
                p.median(Comparator.comparingLong(HardDisk::getCapacityInBytes),
                        HardDisk::getCapacityInBytes));
    }

    @Test
    void median_doesNotMutateInputOrder() throws Exception {
        Vector<HardDisk> snapshot = new Vector<>(disksOdd);
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        p.median(Comparator.comparingLong(HardDisk::getCapacityInBytes),
                HardDisk::getCapacityInBytes);
        // Manual median copies internally → original order must be preserved
        for (int i = 0; i < snapshot.size(); i++) {
            assertSame(snapshot.get(i), disksOdd.get(i), "median must not reorder internal vector");
        }
    }

    // -------- countDistinctStrings --------

    @Test
    void countDistinctStrings_nullFunction_throws() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertThrows(IllegalArgumentException.class, () -> p.countDistinctStrings(null));
    }

    @Test
    void countDistinctStrings_distinctModels() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        // models: ModelA(x3), ModelB, ModelC → 3 distinct
        assertEquals(3L, p.countDistinctStrings(HardDisk::getModel));
    }

    @Test
    void countDistinctStrings_allSame_returnsOne() {
        Vector<HardDisk> sameModel = new Vector<>(List.of(
                disk("a", "M", 1L, false, 0),
                disk("b", "M", 2L, false, 0),
                disk("c", "M", 3L, false, 0)
        ));
        assertEquals(1L, new DataProcessorManual(sameModel).countDistinctStrings(HardDisk::getModel));
    }

    @Test
    void countDistinctStrings_allDifferent_returnsSize() {
        DataProcessorManual p = new DataProcessorManual(disksOdd);
        assertEquals(5L, p.countDistinctStrings(HardDisk::getSerialNumber));
    }

    @Test
    void countDistinctStrings_emptyVector_returnsZero() {
        DataProcessorManual p = new DataProcessorManual(disksEmpty);
        assertEquals(0L, p.countDistinctStrings(HardDisk::getModel));
    }

    // -------- end-to-end with DummyHardDiskDataSource --------

    private static Vector<HardDisk> loadDummy() {
        DummyHardDiskDataSource src = new DummyHardDiskDataSource();
        Vector<HardDisk> v = new Vector<>();
        HardDisk hd = src.next();
        while (hd != null) {
            v.add(hd);
            hd = src.next();
        }
        return v;
    }

    @Test
    void dummyDataSource_allQueries_returnExpectedValues() throws Exception {
        DataProcessorManual p = new DataProcessorManual(loadDummy());

        assertEquals(5L, p.count());
        assertEquals(2, p.filter(HardDisk::isFailing).size());
        assertEquals(4_000_000_000_000L,
                p.max(Comparator.comparingLong(HardDisk::getCapacityInBytes)).getCapacityInBytes());
        assertEquals(500_000_000_000L,
                p.min(Comparator.comparingLong(HardDisk::getCapacityInBytes)).getCapacityInBytes());
        assertEquals(1_650_000_000_000.0, p.mean(HardDisk::getCapacityInBytes), 1e-3);
        assertEquals(1_000_000_000_000L,
                p.median(Comparator.comparingLong(HardDisk::getCapacityInBytes),
                        HardDisk::getCapacityInBytes));
        assertEquals(4L, p.countDistinctStrings(HardDisk::getModel));
        assertEquals(2.4, p.mean(x -> (long) x.getSmartValues().size()), 1e-9);
    }
}
