package springboot.boilerplate.metrics;

import java.util.OptionalDouble;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Service;

/**
 * This is a simple metrics service. Customize it as your needs.
 * counter: just make increments and decrements for indicator
 * gauge:  can inform a little bit more, but you have to manage how your data is
 * calculated. This is a simple example of mean calculation. 
 * @author bruno
 *
 */
@Service
public class QueueMetricsService  {

    private final CounterService counterService;
    private final GaugeService gaugeService;
    
    @Autowired
    public QueueMetricsService(CounterService counterService, GaugeService gaugeService) {
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

	LinkedBlockingQueue<Long> lastExecutionTimes = new LinkedBlockingQueue<Long>();

    public void recordMsgProcessed(boolean wasDone, long processTime) {
    	
		try {
			lastExecutionTimes.put(processTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (lastExecutionTimes.size() % 10 == 0) {
			OptionalDouble average = lastExecutionTimes.stream().mapToDouble(a -> a).average();
			lastExecutionTimes.clear();
			
			this.gaugeService.submit("services.amqp.msgprocessed.meantime", average.getAsDouble());
		}

    	
        if (wasDone)
        	this.counterService.increment("services.amqp.msgprocessed.succeeded");
        else
        	this.counterService.increment("services.amqp.msgprocessed.failed");
    }

}