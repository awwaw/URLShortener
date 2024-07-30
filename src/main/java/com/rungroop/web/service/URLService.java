package com.rungroop.web.service;

import com.rungroop.web.models.URLEntity;
import com.rungroop.web.repository.URLRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class URLService {

    private final URLRepository urlRepository;

    public URLService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    List<URLEntity> listAllUrls() {
        return urlRepository.findAll();
    }

    public URLEntity save(final String url, final String shortenedUrl) {
        URLEntity entity = URLEntity.builder()
                .url(url)
                .shortenedUrl(shortenedUrl)
                .build();
        if (urlRepository.existsByShortenedUrl(shortenedUrl)) {
            return entity;
        }
        urlRepository.save(entity);
        return entity;
    }
}
