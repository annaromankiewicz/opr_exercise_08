import at.fhhgb.mc.opr.backblazedata.loaders.LiveHardDiskDataSource;
import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Comparator;
import java.util.Vector;

public class App {

    public static void main(String[] args) throws Exception {
        DummyHardDiskDataSource dummyHardDisks = new DummyHardDiskDataSource();
        Vector<HardDisk> hd = new Vector<HardDisk>();
        HardDisk current = dummyHardDisks.next();
        while (current != null) {
            hd.add(current);
            current = dummyHardDisks.next();
        }
        DataProcessorStreams datacenterStreams = new DataProcessorStreams(hd);
        DataProcessorManual datacenterManual = new DataProcessorManual(hd);
        System.out.println("\n");

        System.out.println("------------------------DUMMY DATA-----------------------------");
        System.out.println("\n");

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


        System.out.println("\n");
        System.out.println("\n");

        System.out.println("------------------------LIVE DATA-----------------------------");
        System.out.println("\n");



        LiveHardDiskDataSource liveHardDisks = new LiveHardDiskDataSource();
        Vector<HardDisk> liveData = new Vector<HardDisk>();
        while (liveHardDisks.next() != null) liveData.add(liveHardDisks.next());
        DataProcessorStreams liveDatacenterStreams = new DataProcessorStreams(liveData);
        DataProcessorManual liveDatacenterManual = new DataProcessorManual(liveData);

        System.out.println("----------Abfrage 1----------");
        System.out.println("Streams - This datacenter contains: " + liveDatacenterStreams.count() + " HardDisks");
        System.out.println("Manual - This datacenter contains: " + liveDatacenterManual.count() + " HardDisks");
        System.out.println("\n");

        System.out.println("----------Abfrage 2----------");
        System.out.println("Streams - HardDisks fail: " + liveDatacenterStreams.filter(x -> x.isFailing()).stream().count());
        System.out.println("Manual - HardDisks fail: " + liveDatacenterManual.filter(x -> x.isFailing()).stream().count());
        System.out.println("\n");


        System.out.println("----------Abfrage 3----------");
        System.out.println("Streams - Max HardDisks: " + liveDatacenterStreams.max((a, b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()))
                .getSerialNumber());
        System.out.println("Manual - Max HardDisks: " + liveDatacenterManual.max((a, b)
                -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes())).getSerialNumber());
        System.out.println("\n");


        System.out.println("----------Abfrage 4----------");
        System.out.println("Streams - Max HardDisks: " + liveDatacenterStreams.min((a, b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()))
                .getSerialNumber()); //SN004
        System.out.println("Manual - Max HardDisks: " + liveDatacenterManual.min((a, b)
                -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes())).getSerialNumber());


        System.out.println("\n");


        System.out.println("----------Abfrage 5----------");
        System.out.println("Streams - Mean of HardDisks: " + liveDatacenterStreams.mean(x
                -> x.getCapacityInBytes()));

        System.out.println("Manual - Mean of HardDisks: " + liveDatacenterManual.mean(x
                -> x.getCapacityInBytes()));


        System.out.println("\n");


        System.out.println("----------Abfrage 6----------");

        System.out.println("Streams - Median of HardDisks: " + liveDatacenterStreams.median((a,b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                x -> x.getCapacityInBytes()));

        System.out.println("Streams - Median of HardDisks: " + liveDatacenterManual.median((a,b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                x -> x.getCapacityInBytes()));



        System.out.println("\n");


        System.out.println("----------Abfrage 7----------");


        System.out.println("Streams - Count of different models of HardDisks: " + liveDatacenterStreams.countDistinctStrings(
                x -> x.getModel()));

        System.out.println("Streams - Count of different models of HardDisks: " + liveDatacenterManual.countDistinctStrings(
                x -> x.getModel()));


        System.out.println("\n");


        System.out.println("----------Abfrage 8----------");
        System.out.println("Streams - Median of smartValues per HardDisk: " + liveDatacenterStreams.median ((a,b) ->
                Long.compare(a.getSmartValues().size(), b.getSmartValues().size()), x -> (long) x.getSmartValues().size()));

        System.out.println("Streams - Median of smartValues per HardDisk: " + liveDatacenterManual.median ((a,b) ->
                Long.compare(a.getSmartValues().size(), b.getSmartValues().size()), x -> (long) x.getSmartValues().size()));



    }
}
