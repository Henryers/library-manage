package fun.cyhgraph.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LendReturn implements Serializable {

    private Integer id;
    // 一个字母+一个单词的写法会被转成全小写，无法实现驼峰命名，只能加注解解决
    @JsonProperty("rId")
    private Integer rId;
    @JsonProperty("bId")
    private Integer bId;
    private LocalDate lendDate;
    private LocalDate returnDate;
    private Integer status;
    private String notes;

}
