package info.tmpz84.app.kassis.fileprocessor.doma.dao;

import info.tmpz84.app.kassis.fileprocessor.ConfigAdapter;
import info.tmpz84.app.kassis.fileprocessor.doma.entity.MessageAdapterEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao(config = ConfigAdapter.class)
public interface MessageAdapterDao {
    @Select(ensureResult = true)
    MessageAdapterEntity selectByMsgid(String msgid);

    @Update
    int update(MessageAdapterEntity entity);
}