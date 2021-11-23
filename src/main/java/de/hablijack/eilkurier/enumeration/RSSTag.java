package de.hablijack.eilkurier.enumeration;

public enum RSSTag {
  TITLE("title"),
  DESCRIPTION("description"),
  CONTENT("content"),
  CHANNEL("channel"),
  LANGUAGE("language"),
  COPYRIGHT("copyright"),
  LINK("link"),
  AUTHOR("author"),
  ITEM("item"),
  PUB_DATE("pubDate"),
  GUID("guid"),
  ENCLOSURE("enclosure");

  private final String name;

  RSSTag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
