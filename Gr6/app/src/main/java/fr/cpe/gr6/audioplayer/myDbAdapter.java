package fr.cpe.gr6.audioplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class myDbAdapter {
    myDbHelper myhelper;
    public myDbAdapter(Context context){
        myhelper = new myDbHelper(context);
    }

    public void creaionBd(){
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        myhelper.onCreate(dbb);
    }

    public long insertGenre(String TABLE_NAME,String name, String url) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tagname", name);
        contentValues.put("tagurl", url);
        return dbb.insert(TABLE_NAME, null , contentValues);
    }

    public String getData(String TABLE_NAME){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,"tagname","tagurl"};
        Cursor cursor =db.query(TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext()){
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name =cursor.getString(cursor.getColumnIndex("tagname"));
            String  password =cursor.getString(cursor.getColumnIndex("tagurl"));
            buffer.append(cid+ "   " + name + "   " + password +" \n");
        }
        return buffer.toString();
    }

    public  int delete(String uname) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete("TABLE_NAME","tagname"+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tagname",newName);
        String[] whereArgs= {oldName};
        int count =db.update("TABLE_NAME",contentValues, "tagname"+" = ?",whereArgs );
        return count;
    }

    //cette classe est utilisée pour créer la base de données, inserer des tables et mettre à jour la base
    // la creation de la bd se fait avec l'execution du script sql "script.sql"
    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        //private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        //private static final String NAME = "jaiPasDeNom";    //Column II
        //private static final String = "NeLeDisApersonne";    // Column III
        //private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+ MyPASSWORD+" VARCHAR(225));";
        //private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private final Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                System.out.println("created _____________________________");
                InputStream is = context.getResources().getAssets().open("script.sql");
                String CREATE_TABLE= convertStreamToString(is);
                System.out.println( CREATE_TABLE);
                System.out.println( db);
                db.execSQL(CREATE_TABLE);
                //SQLiteDatabase db;
                //int factt = 0x10000000;
                //SQLiteDatabase dbb = SQLiteDatabase.openOrCreateDatabase("myDatabase", null, null );
                //dbb.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.d("Message", "test ____"+e);
                //Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //Message.message(context,"OnUpgrade");
                Log.d("Message", "OnUpgrade");
                //db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                Log.d("Message", ""+e);
                //Message.message(context,""+e);
            }
        }

        public static String convertStreamToString(InputStream is) throws IOException {
            Writer writer = new StringWriter();
            char[] buffer = new char[2048];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }

    }
}
