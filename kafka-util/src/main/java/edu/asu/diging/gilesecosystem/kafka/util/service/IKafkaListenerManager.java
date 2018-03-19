package edu.asu.diging.gilesecosystem.kafka.util.service;

public interface IKafkaListenerManager {

    void shutdownListeners();

    void startListeners();

    boolean isListening();

}