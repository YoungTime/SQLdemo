package com.example.duzeming.sqldemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button newDatabase;
    private Button addData;
    private Button upData;
    private Button deleteData;
    private Button queryData;
    private TextView data;
    private String all;
    private List<String> stringList;
    private MyDatabaseHelper myDatabaseHelper;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newDatabase = (Button) findViewById(R.id.btn_new_database);
        addData = (Button) findViewById(R.id.add_data);
        upData = (Button) findViewById(R.id.up_data);
        deleteData = (Button) findViewById(R.id.delete_data);
        queryData = (Button) findViewById(R.id.query_data);
        data = (TextView) findViewById(R.id.tv_data);

        stringList = new ArrayList<String>();
        myDatabaseHelper = new MyDatabaseHelper(this, "Bookstore.db", null, 2);
        sb = new StringBuilder();
        newDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabaseHelper.getWritableDatabase();
            }
        });
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", "412");
                values.put("price", "16.96");
                db.insert("Book", null, values);

                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", "410");
                values.put("price", "19.95");
                db.insert("Book", null, values);
            }
        });
        upData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});
            }
        });
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[]{"500"});
            }
        });
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                sb.delete(0,sb.length());

                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
//                        stringList.add("《" + name + "》是由" + author + "编写,本书有" + pages + "页，售价" + price + "元");
                        all="《" + name + "》是由" + author + "编写,本书有" + pages + "页，售价" + price + "元";
//                            data.setText(stringList.get(i));
                            sb.append(all);
                        Log.d("Cursor",sb.toString());
                    } while (cursor.moveToNext());
                    data.setText(sb.toString());
                }
                cursor.close();
            }
        });

    }
}
