package fun.cyhgraph.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface ChartMapper {

    @Select("select count(id) from lend_return where lend_date = #{date}")
    Integer sumByLendDate(LocalDate date);

    @Select("select count(id) from lend_return where return_date = #{date}")
    Integer sumByReturnDate(LocalDate date);
}
