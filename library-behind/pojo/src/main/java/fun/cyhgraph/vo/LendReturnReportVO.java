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
public class LendReturnReportVO implements Serializable {

    // 日期，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
    private String dateList;
    // 借书还书数量，以逗号分隔，例如：4,15,7,13
    private String lendOrReturnList;

}
