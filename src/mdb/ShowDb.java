// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mdb;
import static mdb.Main.catalog;
import static mdb.Main.isOpen;
import static mdb.Main.store;
import static mdb.Main.tableCount;
import static mdb.Main.tableIndex;
import static mdb.Main.txn;
import Jakarta.util.*;

import java.io.*;
import java.util.*;

import com.sleepycat.je.CursorConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;

public class ShowDb extends Show {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 2 ;
    
    public void execute () {
    	if(isOpen==1){
    		execute2();
    	}
    	else{
    		System.out.println("Please open a database first!");
    	}
    }

    public void execute2 () {
        
        //super.execute();
    	System.out.println("Table Information:");
    	catalog.Show();
    	
    	/*
    	System.out.println("Tuple Information:");
    	for(int i =0 ; i< tableCount; ++i){
    		String tableName = catalog.TableName.get(i);
    		System.out.println(tableName);
    		
    		PrimaryIndex<String,Tuple> pi = store.get(i).getPrimaryIndex(String.class, Tuple.class);
    		
    		CursorConfig config = new CursorConfig();
    		config.setReadUncommitted(true);  
    		
    		
    		EntityCursor<Tuple> pi_cursor = pi.entities(txn.get(tableIndex.get(tableName)),config);
    		try {
    				for (Tuple seci : pi_cursor) {
    					ArrayList<String> temp = seci.getValue();
    					for(String ss : temp){
    						System.out.print(ss+" ");
    					}
    					System.out.println();
    				}
    			} finally {
    			// Always close the cursor
    			pi_cursor.close();
    			}
    		
    		
    	}
    	*/
    }

    public AstToken getSEMI () {
        
        return (AstToken) tok [1] ;
    }

    public AstToken getSHOW () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true} ;
    }

    public ShowDb setParms (AstToken tok0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* SHOW */
        tok [1] = tok1 ;            /* SEMI */
        
        InitChildren () ;
        return (ShowDb) this ;
    }

}