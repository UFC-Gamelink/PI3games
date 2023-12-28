package com.gamelink.gamelinkapi.models.users;

import com.gamelink.gamelinkapi.enums.GameTime;
import com.gamelink.gamelinkapi.enums.Gender;
import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends BaseModel {
    @NotBlank
    private String name;

    @OneToOne
    @NotNull
    private User user;

    @OneToOne
    private ImageModel banner;

    @OneToOne
    private ImageModel icon;

    private String bio = "";

    private LocalDate birthdayDate;

    private List<GameTime> gameTimes = List.of();
    @NotNull
    private Gender gender;
    private double latitude;
    private double longitude;
    private boolean showBirthday;
    private boolean showLocation;
}
