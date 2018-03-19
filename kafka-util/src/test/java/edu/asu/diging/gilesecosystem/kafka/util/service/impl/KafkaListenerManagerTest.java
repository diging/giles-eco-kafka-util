package edu.asu.diging.gilesecosystem.kafka.util.service.impl;

import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.gilesecosystem.septemberutil.service.ISystemMessageHandler;
import junit.framework.Assert;

public class KafkaListenerManagerTest {

    @Mock
    private KafkaListenerEndpointRegistry registry;
    
    @Mock
    private ISystemMessageHandler messageHandler;
    
    @InjectMocks
    private KafkaListenerManager managerToTest;
    
    private String APP_NAME = "TestApp";
    private String APP_URL = "http://appurl.test";
    
    @Before
    public void setUp() {
        managerToTest = new KafkaListenerManager();
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(managerToTest, "appName", APP_NAME);
        ReflectionTestUtils.setField(managerToTest, "appUrl", APP_URL);
    }
    
    @Test
    public void test_shutdownListeners_isRunning() {
        Mockito.when(registry.isRunning()).thenReturn(true);
        managerToTest.shutdownListeners();
        Mockito.verify(registry).stop();
    }
    
    @Test
    public void test_shutdownListeners_isNotRunning() {
        Mockito.when(registry.isRunning()).thenReturn(false);
        managerToTest.shutdownListeners();
        Mockito.verify(registry, never()).stop();
    }
    
    @Test 
    public void test_startListeners_isRunning() {
        Mockito.when(registry.isRunning()).thenReturn(true);
        managerToTest.startListeners();
        Mockito.verify(registry, never()).start();
    }
    
    @Test 
    public void test_startListeners_isNotRunning() {
        Mockito.when(registry.isRunning()).thenReturn(false);
        managerToTest.startListeners();
        Mockito.verify(registry).start();
    }
    
    @Test
    public void test_isListening_isRunning() {
        Mockito.when(registry.isRunning()).thenReturn(true);
        Assert.assertTrue(managerToTest.isListening());
    }
    
    @Test
    public void test_isListening_isNotRunning() {
        Mockito.when(registry.isRunning()).thenReturn(false);
        Assert.assertFalse(managerToTest.isListening());
    }
}
