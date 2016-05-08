package me.karun.eddystone;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.Integer.toHexString;
import static java.util.Optional.ofNullable;

class Eddystone {
  private static final String paddingChar = "00";
  private final String signalStrength;
  private final Map<String, String> protocolToHex;
  private final Map<String, String> tldToHex;

  Eddystone(final String signalStrength) {
    this.signalStrength = signalStrength;

    protocolToHex = new HashMap<>();
    protocolToHex.put("http://www.", "00");
    protocolToHex.put("https://www.", "01");
    protocolToHex.put("http://", "02");
    protocolToHex.put("https://", "03");

    tldToHex = new HashMap<>();
    tldToHex.put(".com/", "00");
    tldToHex.put(".org/", "01");
    tldToHex.put(".edu/", "02");
    tldToHex.put(".net/", "03");
    tldToHex.put(".info/", "04");
    tldToHex.put(".biz/", "05");
    tldToHex.put(".gov/", "06");
    tldToHex.put(".com", "07");
    tldToHex.put(".org", "08");
    tldToHex.put(".edu", "09");
    tldToHex.put(".net", "0a");
    tldToHex.put(".info", "0b");
    tldToHex.put(".biz", "0c");
    tldToHex.put(".gov", "0d");
  }

  String getHexUrl(final Url url) throws UrlTooLongException {
    final StringBuilder hex = new StringBuilder("10").append(signalStrength);

    hex.append(protocolToHex.get(url.getProtocol()));
    hex.append(stringToHex(url.getDomain()));
    hex.append(resolveTld(url));
    hex.append(pathToHexWithSpace(url.getPath()));

    return padStringToLength(20, hex);
  }

  private String resolveTld(final Url url) {
    final String tld = url.getTld();

    return ofNullable(tldToHex.get(tld))
      .orElse(stringToHex(tld));
  }

  private String padStringToLength(final int requiredByteCount, final StringBuilder strToPad) throws UrlTooLongException {
    if (requiredByteCount * 2 < strToPad.length()) {
      throw new UrlTooLongException();
    }

    while (requiredByteCount > strToPad.length() / 2) {
      strToPad.append(paddingChar);
    }

    return strToPad.toString();
  }

  private String pathToHexWithSpace(final Optional<String> path) {
    return path.isPresent() ? stringToHex(path.get()) : "";
  }

  private String stringToHex(final String host) {
    final StringBuilder hex = new StringBuilder();

    for(int i = 0; i < host.length(); i++){
      hex.append(toHexString(host.charAt(i)));
    }

    return hex.toString().trim();
  }
}
