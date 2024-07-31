package com.rungroop.web.repository;

import com.rungroop.web.models.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<URLEntity, Long> {
    Optional<URLEntity> findURLEntityByShortenedUrl(final String shortenedUrl);

    boolean existsByShortenedUrl(final String shortenedUrl);

    Optional<URLEntity> findURLEntityByUrl(final String url);

    @Modifying
    @Transactional
    @Query("UPDATE URLEntity url SET url.shortenedUrl = ?2 WHERE url.id = ?1")
    void setShortenedUrlById(Long id, final String shortenedUrl);
}
