package com.northconcepts.datapipeline.examples.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.northconcepts.datapipeline.core.DataException;

public class DB {
    
    //==============================================

    private Connection connection;

    public DB() throws DataException { 
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
            
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    shutdown();
                }
            });
        } catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public Connection getConnection() {
        return connection;
    }

    public synchronized void shutdown() throws DataException {
        if (connection != null) {
            try {
                System.out.println("Shutting down...");
                Statement st = connection.createStatement();
                st.execute("SHUTDOWN");
                connection.close();
            } catch (Throwable e) {
                throw DataException.wrap(e);
            } finally {
                connection = null;
                System.out.println("shutdown complete");
            }
        }
    }

    public synchronized void executeQuery(String expression, Object ... args) throws DataException {
        try {
            PreparedStatement st = null;
            try {
                ResultSet rs = null;
                try {
                    st = connection.prepareStatement(expression);
                    for (int i = 0; i < args.length; i++) {
                        st.setObject(i+1, args[i]);
                    }
                    rs = st.executeQuery();
                    dump(rs);
                } finally {
                    rs.close();
                }
            } finally {
                st.close();
            }
        } catch (Throwable e) {
            throw DataException.wrap(e)
                .set("DB.expression", expression);
        }
    }

    public synchronized void executeUpdate(String expression, Object ... args) throws DataException {
        try {
            PreparedStatement st = null;
            try {
                    st = connection.prepareStatement(expression);
                    for (int i = 0; i < args.length; i++) {
                        st.setObject(i+1, args[i]);
                    }
                    int recordsAffected = st.executeUpdate();
                    System.out.println("recordsAffected=" + recordsAffected); 
            } finally {
                st.close();
            }
        } catch (Throwable e) {
            throw DataException.wrap(e)
                .set("DB.expression", expression);
        }
    }

    public synchronized void execute(String expression, Object ... args) throws DataException {
        try {
            PreparedStatement st = null;
            try {
                    st = connection.prepareStatement(expression);
                    for (int i = 0; i < args.length; i++) {
                        st.setObject(i+1, args[i]);
                    }
                    boolean isResultSet = st.execute();
                    if (isResultSet) {
                        ResultSet rs = st.getResultSet();
                        try {
//                            System.out.println(); 
                            dump(rs);
                        } finally {
                            rs.close();
                        }
                    } else {
                        System.out.println("        - recordsAffected=" + st.getUpdateCount()); 
                    }
            } finally {
                st.close();
            }
        } catch (Throwable e) {
            throw DataException.wrap(e)
                .set("DB.expression", expression);
        }
    }

    public static void dump(ResultSet rs) throws DataException {

        System.out.println("----------------------------------"); 
        try {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int row = 0; rs.next(); row++) {
                System.out.println("SQL Record " + row);
                for (int column = 1; column <= columnCount; column++) {
                    Object value = rs.getObject(column);
                    
                    System.out.println("    "+column+":[" + meta.getColumnName(column) + "]:"+
                            meta.getColumnTypeName(column)+"=[" + value + "]" +
                            (value==null?"":":"+value.getClass().getName()));
                }
                System.out.println();
            }
            System.out.println("----------------------------------"); 
        } catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }

    public synchronized void executeFile(File file) throws DataException {
        System.out.println("execute file " + file.getAbsolutePath());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder s = new StringBuilder(1024);
            
            String line;
            READ_FILE_LOOP:
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.length() == 0) { // ignore blank lines
                    continue READ_FILE_LOOP;
                }
                
                if (s.length() > 0) { // add a space between multi-line statements
                    s.append(" ");
                }
                s.append(line);
                
                if (line.endsWith(";")) { // execute statement
                    System.out.println("    execute:  " + s);
                    execute(s.toString());
                    s.setLength(0);
                }
            }
            
            if (s.length() > 0) { // execute statement
                System.out.println("    execute:  " + s);
                execute(s.toString());
            }
        } catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
}
