package it.unisalento.iot.iotdigitaltwin.configuration;

public class MQTTprotocol implements IoTProtocol{
    @Override
    public void initialize() {
        System.out.println("Inizializzo protocollo MQTT");
    }
}
