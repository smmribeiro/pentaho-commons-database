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


package org.pentaho.ui.database.services;

import java.util.List;

import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.ui.xul.XulServiceCallback;

public interface IXulAsyncDatabaseConnectionService {
  void checkParameters(IDatabaseConnection connection, XulServiceCallback<List<String>> callback);
  void testConnection(IDatabaseConnection connection, XulServiceCallback<String> callback);
  void getPoolingParameters(XulServiceCallback<DatabaseConnectionPoolParameter[]> callback);
  void createDatabaseConnection(String driver, String url, XulServiceCallback<IDatabaseConnection> callback );
 }
