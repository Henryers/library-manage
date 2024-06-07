package fun.cyhgraph.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fun.cyhgraph.dto.ReaderDTO;
import fun.cyhgraph.dto.ReaderPageDTO;
import fun.cyhgraph.entity.Reader;
import fun.cyhgraph.mapper.ReaderMapper;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.service.ReaderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderMapper readerMapper;

    /**
     * 新增读者
     * @param readerDTO
     */
    public void addReader(ReaderDTO readerDTO) {
        Reader reader = new Reader();
        BeanUtils.copyProperties(readerDTO, reader);
        readerMapper.insert(reader);
    }

    /**
     * 条件分页查询读者
     * @param readerPageDTO
     * @return
     */
    public PageResult page(ReaderPageDTO readerPageDTO) {
        PageHelper.startPage(readerPageDTO.getPage(), readerPageDTO.getPageSize());
        Page<Reader> readerPage = readerMapper.page(readerPageDTO);
        return new PageResult(readerPage.getTotal(), readerPage.getResult());
    }

    /**
     * 根据id查询读者
     * @param id
     * @return
     */
    public Reader getById(Integer id) {
        return readerMapper.getById(id);
    }

    /**
     * 更新读者信息
     * @param readerDTO
     */
    public void update(ReaderDTO readerDTO) {
        Reader reader = new Reader();
        BeanUtils.copyProperties(readerDTO, reader);
        readerMapper.update(reader);
    }

    /**
     * 批量删除读者
     * @param ids
     */
    public void deleteBatch(List<Integer> ids) {
        readerMapper.deleteBatch(ids);
    }


}