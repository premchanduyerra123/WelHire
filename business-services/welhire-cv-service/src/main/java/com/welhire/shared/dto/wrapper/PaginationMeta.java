package com.welhire.shared.dto.wrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationMeta {

    private long total;
    private int page;
    private int size;

}
