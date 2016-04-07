package edu.ucla.library.libservices.vgrload.beans;

import java.util.Date;

public class VgrPatron
{
  private String identifier;
  private String firstName;
  private String middleName;
  private String lastName;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String zip;
  private String country;
  private String province;
  private String homePhone;
  private String emailAddress;
  private String gender;
  private Date startDate;
  private Date endDate;

  public VgrPatron()
  {
  }

  public void setIdentifier( String identifier )
  {
    this.identifier = identifier;
  }

  public String getIdentifier()
  {
    return identifier;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setAddressLine1( String addressLine1 )
  {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine1()
  {
    return addressLine1;
  }

  public void setAddressLine2( String addressLine2 )
  {
    this.addressLine2 = addressLine2;
  }

  public String getAddressLine2()
  {
    return addressLine2;
  }

  public void setCity( String city )
  {
    this.city = city;
  }

  public String getCity()
  {
    return city;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  public String getState()
  {
    return state;
  }

  public void setZip( String zip )
  {
    this.zip = zip;
  }

  public String getZip()
  {
    return zip;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getCountry()
  {
    return country;
  }

  public void setProvince( String province )
  {
    this.province = province;
  }

  public String getProvince()
  {
    return province;
  }

  public void setHomePhone( String homePhone )
  {
    this.homePhone = homePhone;
  }

  public String getHomePhone()
  {
    return homePhone;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setGender( String gender )
  {
    this.gender = gender;
  }

  public String getGender()
  {
    return gender;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }
}
