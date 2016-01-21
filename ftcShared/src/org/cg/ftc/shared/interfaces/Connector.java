
package org.cg.ftc.shared.interfaces;

import java.io.IOException;
import java.util.Dictionary;
import java.util.List;

import org.cg.ftc.shared.structures.ConnectionStatus;
import org.cg.ftc.shared.structures.QueryResult;
import org.cg.ftc.shared.structures.TableInfo;


public interface Connector {

  void clearStoredLoginData();
	
  ConnectionStatus reset(Dictionary<String, String> connectionInfo) ;
	
  List<TableInfo> getTableInfo();

  String executeSql(String query) throws IOException;
  
  String execSql(String query);

  QueryResult fetch(String query);
  
  void deleteTable(String tableId) throws IOException;

  String renameTable(String tableId, String newName);

}
