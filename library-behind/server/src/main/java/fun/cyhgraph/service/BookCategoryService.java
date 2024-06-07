package fun.cyhgraph.service;

import fun.cyhgraph.dto.BookCatePageDTO;
import fun.cyhgraph.dto.BookCategoryDTO;
import fun.cyhgraph.entity.BookCategory;
import fun.cyhgraph.result.PageResult;

public interface BookCategoryService {
    void addCategory(BookCategoryDTO bookCategoryDTO);

    PageResult page(BookCatePageDTO bookCatePageDTO);

    BookCategory getById(Integer id);

    void update(BookCategoryDTO bookCategoryDTO);

    void deleteById(Integer id);
}
