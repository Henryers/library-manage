package fun.cyhgraph.mapper;

import com.github.pagehelper.Page;
import fun.cyhgraph.annotation.AutoFill;
import fun.cyhgraph.dto.BookPageDTO;
import fun.cyhgraph.entity.Book;
import fun.cyhgraph.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookMapper {

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into book (name, category_id, author, press, publish_date, " +
            "price, page_number, keywords, create_time, status, notes) " +
            "VALUES " +
            "(#{name}, #{categoryId}, #{author}, #{press}, #{publishDate}," +
            "#{price}, #{pageNumber}, #{keywords}, #{createTime}, #{status}, #{notes})")
    void insert(Book book);

    Page<Book> page(BookPageDTO bookPageDTO);

    @Select("select * from book where id = #{id}")
    Book getById(Integer id);

    void update(Book book);

    void deleteBatch(List<Integer> ids);

    @Update("update book set status = IF(status = 1, 0, 1) where id = #{id}")
    void status(Integer id);

    @Select("select count(id) from book where category_id = #{id}")
    Integer sumByCategoryId(Integer id);
}
