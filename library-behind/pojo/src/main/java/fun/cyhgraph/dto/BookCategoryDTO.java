package fun.cyhgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryDTO implements Serializable {

    private Integer id;
    private String name;
    private String keywords;
    private String notes;

}
