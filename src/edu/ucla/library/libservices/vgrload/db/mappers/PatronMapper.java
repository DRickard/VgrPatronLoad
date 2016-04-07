package edu.ucla.library.libservices.vgrload.db.mappers;

import edu.ucla.library.libservices.vgrload.beans.Patron;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PatronMapper
  implements RowMapper
{
  public PatronMapper()
  {
  }

  public Object mapRow( ResultSet resultSet, int i )
    throws SQLException
  {
    Patron bean;
    
    bean = new Patron();
    bean.setFirstName(resultSet.getString("FIRST_NAME"));
    bean.setLastName(resultSet.getString("LAST_NAME"));
    bean.setMiddleName(resultSet.getString("MIDDLE_NAME"));
    if ( resultSet.wasNull() )
      bean.setMiddleName("");
    bean.setPatronID(resultSet.getInt("PATRON_ID"));
    
    return bean;
  }
}
