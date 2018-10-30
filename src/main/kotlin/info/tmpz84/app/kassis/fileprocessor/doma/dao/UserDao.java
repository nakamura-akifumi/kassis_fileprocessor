package info.tmpz84.app.kassis.fileprocessor.doma.dao;

import info.tmpz84.app.kassis.fileprocessor.doma.entity.UserEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@ConfigAutowireable
@Dao
public interface UserDao {
    @Select
    List<UserEntity> selectAll();
    @Insert
    int insert(UserEntity user);
}