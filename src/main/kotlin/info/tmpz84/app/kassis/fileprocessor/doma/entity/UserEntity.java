package info.tmpz84.app.kassis.fileprocessor.doma.entity;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

import java.sql.Timestamp;

@Entity(naming = NamingType.SNAKE_UPPER_CASE)
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String username;

    public String email;

    public String personid;
    public String cardid;
    public String full_name;
    public String full_name_transcription;
    public String note;

    public Timestamp created_at;
    public Timestamp updated_at;
}