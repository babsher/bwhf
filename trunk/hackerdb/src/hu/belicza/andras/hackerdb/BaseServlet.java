package hu.belicza.andras.hackerdb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Base functionalities for the servlets.
 * 
 * @author Andras Belicza
 */
public class BaseServlet extends HttpServlet {
	
	/** URL of the database. */
	private static final String DATABASE_URL = "jdbc:hsqldb:hsql://localhost/hackers";
	
	/** Data source to provide pooled connections to the hacker database. */
	protected final BasicDataSource dataSource = new BasicDataSource();
	
	@Override
	public void init( final ServletConfig config ) throws ServletException {
		dataSource.setDriverClassName( "org.hsqldb.jdbcDriver" );
		dataSource.setUsername( "sa" );
		dataSource.setPassword( "" );
		dataSource.setUrl( DATABASE_URL );
		
		super.init( config );
	}
	
	/**
	 * Sends back a message as plain text.
	 * @param message  message to be sent
	 * @param response response to be used
	 */
	protected static void sendBackPlainMessage( final String message, final HttpServletResponse response ) {
		response.setContentType( "text/plain" );
		
		PrintWriter output = null;
		try {
			output = response.getWriter();
			output.println( message );
			output.flush();
		} catch ( final IOException ie ) {
			ie.printStackTrace();
		}
		finally {
			if ( output != null )
				 output.close();
		}
	}
	
	/**
	 * Sends back an error message to the client indicating a bad request.
	 * @param response response to be used
	 */
	protected static void sendBackErrorMessage( final HttpServletResponse response ) {
		sendBackErrorMessage( response, "Bad request!" );
	}
	
	/**
	 * Sends back an error message to the client.
	 * @param response response to be used
	 * @param message  message to be sent back
	 */
	protected static void sendBackErrorMessage( final HttpServletResponse response, final String message ) {
		response.setContentType( "text/html" );
		
		PrintWriter output = null;
		try {
			output = response.getWriter();
			output.println( message );
			output.flush();
		} catch ( final IOException ie ) {
			ie.printStackTrace();
		}
		finally {
			if ( output != null )
				 output.close();
		}
	}
	
	/**
	 * Configures the response for no caching.
	 * @param response response to be configured
	 */
	protected static void setNoCache( final HttpServletResponse response ) {
		response.setHeader( "Cache-Control", "no-cache" ); // For HTTP 1.1
		response.setHeader( "Pragma"       , "no-cache" ); // For HTTP 1.0
		response.setDateHeader( "Expires", -0 );           // For proxies
	}
	
	/**
	 * Encodes an input string for HTML rendering.
	 * @param input input string to be encoded
	 * @return an encoded string for HTML rendering
	 */
	protected static String encodeHtmlString( final String input ) {
		final StringBuilder encodedHtml = new StringBuilder();
		final int length = input.length();
		
		for ( int i = 0; i < length; i++ ) {
			final char ch = input.charAt( i );
			
			if ( ch >= 'a' && ch <='z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9' )
				encodedHtml.append( ch ); // safe
			else if ( Character.isISOControl( ch ) )
				; // Not safe, do not include it in the output
			else
				encodedHtml.append( "&#" ).append( (int) ch ).append( ';' );
		}
		
		return encodedHtml.toString();
	}
	
	/**
	 * Returns the value of a request parameter.
	 * @param request   http request object
	 * @param paramName name of the parameter whose value is to be returned
	 * @return the value of a request parameter or an empty string if the parameter does not have a value
	 */
	protected static String getStringParamValue( final HttpServletRequest request, final String paramName ) {
		final String paramValue = request.getParameter( paramName );
		return paramValue == null ? "" : paramValue;
	}
	
	/**
	 * Returns the integer value parsed from a request parameter.
	 * @param request      http request object
	 * @param paramName    name of the parameter whose value is to be returned
	 * @param defaultValue default value to be returned if the param is missing or invalid (not an int)
	 * @return the value of a request parameter or <code>defaultValue</code> if the parameter does not have a value or it does not contain a valid integer
	 */
	protected static int getIntParamValue( final HttpServletRequest request, final String paramName, final int defaultValue ) {
		try {
			return Integer.parseInt( request.getParameter( paramName ) );
		}
		catch ( final Exception e ) {
			return defaultValue;
		}
	}
	
	/**
	 * Returns the integer value parsed from a request parameter.
	 * @param request   http request object
	 * @param paramName name of the parameter whose value is to be returned
	 * @return the value of a request parameter 
	 */
	protected static Integer getIntegerParamValue( final HttpServletRequest request, final String paramName ) {
		try {
			return Integer.valueOf( request.getParameter( paramName ) );
		}
		catch ( final Exception e ) {
			return null;
		}
	}
	
}