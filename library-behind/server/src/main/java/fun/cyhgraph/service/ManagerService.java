package fun.cyhgraph.service;

import fun.cyhgraph.dto.ManagerDTO;
import fun.cyhgraph.dto.ManagerLoginDTO;
import fun.cyhgraph.entity.Manager;

public interface ManagerService {
    Manager getManagerById(Integer id);

    Manager login(ManagerLoginDTO managerLoginDTO);

    void register(ManagerLoginDTO managerLoginDTO);

    void update(ManagerDTO managerDTO);
}
