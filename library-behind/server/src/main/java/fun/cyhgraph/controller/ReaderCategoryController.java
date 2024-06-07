package fun.cyhgraph.controller;

import fun.cyhgraph.dto.BookCategoryDTO;
import fun.cyhgraph.dto.ReaderCatePageDTO;
import fun.cyhgraph.dto.ReaderCategoryDTO;
import fun.cyhgraph.entity.BookCategory;
import fun.cyhgraph.entity.ReaderCategory;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.ReaderCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/readerCategory")
@Slf4j
public class ReaderCategoryController {

    @Autowired
    private ReaderCategoryService  readerCategoryService;

    /**
     * 添加读者种类
     * @return
     */
    @PostMapping
    public Result addCategory(@RequestBody ReaderCategoryDTO readerCategoryDTO){
        log.info("读者种类信息：{}", readerCategoryDTO);
        readerCategoryService.addCategory(readerCategoryDTO);
        return Result.success();
    }

    /**
     * 读者分类分页查询
     * @param readerCatePageDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(ReaderCatePageDTO readerCatePageDTO){
        log.info("读者种类分页查询条件：{}", readerCatePageDTO);
        PageResult pageResult = readerCategoryService.page(readerCatePageDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询书籍分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("要查询的读者id：{}", id);
        ReaderCategory readerCategory = readerCategoryService.getById(id);
        return Result.success(readerCategory);
    }

    /**
     * 修改读者分类
     * @param readerCategoryDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody ReaderCategoryDTO readerCategoryDTO){
        log.info("要修改的读者分类信息：{}", readerCategoryDTO);
        readerCategoryService.update(readerCategoryDTO);
        return Result.success();
    }

    /**
     * 根据id删除读者分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        log.info("根据id删除读者分类：{}", id);
        readerCategoryService.deleteById(id);
        return Result.success();
    }
}
