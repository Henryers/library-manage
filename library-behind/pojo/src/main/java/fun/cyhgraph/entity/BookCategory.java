package fun.cyhgraph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCategory implements Serializable {

    private Integer id;
    private String name;
    private String keywords;
    private String notes;

}
