package com.mphasis.javaproject.model;


import java.time.Instant;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events",
       uniqueConstraints = @UniqueConstraint(columnNames = "eventId"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String eventId;
    @NotBlank
    private String accountId;
    @NotBlank
    private String type;

    @Min(value = 1, message = "Must Be greater than 0")
    private double amount;
    @NotBlank
    private String currency;
    @NotNull
    private Instant eventTimestamp;

    @Nullable
    private Metadata metadata;
    
    
}
