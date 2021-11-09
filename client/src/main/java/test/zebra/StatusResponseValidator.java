package test.zebra;

import com.zebra.sdk.comm.ResponseValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StatusResponseValidator implements ResponseValidator {
    String line1;
    String line2;

    public boolean isPaperOut() {
        return Pattern.matches("^...,1,.*", line1);
    }

    public boolean isPaused() {
        return Pattern.matches("^...,.,1,.*", line1);
    }

    public int usedBufferSize() {
        Matcher x = Pattern.compile("^...,.,.,....,(...),.*").matcher(line1);
        if (x.find()) {
            return Integer.parseInt(x.group(1));
        }
        return 0;
    }

    public boolean isReceiveBufferFull() {
        return Pattern.matches("^...,.,.,....,...,1,.*", line1);
    }

    public boolean isPartialFormatInProgress() {
        return false;
//            For some reason is always true on gc420t;
//            return Pattern.matches("^...,.,.,....,...,.,.,1,.*", linha1);
    }

    public boolean isHeadOpen() {
        return Pattern.matches("^...,.,1,.*", line2);
    }

    public boolean isRibbonOut() {
        return Pattern.matches("^...,.,.,1,.*", line2);
    }

    public void build(byte[] bytes) {
        String[] stringStatus = new String(bytes).split("\\r\\n");
        if(stringStatus.length >= 2) {
            line1 = stringStatus[0].substring(1, stringStatus[0].length() - 1);
            line2 = stringStatus[1].substring(1, stringStatus[1].length() - 1);
        }
    }

    public void build(String string) {
        build(string.getBytes());
    }

    @Override
    public boolean isResponseComplete(byte[] bytes) {
        build(bytes);
        return isValid();
    }

    public boolean isValid() {
        if (line1 == null || line1.isEmpty()) {
            return false;
        }

        if (line2 == null || line2.isEmpty()) {
            return false;
        }

        return true;
    }
}
