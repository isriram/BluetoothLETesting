package me.karun.eddystone;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlTest {
  @Test
  public void shouldParseSimpleUrl() {
    final Url url = Url.build("http://karun.me");

    assertThat(url.getProtocol()).isEqualTo("http://");
    assertThat(url.getDomain()).isEqualTo("karun");
    assertThat(url.getTld()).isEqualTo(".me");
    assertThat(url.getPath()).isNotPresent();
  }

  @Test
  public void shouldParseUrlEndingWithSlash() {
    final Url url = Url.build("http://karun.me/");

    assertThat(url.getProtocol()).isEqualTo("http://");
    assertThat(url.getDomain()).isEqualTo("karun");
    assertThat(url.getTld()).isEqualTo(".me/");
    assertThat(url.getPath()).isNotPresent();
  }

  @Test
  public void shouldParseUrlEndingWithWww() {
    final Url url = Url.build("http://www.karun.me");

    assertThat(url.getProtocol()).isEqualTo("http://www.");
    assertThat(url.getDomain()).isEqualTo("karun");
    assertThat(url.getTld()).isEqualTo(".me");
    assertThat(url.getPath()).isNotPresent();
  }

  @Test
  public void shouldParseUrlWithPath() {
    final Url url = Url.build("http://karun.me/path");

    assertThat(url.getProtocol()).isEqualTo("http://");
    assertThat(url.getDomain()).isEqualTo("karun");
    assertThat(url.getTld()).isEqualTo(".me/");
    assertThat(url.getPath()).contains("path");
  }
}