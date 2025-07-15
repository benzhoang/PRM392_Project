package com.example.prm392_project.ui.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivityMainBinding;
import com.example.prm392_project.ui.activities.auth.AuthActivity;
import com.example.prm392_project.util.SharedPrefUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogin = SharedPrefUtils.getBoolean(this, "isLogin", false);
        if (!isLogin) {
            // Nếu chưa đăng nhập, chuyển sang màn hình đăng nhập
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
            return; // Ngăn không cho vào home/MainActivity nữa!
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Edge-to-edge
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        setupBottomNav();

        // Nếu Android 13+, kiểm tra & xin quyền thông báo trước khi show
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            } else {
                checkCartAndShowNotification();
            }
        } else {
            checkCartAndShowNotification();
        }
    }

    // Handle user's permission response
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkCartAndShowNotification();
            }
        }
    }

    private void setupBottomNav() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment is null! Check your layout id!");
        }
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    }

    private void checkCartAndShowNotification() {
        String cartJson = SharedPrefUtils.getString(this, "cart_items", "[]");
        boolean hasCartItems = false;
        try {
            org.json.JSONArray array = new org.json.JSONArray(cartJson);
            hasCartItems = array.length() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hasCartItems) {
            showCartNotification();
        }
    }

    private void showCartNotification() {
        String channelId = "cart_channel_id";
        String channelName = "Cart Notifications";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo channel cho Android O trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Intent để mở Cart/MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("show_cart", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle("Nhắc nhở giỏ hàng")
                .setContentText("Bạn có sản phẩm trong giỏ hàng, tranh thủ đặt thôiii!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1001, builder.build());
    }
}
