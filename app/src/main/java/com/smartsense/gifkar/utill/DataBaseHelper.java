package com.smartsense.gifkar.utill;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class DataBaseHelper extends SQLiteOpenHelper {
	// The Android's default system path of your application database.
	private static String DB_PATH = Constants.DB_PATH;
	private static String DB_NAME = "fmcg.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 *
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public DataBaseHelper(Context context, String string) {
		super(context, null, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
		} else {
			// By calling this method and empty database will be created into
			// the default system path of your application so we are gonna be
			// able to overwrite that database with our database.
			this.getReadableDatabase().close();
			try {
				File DBFile = new File(DB_PATH + DB_NAME);
				cpDBs(myContext, DBFile);
			} catch (IOException e) {
				Log.e("MyError", "Error copying database");
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 *
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// database does't exist yet.
			Log.e("MyError", "database does not exist yet");
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void cpDBs(Context Ctxt, File DBFile) throws IOException {
		AssetManager am = Ctxt.getAssets();
		OutputStream os = new FileOutputStream(DBFile);
		DBFile.createNewFile();
		byte[] b = new byte[1024];
		int i, r;
		String[] Files = am.list("");
		Arrays.sort(Files);
		for (i = 1; i < 10; i++) {
			String fn = String.format("%d.db", i);
			if (Arrays.binarySearch(Files, fn) < 0)
				break;
			InputStream is = am.open(fn);
			while ((r = is.read(b)) != -1)
				os.write(b, 0, r);
			is.close();
		}
		os.close();
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transferring bytestream.
	 * */
	@SuppressWarnings("unused")
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[2048];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	// public void openDataBase() throws SQLException {
	public SQLiteDatabase openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE); // OPEN_READONLY
		return myDataBase;
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// public void onUpgrade() {
	// File myFile = new File("/data/data/mr.twickle.hollyquotes/quotes.txt");
	// if (myFile.exists()) {
	// myFile.delete();
	// } else {
	// try {
	// myFile.createNewFile();
	// } catch (IOException e) {
	// Log.e("MyUpdateError", "Error updating database");
	// throw new Error("Error updating database");
	// }
	// }
	// this.getReadableDatabase().close();
	// try {
	// File DBFile = new File(DB_PATH + DB_NAME);
	// cpDBs(myContext, DBFile);
	// } catch (IOException e) {
	// Log.e("MyError", "Error copying database");
	// throw new Error("Error copying database");
	// }
	// }

	public void deleteDatabse() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			File dbFile = new File(DB_PATH + DB_NAME);
			dbFile.delete();
			// db.close();
		} else {
			Log.e("Database", "does not exists");
		}
	}
	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

}