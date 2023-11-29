package com.gamelink.gamelinkapi.models.posts;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.commentaries.CommentaryModel;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<CommentaryModel> commentaries;
}
