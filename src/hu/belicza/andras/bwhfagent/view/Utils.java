package hu.belicza.andras.bwhfagent.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 * Utility methods related to view and GUI.
 * 
 * @author Belicza Andras
 */
public class Utils {
	
	/** Stores the reference of the main frame. */
	private static MainFrame mainFrame;
	
	/**
	 * Sets the <code>MainFrame</code> reference.
	 * @param mainFrame reference to the main frame to be set 
	 */
	public static void setMainFrame( final MainFrame mainFrame ) {
		Utils.mainFrame = mainFrame;
	}
	
	/**
	 * Returns the reference of the main frame.
	 * @return the reference of the main frame
	 */
	public static MainFrame getMainFrame() {
		return mainFrame;
	}
	
	/**
	 * Wraps a component into a {@link JPanel} with a {@link FlowLayout}.
	 * @param component component to be wrapped
	 * @return the panel wrapping the component
	 */
	public static JPanel wrapInPanel( final JComponent component ) {
		final JPanel panel = new JPanel();
		panel.add( component );
		return panel;
	}
	
	/**
	 * Creates and returns a button with a registered action listener which opens a file chooser
	 * with the specified file selection mode, and on approved returned option stores the selected file
	 * into the target text field. 
	 * @param parent            component to be used as parent for the file chooser dialog
	 * @param targetTextField   text field to be updated if file/folder is selected
	 * @param fileSelectionMode the type of files to be displayed
	 * 							<ul>
	 * 								<li>JFileChooser.FILES_ONLY
	 * 								<li>JFileChooser.DIRECTORIES_ONLY
	 * 								<li>JFileChooser.FILES_AND_DIRECTORIES
	 * 							</ul>
	 * @param choosableFileFilter file filter to add as a choosable file filter
	 * @return a button handling the file chooser
	 */
	public static JButton createFileChooserButton( final Component parent, final JTextField targetTextField, final int fileSelectionMode, final FileFilter choosableFileFilter ) {
		final JButton chooseButton = new JButton( "Choose..." );
		
		chooseButton.addActionListener( new ActionListener() {
			public void actionPerformed( final ActionEvent event ) {
				final JFileChooser fileChooser = new JFileChooser( targetTextField.getText() );
				
				if ( choosableFileFilter != null )
					fileChooser.addChoosableFileFilter( choosableFileFilter ); 
				
				fileChooser.setFileSelectionMode( fileSelectionMode );
				if ( fileChooser.showOpenDialog( parent ) == JFileChooser.APPROVE_OPTION )
					targetTextField.setText( fileChooser.getSelectedFile().getAbsolutePath() );
			}
		} );
		
		return chooseButton;
	}
	
	/**
	 * Opens the web page specified by the url in the system's default browser.
	 * @param url url to be opened
	 */
	public static void showURLInBrowser( final String url ) {
		try {
			final String osName = System.getProperty( "os.name" );
			
			String[] cmdArray = null;
			if ( osName != null && osName.startsWith( "Windows" ) )
				cmdArray = new String[] { "rundll32", "url.dll,FileProtocolHandler", url };
			else
				cmdArray = new String[] { "netscape", "-remote", "openURL", url };
			
			Runtime.getRuntime().exec( cmdArray );
		} catch ( final IOException ie ) {
		}
	}
	
}
