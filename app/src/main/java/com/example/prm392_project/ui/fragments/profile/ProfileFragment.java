package com.example.prm392_project.ui.fragments.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.prm392_project.R;
import com.example.prm392_project.data.model.main.user.User;
import com.example.prm392_project.databinding.FragmentProfileBinding;
import com.example.prm392_project.ui.activities.auth.AuthActivity;
import com.example.prm392_project.ui.activities.settings.SettingActivity;
import com.example.prm392_project.util.RealPathUtil;
import com.example.prm392_project.util.SharedPrefUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private Uri imageUri = null;
    private String userId;
    private String token;

    private final ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show();
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            imageUri = data.getData();
                            Log.d("RegisterActivity", "Image uri: " + imageUri);
                            Glide.with(ProfileFragment.this).load(imageUri).into(binding.profileImageIv);
                            uploadImage();
                        } else {
                            Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        userId = SharedPrefUtils.getString(requireContext(), "id", "");
        token = "Bearer " + SharedPrefUtils.getString(requireContext(), "accessToken", "");
        String name = SharedPrefUtils.getString(requireContext(), "name", "");
        String avatar = SharedPrefUtils.getString(requireContext(), "avatar", "");

        binding.profileNameTv.setText(name);
        Glide.with(this)
                .load(avatar)
                .placeholder(R.drawable.ic_user_profile)
                .into(binding.profileImageIv);

        binding.profileImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenCameraOrGallery();
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialog.getInstance(requireContext())
                        .setStyle(Styles.STANDARD)
                        .setHeading("Logout")
                        .setDescription("Are you sure you want to logout?")
                        .setPositiveButtonText("Yes")
                        .setPositiveButtonBackground(com.saadahmedsoft.popupdialog.R.color.colorBlack)
                        .setNegativeButtonBackground(com.saadahmedsoft.popupdialog.R.color.colorWhite)
                        .setNegativeButtonText("No")
                        .setCancelable(true)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onNegativeClicked(Dialog dialog) {
                                super.onNegativeClicked(dialog);
                                if (dialog != null) dialog.dismiss();
                            }

                            @Override
                            public void onPositiveClicked(Dialog dialog) {
                                super.onPositiveClicked(dialog);
                                if (dialog != null) dialog.dismiss();
                                SharedPrefUtils.clear(requireContext());
                                Intent intent = new Intent(requireContext(), AuthActivity.class);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                        });
            }
        });
        final String storeAddress = "123 Nguyễn Văn Cừ, Cần Thơ";

        // 2. Xử lý sự kiện click mở Google Maps
        binding.storeAddressCv.setOnClickListener(v -> {
            // Dạng search theo địa chỉ text
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(storeAddress));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            // Check xem thiết bị có cài Google Maps không
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Nếu không có Google Maps thì mở intent bình thường
                startActivity(new Intent(Intent.ACTION_VIEW, gmmIntentUri));
            }
        });

    }

    private void checkPermissionAndOpenCameraOrGallery() {
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        openCameraOrGallery();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPermissions(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_MEDIA_IMAGES
                )
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .check();
    }

    private void openCameraOrGallery() {
        ImagePicker.with(this)
                .crop()
                .cropSquare()
                .galleryMimeTypes(new String[]{"image/png", "image/jpeg"})
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    getResult.launch(intent);
                    return null;
                });
    }

    private void uploadImage() {
        if (imageUri != null) {
            String realPath = RealPathUtil.getRealPath(requireContext(), imageUri);
            File avatarFile = realPath != null ? new File(realPath) : null;
            if (avatarFile != null) {
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), avatarFile);
                MultipartBody.Part requestUploadAvatar = MultipartBody.Part.createFormData(
                        "avatar",
                        avatarFile.getName(),
                        reqFile
                );
                viewModel.updateUserImageProfile(
                        token,
                        userId,
                        requestUploadAvatar,
                        new ProfileViewModel.ProfileUpdateCallback() {
                            @Override
                            public void onSuccess(User user) {
                                if (user != null && user.getAvatar() != null) {
                                    SharedPrefUtils.saveString(requireContext(), "avatar", user.getAvatar());
                                    Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure() {
                                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
