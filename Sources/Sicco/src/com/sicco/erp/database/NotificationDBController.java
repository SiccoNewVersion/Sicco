package com.sicco.erp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sicco.erp.model.NotificationModel;

public class NotificationDBController extends SQLiteOpenHelper {
	
	public static String NOTIFICATION_STATE_NEW = "new";
	public static String NOTIFICATION_STATE_CHECKED = "checked";

	private static String DB_NAME = "notification.db";
	private static int DB_VERSION = 1;
	private static String DB_PATH = "/data/data/com.sicco.erp/databases/";
	
	private SQLiteDatabase mDatabase;
	private static NotificationDBController sInstance;
	private Context mContext;
	public NotificationDBController(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mDatabase = getWritableDatabase();
		mContext = context;
	}
	public static NotificationDBController getInstance(Context context){
		if(sInstance == null) {
			sInstance = new NotificationDBController(context);
		}
		
		return sInstance;
	}
	
	public static String TABLE_NAME = "notification_tbl";
	public static String ID_COL = "_id";
//	public static String USERNAME_COL = "username";
	public static String ID_NOTYFI_COL = "id_notyfi";
	public static String NOTIFI_TYPE_COL = "notifi_type";
	public static String MSG_TYPE_COL = "msg_type";
	public static String NAME_COL = "ten";
	public static String CONTENT_COL = "noi_dung";
	public static String URL_COL = "url";
	public static String STATE_COL = "state";
	
	private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ ID_COL + " integer primary key autoincrement,"
//			+ USERNAME_COL + " text,"
			+ ID_NOTYFI_COL + " text,"
			+ NOTIFI_TYPE_COL + " text,"
			+ MSG_TYPE_COL + " text,"
			+ NAME_COL + " text,"
			+ CONTENT_COL + " text,"
			+ URL_COL + " text,"
			+ STATE_COL + " text);";
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void openDB() throws SQLException{
		String myPath = DB_PATH+ DB_NAME;
		try {
			mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having, String orderBy){
		if (mDatabase == null) openDB();
		
		Cursor cursor = mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
	
	public Cursor rawQuery(String stSql, String[] data) {
		if (mDatabase == null)
			openDB();

		Cursor cursor = mDatabase.rawQuery(stSql, data);
		return cursor;
	}
	
	public long insert(String table, String nullColumnHack, ContentValues values){
		if (mDatabase == null) 
			openDB();
		
		long insertRet = mDatabase.insert(table, nullColumnHack, values);
		return insertRet;
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
		if( mDatabase == null) openDB();
		
		int ret = mDatabase.update(table, values, whereClause, whereArgs);
		
		return ret;
	}
	

//	public int updateNotufi(String table, ContentValues values,
//			String whereClause, String[] whereArgs) {
//		if (mDatabase == null)
//			try {
//				openDB();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		whereClause = ID_NOTYFI_COL + " IN (";
//		whereClause += "?,";
//		whereClause = whereClause.substring(0, whereClause.length() - 1);
//		whereClause += ") ";
//
//		int ret = mDatabase.update(TABLE_NAME, values, whereClause, whereArgs);
//		return ret;
//	}
	
	public void checkedNotification(NotificationModel item, int id){
		ContentValues values = new ContentValues();
		values.put(STATE_COL, NOTIFICATION_STATE_CHECKED);
		
		String where = NotificationDBController.ID_COL + " = " + id;
		update(TABLE_NAME, values, where, null);
	}
	
	public void deleteAllData()
	{
	    SQLiteDatabase sdb= this.getWritableDatabase();
	    sdb.delete(TABLE_NAME, null, null);

	}
	public void getSize(){
	}
}
