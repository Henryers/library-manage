package fun.cyhgraph.mapper;

import com.github.pagehelper.Page;
import fun.cyhgraph.annotation.AutoFill;
import fun.cyhgraph.dto.ReaderPageDTO;
import fun.cyhgraph.entity.Reader;
import fun.cyhgraph.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReaderMapper {

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into reader (name, category_id, sex, w_address, h_address, phone, email, create_time, notes) " +
            "VALUES " +
            "(#{name}, #{categoryId}, #{sex}, #{wAddress}, #{hAddress}, #{phone}, #{email}, #{createTime}, #{notes})")
    void insert(Reader reader);

    Page<Reader> page(ReaderPageDTO readerPageDTO);

    @Select("select * from reader where id = #{id}")
    Reader getById(Integer id);

    void update(Reader reader);

    void deleteBatch(List<Integer> ids);

    @Select("select count(id) from reader where category_id = #{id}")
    Integer sumByCategoryId(Integer id);
}
