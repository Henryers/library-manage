package fun.cyhgraph.service;

import fun.cyhgraph.dto.ReaderCatePageDTO;
import fun.cyhgraph.dto.ReaderCategoryDTO;
import fun.cyhgraph.entity.ReaderCategory;
import fun.cyhgraph.result.PageResult;

public interface ReaderCategoryService {
    void addCategory(ReaderCategoryDTO readerCategoryDTO);

    PageResult page(ReaderCatePageDTO readerCatePageDTO);

    ReaderCategory getById(Integer id);

    void update(ReaderCategoryDTO readerCategoryDTO);

    void deleteById(Integer id);
}
