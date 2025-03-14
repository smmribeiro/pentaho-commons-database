/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.test.ui.database;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.pentaho.database.service.DatabaseConnectionService;
import org.pentaho.database.service.DatabaseDialectService;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.ui.database.DatabaseConnectionDialog;
import org.pentaho.ui.database.Messages;
//import org.pentaho.ui.database.XulAsyncDatabaseConnectionService;
import org.pentaho.ui.database.event.DataHandler;
import org.pentaho.ui.database.event.FragmentHandler;
import org.pentaho.ui.util.Launch;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.swing.SwingXulLoader;


public class SwingTest {

	DatabaseMeta database = null;

	public static void main(String[] args) {

		SwingTest harness = new SwingTest();

		try {
			InputStream in = DatabaseDialogHarness.class.getClassLoader()
					.getResourceAsStream("org/pentaho/ui/database/resources/databasedialog.xul"); //$NON-NLS-1$
			if (in == null) {
				System.out.println("Invalid Input"); //$NON-NLS-1$
				return;
			}

			SAXReader rdr = new SAXReader();
			final Document doc = rdr.read(in);

			harness.showDialog(doc);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showDialog(final Document doc) {

	  Messages messages = new Messages();
	  
		XulDomContainer container = null;
		try {
			container = new SwingXulLoader().loadXul(DatabaseConnectionDialog.DIALOG_DEFINITION_FILE, messages.getBundle());
			
			// core services
			DatabaseConnectionService connectionService = new DatabaseConnectionService();
			DatabaseDialectService dialectService = new DatabaseDialectService();
	    DatabaseTypeHelper databaseTypeHelper = new DatabaseTypeHelper(dialectService.getDatabaseTypes());
	    
	    // ui services
//	    XulAsyncDatabaseConnectionService service = new XulAsyncDatabaseConnectionService(connectionService);
	    
	    // ui controllers
	    DataHandler dataHandler = new DataHandler();
	    FragmentHandler fragmentHandler = new FragmentHandler();
	    
	    dataHandler.setDatabaseTypeHelper(databaseTypeHelper);
	    dataHandler.setMessages(messages);
//	    dataHandler.setAsyncDatabaseConnectionService(service);
	    dataHandler.setLaunch(new Launch());
	    dataHandler.setFragmentHandler(fragmentHandler);
	    fragmentHandler.setDatabaseTypeHelper(databaseTypeHelper);
	    
	    container.addEventHandler(fragmentHandler);
	    container.addEventHandler(dataHandler);
	 
			
			if (database != null) {
				container.getEventHandler("dataHandler").setData(database); //$NON-NLS-1$
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		XulDialog dialog = (XulDialog) container.getDocumentRoot().getRootElement();
		container.initialize();
		dialog.show();
		try{
			@SuppressWarnings("unused")
      Object data = container.getEventHandler("dataHandler").getData(); //$NON-NLS-1$
			int i=0;
			i++;
		} catch(XulException e){
			System.out.println("Error getting data"); //$NON-NLS-1$
		}
	}

}
