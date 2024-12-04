package com.mporto.demo_park_api.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@SuppressWarnings({ "rawtypes" })
@Getter @Setter
public class PageableDto {
   
    private List content = new ArrayList<>();
    private boolean first;
    private boolean last;

    @JsonProperty("page")
    private int number;
    private int size;
    
    @JsonProperty("pageElements")
    private int numberofElements;

    private int totalPages;
    private int totalElements;

}
