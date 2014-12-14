// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mdb;
import static mdb.Main.catalog;
import static mdb.Main.dbName;
import static mdb.Main.isOpen;
import static mdb.Main.tableCount;
import static mdb.Main.tableIndex;
import static mdb.Main.txn;
import static mdb.Main.envmnt;
import static mdb.Main.store;
import static mdb.Main.unCommittedIndex;
import static mdb.Main.unCommittedTable;
import Jakarta.util.*;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

public class CloseCmd extends Close {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 2 ;
    private OutputStream startHome = null;
    private Environment startEnv;
    private EntityStore startStore;
    
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
    	isOpen = 0;

    	for(Transaction x : txn){
    		x.abort();
    	
    	}
    	//System.out.println(tableCount);
    	
    	for(int i=0; i< tableCount; ++i){
    		String tableName = catalog.TableName.get(i);
    		/*for(int j=0; j<catalog.ColInfo.get(tableName).size(); ++j){
    			catalog.indexTxn.get(tableName).get(j).abort();
    		}*/
    		for(Transaction s : catalog.indexTxn.get(tableName)){
				s.abort();	
		}
    	}
    	
    	
    	
    	
    	/*
    	for(int i=0; i< tableCount; ++i){
    		String tableName = catalog.TableName.get(i);
    		for(int j=0; j<catalog.ColInfo.get(tableName).size(); ++j){
    			catalog.indexTxn.get(tableName).clear();
    		}
    	}*/
    	
    	txn.clear();
    	
    	for(int i=0;i<unCommittedTable.size();++i){
    		catalog.ColField.remove(unCommittedTable.get(i));
    		catalog.ColInfo.remove(unCommittedTable.get(i));
    		catalog.indexEnv.remove(unCommittedTable.get(i));
    		catalog.indexHome.remove(unCommittedTable.get(i));
    		catalog.indexStore.remove(unCommittedTable.get(i));
    		catalog.indexTxn.remove(unCommittedTable.get(i));
    		catalog.isIndexed.remove(unCommittedTable.get(i));
    		catalog.TableName.remove(unCommittedTable.get(i));
    		
    		File dir = new File("./DataBase/"+dbName+"/db/"+unCommittedTable.get(i));
    		deleteDir(dir);
    		File dir2 = new File("./DataBase/"+dbName+"/dbIndex/"+unCommittedTable.get(i));
    		deleteDir(dir2);
    		tableCount--;
    	}
    	
    	
    	for(int i=0;i<unCommittedIndex.size()-1;i=i+2){
    		String tableName = unCommittedIndex.get(i);
    		String colName = unCommittedIndex.get(i+1);
    		
    		if(catalog.ColInfo.get(tableName)!=null){
    		int index = catalog.ColInfo.get(tableName).indexOf(colName);
    		catalog.isIndexed.get(tableName).set(index, false);
    		}
    	}
    	unCommittedTable.clear();
    	unCommittedIndex.clear();
    	
    	for(int i=0;i<store.size();++i){
        	try{
        		if(store.get(i)!=null){	
        		store.get(i).close();
        		}
        	}catch(DatabaseException dbe) {
        		System.err.println("Error closing store" +
        				dbe.toString());
        				}
        		
        	}
    	
    	for(int i=0;i<envmnt.size();++i){
    	try{
    		if(envmnt.get(i)!=null){
    			//envmnt.get(i).cleanLog();
    			envmnt.get(i).close();
    		}
    	}catch(DatabaseException dbe) {
    		System.err.println("Error closing environment" +
    				dbe.toString());
    				}
    		
    	}
    	
    	for(int i=0;i<tableCount;++i){
    		for(int j=0;j<catalog.indexStore.get(catalog.TableName.get(i)).size();++j){
    			catalog.indexStore.get(catalog.TableName.get(i)).get(j).close();
    		}
    	}
    	
    	for(int i=0;i<tableCount;++i){
    		for(int j=0;j<catalog.indexEnv.get(catalog.TableName.get(i)).size();++j){
    			//catalog.indexEnv.get(catalog.TableName.get(i)).get(j).cleanLog();
    			catalog.indexEnv.get(catalog.TableName.get(i)).get(j).close();
    		}
    	}
    	
    
    	
    	try {
			startHome = new FileOutputStream("./DataBase/"+dbName+"/startFile/start.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	


        
    	
        
	   	
	   	StartData temp = new StartData();
	   	temp.setTableCount(tableCount);
	   	temp.setTableIndex(tableIndex);
	   	temp.setColField(catalog.ColField);
	   	temp.setColInfo(catalog.ColInfo);
	   	temp.SetIsIndexed(catalog.isIndexed);
	   	temp.setTableName(catalog.TableName);
	   	
	   	
	   	
	   	
	   	ObjectOutputStream outputStream = null;
	    try {
	    	//outputStream = new ObjectOutputStream(new FileOutputStream("./DataBase/"+dbName+"/startFile"));
	    	outputStream = new ObjectOutputStream(startHome);
	    	outputStream.writeObject(temp);
	    }catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }  finally {
            //Close the ObjectOutputStream
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
	   	
	   	

   	   	   
   	   	   	
   	   	   	
   	   envmnt.clear();
   	   store.clear();
   	   catalog = new TableCatalog();
   	   tableCount=0;
   	   tableIndex.clear();
   	   txn.clear();
   	   	 
   	   	   	
 
    	
    	
    	
    	
    	
    }
    
    public static void deleteDir(File dir) { 
 	   if (dir == null || !dir.exists() || !dir.isDirectory()) 
 	       return; 
 	   for (File file : dir.listFiles()) { 
 	       if (file.isFile()) 
 	           file.delete(); 
 	       else if (file.isDirectory()) 
 	           deleteDir(file); 
 	    } 
 	    dir.delete(); 
    }

    public AstToken getCLOSE () {
        
        return (AstToken) tok [0] ;
    }

    public AstToken getSEMI () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true} ;
    }

    public CloseCmd setParms (AstToken tok0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* CLOSE */
        tok [1] = tok1 ;            /* SEMI */
        
        InitChildren () ;
        return (CloseCmd) this ;
    }

}