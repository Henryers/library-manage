package fun.cyhgraph.controller;

import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.ChartService;
import fun.cyhgraph.vo.BookCategoryVO;
import fun.cyhgraph.vo.LendReturnReportVO;
import fun.cyhgraph.vo.ReaderCategoryVo;
import fun.cyhgraph.vo.TopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Autowired
    private ChartService chartService;


    /**
     * 获取书籍分类对应书籍数量
     * @return
     */
    @GetMapping("/bookCategory")
    public Result<BookCategoryVO> bookPie(){
        log.info("获取书籍分类对应书籍数量");
        return Result.success(chartService.bookPie());
    }

    /**
     * 获取读者分类对应读者数量
     * @return
     */
    @GetMapping("/readerCategory")
    public Result<ReaderCategoryVo> readerPie(){
        log.info("获取读者分类对应读者数量");
        return Result.success(chartService.readerPie());
    }

    /**
     * 查询近day天的借书数据
     * @param day
     * @return
     */
    @GetMapping("/dayNLend")
    public Result<LendReturnReportVO> dayNLend(Integer day) {
        log.info("借书数据 近day天：{}", day);
        LendReturnReportVO lendReportVO = chartService.dayNLend(day);
        return Result.success(lendReportVO);
    }

    /**
     * 查询近day天的还书数据
     * @param day
     * @return
     */
    @GetMapping("/dayNReturn")
    public Result<LendReturnReportVO> dayNReturn(Integer day) {
        log.info("还书数据 近day天：{}", day);
        LendReturnReportVO returnReportVO = chartService.dayNReturn(day);
        return Result.success(returnReportVO);
    }

    /**
     * 查询近day天的书本销量
     * @param day
     * @return
     */
    @GetMapping("/bookTop")
    public Result<TopVO> bookTop(Integer day){
        log.info("bookTop 近day天：{}", day);
        TopVO topVO = chartService.bookTop(day);
        return Result.success(topVO);
    }

    /**
     * 查询近day天的读者借书排行榜
     * @param day
     * @return
     */
    @GetMapping("/readerTop")
    public Result<TopVO> readerTop(Integer day){
        log.info("readerTop 近day天：{}", day);
        TopVO topVO = chartService.readerTop(day);
        return Result.success(topVO);
    }

}
