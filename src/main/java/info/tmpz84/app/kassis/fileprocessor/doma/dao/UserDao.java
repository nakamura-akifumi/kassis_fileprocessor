package info.tmpz84.app.kassis.fileprocessor.doma.dao;

import info.tmpz84.app.kassis.fileprocessor.ConfigAdapter;
import info.tmpz84.app.kassis.fileprocessor.doma.entity.UserEntity;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@ConfigAutowireable
@Dao(config = ConfigAdapter.class)
public interface UserDao {
    @Select
    List<UserEntity> selectAll();
    @Select
    UserEntity selectById(Integer id);
    @Select
    int selectCount();
    @Insert
    int insert(UserEntity user);
    @Script
    void deleteWithoutAdmin();
}