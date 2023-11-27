package com.gamelink.gamelinkapi.models.posts;

import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_post_table")
@EqualsAndHashCode(callSuper = true)
public class EventPostModel extends PostModel {
    private double latitude;
    private double longitude;
}
