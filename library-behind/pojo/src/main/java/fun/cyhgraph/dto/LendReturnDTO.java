package fun.cyhgraph.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LendReturnDTO implements Serializable {

    private Integer id;
    // 一个字母 + 一个单词的写法会被转成全小写，无法实现驼峰命名，只能加注解解决
    @JsonProperty("rId")
    private Integer rId;
    @JsonProperty("bId")
    private Integer bId;
    private LocalDate lendDate;
    private LocalDate returnDate;
    // 归还书籍状态 0：出借中  1：正常归还  2：逾期归还  3：丢失无法归还  4：损坏归还  5：其他(见备注)
    // 1 4 都是有归还，需要将书籍的状态设为0，允许其他人借阅，2 3 5的书籍状态都设为1，其他要注明信息notes
    private Integer status;
    private String notes;

}
