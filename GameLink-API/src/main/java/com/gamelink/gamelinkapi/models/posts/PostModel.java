package com.gamelink.gamelinkapi.models.posts;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.CommentaryModel;
import com.gamelink.gamelinkapi.models.CommunityModel;
import com.gamelink.gamelinkapi.models.ImageModel;
import com.gamelink.gamelinkapi.models.user.UserProfile;
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
