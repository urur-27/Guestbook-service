package com.sb02.guestbookservice;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
public class GuestbookService {

  private final S3Client s3Client;
  private final GuestbookRepository guestbookRepository;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.base-url}")
  private String baseUrl;

  public GuestbookService(S3Client s3Client, GuestbookRepository guestbookRepository) {
    this.s3Client = s3Client;
    this.guestbookRepository = guestbookRepository;
  }

  // 방명록 등록
  public GuestbookResponse createEntry(GuestbookRequest request) {
    try {
      // 파일 처리
      MultipartFile file = request.getImage();
      String s3Url = null;

      // 파일이 있는 경우에만 업로드 처리
      if (file != null && !file.isEmpty()) {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();

        // S3에 저장할 고유한 키 생성
        String s3Key = UUID.randomUUID() + "-" + fileName;

        // S3에 파일 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .contentType(contentType)
            .build();

        s3Client.putObject(putObjectRequest,
            RequestBody.fromInputStream(file.getInputStream(), size));

        // S3 URL 생성
        s3Url = baseUrl + "/" + s3Key;
      }

      // 방명록 엔티티 생성
      GuestbookEntity entity = GuestbookEntity.builder()
          .name(request.getName())
          .title(request.getTitle())
          .content(request.getContent())
          .imageUrl(s3Url)  // 파일이 없을 때는 null
          .build();

      GuestbookEntity savedEntity = guestbookRepository.save(entity);
      return toResponse(savedEntity);
    } catch (Exception e) {
      log.error("파일 업로드 실패", e);
      throw new RuntimeException("파일 업로드 실패", e);
    }
  }

  // 방명록 목록 조회 (페이지네이션)
  public Page<GuestbookResponse> getAllEntries(int page, int size) {
    return guestbookRepository.findAll(
        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
    ).map(this::toResponse);
  }

  // 방명록 상세 조회
  public GuestbookResponse getEntryById(Long id) {
    GuestbookEntity entity = guestbookRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Entry not found: " + id));

    return toResponse(entity);
  }

  // GuestbookEntity -> GuestbookResponse 변환 메서드 (중복 제거)
  private GuestbookResponse toResponse(GuestbookEntity entity) {
    return new GuestbookResponse(entity);
  }
}
