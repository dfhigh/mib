package org.mib.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by dufei on 17/12/18.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListPayload<T> {
    private List<T> list;
    private long page;
    private long total;
}
