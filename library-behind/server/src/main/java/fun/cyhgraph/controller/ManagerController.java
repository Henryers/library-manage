package fun.cyhgraph.controller;

import fun.cyhgraph.constant.JwtClaimsConstant;
import fun.cyhgraph.dto.ManagerDTO;
import fun.cyhgraph.dto.ManagerLoginDTO;
import fun.cyhgraph.entity.Manager;
import fun.cyhgraph.properties.JwtProperties;
import fun.cyhgraph.result.Result;
import fun.cyhgraph.service.ManagerService;
import fun.cyhgraph.utils.JwtUtil;
import fun.cyhgraph.vo.ManagerLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 根据id获取管理员信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Manager> getUserById(@PathVariable Integer id){
        Manager manager = managerService.getManagerById(id);
        return Result.success(manager);
    }

    /**
     * 管理员登录
     * @param managerLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<ManagerLoginVO> login(@RequestBody ManagerLoginDTO managerLoginDTO){
        log.info("用户传过来的登录信息DTO:{}", managerLoginDTO);
        Manager manager = managerService.login(managerLoginDTO);
        // 上面的没抛异常，正常来到这里，说明登录成功
        // claims就是用户数据payload部分
        Map<String, Object> claims = new HashMap<>(); // jsonwebtoken包底层就是Map<String, Object>格式，不能修改！
        claims.put(JwtClaimsConstant.MANAGER_ID, manager.getId());
        // 需要加个token给他，再返回响应
        String token = JwtUtil.createJWT(
                jwtProperties.getManagerSecretKey(),
                jwtProperties.getManagerTtl(),
                claims);
        ManagerLoginVO managerLoginVO = ManagerLoginVO.builder()
                .id(manager.getId())
                .name(manager.getName())
                .token(token)
                .build();
        return Result.success(managerLoginVO);
    }

    /**
     * 管理员注册（其实就是新增操作而已，和token什么的无关！）
     * @param managerLoginDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody ManagerLoginDTO managerLoginDTO){
        log.info("用户传过来的注册信息(和登录格式一样的DTO):{}", managerLoginDTO);
        managerService.register(managerLoginDTO);
        return Result.success();
    }

    /**
     * 修改个人信息
     * @param managerDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody ManagerDTO managerDTO){
        log.info("修改后的用户信息：{}", managerDTO);
        managerService.update(managerDTO);
        return Result.success();
    }

}
