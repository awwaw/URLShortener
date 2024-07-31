package com.rungroop.web.models;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "url")
public class URLEntity {
    @Id
    @Tsid
    private Long id;

    @Column(name = "original_url")
    private String url;

    @Column(name = "shortened_url")
    private String shortenedUrl;

    public URLEntity(final String url) {
        this.url = url;
    }
}


