package info.tmpz84.app.kassis.fileprocessor.doma.dao;

import info.tmpz84.app.kassis.fileprocessor.ConfigAdapter;
import info.tmpz84.app.kassis.fileprocessor.doma.entity.MessageHistoryEntity;
import info.tmpz84.app.kassis.fileprocessor.doma.entity.UserEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@ConfigAutowireable
@Dao(config = ConfigAdapter.class)
public interface MessageHistoryDao {
    @Insert
    int insert(MessageHistoryEntity message);
}