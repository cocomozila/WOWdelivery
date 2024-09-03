package com.wow.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.DataNotFoundException;
import com.wow.delivery.util.CustomInstantDeserializer;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "date_created", columnDefinition = "DATETIME(6)", nullable = false)
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant dateCreated;

    @LastModifiedDate
    @Column(name = "date_updated", columnDefinition = "DATETIME(6)", nullable = false)
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant dateUpdated;

    @JsonIgnore
    public Long getIdOrThrow() {
        if (id == null) {
            throw new DataNotFoundException(ErrorCode.DATA_NOT_FOUND, "ID가 존재하지 않습니다.");
        }
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }
}
