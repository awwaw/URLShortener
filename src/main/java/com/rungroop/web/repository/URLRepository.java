package com.rungroop.web.repository;

import com.rungroop.web.models.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<URLEntity, Long> {
    Optional<URLEntity> findURLEntityByShortenedUrl(final String shortenedUrl);

    boolean existsByShortenedUrl(final String shortenedUrl);
}
