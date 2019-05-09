package com.johnson.bid.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.johnson.bid.Bid;
import com.johnson.bid.R;
import com.johnson.bid.data.User;

import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class UserManager {

    private User mUser = new User();
    private CallbackManager mFbCallbackManager;
    private boolean mHasUserDataChange = false;

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
                        Log.d(Constants.TAG, "TOKEN : " + loginResult.getAccessToken().getToken());
                        loadCallback.onSuccess();
                    }

                    @Override
                    public void onFail(String errorMessage) {

                        loadCallback.onFail("Get User Info Failed");
                    }
                });
                Log.d(Constants.TAG, "FB Login Success");
            }

            @Override
            public void onCancel() {

                loadCallback.onFail("FB Login Cancel");
            }

            @Override
            public void onError(FacebookException exception) {

                loadCallback.onFail("FB Login Error: " + exception.getMessage());
            }
        });

        loginFacebook(context);
    }

    private void getUserInfoFromFacebook(LoginResult loginResult, LoadCallback loadCallback) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
            try {
                if (response.getConnection().getResponseCode() == 200) {
                    long id = object.getLong(Bid.getAppContext().getString(R.string.fb_id));
                    String name = object.getString(Bid.getAppContext().getString(R.string.fb_name));
                    String userPhoto = Bid.getAppContext().getString(R.string.fb_profile_url_part1) + id + Bid.getAppContext().getString(R.string.fb_profile_url_part2);

                    checkHasUser(id, name, userPhoto, loadCallback);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        graphRequest.executeAsync();
    }

    private void checkHasUser(long id, String name, String userPhoto, LoadCallback loadCallback) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_users))
                .whereEqualTo(Bid.getAppContext().getString(R.string.fb_id), id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        if (task.getResult().size() == 0) {
                            uploadNewUser(id, name, userPhoto);
                            loadCallback.onSuccess();
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                setUser(document.toObject(User.class));
                            }
                            loadCallback.onSuccess();
                        }
                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void uploadNewUser(long id, String name, String userPhoto) {

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setImage(userPhoto);
        setUser(user);

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_users))
                .document(String.valueOf(id))
                .set(user)
                .addOnSuccessListener(documentReference -> Log.d(Constants.TAG, "DocumentSnapshot added"))
                .addOnFailureListener(e -> Log.w(Constants.TAG, "Error adding document", e));
    }

    public void getUserProfile(LoadCallback loadCallback) {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_users))
                .document(AccessToken.getCurrentAccessToken().getUserId().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        setUser(document.toObject(User.class));

                        loadCallback.onSuccess();

                    } else {
                        Log.d(Constants.TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void updateUser2Firebase() {

        Firebase.getInstance().getFirestore().collection(Bid.getAppContext().getString(R.string.firebase_users))
                .document(String.valueOf(mUser.getId()))
                .set(mUser)
                .addOnSuccessListener(documentReference -> Log.d(Constants.TAG, "DocumentSnapshot added"))
                .addOnFailureListener(e -> Log.w(Constants.TAG, "Error adding document", e));

    }

    private void loginFacebook(Context context) {

        LoginManager.getInstance().logInWithReadPermissions(
                (Activity) context, Arrays.asList(context.getString(R.string.fb_email)));
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        mUser = null;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public  void addBiddingProductId(long productId) {
        mUser.getMyBiddingProductsId().add(productId);
    }

    public void removeBiddingProductId(long productId) {

        Iterator<Long> iterator = mUser.getMyBiddingProductsId().iterator();
        while (iterator.hasNext()) {
            long id = iterator.next();
            if (id == productId) {
                iterator.remove();
            }
        }
    }

    public void addSellingProductId(long productId) {
        mUser.getMySellingProductsId().add(productId);
    }

    public void removeSellingProductId(long productId) {

        Iterator<Long> iterator = mUser.getMySellingProductsId().iterator();
        while (iterator.hasNext()) {
            long id = iterator.next();
            if (id == productId) {
                iterator.remove();
            }
        }
    }

    public  void addBoughtProductId(long productId) {
        mUser.getMyBoughtProductsId().add(productId);
    }

    public  void addSoldProductId(long productId) {
        mUser.getMySoldProductsId().add(productId);
    }

    public void addNobodyBidProductId(long productId) {
        mUser.getNobodyBitProductsId().add(productId);
    }

    public void increaseUnreadBought() {
        int unread = mUser.getUnreadBought();
        mUser.setUnreadBought(unread + 1);
    }

    public void decreaseUnreadBought() {
        int unread = mUser.getUnreadBought();
        mUser.setUnreadBought(unread - 1);
    }

    public void increaseUnreadSold() {
        int unread = mUser.getUnreadSold();
        mUser.setUnreadSold(unread + 1);
    }

    public void decreaseUnreadSold() {
        int unread = mUser.getUnreadSold();
        mUser.setUnreadSold(unread - 1);
    }

    public void increaseUnreadNobodyBid() {
        int unread = mUser.getUnreadNobodyBid();
        mUser.setUnreadNobodyBid(unread + 1);
    }

    public void decreaseUnreadNobodyBid() {
        int unread = mUser.getUnreadNobodyBid();
        mUser.setUnreadNobodyBid(unread - 1);
    }

    public void setUserName(String userName) {
        mUser.setName(userName);
    }

    public void setUserProfile(String imageUrl) {
        mUser.setImage(imageUrl);
    }

    public boolean hasChatRoom(long id) {

        boolean hasRoom = false;

        for (int i = 0; i < mUser.getChatList().size(); i++) {
            if (id == mUser.getChatList().get(i)) {
                hasRoom =  true;
            }
        }

        return hasRoom;
    }

    public void setChatList(long id) {
        mUser.getChatList().add(id);
    }

    public void setHasUserDataChange(boolean hasUserDataChange) {
        mHasUserDataChange = hasUserDataChange;
    }

    public boolean isHasUserDataChange() {
        return mHasUserDataChange;
    }



    public CallbackManager getFbCallbackManager() {
        return mFbCallbackManager;
    }

    public interface LoadCallback {

        void onSuccess();

        void onFail(String errorMessage);
    }
}
