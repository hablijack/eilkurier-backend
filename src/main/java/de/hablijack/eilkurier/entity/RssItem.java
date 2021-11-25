package de.hablijack.eilkurier.entity;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RssItem {
  public boolean isFeedHeader = true;
  public String copyright;
  public String description;
  public String content;
  public String title;
  public String link;
  public String language;
  public String author;
  public String pubdate;
  public String guid;
  public String enclosure;

  public boolean hasGUID() {
    return guid != null && guid.length() > 1;
  }

  public boolean hasContent() {
    return content != null && content.length() > 1;
  }

  public boolean hasEnclusure() {
    return enclosure != null && enclosure.length() > 1;
  }

  public Date getPubdate() {
    return Date.from(Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(pubdate)));
  }
}
