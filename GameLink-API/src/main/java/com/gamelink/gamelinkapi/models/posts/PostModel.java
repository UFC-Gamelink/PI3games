package com.gamelink.gamelinkapi.models.posts;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_table")
@EqualsAndHashCode(callSuper = true)
@OnDelete(action = OnDeleteAction.CASCADE)
public class PostModel extends BaseModel {
    private String description;
    @ManyToOne
    private UserProfile owner;
    @OneToOne
    private ImageModel image;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private CommunityModel community;
}
