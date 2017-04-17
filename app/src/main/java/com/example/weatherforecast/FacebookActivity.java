package com.example.weatherforecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;
import java.util.List;

public class FacebookActivity extends FragmentActivity {
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private LoginManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_activity);
        FacebookSdk.sdkInitialize(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        manager = LoginManager.getInstance();

        manager.logInWithPublishPermissions(this, permissionNeeds);
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbShare();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
                finish();
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                finish();
            }
        });
    }

    public void fbShare() {
        try {
            Intent intent = getIntent();
            String summ = intent.getExtras().getString("SUMMARY");
            String desc = intent.getExtras().getString("DESC");
            String url = intent.getExtras().getString("URL");
            String icon = intent.getExtras().getString("ICON");

            String shareText = "Hi! We are sharing.You share ";
            Log.i("shareText", shareText);
            shareDialog = new ShareDialog(this);
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(FacebookActivity.this, "Facebook Post Successful", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "SHARE SUCCESS");
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FacebookActivity.this, "Post Cancelled", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "SHARE CANCELLED");
                        finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FacebookActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Share: " + exception.getMessage());
                        exception.printStackTrace();
                        finish();
                    }
                });

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(summ)
                        .setContentDescription(desc)
                        .setContentUrl(Uri.parse(url))
                        .setImageUrl(Uri.parse(icon))
                        .build();
                shareDialog.show(linkContent);


            } else {
                Log.i("ERROR", "cannot show share");
            }
        }
        catch (Exception e){
            Log.d("FBEXCEPTION", e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode,    Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}


