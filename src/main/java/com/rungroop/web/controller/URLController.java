package com.rungroop.web.controller;

import com.rungroop.web.error.AppError;
import com.rungroop.web.models.URLEntity;
import com.rungroop.web.repository.URLRepository;
import com.rungroop.web.service.URLService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Controller
public class URLController {
    private final URLService urlService;
    private final URLRepository urlRepository;
    Logger logger = LoggerFactory.getLogger(URLController.class);

    public URLController(URLService urlService, URLRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
    }

    private String encodeUrl(final String url) {
        String base64 = Base64.getEncoder().encodeToString(url.getBytes());
        return base64.substring(0, 16);
    }

    private String decodeUrl(final String encodedUrl) {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }

    @PostMapping("/shorten")
    public @ResponseBody ResponseEntity<?> shortenURL(@RequestParam final String urlToShorten) {
        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(urlToShorten)) {
            logger.error("Invalid url provided");

            AppError error = new AppError(HttpStatus.BAD_REQUEST.value(), String.format("Invalid url: %s", urlToShorten));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        String shortened_url = encodeUrl(urlToShorten);
        if (urlRepository.existsByShortenedUrl(shortened_url)) {
            return new ResponseEntity<>(shortened_url, HttpStatus.OK);
        }

        URLEntity entity = urlService.save(urlToShorten, shortened_url);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/{shortenedUrl}")
    public void redirectToOriginalUrl(@PathVariable final String shortenedUrl, HttpServletResponse response) {
        Optional<URLEntity> fullUrl = urlRepository.findURLEntityByShortenedUrl(shortenedUrl);
        if (fullUrl.isPresent()) {
            String url = fullUrl.get().getUrl();
            logger.info(String.format("Redirecting to %s", url));
            try {
                response.sendRedirect(url);
            } catch (IOException e) {
                logger.error("Couldn't redirect to full url. Shortened version was - " + shortenedUrl);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Could not redirect to the full url", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Url not found. Shortened version was - " + shortenedUrl);
        }
    }

}
