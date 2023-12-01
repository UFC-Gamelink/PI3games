package com.gamelink.gamelinkapi.dtos.responses.posts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventPostResponse extends PostResponse{
    private double latitude;
    private double longitude;
    private LocalDateTime eventDate;
}
