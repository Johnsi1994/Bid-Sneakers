package com.johnson.bid.settings;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnson.bid.R;
import com.johnson.bid.util.ImageManager;
import com.johnson.bid.util.ProfileAvatarOutlineProvider;
import com.johnson.bid.util.UserManager;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.johnson.bid.MainMvpController.SETTINGS;

public class SettingsFragment extends Fragment implements SettingsContract.View {

    private SettingsContract.Presenter mPresenter;
    private ImageView mUserProfile;
    private Button mBtnEditProfile;
    private TextView mTextUserName;
    private Button mEditBtn;
    private EditText mEditUserName;
    private boolean isEditing = false;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        mUserProfile = root.findViewById(R.id.image_user_profile);
        mUserProfile.setOutlineProvider(new ProfileAvatarOutlineProvider());
        mBtnEditProfile = root.findViewById(R.id.button_edit_profile);
        mTextUserName = root.findViewById(R.id.text_user_name);
        mEditBtn = root.findViewById(R.id.button_edit_user_name);
        mEditUserName = root.findViewById(R.id.edit_user_name);

        mTextUserName.setText(UserManager.getInstance().getUser().getName());
        ImageManager.getInstance().setImageByUrl(mUserProfile, UserManager.getInstance().getUser().getImage());

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnEditProfile.setOnClickListener(v ->
            mPresenter.openGalleryDialog(SETTINGS)
        );


        mEditBtn.setOnClickListener(v -> {
            if (isEditing) {

                if (!"".equals(mEditUserName.getText().toString())) {
                    mTextUserName.setText(mEditUserName.getText().toString());
                    mPresenter.setUserName(mEditUserName.getText().toString());
                }

                isEditing = false;
                mTextUserName.setVisibility(View.VISIBLE);
                mEditUserName.setVisibility(View.INVISIBLE);
                mEditBtn.setText("修改");


            } else {
                isEditing = true;
                mTextUserName.setVisibility(View.INVISIBLE);
                mEditUserName.setVisibility(View.VISIBLE);
                mEditBtn.setText("完成");
            }
        });

    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showProfile(Bitmap bitmap) {
        mUserProfile.setImageBitmap(bitmap);
    }
}
