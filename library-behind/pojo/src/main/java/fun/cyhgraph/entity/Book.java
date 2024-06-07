package fun.cyhgraph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    private Integer id;
    private String name;
    private String author;
    private String press;
    private LocalDate publishDate;
    private BigDecimal price;
    private Integer pageNumber;
    private String keywords;
    private LocalDateTime createTime;
    private Integer status;
    private String notes;
    private Integer categoryId;

}
