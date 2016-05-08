package me.karun.eddystone;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class EddystoneWithValidDataTest {

  @Mock
  private Url url;
  private Eddystone eddystone;

  private final String signalStrength;
  private final String protocol;
  private final String domain;
  private final String tld;
  private final String path;
  private final String expectedOutput;

  @Parameters(name = "{1}{2}{3}{4} to {5}")
  public static List<String[]> data() {
    return asList(new String[][]{
      {"23", "http://www.", "google", ".com", null, "102300676f6f676c650700000000000000000000"},
      {"00", "http://www.", "google", ".com", null, "100000676f6f676c650700000000000000000000"},
      {"00", "https://www.", "google", ".com", null, "100001676f6f676c650700000000000000000000"},
      {"00", "http://", "google", ".com", null, "100002676f6f676c650700000000000000000000"},
      {"00", "https://", "google", ".com", null, "100003676f6f676c650700000000000000000000"},
      {"00", "http://", "google", ".com/", null, "100002676f6f676c650000000000000000000000"},
      {"00", "http://", "karun", ".me/", null, "1000026b6172756e2e6d652f0000000000000000"},
      {"00", "http://", "karun", ".me/", "page", "1000026b6172756e2e6d652f7061676500000000"},
    });
  }

  @Before
  public void setup() {
    initMocks(this);
    eddystone = new Eddystone(signalStrength);

    when(url.getProtocol()).thenReturn(protocol);
    when(url.getDomain()).thenReturn(domain);
    when(url.getTld()).thenReturn(tld);
    when(url.getPath()).thenReturn(Optional.ofNullable(path));
  }

  @Test
  public void shouldConvert() throws UrlTooLongException {
    assertThat(eddystone.getHexUrl(url)).isEqualTo(expectedOutput);
  }
}
