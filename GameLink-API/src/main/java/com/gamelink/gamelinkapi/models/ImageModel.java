package com.gamelink.gamelinkapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image_table")
@EqualsAndHashCode(callSuper = true)
public class ImageModel extends BaseModel {
    @NotBlank
    private String filename;
    @NotBlank
    private String url;
    @NotBlank
    private String publicId;
}
