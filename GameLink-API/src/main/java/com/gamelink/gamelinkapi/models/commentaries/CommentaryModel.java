package com.gamelink.gamelinkapi.models.commentaries;

import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "commentary_table")
@EqualsAndHashCode(callSuper = true)
public class CommentaryModel extends BaseModel {
    @ManyToOne
    @NotNull
    private UserProfile creator;
    @ManyToOne
    @NotNull
    private PostModel post;
    @NotBlank
    private String text;
}
