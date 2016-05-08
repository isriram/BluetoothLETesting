package net.sriramiyer.beacon.esurl;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BluetoothServiceTester {

    @Test
    public void shouldCreateLEParameterFrame() {
        int signalStrength = -42;
        int advertisingInterval = 100;
        BluetoothLEService bluetoothLEService = new BluetoothLEService(signalStrength, advertisingInterval);
        String signal = bluetoothLEService.getSignalStrength();
        Assert.assertEquals(signal, "ff");
    }

    @Test (expectedExceptions = {IllegalArgumentException.class})
    public void shouldNotAllowOutOfRangeValuesForSignalStrength() {
        // values tested and passed : -200, -129, 128, 200
        // {i.e. range -128 to 127 is valid, and this test fails for that range as expected}
        int signalStrength = 200;
        int advertisingInterval = 100;
        BluetoothLEService bluetoothLEService = new BluetoothLEService(signalStrength, advertisingInterval);
        String signal = bluetoothLEService.getSignalStrength();
    }

    @Test
    public void shouldVerifyAdvertisingTimingInterval() {
        BluetoothLEService bluetoothLEService = new BluetoothLEService();
        String advInterval = bluetoothLEService.getAdvertisingInterval();
        Assert.assertEquals(advInterval, "0x08 0x0006 a000a0000300000000000000000700");
    }

    @Test
    public void shouldVerifyAdvertisingTimingIntervalDifferentValue() {
        BluetoothLEService bluetoothLEService = new BluetoothLEService(150);
        String advInterval = bluetoothLEService.getAdvertisingInterval();
        Assert.assertEquals(advInterval, "0x08 0x0006 f000f0000300000000000000000700");
    }

    @Test
    public void shouldGetEnableDisableLEAdvertisingParameters() {
        BluetoothLEService bluetoothLEService = new BluetoothLEService();
        bluetoothLEService.enableLEAdvertising();
        String leChecker = bluetoothLEService.getLEAdvertising();
        Assert.assertEquals(leChecker, "0x08 0x000a 01");
        bluetoothLEService.disableLEAdvertising();
        leChecker = bluetoothLEService.getLEAdvertising();
        Assert.assertEquals(leChecker, "0x08 0x000a 00");
    }


}
