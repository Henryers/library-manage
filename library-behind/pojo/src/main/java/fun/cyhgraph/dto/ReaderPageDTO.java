package fun.cyhgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderPageDTO implements Serializable {

    int page;
    int pageSize;
    private String name; // 读者名称
    private Integer categoryId; // 读者所属分类

}
