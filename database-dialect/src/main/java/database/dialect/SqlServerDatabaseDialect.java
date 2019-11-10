package database.dialect;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;

//https://docs.microsoft.com/en-us/sql/sql-server/maximum-capacity-specifications-for-sql-server?view=sql-server-2017

public class SqlServerDatabaseDialect implements DatabaseDialect {
 private final static Logger logger = LogManager.getLogger(SqlServerDatabaseDialect.class);
 private final static String DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
 private final static Integer MAX_CONNECTIONS = 1000;
 private final BasicDataSource dataSource = new BasicDataSource();

 public SqlServerDatabaseDialect(String connectionString) {
  dataSource.setMaxTotal(MAX_CONNECTIONS);
  dataSource.setDriverClassName(DRIVER_CLASS_NAME);
  dataSource.setUrl(connectionString);
 }

 @Override
 public Statement createStatement() throws SQLException {
  return dataSource.getConnection().createStatement();
 }

 @Override
 public void close() throws Exception {
  dataSource.close();
 }
}
