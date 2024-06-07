package fun.cyhgraph.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fun.cyhgraph.constant.MessageConstant;
import fun.cyhgraph.dto.LendReturnDTO;
import fun.cyhgraph.dto.LendReturnPageDTO;
import fun.cyhgraph.entity.Book;
import fun.cyhgraph.entity.LendReturn;
import fun.cyhgraph.entity.Reader;
import fun.cyhgraph.entity.ReaderCategory;
import fun.cyhgraph.exception.BorrowMaxException;
import fun.cyhgraph.exception.BorrowTooLongException;
import fun.cyhgraph.mapper.BookMapper;
import fun.cyhgraph.mapper.BorrowMapper;
import fun.cyhgraph.mapper.ReaderCategoryMapper;
import fun.cyhgraph.mapper.ReaderMapper;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BorrowServiceImpl implements BorrowService {


    @Autowired
    private BorrowMapper borrowMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private ReaderCategoryMapper readerCategoryMapper;
    @Autowired
    private ReaderMapper readerMapper;

    /**
     * 新增借还书记录
     *
     * @param lendReturnDTO
     */
    public void addBorrow(LendReturnDTO lendReturnDTO) {
        LendReturn lendReturn = new LendReturn();
        BeanUtils.copyProperties(lendReturnDTO, lendReturn);
        Reader reader = readerMapper.getById(lendReturn.getRId());
        // 读者种类
        ReaderCategory readerCategory = readerCategoryMapper.getById(reader.getCategoryId());
        // 1、如果超过借书有效期，要抛异常
        if (lendReturn.getLendDate().isAfter(readerCategory.getEffectPeriod())){
            throw new BorrowTooLongException(MessageConstant.BORROW_OUT_OF_EFFECT_PERIOD);
        }
        // 2、借书前要看当前借书人可借数量有没有达到上限，达到就抛异常给前端
        // 2.1 拿到这个读者当前已借但未还的书本数 return_date = null || return_date > 当前书本的lend_date
        Integer amount = borrowMapper.getReaderHad(lendReturn);
        log.info("--------------------拿到这个读者当前已借但未还的书本数：{}", amount);
        // 2.2 拿到当前读者的categoryId，并查询这种读者可借书本上限
        Integer maxBooks = readerCategory.getAmount();
        log.info("--------------------查询这种读者可借书本上限：{}", maxBooks);
        // 2.3 比较，达到就抛异常，表示已达上限
        if (amount >= maxBooks) {
            throw new BorrowMaxException(MessageConstant.BORROW_MAX);
        }
        // 3、借书需要将对应书籍的状态设置为1，表示已借出
        // 3.1 先获取对应借出的书籍
        Integer bId = lendReturn.getBId();
        Book book = bookMapper.getById(bId);
        // 3.2 更新状态为1
        book.setStatus(1);
        bookMapper.update(book);
        // 3.4 插入借还书记录
        borrowMapper.insert(lendReturn);
    }

    /**
     * 分页查询借还书记录
     *
     * @return
     */
    public PageResult page(LendReturnPageDTO lendReturnPageDTO) {
        PageHelper.startPage(lendReturnPageDTO.getPage(), lendReturnPageDTO.getPageSize());
        Page<LendReturn> lendReturnPage = borrowMapper.page(lendReturnPageDTO);
        return new PageResult(lendReturnPage.getTotal(), lendReturnPage.getResult());
    }

    /**
     * 根据2个id查询借还书记录
     *
     * @param id
     * @return
     */
    public LendReturn getById(Integer id) {
        return borrowMapper.getById(id);
    }

    /**
     * 修改借还书记录（还书操作）
     *
     * @param lendReturnDTO
     */
    public void update(LendReturnDTO lendReturnDTO) {
        LendReturn lendReturn = new LendReturn();
        BeanUtils.copyProperties(lendReturnDTO, lendReturn);
        // 1、还书前要看读者借书时长是否过长
        Reader reader = readerMapper.getById(lendReturn.getRId());
        // 读者种类
        ReaderCategory readerCategory = readerCategoryMapper.getById(reader.getCategoryId());
        // 1.1 拿到当前种类读者最大可借天数
        Integer maxDays = readerCategory.getLendPeriod();
        // 1.2 还书日期 - 借书日期，看看借书天数（没设置还书日期就不用判断） 2：逾期归还不用判断
        if (lendReturn.getReturnDate() != null && lendReturn.getStatus() != 2) {
            long lendDays = ChronoUnit.DAYS.between(lendReturn.getLendDate(), lendReturn.getReturnDate());
            // 1.3 比较，违反就抛异常（如果超过借书有效期，那也要抛异常）
            log.info("借书天数{}", lendDays);
            log.info("最大借书天数{}", maxDays);
            if (lendDays >= maxDays) {
                throw new BorrowTooLongException(MessageConstant.BORROW_TOO_LONG);
            } else if (lendReturn.getReturnDate().isAfter(readerCategory.getEffectPeriod())){
                throw new BorrowTooLongException(MessageConstant.BORROW_OUT_OF_EFFECT_PERIOD);
            }
        }
        // 先根据这条记录查到对应的书籍信息
        Book book = bookMapper.getById(lendReturnDTO.getBId());
        log.info("书籍归还状态：{}", lendReturnDTO.getStatus());
        if (lendReturnDTO.getStatus() == 1 || lendReturnDTO.getStatus() == 4) {
            // 可以归还，对应书籍的status设为0
            book.setStatus(0);
        } else {
            // 没有归还，对应书籍的status设为1
            book.setStatus(1);
        }
        // 更新书籍状态和借书记录
        bookMapper.update(book);
        borrowMapper.update(lendReturn);
    }

    /**
     * 批量删除异常借还书记录
     *
     * @param ids
     */
    public void delete(List<Integer> ids) {
        // 拿到这些记录对应的书籍记录，储存这些bId
        List<Integer> bIds = new ArrayList<>();
        for (Integer id : ids) {
            Integer bId = borrowMapper.getById(id).getBId();
            bIds.add(bId);
        }
        // 再批量删除记录还有书籍
        borrowMapper.deleteBatch(ids);
        bookMapper.deleteBatch(bIds);
    }

}
