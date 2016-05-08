package net.sriramiyer.beacon.esurl;

class BluetoothLEService {
  private final static int defaultSignalStrength = -41;
  private final static String leSetAdvertisingParametersCommand = "0x08 0x0006";
  private final static String leNonAdvertisableNonConnectable = "03";
  private final static String leSetAdvertisingDataCommand = "0x08 0x0008";
  private final static String leSetAdvertiseEnableCommand = "0x08 0x000a";
  private final String signalStrengthInHexFormat;
  private final int signalStrength;
  private final int bluetoothLEAdvertisingInterval;
  private final String advertisingIntervalInHexFormat;
  private String leAdvertisingAsArgument;

  BluetoothLEService() {
    this(defaultSignalStrength, 100);
  }

  BluetoothLEService(int advertisingInterval) {
    this(defaultSignalStrength, advertisingInterval);
  }

  BluetoothLEService(int signalStrength, int advertisingInterval) {
    int temporaryBLEAdvertisingInterval;
    this.signalStrength = signalStrength;
    int minimumAdvertisingInterval = 100;
    if (advertisingInterval > minimumAdvertisingInterval) {
      temporaryBLEAdvertisingInterval = advertisingInterval;
    } else {
      temporaryBLEAdvertisingInterval = minimumAdvertisingInterval;
    }
    this.bluetoothLEAdvertisingInterval = temporaryBLEAdvertisingInterval;
    this.enableLEAdvertising();
    this.calculateAdvertisingInterval();
    this.signalStrengthInHexFormat = this.calculateSignalStrengthString();
    this.advertisingIntervalInHexFormat = this.calculateAdvertisingInterval();
  }

  private String calculateSignalStrengthString() {
    int lowestTheoreticalSignalStrength = -128;
    int highestTheoreticalSignalStrength = 127;
    int signalStrengthAtByteValue = -42;
    int upperSignalStrengthMagicNumber = 297;
    int lowerSignalStrengthMagicNumber = 41;

    int calculateSignalStrengthValue;

    if ((this.signalStrength < lowestTheoreticalSignalStrength) || (this.signalStrength > highestTheoreticalSignalStrength)) {
      throw new IllegalArgumentException("Signal strength parameter is out of bounds");
    }

    if (this.signalStrength <= signalStrengthAtByteValue) {
      calculateSignalStrengthValue = upperSignalStrengthMagicNumber + this.signalStrength;
    } else {
      calculateSignalStrengthValue = lowerSignalStrengthMagicNumber + this.signalStrength;
    }
    String formattedSignalStrength = Integer.toHexString(calculateSignalStrengthValue);
    if (calculateSignalStrengthValue < 0x10) {
      formattedSignalStrength = "0" + formattedSignalStrength;
    }
    return formattedSignalStrength;
  }

  String getSignalStrength() {
    return this.signalStrengthInHexFormat;
  }

  private String calculateAdvertisingInterval() {
    String calculatedAdvertisingInterval;
    double bluetoothLEGranularity = 0.625;
    int bluetoothLEInterval = (int) (this.bluetoothLEAdvertisingInterval / bluetoothLEGranularity);
    calculatedAdvertisingInterval = Integer.toHexString(bluetoothLEInterval);

    // padding the string to have 4 characters
    int advIntervalLength = calculatedAdvertisingInterval.length();
    calculatedAdvertisingInterval = "0000" + calculatedAdvertisingInterval;
    calculatedAdvertisingInterval = calculatedAdvertisingInterval.substring(advIntervalLength, advIntervalLength + 4);

    // Converting to little endian format since BLE
    String MSB = calculatedAdvertisingInterval.substring(0, 2);
    String LSB = calculatedAdvertisingInterval.substring(2, 4);
    calculatedAdvertisingInterval = LSB + MSB;
    return calculatedAdvertisingInterval;
  }

  String getAdvertisingInterval() {
    calculateAdvertisingInterval();
    String localLEAdvertisingString; // "0x08 0x0006 A0 00 A0 00 03 00 00 00 00 00 00 00 00 07 00";
    localLEAdvertisingString = leSetAdvertisingParametersCommand + " " + this.advertisingIntervalInHexFormat + this.advertisingIntervalInHexFormat
      + leNonAdvertisableNonConnectable + "00000000000000000700";

    return localLEAdvertisingString;
  }

  void enableLEAdvertising() {
    this.leAdvertisingAsArgument = leSetAdvertiseEnableCommand + " 01";
  }

  void disableLEAdvertising() {
    this.leAdvertisingAsArgument = leSetAdvertiseEnableCommand + " 00";
  }

  String getLEAdvertising() {
    return this.leAdvertisingAsArgument;
  }
}