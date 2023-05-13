package com.schema.schemaDemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Table(name = "FallBack")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FallBack {
    @Id
    private String key;
    @Lob
    private String jsonModel;
    private String objectHeader;
    private String status;
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;
    private int reTryCount;
    private String topicName;
    @PrePersist
    public void prePersist()
    {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

}
