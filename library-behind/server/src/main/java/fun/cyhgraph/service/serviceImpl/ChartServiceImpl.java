package fun.cyhgraph.service.serviceImpl;

import fun.cyhgraph.entity.TopInfo;
import fun.cyhgraph.mapper.*;
import fun.cyhgraph.service.ChartService;
import fun.cyhgraph.vo.BookCategoryVO;
import fun.cyhgraph.vo.LendReturnReportVO;
import fun.cyhgraph.vo.ReaderCategoryVo;
import fun.cyhgraph.vo.TopVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.Top;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChartServiceImpl implements ChartService {

    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private BookCategoryMapper bookCategoryMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private ReaderCategoryMapper readerCategoryMapper;
    @Autowired
    private ReaderMapper readerMapper;
    @Autowired
    private BorrowMapper borrowMapper;


    /**
     * 获取不同分类下书籍数量
     *
     * @return
     */
    public BookCategoryVO bookPie() {
        List<String> names = bookCategoryMapper.getNames();
        List<Integer> ids = bookCategoryMapper.getIds();
        List<Integer> bookNumList = new ArrayList<>();
        for (Integer id : ids) {
            Integer amount = bookMapper.sumByCategoryId(id);
            amount = amount == null ? 0 : amount;
            bookNumList.add(amount);
        }
        return BookCategoryVO.builder()
                .bookCategoryNames(StringUtils.join(names,","))
                .bookCategoryNums(StringUtils.join(bookNumList, ","))
                .build();
    }

    /**
     * 获取不同分类下读者数量
     * @return
     */
    public ReaderCategoryVo readerPie() {
        List<String> names = readerCategoryMapper.getNames();
        List<Integer> ids = readerCategoryMapper.getIds();
        List<Integer> readerNumList = new ArrayList<>();
        for (Integer id : ids) {
            Integer amount = readerMapper.sumByCategoryId(id);
            amount = amount == null ? 0 : amount;
            readerNumList.add(amount);
        }
        return ReaderCategoryVo.builder()
                .readerCategoryNames(StringUtils.join(names,","))
                .readerCategoryNums(StringUtils.join(readerNumList, ","))
                .build();
    }

    /**
     * 近n天借书数据
     *
     * @param day
     */
    public LendReturnReportVO dayNLend(Integer day) {
        LocalDate end = LocalDate.now();
        // 前n-1天 + 今天 = day天
        LocalDate begin = LocalDate.now().minusDays(day - 1);
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1); // 日期计算，获得指定日期后1天的日期
            dateList.add(begin);
        }
        List<Integer> lendList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Integer amount = chartMapper.sumByLendDate(date);
            // 当天没有记录要补0，不能传null/空字符串给前端！
            amount = amount == null ? 0 : amount;
            lendList.add(amount);
        }
        return LendReturnReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .lendOrReturnList(StringUtils.join(lendList, ","))
                .build();
    }

    /**
     * 近n天还书数据
     *
     * @param day
     * @return
     */
    public LendReturnReportVO dayNReturn(Integer day) {
        LocalDate end = LocalDate.now();
        // 前n-1天 + 今天 = day天
        LocalDate begin = LocalDate.now().minusDays(day - 1);
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1); // 日期计算，获得指定日期后1天的日期
            dateList.add(begin);
        }
        List<Integer> returnList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Integer amount = chartMapper.sumByReturnDate(date);
            // 当天没有记录要补0，不能传null/空字符串给前端！
            amount = amount == null ? 0 : amount;
            returnList.add(amount);
        }
        return LendReturnReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .lendOrReturnList(StringUtils.join(returnList, ","))
                .build();
    }

    /**
     * 查询近day天的书本销量
     * @param day
     * @return
     */
    public TopVO bookTop(Integer day) {
        LocalDate end = LocalDate.now();
        LocalDate begin = LocalDate.now().minusDays(day - 1);
        // 在lendReturn表中，查询到 begin - end 这段时间内的所有借阅记录
        List<Integer> bIds = borrowMapper.getBIdsByDate(begin);
        // 拿到这些id对应的书名，以及借书数量（用对象数组，方便后续top7处理）
        // 创建一个列表来保存读者信息
        List<TopInfo> bookInfoList = new ArrayList<>();
//        List<String> bookNames = new ArrayList<>();
//        List<Integer> topList = new ArrayList<>();
        for (Integer bId : bIds){
            String name = bookMapper.getById(bId).getName();
            Integer amount = borrowMapper.getBookAmount(begin, bId);
            bookInfoList.add(new TopInfo(name, amount));
//            bookNames.add(name);
//            topList.add(amount);
        }
        // 根据 书被借的次数amount 对书籍信息列表进行降序排序
        bookInfoList.sort(Comparator.comparingInt(TopInfo::getAmount).reversed());
        // 选取前7个元素
        List<String> topBookNames = bookInfoList.stream().limit(7).map(TopInfo::getName).collect(Collectors.toList());
        List<Integer> topAmounts = bookInfoList.stream().limit(7).map(TopInfo::getAmount).collect(Collectors.toList());

        // 封装成VO返回
        TopVO topVO = TopVO.builder()
                .nameList(StringUtils.join(topBookNames,","))
                .topList(StringUtils.join(topAmounts, ","))
                .build();
        return topVO;
    }

    /**
     * 查询近day天的读者借书排行榜
     * @param day
     * @return
     */
    public TopVO readerTop(Integer day) {
        LocalDate end = LocalDate.now();
        LocalDate begin = LocalDate.now().minusDays(day - 1);
        // 在lendReturn表中，查询到 begin - end 这段时间内的所有借阅记录
        List<Integer> rIds = borrowMapper.getRIdsByDate(begin);
        log.info("有没有去重！！！" + rIds);
        // 拿到这些id对应的书名，以及借书数量
        // 创建一个列表来保存读者信息
        List<TopInfo> readerInfoList = new ArrayList<>();
        for (Integer rId : rIds){
            String name = readerMapper.getById(rId).getName();
            Integer amount = borrowMapper.getReaderAmount(begin, rId);
            readerInfoList.add(new TopInfo(name, amount));
        }
        // 根据 借书数量amount 对读者信息列表进行降序排序
        readerInfoList.sort(Comparator.comparingInt(TopInfo::getAmount).reversed());
        // 选取前7个元素
        List<String> topReaderNames = readerInfoList.stream().limit(7).map(TopInfo::getName).collect(Collectors.toList());
        List<Integer> topAmounts = readerInfoList.stream().limit(7).map(TopInfo::getAmount).collect(Collectors.toList());

        // 封装成VO返回
        TopVO topVO = TopVO.builder()
                .nameList(StringUtils.join(topReaderNames,","))
                .topList(StringUtils.join(topAmounts, ","))
                .build();
        return topVO;
    }

}
