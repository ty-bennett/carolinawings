package com.carolinawings.webapp.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder @ToString
public class PagedOrderResponseDTO {
    private List<OrderResponseDTO> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;
}
