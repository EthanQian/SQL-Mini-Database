// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mdb;
import Jakarta.util.*;

import java.io.*;
import java.util.*;

import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

import static mdb.Main.catalog;
import static mdb.Main.envmnt;
import static mdb.Main.store;
import static mdb.Main.tableCount;
import static mdb.Main.tableIndex;
import static mdb.Main.txn;
import static mdb.Main.unCommittedIndex;

public class IndxDecl extends Decl_ind {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;
    private File envHome = null;
    private IndexTupleDA ida;

    public void execute () {
        
        //super.execute();
    	String tableName = arg[0].arg[0].tok[0].getTokenName();
    	String colName = arg[0].arg[1].tok[0].getTokenName();
    	
    	unCommittedIndex.add(tableName);
    	unCommittedIndex.add(colName);
    	
    	if(!catalog.TableName.contains(tableName)){
    		System.out.println("Table doesn't exist!");
    		return;
    	}
    	else if(!catalog.ColInfo.get(tableName).contains(colName)){
    		System.out.println("Field Name doesn't exist!");
    		return;
    	}
    	
    	else if(catalog.isIndexed.get(tableName).get(catalog.ColInfo.get(tableName).indexOf(colName))){
    		System.out.println("Index for"+" "+tableName + "."+colName+" already exist!");
    		return;		
    	}
    	
    	catalog.isIndexed.get(tableName).set(catalog.ColInfo.get(tableName).indexOf(colName), true);
    	
    	envHome = catalog.getEnvHome(tableName, catalog.ColInfo.get(tableName).indexOf(colName));
   	   	
  
   	   
   	   EnvironmentConfig envConfig = new EnvironmentConfig();
   	   StoreConfig storeConfig = new StoreConfig();
   	   envConfig.setAllowCreate(true);
   	   envConfig.setTransactional(true);
   	   storeConfig.setAllowCreate(true);
   	   storeConfig.setTransactional(true);
   	   
   	   int p = catalog.ColInfo.get(tableName).indexOf(colName);
   	   catalog.SetIndexEnv(tableName, p , new Environment(envHome, envConfig));
   	   catalog.SetIndexStore(tableName, p, new EntityStore(catalog.indexEnv.get(tableName).get(p), tableName, storeConfig));
   	   catalog.SetIndexTransaction(tableName, p, catalog.indexEnv.get(tableName).get(p).beginTransaction(null, null));
   	   
   	   
   	   ida = new IndexTupleDA(catalog.indexStore.get(tableName).get(p));
   	   
   	   PrimaryIndex<String,Tuple> pi = store.get(tableIndex.get(tableName)).getPrimaryIndex(String.class, Tuple.class);
	
   	   CursorConfig config = new CursorConfig();
   	   config.setReadUncommitted(true);  
	
	
   	   EntityCursor<Tuple> pi_cursor = pi.entities(txn.get(tableIndex.get(tableName)),config);
   	   try {
			for (Tuple seci : pi_cursor) {
				TempIndexEntity t = new TempIndexEntity();
				if(catalog.ColField.get(tableName).get(p).equals("str")){
					t.setPKey(seci.getPKey());
					t.setStringKey(seci.getValue().get(p));
				}
				
				if(catalog.ColField.get(tableName).get(p).equals("int")){
					t.setPKey(seci.getPKey());
					t.setIntKey(Integer.parseInt(seci.getValue().get(p)));
				}
				ida.pIdx.put(catalog.indexTxn.get(tableName).get(p),t);

			}
		} finally {
		// Always close the cursor
		pi_cursor.close();
		}
   	   
    	
    	
    	
    }

    public AstToken getINDEX () {
        
        return (AstToken) tok [0] ;
    }

    public Rel_dot_field getRel_dot_field () {
        
        return (Rel_dot_field) arg [0] ;
    }

    public AstToken getSEMI () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public IndxDecl setParms (AstToken tok0, Rel_dot_field arg0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* INDEX */
        arg [0] = arg0 ;            /* Rel_dot_field */
        tok [1] = tok1 ;            /* SEMI */
        
        InitChildren () ;
        return (IndxDecl) this ;
    }

}
