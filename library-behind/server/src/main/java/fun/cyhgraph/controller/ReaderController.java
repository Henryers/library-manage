package fun.cyhgraph.controller;

import fun.cyhgraph.dto.BookDTO;
import fun.cyhgraph.dto.BookPageDTO;
import fun.cyhgraph.dto.ReaderDTO;
import fun.cyhgraph.dto.ReaderPageDTO;
import fun.cyhgraph.entity.Book;
import fun.cyhgraph.entity.Reader;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reader")
@Slf4j
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    /**
     * 新增读者
     * @param readerDTO
     * @return
     */
    @PostMapping
    public Result addReader(@RequestBody ReaderDTO readerDTO){
        log.info("新增读者的信息：{}", readerDTO);
        readerService.addReader(readerDTO);
        return Result.success();
    }

    /**
     * 条件读者分页查询
     * @param readerPageDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(ReaderPageDTO readerPageDTO) {
        log.info("分页条件：{}", readerPageDTO);
        PageResult pageResult = readerService.page(readerPageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询读者
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据id查询书籍：{}", id);
        Reader reader = readerService.getById(id);
        return Result.success(reader);
    }

    /**
     * 修改读者信息
     * @param readerDTO
     * @return
     */
    @PutMapping
    public Result udpate(@RequestBody ReaderDTO readerDTO){
        log.info("新的读者信息：{}", readerDTO);
        readerService.update(readerDTO);
        return Result.success(readerDTO);
    }

    /**
     * 批量删除书籍
     * 前端传过来的数组是"1,2,3"这种序列化好的字符串，List<Integer>无法识别
     * 所以需要加 @RequestParam 让它强制识别，否则会发生List类型参数报错
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteBatch(@RequestParam List<Integer> ids){
        log.info("批量删除行数据：{}", ids);
        readerService.deleteBatch(ids);
        return Result.success();
    }
}
