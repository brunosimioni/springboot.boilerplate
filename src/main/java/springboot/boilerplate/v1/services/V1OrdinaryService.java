package springboot.boilerplate.v1.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springboot.boilerplate.v1.amqp.V1OrdinaryAMQPPublisher;

@Service
public class V1OrdinaryService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private V1OrdinaryAMQPPublisher amqpPublisher;

    public boolean processRequest(String request) throws Exception {
        final Date startTime = new Date();

        // do some hard computation
        Thread.sleep(1000);
        log.info("did a hard work");

        final Date endTime = new Date();
        amqpPublisher.send(request, (endTime.getTime() - startTime.getTime()));
        return true;
    }
}
