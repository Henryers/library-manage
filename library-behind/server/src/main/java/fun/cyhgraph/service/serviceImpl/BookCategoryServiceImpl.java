package fun.cyhgraph.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fun.cyhgraph.dto.BookCatePageDTO;
import fun.cyhgraph.dto.BookCategoryDTO;
import fun.cyhgraph.entity.BookCategory;
import fun.cyhgraph.mapper.BookCategoryMapper;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.service.BookCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookCategoryServiceImpl implements BookCategoryService {

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    /**
     * 添加书籍分类
     * @param bookCategoryDTO
     */
    public void addCategory(BookCategoryDTO bookCategoryDTO) {
        BookCategory bookCategory = new BookCategory();
        BeanUtils.copyProperties(bookCategoryDTO, bookCategory);
        bookCategoryMapper.insert(bookCategory);
    }

    /**
     * 书籍分类分页查询
     * @param bookCatePageDTO
     * @return
     */
    public PageResult page(BookCatePageDTO bookCatePageDTO) {
        PageHelper.startPage(bookCatePageDTO.getPage(), bookCatePageDTO.getPageSize());
        Page<BookCategory> bookCategories = bookCategoryMapper.page(bookCatePageDTO);
        return new PageResult(bookCategories.getTotal(), bookCategories.getResult());
    }

    /**
     * 根据id查询书籍分类
     * @param id
     * @return
     */
    public BookCategory getById(Integer id) {
        return bookCategoryMapper.getById(id);
    }

    /**
     * 修改书籍分类
     * @param bookCategoryDTO
     */
    public void update(BookCategoryDTO bookCategoryDTO) {
        BookCategory bookCategory = new BookCategory();
        BeanUtils.copyProperties(bookCategoryDTO, bookCategory);
        bookCategoryMapper.update(bookCategory);
    }

    /**
     * 根据id删除书籍分类
     * @param id
     */
    public void deleteById(Integer id) {
        bookCategoryMapper.deleteById(id);
    }
}
