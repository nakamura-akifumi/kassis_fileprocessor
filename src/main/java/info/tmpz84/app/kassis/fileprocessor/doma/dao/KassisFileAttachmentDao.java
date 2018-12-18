package info.tmpz84.app.kassis.fileprocessor.doma.dao;

import info.tmpz84.app.kassis.fileprocessor.ConfigAdapter;
import info.tmpz84.app.kassis.fileprocessor.doma.entity.KassisFileAttachmentEntity;
import info.tmpz84.app.kassis.fileprocessor.doma.entity.MessageHistoryEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao(config = ConfigAdapter.class)
public interface KassisFileAttachmentDao {
    @Insert
    int insert(KassisFileAttachmentEntity entity);
}