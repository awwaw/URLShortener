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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final static String RESULT_MESSAGE = "Your shortened string is: http://localhost:8080/";

    public URLController(URLService urlService, URLRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
    }

    private String encodeUrl(final String url) {
        String base64 = Base64.getEncoder().encodeToString(url.getBytes());
        return base64.substring(8, Math.min(base64.length() - 1, 24));
    }

    @PostMapping("/shorten")
    public String shortenURL(@RequestParam final String urlToShorten,
                             Model model) {
        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(urlToShorten)) {
            logger.error("Invalid url provided");

            return "ErrorPage";
        }

        String shortenedUrl = encodeUrl(urlToShorten);
        if (urlRepository.existsByShortenedUrl(shortenedUrl)) {
            model.addAttribute("shortenedUrl", RESULT_MESSAGE + shortenedUrl);
            return "IndexPage";
        }

        URLEntity entity = urlService.save(urlToShorten, shortenedUrl);
        model.addAttribute("shortenedUrl", RESULT_MESSAGE + entity.getShortenedUrl());
        return "IndexPage";
    }

    @GetMapping("/{shortenedUrl}")
    public String redirectToOriginalUrl(@PathVariable final String shortenedUrl,
                                      RedirectAttributes attributes) {
        Optional<URLEntity> fullUrl = urlRepository.findURLEntityByShortenedUrl(shortenedUrl);
        if (fullUrl.isPresent()) {
            String url = fullUrl.get().getUrl();
            logger.info(String.format("Redirecting to %s", url));
            return "redirect:/" + url;
        } else {
            logger.error("There are no shortened url in database: " + shortenedUrl);
            attributes.addFlashAttribute("errorMessage",
                    "There are no shortened url in database: " + shortenedUrl);
            return "redirect:/error_page";
        }
    }

}
