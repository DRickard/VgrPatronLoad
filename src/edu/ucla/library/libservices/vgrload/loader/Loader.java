package edu.ucla.library.libservices.vgrload.loader;

import edu.ucla.library.libservices.vgrload.api.handlers.PatronHandler;
import edu.ucla.library.libservices.vgrload.beans.VgrPatron;
import edu.ucla.library.libservices.vgrload.db.mappers.VgrPatronMapper;
import edu.ucla.library.libservices.voyager.api.core.ApiServer;

import edu.ucla.library.libservices.voyager.api.core.VoyagerException;
import edu.ucla.library.libservices.voyager.reserves.utility.CodeIdPair;
import edu.ucla.library.libservices.voyager.reserves.utility.VoyagerConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.List;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Loader
{
  private static final String VGR_QUERY = 
    "SELECT * FROM vgr.dbo.vwLibraryList WHERE appt_begin_date <= GETDATE() AND" 
    + " aid not in (637,640,641,642,654,679,683,691,695,714,756,769,783,843,856)";
  private static final String VOYAGER_COUNT_QUERY = 
    "SELECT count(patron_id) FROM ucladb.patron WHERE institution_id = ?";
  private static final String VOYAGER_ID_QUERY = 
  "SELECT patron_id FROM ucladb.patron WHERE institution_id = ?";

  private static DriverManagerDataSource voyager;
  private static DriverManagerDataSource vgr;
  private static ApiServer server;
  private static Properties props;
  private static String machine = null;
  private static String version = null;
  private static String dbKey = "";
  private static List<VgrPatron> patrons;
  private static int port = 0;

  public Loader()
  {
  }

  public static void main( String[] args )
  {
    long start = System.currentTimeMillis();
    loadProperties( args[ 0 ] );
    makeDbConnections();
    initializeVariables();
    initializeConnection();
    getPatrons();
    loadOrUpdatePatrons();
    long end = System.currentTimeMillis();
    System.out.println( "run time = " + ( end - start ) / 1000 );
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

  private static void initializeVariables()
  {
    machine = props.getProperty( "voyager.server" );
    version = props.getProperty( "voyager.version" );
    port = Integer.parseInt( props.getProperty( "voyager.circsvr" ) );
    dbKey = props.getProperty( "voyager.dbkey" );
  }

  private static void initializeConnection()
  {
    try
    {
      server = 
          VoyagerConnectionFactory.getConnection( props, props.getProperty( "voyager.appcode" ) );
    }
    catch ( VoyagerException ve )
    {
      System.err.println( "Program failed to establish Voyager connection: " + 
                          ve.getMessage() );
      ve.printResponse();
      System.exit( -2 );
    }
  }

  private static void getPatrons()
  {
    patrons = 
        new JdbcTemplate( vgr ).query( VGR_QUERY, new VgrPatronMapper() );
  }

  private static void loadPatron( VgrPatron thePatron )
  {
    CodeIdPair results;
    PatronHandler handler;

    handler = new PatronHandler();
    handler.setThePatron( thePatron );
    results = 
        handler.addPatron( server, props.getProperty( "voyager.appcode" ) );

    if ( results.getReturnCode() == 0 )
    {
      System.out.println( "patron " + thePatron.getIdentifier().trim() + " loaded, new ID = " + 
                          results.getNewID() );
      handler.addPatronStat( server, 
                             props.getProperty( "voyager.appcode" ), 
                             String.valueOf( results.getNewID() ), 
                             ( thePatron.getGender().trim().equalsIgnoreCase( "M" )? 
                               "10" : "8" ), 
                             props.getProperty( "voyager.dbkey" ) );
    }
    else
    {
      System.err.println( "patron " + thePatron.getIdentifier().trim() + 
                          " failed to load" );
    }
  }

  private static void makeDbConnections()
  {
    voyager = new DriverManagerDataSource();

    voyager.setDriverClassName( props.getProperty( "db.driver.voyager" ) );
    voyager.setUrl( props.getProperty( "db.url.voyager" ) );
    voyager.setUsername( props.getProperty( "db.user.voyager" ) );
    voyager.setPassword( props.getProperty( "db.password.voyager" ) );

    vgr = new DriverManagerDataSource();

    vgr.setDriverClassName( props.getProperty( "db.driver.vgr" ) );
    vgr.setUrl( props.getProperty( "db.url.vgr" ) );
    vgr.setUsername( props.getProperty( "db.user.vgr" ) );
    vgr.setPassword( props.getProperty( "db.password.vgr" ) );
  }

  private static void loadOrUpdatePatrons()
  {
    for ( VgrPatron thePatron: patrons )
    {
      int count;
      count = 
          new JdbcTemplate( voyager ).queryForInt( VOYAGER_COUNT_QUERY, new Object[]
            { thePatron.getIdentifier().trim() } );

      if ( count != 0 )
      {
        System.out.println("patron " + thePatron.getIdentifier().trim() + " in system");
        int id;
        id = 
            new JdbcTemplate( voyager ).queryForInt( VOYAGER_ID_QUERY, new Object[]
              { thePatron.getIdentifier().trim() } );
        updatePatron( thePatron, id );
      }
      else
      {
        System.out.println("patron " + thePatron.getIdentifier() + " not in system");
        loadPatron( thePatron );
      }
    }
  }

  private static void updatePatron( VgrPatron thePatron, int id )
  {
    CodeIdPair results;
    PatronHandler handler;

    handler = new PatronHandler();
    handler.setThePatron( thePatron );

    results = 
        handler.updatePatron( server, props.getProperty( "voyager.appcode" ), 
                              id, props.getProperty( "voyager.dbkey" ) );
    results = 
        handler.updatePatronAddress( server, props.getProperty( "voyager.appcode" ), 
                                     id, 
                                     props.getProperty( "voyager.dbkey" ) );
    results = 
        handler.updatePatronEmail( server, props.getProperty( "voyager.appcode" ), 
                                   id, 
                                   props.getProperty( "voyager.dbkey" ) );
  }
}
