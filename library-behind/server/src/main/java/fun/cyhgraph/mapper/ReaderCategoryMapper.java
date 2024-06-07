package fun.cyhgraph.mapper;

import com.github.pagehelper.Page;
import fun.cyhgraph.dto.ReaderCatePageDTO;
import fun.cyhgraph.dto.ReaderCategoryDTO;
import fun.cyhgraph.entity.ReaderCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReaderCategoryMapper {

    @Insert("insert into r_category (name, amount, lend_period, effect_period, notes) VALUES " +
            "(#{name}, #{amount}, #{lendPeriod}, #{effectPeriod}, #{notes})")
    void insert(ReaderCategory readerCategory);

    Page<ReaderCategory> page(ReaderCatePageDTO readerCatePageDTO);

    @Select("select * from r_category where id = #{id}")
    ReaderCategory getById(Integer id);

    void update(ReaderCategory readerCategory);

    @Delete("delete from r_category where id = #{id}")
    void deleteById(Integer id);

    @Select("select amount from r_category where id = #{id}")
    Integer getAmountById(Integer rId);

    @Select("select name from r_category")
    List<String> getNames();

    @Select("select id from r_category")
    List<Integer> getIds();
}
