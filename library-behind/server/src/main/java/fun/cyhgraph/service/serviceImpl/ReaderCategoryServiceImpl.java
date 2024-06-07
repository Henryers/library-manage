package fun.cyhgraph.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import fun.cyhgraph.dto.ReaderCatePageDTO;
import fun.cyhgraph.dto.ReaderCategoryDTO;
import fun.cyhgraph.entity.ReaderCategory;
import fun.cyhgraph.mapper.ReaderCategoryMapper;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.ReaderCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ReaderCategoryServiceImpl implements ReaderCategoryService {

    @Autowired
    private ReaderCategoryMapper readerCategoryMapper;

    /**
     * 添加读者分类
     * @param readerCategoryDTO
     */
    public void addCategory(ReaderCategoryDTO readerCategoryDTO) {
        ReaderCategory readerCategory = new ReaderCategory();
        BeanUtils.copyProperties(readerCategoryDTO, readerCategory);
        readerCategoryMapper.insert(readerCategory);
    }

    /**
     * 读者种类分页查询
     * @param readerCatePageDTO
     * @return
     */
    public PageResult page(ReaderCatePageDTO readerCatePageDTO) {
        PageHelper.startPage(readerCatePageDTO.getPage(), readerCatePageDTO.getPageSize());
        Page<ReaderCategory> readerCategories = readerCategoryMapper.page(readerCatePageDTO);
        return new PageResult(readerCategories.getTotal(), readerCategories.getResult());
    }

    /**
     * 根据id查询读者
     * @param id
     * @return
     */
    public ReaderCategory getById(Integer id) {
        return readerCategoryMapper.getById(id);
    }

    /**
     * 修改读者分类
     * @param readerCategoryDTO
     */
    public void update(ReaderCategoryDTO readerCategoryDTO) {
        ReaderCategory readerCategory = new ReaderCategory();
        BeanUtils.copyProperties(readerCategoryDTO, readerCategory);
        readerCategoryMapper.update(readerCategory);
    }

    /**
     * 根据id删除读者分类
     * @param id
     */
    public void deleteById(Integer id) {
        readerCategoryMapper.deleteById(id);
    }
}
