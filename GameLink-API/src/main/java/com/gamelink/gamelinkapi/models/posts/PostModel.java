package com.gamelink.gamelinkapi.models.posts;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
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
    @ManyToOne
    private CommunityModel community;
}
