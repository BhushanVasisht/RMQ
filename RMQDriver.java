/**
 * A driver for the Range Minimum Query Class.
 * Implements tests and timing measurements over large input and query ranges
 * @author Bhushan Vasisht BSV180000
 */

package bsv180000;

import java.util.Random;
import bsv180000.Timer;
import bsv180000.RMQ;

/**
 * Driver class for RMQ
 */
public class RMQDriver {
    private static Random random = new Random();
    private static final int[] ranges = {1280, 2560, 5120};

    public static void main(String[] args)
    {
        System.out.println("------------------");
        for(int longRange : ranges)
        {
            int[] input = new int[longRange];

            for(int i = 1; i < longRange; ++i)
                input[i] = random.nextInt();

            Timer preT = new Timer();
            preT.start();
            RMQ rmq = new RMQ(input);
            preT.end();

            System.out.println("Preprocessing of " + longRange + " numbers took " + preT);

            Timer qT = new Timer();
            qT.start();
            for(int i = 1; i < longRange; ++i)
            {
                int l = (int)(Math.random() * longRange);
                int r = (int)(Math.random() * longRange);
                rmq.queryMin(l, r);
            }
            qT.end();

            System.out.println("Querying for " + longRange + " ranges took " + qT);
            System.out.println("------------------");
        }
    }
}
