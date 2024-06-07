package fun.cyhgraph.mapper;

import com.github.pagehelper.Page;
import fun.cyhgraph.dto.BookCatePageDTO;
import fun.cyhgraph.entity.BookCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookCategoryMapper {

    @Insert("insert into b_category (name, keywords, notes) VALUES (#{name}, #{keywords}, #{notes})")
    void insert(BookCategory bookCategory);

    Page<BookCategory> page(BookCatePageDTO bookCatePageDTO);

    @Select("select * from b_category where id = #{id}")
    BookCategory getById(Integer id);

    void update(BookCategory bookCategory);

    @Delete("delete from b_category where id = #{id}")
    void deleteById(Integer id);

    @Select("select name from b_category")
    List<String> getNames();

    @Select("select id from b_category")
    List<Integer> getIds();
}
