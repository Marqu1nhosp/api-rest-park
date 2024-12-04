package com.mporto.demo_park_api.web.dto.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import com.mporto.demo_park_api.entity.Cliente;
import com.mporto.demo_park_api.web.dto.ClienteCreateDto;
import com.mporto.demo_park_api.web.dto.ClienteResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }
}