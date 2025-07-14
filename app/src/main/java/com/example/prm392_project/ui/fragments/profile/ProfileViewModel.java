package com.example.prm392_project.ui.fragments.profile;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.user.User;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private final CosmeticsRepository cosmeticsRepository;

    @Inject
    public ProfileViewModel(CosmeticsRepository cosmeticsRepository) {
        this.cosmeticsRepository = cosmeticsRepository;
    }

    public void updateUserImageProfile(
            String token,
            String userId,
            MultipartBody.Part image,
            final ProfileUpdateCallback callback
    ) {
        // Giả sử CosmeticsRepository.updateUserImageProfile trả về retrofit2.Call<User>
        // Nếu vẫn dùng suspend function thì cần sử dụng executor/coroutine bridge

        Call<User> call = cosmeticsRepository.updateUserImageProfile(token, userId, image);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.d("ProfileViewModel", "User image profile updated: " + response.body());
                    callback.onSuccess(response.body());
                } else {
                    Log.e("ProfileViewModel", "User image update failed");
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ProfileViewModel", "Error updating user image profile: " + t.getMessage());
                callback.onFailure();
            }
        });
    }

    public interface ProfileUpdateCallback {
        void onSuccess(User user);
        void onFailure();
    }
}
