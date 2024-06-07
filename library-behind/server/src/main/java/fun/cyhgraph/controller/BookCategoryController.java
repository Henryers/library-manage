package fun.cyhgraph.controller;

import com.github.pagehelper.Page;
import fun.cyhgraph.dto.BookCatePageDTO;
import fun.cyhgraph.dto.BookCategoryDTO;
import fun.cyhgraph.entity.BookCategory;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.BookCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookCategory")
@Slf4j
public class BookCategoryController {

    @Autowired
    private BookCategoryService bookCategoryService;

    /**
     * 添加书籍分类
     * @return
     */
    @PostMapping
    public Result addCategory(@RequestBody BookCategoryDTO bookCategoryDTO){
        log.info("要添加的书籍分类信息：{}", bookCategoryDTO);
        bookCategoryService.addCategory(bookCategoryDTO);
        return Result.success();
    }

    /**
     * 书籍分类分页查询
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(BookCatePageDTO bookCatePageDTO){
        log.info("分页查询条件：{}", bookCatePageDTO);
        PageResult pageResult = bookCategoryService.page(bookCatePageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询书籍分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("要查询的书籍id：{}", id);
        BookCategory bookCategory = bookCategoryService.getById(id);
        return Result.success(bookCategory);
    }

    /**
     * 修改书籍分类
     * @param bookCategoryDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody BookCategoryDTO bookCategoryDTO){
        log.info("要修改的书籍分类信息：{}", bookCategoryDTO);
        bookCategoryService.update(bookCategoryDTO);
        return Result.success();
    }

    /**
     * 根据id删除书籍分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        log.info("要删除的书籍分类：{}", id);
        bookCategoryService.deleteById(id);
        return Result.success();
    }
}
