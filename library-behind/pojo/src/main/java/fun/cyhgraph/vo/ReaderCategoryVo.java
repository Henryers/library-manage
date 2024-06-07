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
public class ReaderCategoryVo implements Serializable {

    private String readerCategoryNames; // 读者分类名称列表
    private String readerCategoryNums; // 读者分类对应读者的数量

}
