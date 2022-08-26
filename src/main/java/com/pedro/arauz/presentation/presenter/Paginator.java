package com.pedro.arauz.presentation.presenter;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Paginator {

    private Integer totalPages;
    private Long totalElements;
    private Set data;

}
