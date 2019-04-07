package com.johnson.bid.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.johnson.bid.Bid;
import com.johnson.bid.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class UserManager {

    private User mUser;
    private CallbackManager mFbCallbackManager;

    private static class UserManagerHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    private UserManager() {
    }

    public static UserManager getInstance() {
        return UserManagerHolder.INSTANCE;
    }

    public void loginBidByFacebook(Context context, final LoadCallback loadCallback) {

        mFbCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserInfoFromFacebook(loginResult, new LoadCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Johnsi", "TOKEN : " + loginResult.getAccessToken().getToken());
                        loadCallback.onSuccess();
                    }

                    @Override
                    public void onFail(String errorMessage) {
                        loadCallback.onFail("Get User Info Failed");
                    }

                    @Override
                    public void onInvalidToken(String errorMessage) {

                    }
                });
                Log.d("Johnsi", "FB Login Success");
            }

            @Override
            public void onCancel() {

                Log.d("Johnsi", "FB Login Cancel");
                loadCallback.onFail("FB Login Cancel");
            }

            @Override
            public void onError(FacebookException exception) {

                Log.d("Johnsi", "FB Login Error");
                loadCallback.onFail("FB Login Error: " + exception.getMessage());
            }
        });

        loginFacebook(context);
    }

    private void getUserInfoFromFacebook(LoginResult loginResult, LoadCallback loadCallback) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
            try {
                if (response.getConnection().getResponseCode() == 200) {
                    long id = object.getLong("id");
                    String name = object.getString("name");

                    Profile profile = Profile.getCurrentProfile();
                    Uri userPhoto = profile.getProfilePictureUri(300, 300);

                    User user = new User();
                    user.setId(id);
                    user.setName(name);
                    user.setImage(userPhoto.toString());

                    setUser(user);

                    Bid.getAppContext()
                            .getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
                            .edit()
                            .putString(Constants.USER_TOKEN, loginResult.getAccessToken().getToken())
                            .apply();
                    loadCallback.onSuccess();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        graphRequest.executeAsync();
    }

    private void loginFacebook(Context context) {

        LoginManager.getInstance().logInWithReadPermissions(
                (Activity) context, Arrays.asList("email"));
    }

    public String getUserToken() {

        return Bid.getAppContext()
                .getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE)
                .getString(Constants.USER_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return getUserToken() != null;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public CallbackManager getFbCallbackManager() {
        return mFbCallbackManager;
    }

    public interface LoadCallback {

        void onSuccess();

        void onFail(String errorMessage);

        void onInvalidToken(String errorMessage);
    }
}
