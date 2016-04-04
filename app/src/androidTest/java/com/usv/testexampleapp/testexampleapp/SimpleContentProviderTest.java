package com.usv.testexampleapp.testexampleapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Ser on 13.03.2016.
 */
@RunWith(AndroidJUnit4.class)
public class SimpleContentProviderTest extends ProviderTestCase2<SimpleContentProvider> {

    private MockContentResolver mMockResolver;

    public SimpleContentProviderTest() {
        super(SimpleContentProvider.class, SimpleContentProvider.AUTHORITY);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
        mMockResolver = getMockContentResolver();
    }

    @Test
    public void testInsert() {
        Uri uri = SimpleContentProvider.NOTE_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(SimpleContentProvider.NOTE_TEXT, "value 1");
        Uri resultingUri = mMockResolver.insert(uri, values);
        assertNotNull(resultingUri);
        long id = ContentUris.parseId(resultingUri);
        assertTrue(id > 0);
    }

    @Test
    public  void  testQuery(){
        Uri NOTE_URI = Uri.parse("content://com.usv.testexampleapp.Notes/note_list");
        Cursor cursor = mMockResolver.query(NOTE_URI, new String[]{SimpleContentProvider.NOTE_ID + " as _id", SimpleContentProvider.NOTE_TEXT}, null, null, null);
        assertNotNull(cursor);
    }

}
