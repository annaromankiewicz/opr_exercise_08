import at.fhhgb.mc.opr.backblazedata.loaders.HardDiskDataSource;
import at.fhhgb.mc.opr.backblazedata.model.HardDisk;
import at.fhhgb.mc.opr.backblazedata.model.SMARTValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DummyHardDiskDataSource implements HardDiskDataSource  {
    private List<HardDisk> hardDisks = new ArrayList<>();
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
        HardDisk hd = hardDisks.get(count);
        count++;
        return hd;
    }
}
