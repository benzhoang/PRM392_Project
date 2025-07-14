package com.example.prm392_project.data.repository;

import com.example.prm392_project.data.model.auth.sign_in.SignInResponse;
import com.example.prm392_project.data.model.auth.sign_up.SignUpResponse;
import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.data.model.main.cart.CartRequest;
import com.example.prm392_project.data.model.main.cart.CartResponse;
import com.example.prm392_project.data.model.main.category.Category;
import com.example.prm392_project.data.model.main.category.CategoryResponse;
import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.data.model.auth.sign_in.SignInRequest;
import com.example.prm392_project.data.model.auth.sign_up.SignUpRequest;
import com.example.prm392_project.data.model.main.order.OrderResponse;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.model.main.user.User;
import com.example.prm392_project.data.remote.CosmeticsApi;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

public class CosmeticsRepository {

    private final CosmeticsApi cosmeticsApi;

    @Inject
    public CosmeticsRepository(CosmeticsApi cosmeticsApi) {
        this.cosmeticsApi = cosmeticsApi;
    }

    public retrofit2.Call<Boolean> checkUserExist(String email) {
        return cosmeticsApi.checkUserExist(email);
    }

    public retrofit2.Call<SignInResponse> signIn(SignInRequest signInRequest) {
        return cosmeticsApi.signIn(signInRequest);
    }

    public retrofit2.Call<SignUpResponse> signUp(SignUpRequest signUpRequest) {
        return cosmeticsApi.signUp(signUpRequest);
    }

    public retrofit2.Call<List<Category>> getCategories() {
        return cosmeticsApi.getCategories();
    }

    public retrofit2.Call<CategoryResponse> getCategoryById(String token, String id) {
        return cosmeticsApi.getCategoryById(token, id);
    }

    public retrofit2.Call<List<Product>> getProducts() {
        return cosmeticsApi.getProducts();
    }

    public retrofit2.Call<Product> getProductById( String id) {
        return cosmeticsApi.getProductById(id);
    }

    public retrofit2.Call<List<Product>> searchProductByName(String token, String name) {
        return cosmeticsApi.searchProductByName(token, name);
    }

    public retrofit2.Call<List<Product>> getProductsByCategory(String token, String categoryId) {
        return cosmeticsApi.getProductsByCategoryId(token, categoryId);
    }


    public retrofit2.Call<?> updateProductStock(String token, String id, Map<String, Integer> map) {
        return cosmeticsApi.updateProductStock(token, id, map);
    }

    public retrofit2.Call<CartResponse> getAllCarts(String token, String userId) {
        return cosmeticsApi.getCartItemsByUserId(token, userId);
    }

    public retrofit2.Call<Cart> createCart(String token, CartRequest cartRequest) {
        return cosmeticsApi.addProductToCart(token, cartRequest);
    }

    public retrofit2.Call<?> updateCart(String token, String id, Map<String, Integer> map) {
        return cosmeticsApi.updateCartItemQuantity(token, id, map);
    }

    public retrofit2.Call<?> deleteCart(String token, String id) {
        return cosmeticsApi.deleteCartItem(token, id);
    }

    public retrofit2.Call<?> deleteAllCart(String token, String userId) {
        return cosmeticsApi.deleteAllCartItemsByUserId(token, userId);
    }

    public Call<OrderResponse> createOrder(String token, Map<String, Object> body) {
        return cosmeticsApi.createOrder( token, body);
    }

    public retrofit2.Call<List<Order>> getOrders(String token, String userId) {
        return cosmeticsApi.getOrdersByUserId(token, userId);
    }

    public retrofit2.Call<Order> getOrderById(String token, String id) {
        return cosmeticsApi.getOrder(token, id);
    }

    public Call<User> updateUserImageProfile(String token, String userId, MultipartBody.Part image) {
        return cosmeticsApi.updateAvatar(token, userId, image);
    }

}
