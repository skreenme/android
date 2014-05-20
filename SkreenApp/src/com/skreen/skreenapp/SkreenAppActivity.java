/*
 * SkreenMeApp
 * Copyright (c) 2014, GSLab Pvt. Ltd., All rights reserved.
 * Company Name: GSLab Pvt. Ltd.
 * Website: http://www.gslab.com
 * Skreenme Website: http://skreen.me
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * 
 */
package com.skreen.skreenapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * This Skreen.Me android app demostrats skreen.me android lib
 * usage and skreen.me plateform capabilities.
 * 
 * This is a sample app to show you that it is quite easy to
 * get along with skreen.me android lib with any android app
 * and start sharing app's screen.
 * 
 * @author Pratik
 *
 */
public class SkreenAppActivity extends Activity implements OnClickListener {

    private static final String TAG = SkreenAppActivity.class.getSimpleName()
            + ":";

    private Button mShareMySkreenBtn;
    private Button mViewSkreenBtn;
    private TextView disclaimnerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate#called");
        setContentView(R.layout.activity_skreen_app);
        this.mShareMySkreenBtn = (Button) this.findViewById(R.id.share_my_skreen_btn);
        this.mShareMySkreenBtn.setOnClickListener(this);
        
        this.mViewSkreenBtn = (Button) this.findViewById(R.id.view_skreen_btn);
        this.mViewSkreenBtn.setOnClickListener(this);
        
        this.disclaimnerText = (TextView) this.findViewById(R.id.disclaimner_text);
        this.disclaimnerText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu aMenu) {
        Log.v(TAG, "onPrepareOptionsMenu#called");
        return super.onPrepareOptionsMenu(aMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.skreen_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem aItem) {
        return super.onOptionsItemSelected(aItem);
    }

    @Override
    public void onClick(View aV) {
        /* actions for quick links */
        if (aV == this.mShareMySkreenBtn) {
            this.launchShareMySkreenActivity();
        } else if (aV == this.mViewSkreenBtn) {
            this.launchViewSkreenActivity();
        } else {
            Log.i(TAG, "onClick: nothing to be done");
        }
    }

    private void launchShareMySkreenActivity() {
        // Sending id to KapsuleViewActivity
        Intent intent = new Intent(this.getApplicationContext(),
                ShareSkreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    
    private void launchViewSkreenActivity() {
        // Sending id to KapsuleViewActivity
        Intent intent = new Intent(this.getApplicationContext(),
                ViewSkreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);        
    }
}
