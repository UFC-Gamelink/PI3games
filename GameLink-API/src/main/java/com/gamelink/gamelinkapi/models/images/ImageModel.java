package com.gamelink.gamelinkapi.models.images;

import com.gamelink.gamelinkapi.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ImageModel extends BaseModel {
    @NotBlank
    private String filename;
    @NotBlank
    private String url;
    @NotBlank
    private String publicId;
}
