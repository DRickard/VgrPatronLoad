package edu.ucla.library.libservices.vgrload.beans;

public class Patron
{
  private int patronID;
  private String firstName;
  private String middleName;
  private String lastName;

  public Patron()
  {
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

  public void setPatronID( int patronID )
  {
    this.patronID = patronID;
  }

  public int getPatronID()
  {
    return patronID;
  }
}
