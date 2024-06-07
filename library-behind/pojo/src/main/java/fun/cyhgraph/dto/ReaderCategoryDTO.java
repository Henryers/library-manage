package fun.cyhgraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderCategoryDTO implements Serializable {

    private Integer id;
    private String name;
    private Integer amount;
    private Integer lendPeriod;
    private LocalDate effectPeriod;
    private String notes;

}
