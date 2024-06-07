package fun.cyhgraph.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LendReturnPageDTO implements Serializable {

    int page;
    int pageSize;
    @JsonProperty("rId")
    private Integer rId; // 读者名称
    @JsonProperty("bId")
    private Integer bId; // 书籍名称
    private String notes;
}
