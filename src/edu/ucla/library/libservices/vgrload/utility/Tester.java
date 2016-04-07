package edu.ucla.library.libservices.vgrload.utility;

import edu.ucla.library.libservices.vgrload.beans.VgrPatron;
import edu.ucla.library.libservices.vgrload.db.mappers.VgrPatronMapper;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Tester
{
  private static final String VGR_QUERY = 
    "SELECT * FROM vgr.dbo.vwLibraryList WHERE appt_begin_date <= GETDATE() AND aid not in " + 
    "(637,640,641,642,654,679,683,691,695,714,756,769,783,843,856)";
  private static DriverManagerDataSource vgr;
  private static Properties props;
  private static List<VgrPatron> patrons;

  public Tester()
  {
  }

  public static void main( String[] args )
  {
    loadProperties( args[ 0 ] );
    makeDbConnections();
    getPatrons();
    for ( VgrPatron thePatron: patrons )
    {
      System.out.println( "patron " + thePatron.getIdentifier() + 
                        " expires " + thePatron.getEndDate().toString().concat(" 23:59:59") );
    }
  }

  private static void getPatrons()
  {
    patrons = 
        new JdbcTemplate( vgr ).query( VGR_QUERY, new VgrPatronMapper() );
  }

  private static void loadProperties( String fileName )
  {
    props = new Properties();
    try
    {
      props.load( new FileInputStream( fileName ) );
    }
    catch ( IOException ioe )
    {
      System.err.println( "Program failed to load properties file: " + 
                          ioe.getMessage() );
      System.exit( -1 );
    }
  }

  private static void makeDbConnections()
  {
    vgr = new DriverManagerDataSource();

    vgr.setDriverClassName( props.getProperty( "db.driver.vgr" ) );
    vgr.setUrl( props.getProperty( "db.url.vgr" ) );
    vgr.setUsername( props.getProperty( "db.user.vgr" ) );
    vgr.setPassword( props.getProperty( "db.password.vgr" ) );
  }
}
