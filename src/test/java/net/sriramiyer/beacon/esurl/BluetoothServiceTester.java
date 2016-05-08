package net.sriramiyer.beacon.esurl;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BluetoothServiceTester {

  @Test
  public void shouldCreateLEParameterFrame() {
    int signalStrength = -42;
    int advertisingInterval = 100;
    BluetoothLEService bluetoothLEService = new BluetoothLEService(signalStrength, advertisingInterval);
    String signal = bluetoothLEService.getSignalStrength();
    assertThat(signal).isEqualTo("ff");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowOutOfRangeValuesForSignalStrength() {
    int signalStrength = 200;
    int advertisingInterval = 100;

    new BluetoothLEService(signalStrength, advertisingInterval).getSignalStrength();
  }

  @Test
  public void shouldVerifyAdvertisingTimingInterval() {
    BluetoothLEService bluetoothLEService = new BluetoothLEService();
    String advInterval = bluetoothLEService.getAdvertisingInterval();
    assertThat(advInterval).isEqualTo("0x08 0x0006 a000a0000300000000000000000700");
  }

  @Test
  public void shouldVerifyAdvertisingTimingIntervalDifferentValue() {
    BluetoothLEService bluetoothLEService = new BluetoothLEService(150);
    String advInterval = bluetoothLEService.getAdvertisingInterval();

    assertThat(advInterval).isEqualTo("0x08 0x0006 f000f0000300000000000000000700");
  }

  @Test
  public void shouldGetEnableDisableLEAdvertisingParameters() {
    BluetoothLEService bluetoothLEService = new BluetoothLEService();
    bluetoothLEService.enableLEAdvertising();

    String leChecker = bluetoothLEService.getLEAdvertising();
    assertThat(leChecker).isEqualTo("0x08 0x000a 01");

    bluetoothLEService.disableLEAdvertising();
    leChecker = bluetoothLEService.getLEAdvertising();
    assertThat(leChecker).isEqualTo("0x08 0x000a 00");
  }
}
