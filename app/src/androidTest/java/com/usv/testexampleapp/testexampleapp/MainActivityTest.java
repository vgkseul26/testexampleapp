package com.usv.testexampleapp.testexampleapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity activity;
    private Button addButton;
    private EditText editText;
    private View mainActivityView;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        activity = getActivity();
        super.setUp();
        mainActivityView = activity.getWindow().getDecorView();
        addButton = (Button) activity.findViewById(R.id.add_button);
        editText = (EditText) activity.findViewById(R.id.edit_text);
    }

    public void testOrientationLandscape(){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ViewAsserts.assertOnScreen(mainActivityView, addButton);
        ViewAsserts.assertOnScreen(mainActivityView, editText);
    }

    public void testOrientationPortrait(){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewAsserts.assertOnScreen(mainActivityView, addButton);
        ViewAsserts.assertOnScreen(mainActivityView, editText);
    }
}