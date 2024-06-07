package fun.cyhgraph.service;

import fun.cyhgraph.vo.BookCategoryVO;
import fun.cyhgraph.vo.LendReturnReportVO;
import fun.cyhgraph.vo.ReaderCategoryVo;
import fun.cyhgraph.vo.TopVO;

public interface ChartService {
    BookCategoryVO bookPie();

    ReaderCategoryVo readerPie();
    LendReturnReportVO dayNLend(Integer day);

    LendReturnReportVO dayNReturn(Integer day);

    TopVO bookTop(Integer day);

    TopVO readerTop(Integer day);
}
