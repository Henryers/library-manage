package fun.cyhgraph.service.serviceImpl;

import fun.cyhgraph.constant.MessageConstant;
import fun.cyhgraph.context.BaseContext;
import fun.cyhgraph.dto.ManagerDTO;
import fun.cyhgraph.dto.ManagerLoginDTO;
import fun.cyhgraph.entity.Manager;
import fun.cyhgraph.exception.PasswordErrorException;
import fun.cyhgraph.exception.ManagerNotFoundException;
import fun.cyhgraph.mapper.ManagerMapper;
import fun.cyhgraph.service.ManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    /**
     * 根据id获取用户信息
     * @return
     */
    public Manager getManagerById(Integer id) {
        Manager manager = managerMapper.getById(id);
        return manager;
    }

    /**
     * 用户登录
     * @param managerLoginDTO
     * @return
     */
    public Manager login(ManagerLoginDTO managerLoginDTO) {
        String name = managerLoginDTO.getName();
        String password = managerLoginDTO.getPassword();
        // 先查数据库，看是否存在该账号
        Manager manager = managerMapper.getByName(name);
        if (manager == null){
            throw new ManagerNotFoundException(MessageConstant.MANAGER_NOT_FOUND);
        }
        // 再将前端传过来的密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 和之前存进数据库的加密的密码进行比对，看看是否一样，不一样要抛异常
        if (!password.equals(manager.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        return manager;
    }

    /**
     * 注册/新增用户
     */
    public void register(ManagerLoginDTO managerLoginDTO) {
        // 先对用户的密码进行MD5加密，再存到数据库中
        String password = managerLoginDTO.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        managerLoginDTO.setPassword(password);

        Manager manager = new Manager();
        // 将userLoginDTO的属性拷贝到user中
        BeanUtils.copyProperties(managerLoginDTO, manager);
        managerMapper.addManager(manager);
    }

    /**
     * 修改管理员信息
     * @param managerDTO
     */
    public void update(ManagerDTO managerDTO) {
        String oldPwd = managerDTO.getOldPwd();
        // 将前端传过来的旧密码进行MD5加密
        oldPwd = DigestUtils.md5DigestAsHex(oldPwd.getBytes());
        // 根据id查询当前账号信息
        Integer id = BaseContext.getCurrentId();
        Manager manager = managerMapper.getById(id);
        // 和之前存进数据库的加密的密码进行比对，看看是否一样，不一样要抛异常
        if (!oldPwd.equals(manager.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        // 旧密码正确，将用户名修改，新密码加密后，进行更新
        manager.setName(managerDTO.getName());
        String newPwd = managerDTO.getNewPwd();
        String password = DigestUtils.md5DigestAsHex(newPwd.getBytes());
        manager.setPassword(password);
        managerMapper.update(manager);
    }

}
