package fun.cyhgraph.controller;

import fun.cyhgraph.dto.LendReturnDTO;
import fun.cyhgraph.dto.LendReturnPageDTO;
import fun.cyhgraph.entity.LendReturn;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
@Slf4j
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    /**
     * 新增借还书记录
     * @param lendReturnDTO
     * @return
     */
    @PostMapping
    public Result addBorrow(@RequestBody LendReturnDTO lendReturnDTO){
        log.info("新增借还书记录：{}", lendReturnDTO);
        borrowService.addBorrow(lendReturnDTO);
        return Result.success();
    }

    /**
     * 分页查询借还书记录
     * @param lendReturnPageDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(LendReturnPageDTO lendReturnPageDTO){
        log.info("分页查询借还书记录的条件：{}", lendReturnPageDTO);
        PageResult pageResult = borrowService.page(lendReturnPageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据书、人的信息查询借阅记录
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<LendReturn> getById(@PathVariable Integer id){
        log.info("借书人和书籍分别为：{}", id);
        LendReturn lendReturn = borrowService.getById(id);
        return Result.success(lendReturn);
    }

    /**
     * 更新借还书记录
     * @param lendReturnDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody LendReturnDTO lendReturnDTO){
        log.info("新的借还书记录：{}", lendReturnDTO);
        borrowService.update(lendReturnDTO);
        return Result.success();
    }

    /**
     * 批量删除异常借还书记录（没有异常就不要随便删除记录！）
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteBatch(@RequestParam List<Integer> ids){
        log.info("批量删除异常借还书记录：{}", ids);
        borrowService.delete(ids);
        return Result.success();
    }
}
