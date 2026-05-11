import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Comparator;

public class App {

    public static void main(String[] args) throws Exception {
        DummyHardDiskDataSource dummyHardDisks = new DummyHardDiskDataSource();
        DataProcessorStreams datacenterStreams = new DataProcessorStreams(dummyHardDisks.get());
        DataProcessorManual datacenterManual = new DataProcessorManual(dummyHardDisks.get());

        System.out.println("----------Abfrage 1----------");
        System.out.println("Streams - This datacenter contains: " + datacenterStreams.count() + " HardDisks");  // 5
        System.out.println("Manual - This datacenter contains: " + datacenterManual.count() + " HardDisks");    // 5
        System.out.println("\n");

        System.out.println("----------Abfrage 2----------");
        System.out.println("Streams - HardDisks fail: " + datacenterStreams.filter(x -> x.isFailing()).stream().count()); // 2
        System.out.println("Manual - HardDisks fail: " + datacenterManual.filter(x -> x.isFailing()).stream().count());   // 2
        System.out.println("\n");


        System.out.println("----------Abfrage 3----------");
        System.out.println("Streams - Max HardDisks: " + datacenterStreams.max((a, b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()))
                .getSerialNumber());                                                                    //SN004
        System.out.println("Manual - Max HardDisks: " + datacenterManual.max((a, b)
                -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes())).getSerialNumber());
        System.out.println("\n");


        System.out.println("----------Abfrage 4----------");
        System.out.println("Streams - Max HardDisks: " + datacenterStreams.min((a, b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()))                // SN001
                .getSerialNumber()); //SN004
        System.out.println("Manual - Max HardDisks: " + datacenterManual.min((a, b)
                -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes())).getSerialNumber());


        System.out.println("\n");


        System.out.println("----------Abfrage 5----------");
        System.out.println("Streams - Mean of HardDisks: " + datacenterStreams.mean(x
                -> x.getCapacityInBytes()));                                        // 1 650 000 000 000L

        System.out.println("Manual - Mean of HardDisks: " + datacenterManual.mean(x
                -> x.getCapacityInBytes()));


        System.out.println("\n");


        System.out.println("----------Abfrage 6----------");

        System.out.println("Streams - Median of HardDisks: " + datacenterStreams.median((a,b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                x -> x.getCapacityInBytes()));                                           // 1_000_000_000_000L

        System.out.println("Streams - Median of HardDisks: " + datacenterManual.median((a,b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                x -> x.getCapacityInBytes()));                                           // 1_000_000_000_000L



        System.out.println("\n");


        System.out.println("----------Abfrage 7----------");


        System.out.println("Streams - Count of different models of HardDisks: " + datacenterStreams.countDistinctStrings(
                x -> x.getModel()));                                           // 4

        System.out.println("Streams - Count of different models of HardDisks: " + datacenterManual.countDistinctStrings(
                x -> x.getModel()));                                           // 4


        System.out.println("\n");


        System.out.println("----------Abfrage 8----------");        // 2
        System.out.println("Streams - Median of smartValues per HardDisk: " + datacenterStreams.median ((a,b) ->
                        Long.compare(a.getSmartValues().size(), b.getSmartValues().size()), x -> (long) x.getSmartValues().size()));

        System.out.println("Streams - Median of smartValues per HardDisk: " + datacenterManual.median ((a,b) ->
                Long.compare(a.getSmartValues().size(), b.getSmartValues().size()), x -> (long) x.getSmartValues().size()));




    }
}
