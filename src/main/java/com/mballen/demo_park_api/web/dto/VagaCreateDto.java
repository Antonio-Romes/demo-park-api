package com.mballen.demo_park_api.web.dto;

import jakarta.validation.constraints.*;
import lombok.*; 


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class VagaCreateDto {
    
    @NotBlank
    @Size(min = 4 , max = 4)
    private String codigo;

    @NotBlank
    @Pattern(regexp = "LIVRE|OCUPADA")
    private String status;

}
