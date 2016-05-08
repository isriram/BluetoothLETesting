package net.sriramiyer.beacon.esurl;

class EddystoneURL {

    private final String asciiURL;
    private final static String eddystoneFramePrefix = "10";
    private final static int eddystoneURLLength = 40;
    private String finalizedURL;
    private String eddystoneURL;

    EddystoneURL(String s) {
        this.asciiURL = s;
        this.eddystoneURL = "";
        this.finalizedURL = "";
    }

    private void processURL() {
        int URLLength = this.asciiURL.length();
        int asciiValueOfChar;
        char character;

        for (int i = 0; i < URLLength; i++) {
            character = this.asciiURL.charAt(i);
            asciiValueOfChar = (int) character;
            this.eddystoneURL = this.eddystoneURL + Integer.toHexString(asciiValueOfChar);
        }
    }

    private void setURLScheme() {
        String[] URLConstants = {"687474703a2f2f7777772e",  // 0x00 	http://www.
                "68747470733a2f2f7777772e",                 // 0x01 	https://www.
                "687474703a2f2f",                           // 0x02 	http://
                "68747470733a2f2f"};                        // 0x03     https://

        String URLScheme;
        boolean matchFound = false;
        int stringLength = URLConstants.length;
        int i = 0;
        while (i < stringLength && !matchFound) {
            if (this.eddystoneURL.contains(URLConstants[i])) {
                if (i < 16) {
                    URLScheme = "0" + Integer.toHexString(i);
                } else {
                    URLScheme = Integer.toHexString(i);
                }
                this.eddystoneURL = this.eddystoneURL.replace(URLConstants[i], URLScheme);
                matchFound = true;
            }
            i++;
        }
    }

    private void setURLExtension() {
        String[] URLExtension = {"2e636f6d2f",      // 0x00 .com/
                "2e6f72672f",                       // 0x01 .org/
                "2e6564752f",                       // 0x02 .edu/
                "2e6e65742f",                       // 0x03 .net/
                "2e696e666f2f",                     // 0x04 .info/
                "2e62697a2f",                       // 0x05 .biz/
                "2e676f762f",                       // 0x06 .gov/
                "2e636f6d",                         // 0x07 .com
                "2e6f7267",                         // 0x08 .org
                "2e656475",                         // 0x09 .edu
                "2e6e6574",                         // 0x0a .net
                "2e696e666f",                       // 0x0b .info
                "2e62697a",                         // 0x0c .biz
                "2e676f76"                          // 0x0d .gov
        };

        boolean matchFound = false;
        String URLScheme;
        int stringLength = URLExtension.length;
        int i = 0;
        while (i < stringLength && !matchFound) {
            if (this.eddystoneURL.contains(URLExtension[i])) {
                if (i < 16) {
                    URLScheme = "0" + Integer.toHexString(i);
                } else {
                    URLScheme = Integer.toHexString(i);
                }
                this.eddystoneURL = this.eddystoneURL.replace(URLExtension[i], URLScheme);
                matchFound = true;
            }
            i++;
        }
    }

    private void formatURL() {
        int difference = eddystoneURLLength - this.eddystoneURL.length();

        if(difference < 0) {
            throw new IllegalArgumentException("URL is too long");
        }

        for(int i = 0; i < difference; i++) {
            this.eddystoneURL = this.eddystoneURL + "0";
        }

        for(int i = 0; i<(eddystoneURLLength)/2; i++) {
            this.finalizedURL = this.finalizedURL + this.eddystoneURL.substring(2*i, (2*i)+ 2) + " ";
        }

        int length = this.finalizedURL.length();
        if(this.finalizedURL.substring(length - 1, length).equals(" ")) {
           this.finalizedURL = this.finalizedURL.substring(0, length - 1);
        }
    }

    private void calculateEddystoneURLParameters(String signalStrengthAsHex){
        processURL();
        setURLScheme();
        setURLExtension();
        this.eddystoneURL = eddystoneFramePrefix + signalStrengthAsHex + this.eddystoneURL;
        formatURL();
    }

    String getMachineReadableURL() {
        getMachineReadableURL(-41);
        return this.finalizedURL;
    }

    String getMachineReadableURL(int signalStrength){
        int defaultAdvertisingInterval = 100;
        BluetoothLEService bluetoothLEService = new BluetoothLEService(signalStrength, defaultAdvertisingInterval);
        String signalStrengthAsHex = bluetoothLEService.getSignalStrength();
        calculateEddystoneURLParameters(signalStrengthAsHex);
        return this.finalizedURL;
    }
}