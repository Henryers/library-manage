package fun.cyhgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderCatePageDTO implements Serializable {

    int page;
    int pageSize;
    private String name; // 读者分类名称

}
