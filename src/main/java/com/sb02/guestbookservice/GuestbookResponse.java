package com.sb02.guestbookservice;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuestbookResponse {
  private Long id;
  private String name;
  private String title;
  private String content;
  private String imageUrl;
  private String createdAt;

  public GuestbookResponse(GuestbookEntity entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.title = entity.getTitle();
    this.content = entity.getContent();
    this.imageUrl = entity.getImageUrl();
    this.createdAt = ZonedDateTime.ofInstant(entity.getCreatedAt(), ZoneId.of("Asia/Seoul"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
