package com.welhire.shared.dto.wrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> items;
    private PaginationMeta meta;

    public PageResponse(List<T> items, long total, int page, int size) {
        this.items = items;
        this.meta = new PaginationMeta(total, page, size);
    }

}
