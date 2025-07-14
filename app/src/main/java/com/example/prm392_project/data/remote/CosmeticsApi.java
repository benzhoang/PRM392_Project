package com.example.prm392_project.data.remote;

import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.data.model.main.cart.CartRequest;
import com.example.prm392_project.data.model.main.cart.CartResponse;
import com.example.prm392_project.data.model.main.category.Category;
import com.example.prm392_project.data.model.main.category.CategoryResponse;
import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.model.auth.sign_in.SignInRequest;
import com.example.prm392_project.data.model.auth.sign_in.SignInResponse;
import com.example.prm392_project.data.model.auth.sign_up.SignUpRequest;
import com.example.prm392_project.data.model.auth.sign_up.SignUpResponse;
import com.example.prm392_project.data.model.main.user.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CosmeticsApi {

    // USER AUTH
    @GET("/users/checkUserExits/{email}")
    Call<Boolean> checkUserExist(@Path("email") String email);

    @POST("/api/users/signIn")
    Call<SignInResponse> signIn(@Body SignInRequest signInRequest);

    @POST("/api/users/signUp")
    Call<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);

    // CATEGORY
    @GET("/api/categories")
    Call<List<Category>> getCategories();

    @GET("/api/categories/{id}")
    Call<CategoryResponse> getCategoryById(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    // PRODUCT
    @GET("/api/products")
    Call<List<Product>> getProducts();

    @GET("/api/products/{id}")
    Call<Product> getProductById(
            @Path("id") String id
    );

    @GET("/api/products/search")
    Call<List<Product>> searchProductByName(
            @Header("Authorization") String token,
            @Query("name") String name
    );

    @GET("/api/products/category/{categoryId}")
    Call<List<Product>> getProductsByCategoryId(
            @Header("Authorization") String token,
            @Path("categoryId") String categoryId
    );

    @PATCH("/products/{id}")
    Call<Product> updateProductStock(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body Map<String, Integer> map
    );

    // CART
    @GET("/carts/user/{id}")
    Call<CartResponse> getCartItemsByUserId(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @POST("/carts")
    Call<Cart> addProductToCart(
            @Header("Authorization") String token,
            @Body CartRequest cart
    );

    @PATCH("/carts/{id}")
    Call<Cart> updateCartItemQuantity(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body Map<String, Integer> quantity
    );

    @DELETE("/carts/{id}")
    Call<Void> deleteCartItem(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @DELETE("/carts/user/{userId}")
    Call<Void> deleteAllCartItemsByUserId(
            @Header("Authorization") String token,
            @Path("userId") String id
    );

    // ORDER
    @GET("/orders/user/{userId}")
    Call<List<Order>> getOrdersByUserId(
            @Header("Authorization") String token,
            @Path("userId") String userId
    );

    @GET("/api/orders/{id}")
    Call<Order> getOrder(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    @POST("/api/orders")
    Call<Order> createOrder(
            @Header("Authorization") String token,
            @Body Order order
    );

    @PATCH("/api/orders/{id}")
    Call<Order> updateOrderStatus(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body String status
    );

    @DELETE("/api/orders/{id}")
    Call<Void> deleteOrder(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    // USER PROFILE
    @Multipart
    @PATCH("users/updateUser/{id}")
    Call<User> updateAvatar(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Part MultipartBody.Part avatar
    );
}
