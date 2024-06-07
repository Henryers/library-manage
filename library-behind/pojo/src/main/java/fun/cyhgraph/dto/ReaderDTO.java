package fun.cyhgraph.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderDTO implements Serializable {

    private Integer id;
    private String name;
    private Integer sex;
    // 一个字母+一个单词的写法会被转成全小写，无法实现驼峰命名，只能加注解解决
    @JsonProperty("wAddress")
    private String wAddress; // 工作单位
    @JsonProperty("hAddress")
    private String hAddress; // 家庭地址
    private String phone;
    private String email;
    private String notes;
    private Integer categoryId;
}
