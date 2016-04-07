package edu.ucla.library.libservices.vgrload.db.mappers;

import edu.ucla.library.libservices.vgrload.beans.VgrPatron;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class VgrPatronMapper
  implements RowMapper
{
  public VgrPatronMapper()
  {
  }

  public Object mapRow( ResultSet rs, int i )
    throws SQLException
  {
    VgrPatron bean;
    
    bean = new VgrPatron();
    bean.setAddressLine1(rs.getString("VGR_Address_Line1"));
    if ( rs.wasNull() )
      bean.setAddressLine1("");
    bean.setAddressLine2(rs.getString("VGR_Address_Line2"));
    if ( rs.wasNull() )
      bean.setAddressLine2("");
    bean.setCity(rs.getString("VGR_Address_City"));
    if ( rs.wasNull() )
      bean.setCity("");
    bean.setCountry(rs.getString("VGR_Address_Country"));
    if ( rs.wasNull() )
      bean.setCountry("");
    bean.setEmailAddress(rs.getString("VGR_Email_Address"));
    bean.setEndDate(rs.getDate("appt_end_date"));
    bean.setFirstName(rs.getString("VGR_First_Name"));
    bean.setGender(rs.getString("VGR_Gender"));
    if ( rs.wasNull() )
      bean.setGender("");
    bean.setHomePhone(rs.getString("VGR_Home_Phone"));
    if ( rs.wasNull() )
      bean.setHomePhone("");
    bean.setIdentifier(rs.getString("Identifier"));
    if ( rs.wasNull() )
      bean.setIdentifier("");
    bean.setLastName(rs.getString("VGR_Last_Name"));
    bean.setMiddleName(rs.getString("VGR_Middle_Name"));
    if ( rs.wasNull() )
      bean.setMiddleName("");
    bean.setProvince(rs.getString("VGR_Address_Province"));
    if ( rs.wasNull() )
      bean.setProvince("");
    bean.setStartDate(rs.getDate("appt_begin_date"));
    bean.setState(rs.getString("VGR_Address_State"));
    if ( rs.wasNull() )
      bean.setState("");
    bean.setZip(rs.getString("VGR_Address_Zip"));
    if ( rs.wasNull() )
      bean.setZip("");
    
    return bean;
  }
}
