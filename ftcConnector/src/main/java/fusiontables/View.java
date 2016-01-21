/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package main.java.fusiontables;

import org.cg.ftc.shared.structures.TableInfo;

/**
 * Utility methods to print to the command line.
 * 
 * @author Christian Junk
 */
public class View {

  static void header(String name) {
    System.out.println();
    System.out.println("================== " + name + " ==================");
    System.out.println();
  }

  static void show(com.google.api.services.fusiontables.model.Table r)
  {
    System.out.println("show Table table signature is only here to keep this stuff complilable");
  } 
  
  static void show(TableInfo table) {
    System.out.println("id: " + table.id);
    System.out.println("name: " + table.name);
    System.out.println("description: " + table.description);
  //  System.out.println("attribution: " + table.getAttribution());
  //  System.out.println("attribution link: " + table.getAttributionLink());
  //  System.out.println("kind: " + table.getKind());

  }

  static void separator() {
    System.out.println();
    System.out.println("------------------------------------------------------");
    System.out.println();
  }
}
