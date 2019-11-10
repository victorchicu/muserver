package database.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public interface DatabaseDialect extends AutoCloseable {
 Statement createStatement() throws SQLException;
}
