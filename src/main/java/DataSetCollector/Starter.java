package DataSetCollector;


import java.util.Collections;


public class Starter {

    public static void main(String[] args) throws InterruptedException {
        KafkaHandlerConfig config = buildConfig();
        KafkaPublisher publisher = new KafkaPublisher(config);
        DataSetCollector dataSetCollector = new DataSetCollector(publisher);
        scheduleCollect(dataSetCollector);
    }

    private static KafkaHandlerConfig buildConfig() {
        KafkaHandlerConfig config = new KafkaHandlerConfig();
        config.setBootstrapServers(Collections.singletonList("localhost:9092"));
        config.setTopicName("parking-rows-stats");
        return config;
    }

    private static void scheduleCollect(DataSetCollector dataSetCollector) throws InterruptedException {
        ScheduledExecutorRepeat executorRepeat = new ScheduledExecutorRepeat(dataSetCollector, 10);
        executorRepeat.repeat();
    }
}
