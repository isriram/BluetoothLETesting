package net.sriramiyer.beacon.esurl;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EddystoneTester {

  @Test
  public void shouldCheckBasicURL() {
    EddystoneURL eddystoneURL = new EddystoneURL("https://www.google.com/");
    String esurl = eddystoneURL.getMachineReadableURL();
    assertThat(esurl).isEqualTo("10 00 01 67 6f 6f 67 6c 65 00 00 00 00 00 00 00 00 00 00 00");
  }

  @Test
  public void shouldGetCorrectURLSchemes() {
    EddystoneURL eddystoneURL = new EddystoneURL("http://sriramiyer.net");
    String esurl = eddystoneURL.getMachineReadableURL();

    assertThat(esurl).isEqualTo("10 00 02 73 72 69 72 61 6d 69 79 65 72 0a 00 00 00 00 00 00");
  }

  @Test
  public void shouldCheckSpecialCharacters() {
    EddystoneURL eddystoneURL = new EddystoneURL("http://sriramiyer.net/?p=1");
    String esurl = eddystoneURL.getMachineReadableURL();

    assertThat(esurl).isEqualTo("10 00 02 73 72 69 72 61 6d 69 79 65 72 03 3f 70 3d 31 00 00");
  }

  @Test
  public void shouldPadURLToCorrectLength() {
    EddystoneURL eddystoneURL = new EddystoneURL("https://google.com/");
    String esurl = eddystoneURL.getMachineReadableURL();

    assertThat(esurl).isEqualTo("10 00 03 67 6f 6f 67 6c 65 00 00 00 00 00 00 00 00 00 00 00");
  }

  @Test
  public void shouldGetEddystoneURLWithCustomSignalStrength() {
    EddystoneURL eddystoneURL = new EddystoneURL("https://google.com/");
    String esurl = eddystoneURL.getMachineReadableURL(-42);

    assertThat(esurl).isEqualTo("10 ff 03 67 6f 6f 67 6c 65 00 00 00 00 00 00 00 00 00 00 00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailMiserablyBecauseLengthIsOverLimit() {
    new EddystoneURL("https://google.com/1234567890123456789012345678901234567890").getMachineReadableURL();
  }

}
