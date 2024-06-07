package fun.cyhgraph.service;

import fun.cyhgraph.dto.ReaderDTO;
import fun.cyhgraph.dto.ReaderPageDTO;
import fun.cyhgraph.entity.Reader;
import fun.cyhgraph.result.PageResult;

import java.util.List;

public interface ReaderService {
    void addReader(ReaderDTO readerDTO);

    PageResult page(ReaderPageDTO readerPageDTO);

    Reader getById(Integer id);

    void update(ReaderDTO readerDTO);

    void deleteBatch(List<Integer> ids);
}
