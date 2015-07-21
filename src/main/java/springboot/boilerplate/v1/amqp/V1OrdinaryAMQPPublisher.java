package springboot.boilerplate.v1.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import springboot.boilerplate.metrics.QueueMetricsService;

/**
 * This is a simple programmatic AMQP publisher, connected to localhost. Use
 * RabbitMQ as example. You can initialize mannualy or use spring managed beans.
 *
 * @author bruno
 *
 */
@Configuration
public class V1OrdinaryAMQPPublisher {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String queueName = "ordinary-queue";
    private final String topicName = "ordinary-exchange";
    private final CachingConnectionFactory fact;
    private final RabbitTemplate client;

    @Autowired
    QueueMetricsService metricsService;

    public V1OrdinaryAMQPPublisher() {

        log.info("Initializing publisher");

        // inicialização da fila
        fact = new CachingConnectionFactory("localhost");
        final RabbitAdmin admin = new RabbitAdmin(fact);
        final Queue queue = new Queue(queueName);
        admin.declareQueue(queue);
        final TopicExchange exchange = new TopicExchange(topicName);
        admin.declareExchange(exchange);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(queueName));
        client = new RabbitTemplate(fact);
        client.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void send(String request, long l) throws Exception {

        try {
            log.info(String.format("Publishing %s to queue %s", request, queueName));
            client.convertAndSend(queueName, request);
            metricsService.recordMsgProcessed(true, l);
        } catch (final Exception e) {
            metricsService.recordMsgProcessed(false, l);
            throw new Exception(e.getMessage());
        }
    }
}
