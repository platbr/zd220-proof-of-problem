package test.zebra;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.ResponseValidator;

import com.zebra.sdk.printer.discovery.DiscoveredUsbPrinter;

import java.time.Duration;
import java.time.Instant;

public class Zebra {
    private final String COMMAND_TERMINATOR = String.valueOf(0x03);
    private DiscoveredUsbPrinter discoveredUsbPrinter;
    private int usedBufferSize = 0;
    private Connection connection;

    public Zebra(DiscoveredUsbPrinter discoveredUsbPrinter) {
        this.discoveredUsbPrinter = discoveredUsbPrinter;
    }

    public Zebra() {

    }

    public DiscoveredUsbPrinter getDiscoveredUsbPrinter()  {
        return discoveredUsbPrinter;
    }

    private Connection getConnection(ZerbraTimeout zerbraTimeout) throws ConnectionException {
        if (connection != null && !connection.isConnected()) {
            connection.open();
        }
        if (connection == null) {
            this.connection = getDiscoveredUsbPrinter().getConnection();
            connection.open();
        }
        connection.setMaxTimeoutForRead(zerbraTimeout.getMaxTimeoutForRead());
        return connection;
    }


    private String sendCommandAndGetOutput(String command, ResponseValidator responseValidator, ZerbraTimeout zerbraTimeout) throws ConnectionException {
        String result;
        Instant start = Instant.now();
        if (responseValidator == null) {
            System.out.println("Using CommandTerminator - " + zerbraTimeout.toString());
            result = new String(getConnection(zerbraTimeout).sendAndWaitForResponse(command.getBytes(), zerbraTimeout.getInitialResponseTimeout() , zerbraTimeout.getResponseCompletionTimeout(), COMMAND_TERMINATOR));
        } else {
            System.out.println("Using ResponseValidator - " + zerbraTimeout.toString());
            result = new String(getConnection(zerbraTimeout).sendAndWaitForValidResponse(command.getBytes(), zerbraTimeout.getInitialResponseTimeout(), zerbraTimeout.getResponseCompletionTimeout(), responseValidator));
        }
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time Taken: " + timeElapsed.toMillis() + " milliseconds");
        return result;
    }

    private void closeConnection() throws ConnectionException {
        if (connection != null && connection.isConnected()) {
            connection.close();
        }
    }


    public void benchmark() {
        StatusResponseValidator statusResponseValidator = new StatusResponseValidator();
        System.out.println(discoveredUsbPrinter.getDiscoveryDataMap().get("MODEL"));
        step(new ZerbraTimeout(500,5000,1), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,125), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,250), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,500), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,1000), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,2000), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,4000), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,8000), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,16000), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,1), statusResponseValidator);
        step(new ZerbraTimeout(500,5000,125), null);
        step(new ZerbraTimeout(500,5000,250), null);
        step(new ZerbraTimeout(500,5000,500), null);
        step(new ZerbraTimeout(500,5000,1000), null);
        step(new ZerbraTimeout(500,5000,2000), null);
        step(new ZerbraTimeout(500,5000,4000), null);
        step(new ZerbraTimeout(500,5000,8000), null);
        step(new ZerbraTimeout(500,5000,16000), null);
    }

    private void step(ZerbraTimeout zebraTimeout, StatusResponseValidator statusResponseValidator) {
        try {
            sendCommandAndGetOutput("~HS", statusResponseValidator, zebraTimeout);
        } catch (ConnectionException e) {
            System.out.println("ERROR:" + e.getMessage());;
        }
    }
}
