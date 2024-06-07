package fun.cyhgraph.service;

import fun.cyhgraph.dto.BookDTO;
import fun.cyhgraph.dto.BookPageDTO;
import fun.cyhgraph.entity.Book;
import fun.cyhgraph.result.PageResult;

import java.util.List;

public interface BookService {
    void addBook(BookDTO bookDTO);

    PageResult page(BookPageDTO bookPageDTO);

    Book getById(Integer id);

    void update(BookDTO bookDTO);

    void deleteBatch(List<Integer> ids);

    void status(Integer id);
}
