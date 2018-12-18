package info.tmpz84.app.kassis.fileprocessor.doma.entity;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity(naming = NamingType.SNAKE_UPPER_CASE)
@Table(name = "kassis_file_attachments")
public class KassisFileAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String msgid;
    public String fileid;
    public String filename;
    public Long byte_size;
    public String checksum;

    public Timestamp created_at;
    public Timestamp updated_at;
}
