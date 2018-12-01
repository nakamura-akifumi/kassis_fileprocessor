package info.tmpz84.app.kassis.fileprocessor.doma.entity;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

import java.sql.Timestamp;

@Entity(naming = NamingType.SNAKE_UPPER_CASE)
@Table(name = "message_adapters")
public class MessageAdapterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String msgid;
    public String title;
    public String status;
    public String state;
    public String message_type;
    public Integer created_by;

    public Timestamp created_at;
    public Timestamp updated_at;
}
