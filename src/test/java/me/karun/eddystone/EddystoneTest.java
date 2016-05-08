package me.karun.eddystone;

import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Enclosed.class)
public class EddystoneTest {

  @RunWith(Parameterized.class)
  @RequiredArgsConstructor
  public static class WithValidData {
    @Mock
    private Url url;
    private Eddystone eddystone;
    private final String protocol;
    private final String domain;
    private final String tld;
    private final String path;
    private final String expectedOutput;

    @Parameters(name = "{0}{1}{2}{3} to {4}")
    public static List<String[]> data() {
      return asList(new String[][]{
        {"http://", "google", ".com", null, "100002676f6f676c650700000000000000000000"},
        {"http://", "google", ".com/", null, "100002676f6f676c650000000000000000000000"},
        {"http://", "karun", ".me/", null, "1000026b6172756e2e6d652f0000000000000000"},
        {"http://", "karun", ".me/", "page", "1000026b6172756e2e6d652f7061676500000000"},
      });
    }

    @Before
    public void setup() {
      initMocks(this);
      eddystone = new Eddystone("00");

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

  @RunWith(MockitoJUnitRunner.class)
  public static class WithInvalidData {
    @Mock
    private Url url;

    @Before
    public void setup() {
      when(url.getProtocol()).thenReturn("http://");
      when(url.getDomain()).thenReturn("google");
      when(url.getTld()).thenReturn(".com/");
      when(url.getPath()).thenReturn(empty());
    }

    @Test(expected = UrlTooLongException.class)
    public void shouldNotHexConvertLongUrls() throws UrlTooLongException {
      when(url.getPath()).thenReturn(Optional.of("extremelyLongTextToMakeTheUrlGoOverTheLimit"));

      new Eddystone("00").getHexUrl(url);
    }
  }
}
