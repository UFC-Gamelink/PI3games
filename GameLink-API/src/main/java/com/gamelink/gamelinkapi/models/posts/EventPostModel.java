package com.gamelink.gamelinkapi.models.posts;

import jakarta.persistence.Entity;
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
    private double latitude;
    private double longitude;
    private LocalDateTime eventDate;
}
