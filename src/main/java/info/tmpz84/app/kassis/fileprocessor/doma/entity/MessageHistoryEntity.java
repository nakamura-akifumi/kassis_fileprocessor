package info.tmpz84.app.kassis.fileprocessor.doma.entity;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

import java.sql.Timestamp;

@Entity(naming = NamingType.SNAKE_UPPER_CASE)
@Table(name = "message_histories")
public class MessageHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String msgid;
    public String row;
    public String message_type;
    public String status;
    public String note;
    public String note2;

    public Timestamp created_at;
    public Timestamp updated_at;
}
