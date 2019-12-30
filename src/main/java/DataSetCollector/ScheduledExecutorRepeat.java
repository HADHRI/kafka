package DataSetCollector;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

class ScheduledExecutorRepeat {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutorRepeat.class);

    private final DataSetCollector dataSetCollector;
    private final CountDownLatch latch;

    ScheduledExecutorRepeat(DataSetCollector dataSetCollector, int maxRepeat) {
        this.dataSetCollector = dataSetCollector;
        this.latch = new CountDownLatch(maxRepeat);
    }

    void repeat() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        SchedulingTask scheduledTask = new SchedulingTask(latch);
        ScheduledFuture<?> scheduledFuture = executorService.scheduleWithFixedDelay(scheduledTask, 1, 30, TimeUnit.SECONDS);
        latch.await();
        scheduledFuture.cancel(true);
        executorService.shutdown();
    }

    private class SchedulingTask implements Runnable {

        private final CountDownLatch latch;

        private SchedulingTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                logger.info("Launching collector");
                dataSetCollector.collect();
                latch.countDown();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
