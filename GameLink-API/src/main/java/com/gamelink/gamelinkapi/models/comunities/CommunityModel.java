package com.gamelink.gamelinkapi.models.comunities;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community_table")
@EqualsAndHashCode(callSuper = true)
public class CommunityModel extends BaseModel {
    @ManyToOne
    private User owner;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @OneToOne
    private ImageModel banner;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "community")
    private List<PostModel> posts;
    @OneToMany
    private List<User> members;
}
