package com.gamelink.gamelinkapi.models.posts;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.users.User;
import jakarta.persistence.*;
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
    private User user;
    @OneToOne(cascade = CascadeType.PERSIST)
    private ImageModel image;
}
