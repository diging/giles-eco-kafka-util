package edu.asu.diging.gilesecosystem.kafka.util.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.kafka.util.service.IKafkaListenerManager;
import edu.asu.diging.gilesecosystem.septemberutil.properties.MessageType;
import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;

/**
 * Manager class to start and stop Kafka listeners.
 * 
 * This class requires two configuration parameters to be present in a 
 * config file called 'config.properties':
 * 
 * <ul>
 *  <li>app_name: The name of the application to be displayed in info messages.</li>
 *  <li>app_url: The base url of the application to be displayed in info messages.</li>
 * </ul>
 * 
 * @author jdamerow
 *
 */
@Service
@PropertySource("classpath:/config.properties")
public class KafkaListenerManager implements IKafkaListenerManager {

    @Autowired
    private KafkaListenerEndpointRegistry registry;
    
    @Autowired
    private ISystemMessageHandler messageHandler;
    
    @Value("${app_name}")
    private String appName;
    
    @Value("${app_url}")
    private String appUrl;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.cepheus.service.impl.IKafkaListenerManager#shutdownListeners()
     */
    @Override
    public void shutdownListeners() {
        if (registry.isRunning()) {
            registry.stop();
            String msg = String.format("Stopping Kafka listeners for %s at %s.", appName, appUrl);
            messageHandler.handleMessage("Stopping Kafka Listeners", msg, MessageType.INFO);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.cepheus.service.impl.IKafkaListenerManager#startListeners()
     */
    @Override
    public void startListeners() {
        if (!registry.isRunning()) {
            registry.start();
            String msg = String.format("Starting Kafka listeners for %s at %s.", appName, appUrl);
            messageHandler.handleMessage("Starting Kafka Listeners", msg, MessageType.INFO);
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.gilesecosystem.cepheus.service.impl.IKafkaListenerManager#isListening()
     */
    @Override
    public boolean isListening() {
        return registry.isRunning();
    }
}
