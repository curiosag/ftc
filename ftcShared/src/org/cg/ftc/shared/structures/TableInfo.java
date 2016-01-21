package org.cg.ftc.shared.structures;

import java.util.List;


public class TableInfo {
    public final String name;
    public final String id;
    public final String description;
    public final List<ColumnInfo> columns;

    public TableInfo(String name, String id, String description, List<ColumnInfo> columns) {
      this.name = name;
      this.id = id;
      this.description = description;
      this.columns = columns;
    }

  }