package com.shengsheng.police.controller.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.shengsheng.police.R;
import com.shengsheng.police.model.Model;
import com.shengsheng.police.model.bean.UserInfo;
import com.shengsheng.police.utils.Constant;
import com.shengsheng.police.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ChangeInfoActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private Context mContext;
    private AlertDialog profilePictureDialog;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_PERMISSION_CAMERA = 0x001;
    private static final int REQUEST_PERMISSION_WRITE = 0x002;
    private static final int CROP_REQUEST_CODE = 0x003;
    /**
     * 文件相关
     */
    private File captureFile;
    private File rootFile;
    private File cropFile;
    private ImageView iv_head;//头像
    private EditText et_info_account;//账号
    private EditText et_info_name;//姓名
    private EditText et_info_nick;//昵称
    private EditText et_info_pic_url;//头像Url
    private Button bt_save_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        rootFile = new File(Constant.PIC_PATH);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        //从数据库中取用户相关信息
        UserInfo userInfo = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
        et_info_account.setText(userInfo.getHxid());
        et_info_account.setClickable(false);
        et_info_account.setFocusable(false);
        et_info_account.setFocusableInTouchMode(false);
        et_info_name.setText(userInfo.getName());
        et_info_nick.setText(userInfo.getNick());
        et_info_pic_url.setText(userInfo.getPhoto());
        iv_head.setImageBitmap(ImageUtils.returnBitMap(userInfo.getPhoto()));
    }


    private void initListener() {
        //头像的点击事件处理
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
        //保存按钮的点击事件处理
        bt_save_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    //上传头像
    private void updateImage() {
        if (profilePictureDialog == null) {
            @SuppressLint("InflateParams") View rootView = LayoutInflater.from(this).inflate(R.layout.item_profile_picture, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            rootView.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {//拍照按钮的点击事件
                @Override
                public void onClick(View v) {
                    dismissProfilePictureDialog();
                    if (EasyPermissions.hasPermissions(mContext, PERMISSION_CAMERA, PERMISSION_WRITE)) {
                        takePhoto();
                    } else {
                        EasyPermissions.requestPermissions(ChangeInfoActivity.this, "need camera permission", REQUEST_PERMISSION_CAMERA, PERMISSION_CAMERA, PERMISSION_WRITE);
                    }
                }
            });
            rootView.findViewById(R.id.tv_choose_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissProfilePictureDialog();
                    if (EasyPermissions.hasPermissions(mContext, PERMISSION_WRITE)) {
                        choosePhoto();
                    } else {
                        EasyPermissions.requestPermissions(ChangeInfoActivity.this, "need camera permission", REQUEST_PERMISSION_WRITE, PERMISSION_WRITE);
                    }
                }
            });
            rootView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ChangeInfoActivity.this, "你取消了上传", Toast.LENGTH_SHORT).show();
                    profilePictureDialog.dismiss();
                }
            });
            builder.setView(rootView);
            profilePictureDialog = builder.create();
            profilePictureDialog.show();
        } else {
            profilePictureDialog.show();
        }
    }

    //从相册选择
    private void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PERMISSION_WRITE);
    }

    //拍照
    private void takePhoto() {
        //用于保存调用相机拍照后所生成的文件
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        captureFile = new File(rootFile, "temp.jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本 如果在Android7.0以上,使用FileProvider获取Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, getPackageName(), captureFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureFile));
        }
        startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);
    }

    private void dismissProfilePictureDialog() {
        if (profilePictureDialog != null && profilePictureDialog.isShowing()) {
            profilePictureDialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PERMISSION_CAMERA:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(mContext, getPackageName(), captureFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(captureFile));
                    }
                    break;
                case REQUEST_PERMISSION_WRITE:
                    cropPhoto(data.getData());
                    break;
                case CROP_REQUEST_CODE:
                    saveImage(cropFile.getAbsolutePath());//保存裁剪后的文件到本地
                    iv_head.setImageBitmap(BitmapFactory.decodeFile(cropFile.getAbsolutePath()));
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        cropFile = new File(rootFile, "avatar.jpg");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 保存裁剪后的图片到本地
     *
     * @param path
     */
    public String saveImage(String path) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
            FileOutputStream fos = new FileOutputStream(cropFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return cropFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //保存用户修改后的相关信息到数据库
    private void saveInfo() {
        final String new_name = et_info_name.getText().toString();
        final String account = et_info_account.getText().toString();
        final String new_nick = et_info_nick.getText().toString();
        final String pic_url = et_info_pic_url.getText().toString();
            Model.getInstance().getUserAccountDao().updateAccount(new UserInfo(new_name, account, new_nick, pic_url));
        Toast.makeText(ChangeInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initView() {
        iv_head = findViewById(R.id.iv_head);
        et_info_account = findViewById(R.id.et_info_account);
        et_info_name = findViewById(R.id.et_info_name);
        et_info_nick = findViewById(R.id.et_info_nick);
        bt_save_change = findViewById(R.id.bt_save_change);
        et_info_pic_url = findViewById(R.id.et_info_pic_url);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            takePhoto();
        } else if (requestCode == REQUEST_PERMISSION_WRITE) {
            choosePhoto();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
