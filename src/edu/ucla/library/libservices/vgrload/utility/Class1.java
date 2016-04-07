package edu.ucla.library.libservices.vgrload.utility;

import edu.ucla.library.libservices.vgrload.beans.Patron;

import edu.ucla.library.libservices.vgrload.db.mappers.PatronMapper;

import edu.ucla.library.libservices.voyager.api.core.ApiRequest;
import edu.ucla.library.libservices.voyager.api.core.ApiResponse;
import edu.ucla.library.libservices.voyager.api.core.ApiServer;

import edu.ucla.library.libservices.voyager.api.core.VoyagerException;
import edu.ucla.library.libservices.voyager.reserves.utility.CodeIdPair;
import edu.ucla.library.libservices.voyager.reserves.utility.VoyagerConnectionFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Class1
{
  private static DriverManagerDataSource voyager;
  private static final String VOYAGER_COUNT_QUERY = 
    "SELECT count(patron_id) FROM ucladb.patron WHERE institution_id = ?";
  private static final String VOYAGER_PATRON_QUERY = 
    "SELECT * FROM ucladb.patron WHERE institution_id = ?";
  private static Properties props;
  private static ApiServer server;

  public Class1()
  {
  }

  public static void main( String[] args )
  {
    BufferedReader reader;
    BufferedWriter writer;
    String line;

    try
    {
      loadProperties( args[ 0 ] );
      makeDbConnection();
      initializeConnection();

      reader = 
          new BufferedReader( new FileReader( new File( "C:\\Temp\\patrons\\lawGrads2015.txt" ) ) );
      writer = 
          new BufferedWriter( new FileWriter( new File( "C:\\Temp\\patrons\\lawGradsResults2015.txt" ) ) );
      line = null;

      while ( ( line = reader.readLine() ) != null )
      {
        String[] tokens;

        tokens = line.split( "," );

        int count;
        count = 
            new JdbcTemplate( voyager ).queryForInt( VOYAGER_COUNT_QUERY, 
                                                     new Object[]
              { tokens[ 0 ].trim() } );


        if ( count != 0 )
        {
          List<Patron> patrons;

          patrons = 
              new JdbcTemplate( voyager ).query( VOYAGER_PATRON_QUERY, 
                                                 new Object[]
                { tokens[ 0 ].trim() }, new PatronMapper() );
          for ( Patron thePatron: patrons )
          {
            CodeIdPair results;
            writer.write("updating " + tokens[0] );
            results = updatePatron(thePatron, tokens[0]);
            writer.write("\t\tresult code = " + results.getReturnCode());
            writer.newLine();
          }
        }
        else
        {
          writer.write( "patron " + tokens[ 0 ] + //"/" + tokens[ 1 ] + 
                              " is not in db" );
          writer.newLine();
        }
      }
      reader.close();
      writer.flush();
      writer.close();
    }
    catch ( FileNotFoundException fnfe )
    {
      fnfe.printStackTrace();
    }
    catch ( IOException ioe )
    {
      ioe.printStackTrace();
    }
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

  private static void makeDbConnection()
  {
    voyager = new DriverManagerDataSource();

    voyager.setDriverClassName( "oracle.jdbc.OracleDriver" );
    voyager.setUrl( "jdbc:oracle:thin:@ils-db-prod.library.ucla.edu:1521:VGER" );
    voyager.setUsername( "VGER_SUPPORT" );
    voyager.setPassword( "VGER_SUPPORT_PWD" );
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

  private static CodeIdPair updatePatron(Patron thePatron, String uID)
  {
    ApiRequest request;
    ApiResponse response;
    CodeIdPair results;

    results = new CodeIdPair();

    request = new ApiRequest( props.getProperty( "voyager.appcode" ), "UPDPATRON" );
    request.addParameter( "PI", thePatron.getPatronID() );
    request.addParameter( "TC", "1" );
    request.addParameter( "LN", thePatron.getLastName() );
    request.addParameter( "FN", thePatron.getFirstName() );
    request.addParameter( "MN", thePatron.getMiddleName() );
    request.addParameter( "TITL", "" );
    request.addParameter( "SN", "" );
    request.addParameter( "IN", uID );
    request.addParameter( "ED", "2015-12-18 23:59:59" );
    request.addParameter( "PD", "20361231" );
    request.addParameter( "UBID", "1@".concat( props.getProperty( "voyager.dbkey" ) ) );

    server.send( request.toString() );
    response = new ApiResponse( server.receive() );

    results.setReturnCode( response.getReturnCode() );

    return results;
  }
}
