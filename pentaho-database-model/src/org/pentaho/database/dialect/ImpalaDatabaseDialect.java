/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.database.dialect;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.dialect.AbstractDatabaseDialect;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class ImpalaDatabaseDialect extends AbstractDatabaseDialect {
  
  public ImpalaDatabaseDialect() {
    super();
  }
  
  /**
   * UID for serialization
   */
  private static final long serialVersionUID = -6685869374136347923L;

  private static final int DEFAULT_PORT = 21050;

  private static final IDatabaseType DBTYPE = 
      new DatabaseType(
          "Impala",
          "IMPALA",
          DatabaseAccessType.getList(
              DatabaseAccessType.NATIVE, 
              DatabaseAccessType.JNDI
          ), 
          DEFAULT_PORT, 
          "http://www.cloudera.com/content/support/en/documentation/cloudera-impala/cloudera-impala-documentation-v1-latest.html"
      );
          
  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }


  @Override
  public String getNativeDriver() {
    return "org.apache.hive.jdbc.HiveDriver";
  }

  @Override
  public String getURL(IDatabaseConnection connection) throws DatabaseDialectException {
    StringBuffer urlBuffer = new StringBuffer(getNativeJdbcPre());
    /*String username = connection.getUsername();
    if(username != null && !"".equals(username)) {
      urlBuffer.append(username);
      String password = connection.getPassword();
      if(password != null && !"".equals(password)) {
        urlBuffer.append(":");
        urlBuffer.append(password);
      }
      urlBuffer.append("@");
    }*/
    urlBuffer.append(connection.getHostname());
    urlBuffer.append(":");
    urlBuffer.append(connection.getDatabasePort());
    urlBuffer.append("/");
    urlBuffer.append(connection.getDatabaseName());
    urlBuffer.append(";auth=noSasl");
    return urlBuffer.toString();
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:hive2://";
  }


  /**
   * Generates the SQL statement to add a column to the specified table
   * @param tablename The table to add
   * @param v The column defined as a value
   * @param tk the name of the technical key field
   * @param use_autoinc whether or not this field uses auto increment
   * @param pk the name of the primary key field
   * @param semicolon whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to add a column to the specified table
   */
  @Override
  public String getAddColumnStatement(String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk, boolean semicolon)
  {
    return "ALTER TABLE "+tablename+" ADD "+getFieldDefinition(v, tk, pk, use_autoinc, true, false);
  }


  /**
   * Generates the SQL statement to modify a column in the specified table
   * @param tablename The table to add
   * @param v The column defined as a value
   * @param tk the name of the technical key field
   * @param use_autoinc whether or not this field uses auto increment
   * @param pk the name of the primary key field
   * @param semicolon whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to modify a column in the specified table
   */
  @Override
  public String getModifyColumnStatement(String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk, boolean semicolon)
  {
    return "ALTER TABLE "+tablename+" MODIFY "+getFieldDefinition(v, tk, pk, use_autoinc, true, false);
  }


  @Override
  public String getFieldDefinition(IValueMeta v, String tk, String pk, boolean use_autoinc, boolean add_fieldname, boolean add_cr)
  {
    String retval="";
    
    String fieldname = v.getName();
    int    length    = v.getLength();
    int    precision = v.getPrecision();
    
    if (add_fieldname) retval+=fieldname+" ";
    
    int type         = v.getType();
    switch(type)
    {
    case IValueMeta.TYPE_DATE      : retval+="DATETIME"; break;
    case IValueMeta.TYPE_BOOLEAN   : 
      if (supportsBooleanDataType()) {
        retval+="BOOLEAN"; 
      } else {
        retval+="CHAR(1)";
      }
      break;

    case IValueMeta.TYPE_NUMBER    :
    case IValueMeta.TYPE_INTEGER   : 
        case IValueMeta.TYPE_BIGNUMBER : 
      if (fieldname.equalsIgnoreCase(tk) || // Technical key
          fieldname.equalsIgnoreCase(pk)    // Primary key
          ) 
      {
        if (use_autoinc)
        {
          retval+="BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY";
        }
        else
        {
          retval+="BIGINT NOT NULL PRIMARY KEY";
        }
      } 
      else
      {
        // Integer values...
        if (precision==0)
        {
          if (length>9)
          {
            if (length<19) {
              // can hold signed values between -9223372036854775808 and 9223372036854775807
              // 18 significant digits
              retval+="BIGINT";
            }
            else {
              retval+="DECIMAL("+length+")";
            }
          }
          else
          {
            retval+="INT";
          }
        }
        // Floating point values...
        else  
        {
          if (length>15)
          {
            retval+="DECIMAL("+length;
            if (precision>0) retval+=", "+precision;
            retval+=")";
          }
          else
          {
            // A double-precision floating-point number is accurate to approximately 15 decimal places.
            // http://mysql.mirrors-r-us.net/doc/refman/5.1/en/numeric-type-overview.html 
            retval+="DOUBLE";
          }
        }
      }
      break;
    case IValueMeta.TYPE_STRING:
      if (length>0)
      {
        if (length==1) retval+="CHAR(1)";
        else if (length<     256) retval+="VARCHAR("+length+")";
        else if (length<   65536) retval+="TEXT";
        else if (length<16777215) retval+="MEDIUMTEXT";
        else retval+="LONGTEXT";
      }
      else
      {
        retval+="TINYTEXT";
      }
      break;
        case IValueMeta.TYPE_BINARY:
            retval+="LONGBLOB";
            break;
    default:
      retval+=" UNKNOWN";
      break;
    }
    
    if (add_cr) retval+=CR;
    
    return retval;
  }


  @Override
  public String[] getUsedLibraries() {
    return new String[] { "pentaho-hadoop-hive-jdbc-shim-1.4-SNAPSHOT.jar" };
  }
  
  @Override
  public int getDefaultDatabasePort() {
    return DEFAULT_PORT;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.dialect.AbstractDatabaseDialect#supportsSchemas()
   */
  @Override
  public boolean supportsSchemas() {
    return false;
  }

}
