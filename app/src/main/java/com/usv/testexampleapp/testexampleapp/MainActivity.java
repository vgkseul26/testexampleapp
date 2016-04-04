package com.usv.testexampleapp.testexampleapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private final Uri NOTE_URI = Uri.parse("content://com.usv.testexampleapp.Notes/note_list");
    Button addButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = (Button) findViewById(R.id.add_button);
        editText = (EditText) findViewById(R.id.edit_text);
        Cursor cursor =  getContentResolver().query(NOTE_URI, new String[]{SimpleContentProvider.NOTE_ID + " as _id", SimpleContentProvider.NOTE_TEXT}, null, null, null);
        if (cursor.moveToFirst()) {
            try {
                editText.setText(cursor.getString(cursor.getColumnIndex("note_text")));
            } catch (NullPointerException ex){
                editText.setText("Введите текст!!!");
            }
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(NOTE_URI, null, null);
                ContentValues values = new ContentValues();
                values.put(SimpleContentProvider.NOTE_TEXT, editText.getText().toString());
                getContentResolver().insert(SimpleContentProvider.NOTE_CONTENT_URI, values);
            }
        });
    }
}
