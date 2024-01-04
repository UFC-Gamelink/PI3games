package com.gamelink.gamelinkapi.models.posts;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventPostModel extends PostModel {
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    private LocalDateTime eventDate;
}
