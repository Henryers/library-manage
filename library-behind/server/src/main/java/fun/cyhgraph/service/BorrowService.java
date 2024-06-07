package fun.cyhgraph.service;

import fun.cyhgraph.dto.LendReturnDTO;
import fun.cyhgraph.dto.LendReturnPageDTO;
import fun.cyhgraph.entity.LendReturn;
import fun.cyhgraph.result.PageResult;

import java.util.List;

public interface BorrowService {
    void addBorrow(LendReturnDTO lendReturnDTO);

    PageResult page(LendReturnPageDTO lendReturnPageDTO);

    LendReturn getById(Integer id);

    void update(LendReturnDTO lendReturnDTO);

    void delete(List<Integer> ids);
}
