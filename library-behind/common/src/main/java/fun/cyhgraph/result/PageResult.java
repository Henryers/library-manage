package fun.cyhgraph.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询的结果
 * 自动生成toString()等方法、有参无参构造
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {

    private Long total;   // 总记录数
    private List records; // 当前页数据集合
}
