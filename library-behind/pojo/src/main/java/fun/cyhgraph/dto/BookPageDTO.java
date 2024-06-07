package fun.cyhgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookPageDTO implements Serializable {

    int page;
    int pageSize;
    private String name; // 书籍名称
    private String status; // 书籍是否借出
    private Integer categoryId; // 书籍所属分类
}
