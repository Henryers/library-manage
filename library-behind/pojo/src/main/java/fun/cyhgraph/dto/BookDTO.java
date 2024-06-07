package fun.cyhgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO implements Serializable {

    private Integer id;
    private String name;
    private String author;
    private String press;
    private LocalDate publishDate;
    private BigDecimal price;
    private Integer pageNumber;
    private String keywords;
    private String notes;
    private Integer categoryId;

}
