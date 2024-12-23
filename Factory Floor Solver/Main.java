import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    final static Constants con = new Constants();
    static workerThread bestLayout;
    static ConcurrentHashMap<Long, workerThread> layoutPools = new ConcurrentHashMap<>();
    static volatile boolean showing = true;
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors: " + processors);

        //generate one affinity layout for all threads
        double[] affinity = new double[con.numTypes];
        for(int i=0; i<con.numTypes; i++){
            affinity[i] = ThreadLocalRandom.current().nextDouble();
        }

        //give a default value
        if (bestLayout == null) {
            bestLayout = new workerThread(affinity);
        }
        //TODO might be able to rework display code
        Gridv2 gui = new Gridv2(bestLayout);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Optimized Floor Plan");
                frame.setSize(800,800);
                frame.setResizable(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setBackground(new Color(0,0,0));

                frame.add(gui);

                frame.setVisible(true);
                showing = true;
            }
        });

        new Timer(1, event -> gui.repaint()).start();

        while (!showing) {Thread.onSpinWait();}


        final Phaser phaser = new Phaser() {
          @Override
          protected boolean onAdvance(int phase, int registeredParties) {
              System.out.println("Generation " + phase);
              return phase >= con.nGen || registeredParties  == 0;
          }
        };

        phaser.register();

        ForkJoinPool forkJoinPool = new ForkJoinPool(processors,ForkJoinPool.defaultForkJoinWorkerThreadFactory,null,true);
        for(int i = 0; i < processors; i++) {
            phaser.register();
            forkJoinPool.submit(new Thread() {
                workerThread wt = new workerThread(affinity);
                public void run() {
                    do{
                        wt.run();
                        phaser.arriveAndAwaitAdvance();
                    } while (!phaser.isTerminated());
                }
            });
        }
        phaser.arriveAndDeregister();
        System.out.println("Final Affinity Score is: "+ bestLayout.score);
        System.out.println("Number of threads invoking: "+ forkJoinPool.getActiveThreadCount());
    }
}
