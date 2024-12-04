package com.mporto.demo_park_api.web.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class VagaResponseDto {
    private Long id;
    private String codigo;
    private String status;
}
