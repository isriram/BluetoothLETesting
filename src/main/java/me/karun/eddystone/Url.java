package me.karun.eddystone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Getter
class Url {
  private final String protocol;
  private final String domain;
  private final String tld;
  private final Optional<String> path;

  @Override
  public String toString() {
    return protocol + domain + tld + path.orElse("");
  }

  static Url build(final String urlString) {
    final int protocolEndIndex = protocolEndIndex(urlString);
    final String protocol = urlString.substring(0, protocolEndIndex);

    final int domainEndIndex = urlString.indexOf(".", protocolEndIndex);
    final String domain = urlString.substring(protocolEndIndex, domainEndIndex);

    final int tldEndIndex = tldEndIndex(urlString, domainEndIndex);
    final String tld = urlString.substring(domainEndIndex, tldEndIndex);

    final Optional<String> path = evaluatePath(urlString, tldEndIndex);

    return new Url(protocol, domain, tld, path);
  }

  private static int protocolEndIndex(final String urlString) {
    final int indexWithWww = urlString.indexOf("//www.");

    if (indexWithWww != -1) {
      return indexWithWww + "//www.".length();
    }
    return urlString.indexOf("//") + "//".length();
  }

  private static int tldEndIndex(final String urlString, final int domainEndIndex) {
    final int tldEndIndexEval = urlString.indexOf("/", domainEndIndex);

    return tldEndIndexEval < 0 ? urlString.length() : tldEndIndexEval + "/".length();
  }

  private static Optional<String> evaluatePath(final String urlString, final int tldEndIndex) {
    final String path = urlString.substring(tldEndIndex);
    return ofNullable(path.isEmpty() ? null : path);
  }
}
