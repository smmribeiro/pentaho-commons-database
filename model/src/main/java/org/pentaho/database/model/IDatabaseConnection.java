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


package org.pentaho.database.model;

import com.google.gwt.core.shared.GwtIncompatible;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IDatabaseConnection extends Serializable {

  void setId( String id );

  String getId();

  void setAccessType( DatabaseAccessType accessType );

  DatabaseAccessType getAccessType();

  void setDatabaseType( IDatabaseType driver );

  IDatabaseType getDatabaseType();

  void setExtraOptions( Map<String, String> extraOptions );

  Map<String, String> getExtraOptions();

  void setExtraOptionsOrder( Map<String, String> extraOptionsOrder );

  Map<String, String> getExtraOptionsOrder();

  void setName( String name );

  String getName();

  void setHostname( String hostname );

  String getHostname();

  void setDatabaseName( String databaseName );

  String getDatabaseName();

  void setDatabasePort( String databasePort );

  String getDatabasePort();

  void setUsername( String username );

  String getUsername();

  void setPassword( String password );

  String getPassword();

  void setStreamingResults( boolean streamingResults );

  boolean isStreamingResults();

  void setDataTablespace( String dataTablespace );

  String getDataTablespace();

  void setIndexTablespace( String indexTablespace );

  String getIndexTablespace();

  void setSQLServerInstance( String sqlServerInstance );

  String getSQLServerInstance();

  void setUsingDoubleDecimalAsSchemaTableSeparator( boolean usingDoubleDecimalAsSchemaTableSeparator );

  boolean isUsingDoubleDecimalAsSchemaTableSeparator();

  void setInformixServername( String informixServername );

  String getInformixServername();

  void addExtraOption( String databaseTypeCode, String option, String value );

  void setAttributes( Map<String, String> attributes );

  Map<String, String> getAttributes();

  void setChanged( boolean changed );

  boolean getChanged();

  void setQuoteAllFields( boolean quoteAllFields );

  boolean isQuoteAllFields();

  // advanced option (convert to enum with upper, lower, none?)
  void setForcingIdentifiersToLowerCase( boolean forcingIdentifiersToLowerCase );

  boolean isForcingIdentifiersToLowerCase();

  void setForcingIdentifiersToUpperCase( boolean forcingIdentifiersToUpperCase );

  boolean isForcingIdentifiersToUpperCase();

  void setConnectSql( String sql );

  String getConnectSql();

  void setUsingConnectionPool( boolean usingConnectionPool );

  boolean isUsingConnectionPool();

  void setInitialPoolSize( int initialPoolSize );

  int getInitialPoolSize();

  void setMaximumPoolSize( int maxPoolSize );

  int getMaximumPoolSize();

  void setPartitioned( boolean partitioned );

  boolean isPartitioned();

  Map<String, String> getConnectionPoolingProperties();

  void setConnectionPoolingProperties( Map<String, String> connectionPoolingProperties );

  void setPartitioningInformation( List<PartitionDatabaseMeta> partitioningInformation );

  List<PartitionDatabaseMeta> getPartitioningInformation();

  @GwtIncompatible( "Not required for GWT" )
  String calculateHash();

}
