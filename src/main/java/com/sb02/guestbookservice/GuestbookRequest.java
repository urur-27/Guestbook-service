package com.sb02.guestbookservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestbookRequest {
  private String name;
  private String title;
  private String content;
  private MultipartFile image;
}
