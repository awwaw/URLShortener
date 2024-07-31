package com.rungroop.web.controller;

import com.rungroop.web.models.URLEntity;
import com.rungroop.web.repository.URLRepository;
import com.rungroop.web.service.URLService;
import com.rungroop.web.utils.ShorteningUtil;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class URLController {
    private final URLService urlService;
    private final URLRepository urlRepository;
    Logger logger = LoggerFactory.getLogger(URLController.class);

    private final static String RESULT_MESSAGE = "Your shortened URL is: http://localhost:8080/sl/";

    public URLController(URLService urlService, URLRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
    }

    private String encodeUrl(final URLEntity urlEntity) {
        long id = urlEntity.getId();
        String encoded = ShorteningUtil.idToStr(id);
        return encoded.substring(0, Math.min(encoded.length(), 12));
    }

    @PostMapping("shorten")
    public String shortenURL(@RequestParam final String urlToShorten,
                             Model model,
                             RedirectAttributes attributes) {
        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(urlToShorten)) {
            logger.error("Invalid url provided");
            attributes.addFlashAttribute("errorMessage",
                    "Invalid URL provided: " + urlToShorten);
            return "redirect:/error_page";
        }

        Optional<URLEntity> entity = urlRepository.findURLEntityByUrl(urlToShorten);
        if (entity.isPresent()) {
            model.addAttribute("shortenedUrl", RESULT_MESSAGE + entity.get().getShortenedUrl());
            return "ResultPage";
        }

        URLEntity newEntity = urlService.save(urlToShorten);
        final String encodedUrl = encodeUrl(newEntity);
        urlService.updateShortenedUrl(newEntity.getId(), encodedUrl);

        model.addAttribute("shortenedUrl", RESULT_MESSAGE + encodedUrl);
        return "ResultPage";
    }

    @GetMapping("sl/{shortenedUrl}")
    public String redirectToOriginalUrl(@PathVariable final String shortenedUrl,
                                      RedirectAttributes attributes) {
        Optional<URLEntity> fullUrl = urlRepository.findURLEntityByShortenedUrl(shortenedUrl);
        if (fullUrl.isPresent()) {
            String url = fullUrl.get().getUrl();
            logger.info(String.format("Redirecting to %s", url));
            return "redirect:" + url;
        } else {
            logger.error("There are no shortened url in database: " + shortenedUrl);
            attributes.addFlashAttribute("errorMessage",
                    "There are no shortened url in database: " + shortenedUrl);
            return "redirect:/error_page";
        }
    }

}
