package fun.cyhgraph.mapper;

import fun.cyhgraph.entity.Manager;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ManagerMapper {

    @Select("select * from manager where id = #{id}")
    Manager getById(Integer id);

    @Select("select * from manager where name = #{name}")
    Manager getByName(String name);

    @Insert("insert into manager (name, password) values (#{name}, #{password})")
    void addManager(Manager manager);

    @Update("update manager set name = #{name}, password = #{password} where id = #{id}")
    void update(Manager manager);
}
