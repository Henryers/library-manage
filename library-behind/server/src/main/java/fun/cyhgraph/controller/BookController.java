package fun.cyhgraph.controller;

import fun.cyhgraph.dto.BookDTO;
import fun.cyhgraph.dto.BookPageDTO;
import fun.cyhgraph.entity.Book;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.BookService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 新增书籍
     * @return
     */
    @PostMapping
    public Result addBook(@RequestBody BookDTO bookDTO) {
        bookService.addBook(bookDTO);
        return Result.success();
    }

    /**
     * 条件书籍分页查询
     * @param bookPageDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(BookPageDTO bookPageDTO) {
        log.info("分页条件：{}", bookPageDTO);
        PageResult pageResult = bookService.page(bookPageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询书籍
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据id查询书籍：{}", id);
        Book book = bookService.getById(id);
        return Result.success(book);
    }

    /**
     * 修改书籍信息
     * @param bookDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody BookDTO bookDTO){
        log.info("新的书籍信息：{}", bookDTO);
        bookService.update(bookDTO);
        return Result.success(bookDTO);
    }

    /**
     * 根据id修改书籍状态（是否借出），在sql里直接做状态反转
     * @return
     */
    @PutMapping("/status/{id}")
    public Result status(@PathVariable Integer id){
        bookService.status(id);
        return Result.success();
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
        bookService.deleteBatch(ids);
        return Result.success();
    }
}
