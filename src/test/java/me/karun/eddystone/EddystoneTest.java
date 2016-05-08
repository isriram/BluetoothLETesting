package me.karun.eddystone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EddystoneTest {
  @Mock
  private Url url;
  private Eddystone eddystone;

  @Before
  public void setup() {
    eddystone = new Eddystone("00");

    when(url.getProtocol()).thenReturn("http://");
    when(url.getDomain()).thenReturn("google");
    when(url.getTld()).thenReturn(".com");
    when(url.getPath()).thenReturn(empty());
  }

  @Test
  public void shouldProvideHexVersionOfUrl() throws UrlTooLongException {
    assertThat(eddystone.getHexUrl(url)).isEqualTo("100002676f6f676c650700000000000000000000");
  }

  @Test
  public void shouldHandleSlashForStandardTld() throws UrlTooLongException {
    when(url.getTld()).thenReturn(".com/");

    assertThat(eddystone.getHexUrl(url)).isEqualTo("100002676f6f676c650000000000000000000000");
  }

  @Test
  public void shouldHexConvertSlashAtEndForNonStandardTld() throws UrlTooLongException {
    when(url.getDomain()).thenReturn("karun");
    when(url.getTld()).thenReturn(".me/");

    assertThat(eddystone.getHexUrl(url)).isEqualTo("1000026b6172756e2e6d652f0000000000000000");
  }

  @Test
  public void shouldHexConvertNonStandardTld() throws UrlTooLongException {
    when(url.getDomain()).thenReturn("karun");
    when(url.getTld()).thenReturn(".me");

    assertThat(eddystone.getHexUrl(url)).isEqualTo("1000026b6172756e2e6d65000000000000000000");
  }

  @Test
  public void shouldHexConvertPath() throws UrlTooLongException {
    when(url.getPath()).thenReturn(of("page"));

    final String output = eddystone.getHexUrl(url);

    assertThat(output).isEqualTo("100002676f6f676c650770616765000000000000");
  }

  @Test(expected = UrlTooLongException.class)
  public void shouldNotHexConvertLongUrls() throws UrlTooLongException {
    when(url.getPath()).thenReturn(of("extremelyLongTextToMakeTheUrlGoOverTheLimit"));

    eddystone.getHexUrl(url);
  }
}
