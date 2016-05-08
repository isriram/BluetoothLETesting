package me.karun.eddystone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EddystoneWithInvalidDataTest {
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
