import at.fhhgb.mc.opr.backblazedata.loaders.HardDiskDataSource;
import at.fhhgb.mc.opr.backblazedata.model.HardDisk;
import at.fhhgb.mc.opr.backblazedata.model.SMARTValue;

import java.util.*;

public class DummyHardDiskDataSource implements HardDiskDataSource  {
    private Vector<HardDisk> hardDisks = new Vector<>();
    private int count = 0;


    public DummyHardDiskDataSource() {
        hardDisks.add(new HardDisk(
                new Date(2024, 1, 15), "SN001", "Samsung 870 EVO", 500_000_000_000L, false,
                new LinkedList<>(List.of(new SMARTValue(1, 100L, 100L), new SMARTValue(5, 0L, 0L)))
        ));

        hardDisks.add(new HardDisk(
                new Date(2024, 3, 10), "SN002", "Seagate Barracuda", 2_000_000_000_000L, true,
                new LinkedList<>(List.of(new SMARTValue(1, 50L, 50L), new SMARTValue(5, 12L, 12L), new SMARTValue(9, 30000L, 30000L)))
        ));

        hardDisks.add(new HardDisk(
                new Date(2024, 6, 1), "SN003", "WD Blue", 1_000_000_000_000L, false,
                new LinkedList<>(List.of(new SMARTValue(1, 95L, 95L)))
        ));

        hardDisks.add(new HardDisk(
                new Date(2023, 11, 20), "SN004", "Toshiba X300", 4_000_000_000_000L, false,
                new LinkedList<>(List.of(new SMARTValue(1, 80L, 80L), new SMARTValue(5, 2L, 2L), new SMARTValue(9, 50000L, 50000L), new SMARTValue(12, 200L, 200L)))
        ));

        hardDisks.add(new HardDisk(
                new Date(2024, 8, 5), "SN005", "Seagate Barracuda", 750_000_000_000L, true,
                new LinkedList<>(List.of(new SMARTValue(1, 10L, 10L), new SMARTValue(5, 99L, 99L)))
        ));
    }


    @Override
    public HardDisk next() {
        if (hardDisks.isEmpty()) throw new IndexOutOfBoundsException("Vector is empty");
        if (count == hardDisks.size()) return null;
        HardDisk hd = hardDisks.get(count);
        count++;
        return hd;
    }
}
