package test;

import test.zebra.Zebra;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.discovery.DiscoveredUsbPrinter;
import com.zebra.sdk.printer.discovery.UsbDiscoverer;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws ConnectionException {
        ArrayList<Zebra> printers = new ArrayList<>();

        for (DiscoveredUsbPrinter usbPrinter : UsbDiscoverer.getZebraUsbPrinters()) {
            Zebra zebra = new Zebra(usbPrinter);
            printers.add(zebra);
        }

        for(Zebra zebra: printers) {
            zebra.benchmark();
        }

    }
}
