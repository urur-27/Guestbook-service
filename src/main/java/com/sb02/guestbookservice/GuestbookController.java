package com.sb02.guestbookservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guestbooks")
@Slf4j
@RequiredArgsConstructor
public class GuestbookController {

  private final GuestbookService guestbookService;

  // 방명록 등록
  @PostMapping
  public ResponseEntity<GuestbookResponse> createEntry(@ModelAttribute GuestbookRequest request) {
    log.info("GuestbookController createEntry");
    return ResponseEntity.status(201).body(guestbookService.createEntry(request));
  }

  // 방명록 목록 조회 (페이지네이션)
  @GetMapping
  public ResponseEntity<Page<GuestbookResponse>> getAllEntries(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {
    log.info("GuestbookController getAllEntries");
    return ResponseEntity.ok(guestbookService.getAllEntries(page, size));
  }

  // 방명록 상세 조회
  @GetMapping("/{id}")
  public ResponseEntity<GuestbookResponse> getEntryById(@PathVariable Long id) {
    log.info("GuestbookController getEntryById");
    return ResponseEntity.ok(guestbookService.getEntryById(id));
  }
}
