package fun.cyhgraph.mapper;

import com.github.pagehelper.Page;
import fun.cyhgraph.dto.LendReturnPageDTO;
import fun.cyhgraph.entity.LendReturn;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BorrowMapper {

    @Insert("insert into lend_return (r_id, b_id, lend_date, return_date, status, notes) VALUES " +
            "(#{rId}, #{bId}, #{lendDate}, #{returnDate}, #{status}, #{notes})")
    void insert(LendReturn lendReturn);

    Page<LendReturn> page(LendReturnPageDTO lendReturnPageDTO);

    @Select("select * from lend_return where id = #{id}")
    LendReturn getById(Integer id);

    void update(LendReturn lendReturn);

    void deleteBatch(List<Integer> ids);

    List<Integer> getBIdsByDate(LocalDate begin);

    Integer getBookAmount(LocalDate begin, Integer bId);

    List<Integer> getRIdsByDate(LocalDate begin);

    /**
     * 拿到rId读者在begin之后(>=begin)借书的数量
     * @param begin
     * @param rId
     * @return
     */
    Integer getReaderAmount(LocalDate begin, Integer rId);

    /**
     * 拿到rId读者在lendDate之前(<=lendDate)借书的数量
     * @param lendReturn
     * @return
     */
    Integer getReaderHad(LendReturn lendReturn);
}
