import at.fhhgb.mc.opr.backblazedata.loaders.LiveHardDiskDataSource;
import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Vector;

public class App {

    public static void main(String[] args) throws Exception {
        LiveHardDiskDataSource liveHardDisks = new LiveHardDiskDataSource();
        Vector<HardDisk> liveData = new Vector<HardDisk>();
        HardDisk currentLive = liveHardDisks.next();
        while (currentLive != null) {
            liveData.add(currentLive);
            currentLive = liveHardDisks.next();
        }
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
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes())).getCapacityInBytes());

        System.out.println("Manual - Max HardDisks: " + liveDatacenterManual.max((a, b)
                -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes())).getCapacityInBytes());
        System.out.println("\n");


        System.out.println("----------Abfrage 4----------");
        System.out.println("Streams - Min HardDisks: " + liveDatacenterStreams.min((a, b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()))
                .getCapacityInBytes());
        System.out.println("Manual - Min HardDisks: " + liveDatacenterManual.min((a, b)
                -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes())).getCapacityInBytes());


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

        System.out.println("Manual - Median of HardDisks: " + liveDatacenterManual.median((a,b)
                        -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                x -> x.getCapacityInBytes()));



        System.out.println("\n");


        System.out.println("----------Abfrage 7----------");


        System.out.println("Streams - Count of different models of HardDisks: " + liveDatacenterStreams.countDistinctStrings(
                x -> x.getModel()));

        System.out.println("Manual - Count of different models of HardDisks: " + liveDatacenterManual.countDistinctStrings(
                x -> x.getModel()));


        System.out.println("\n");


        System.out.println("----------Abfrage 8----------");
        System.out.println("Streams - Mean of smartValues per HardDisk: " + liveDatacenterStreams.mean (x -> (long) x.getSmartValues().size()));

        System.out.println("Manual - Mean of smartValues per HardDisk: " + liveDatacenterManual.mean (x -> (long) x.getSmartValues().size()));


    }
}
