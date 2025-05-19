package com.sb02.guestbookservice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="guestbooks")
@Getter
@Setter
@NoArgsConstructor
public class GuestbookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String title;
  private String content;
  private String imageUrl;
  private Instant createdAt;

  @Builder
  public GuestbookEntity(String name, String title, String content, String imageUrl) {
    this.name = name;
    this.title = title;
    this.content = content;
    this.imageUrl = imageUrl;
    this.createdAt = Instant.now();
  }



}
