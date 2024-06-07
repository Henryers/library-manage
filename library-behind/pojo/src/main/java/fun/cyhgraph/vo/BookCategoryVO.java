package fun.cyhgraph.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryVO implements Serializable {

    private String bookCategoryNames; // 书籍分类名称列表
    private String bookCategoryNums; // 书籍分类对应书的数量

}
