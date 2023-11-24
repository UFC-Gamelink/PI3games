package com.gamelink.gamelinkapi.models.posts;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_table")
@EqualsAndHashCode(callSuper = true)
public class PostModel extends BaseModel {
    private String description;
    @ManyToOne
    private UserProfile owner;
    @OneToOne
    private ImageModel image;
}
