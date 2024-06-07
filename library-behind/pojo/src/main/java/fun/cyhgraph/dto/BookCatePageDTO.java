package fun.cyhgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCatePageDTO implements Serializable {

    int page;
    int pageSize;
    private String name; // 书籍分类名称
    private String keywords; // 书籍分类关键词

}
