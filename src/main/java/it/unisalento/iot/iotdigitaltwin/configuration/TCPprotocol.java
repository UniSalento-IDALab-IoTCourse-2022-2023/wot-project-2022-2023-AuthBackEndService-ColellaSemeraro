package it.unisalento.iot.iotdigitaltwin.configuration;

public class TCPprotocol implements IoTProtocol{
    @Override
    public void initialize() {
        System.out.println("Inizializzo protocollo TCP");
    }
}
