package com.example.caca_pie.data.api

import com.example.caca_pie.data.model.MenuModel
import com.example.caca_pie.data.model.OrderModel
import com.example.caca_pie.data.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApiService {

    // ================= 1. TABEL USERS (REGISTER & LOGIN) =================
    @POST("users")
    suspend fun registerUser(@Body user: UserModel): Response<Unit>

    @GET("users")
    suspend fun loginUser(
        @Query("username") usernameFilter: String,
        @Query("password") passwordFilter: String
    ): Response<List<UserModel>>

    // ================= 2. TABEL MENUS (CRUD MENU KAFE) =================
    @GET("menus")
    suspend fun getMenus(
        @Query("order") order: String = "id.asc"
    ): Response<List<MenuModel>>

    @POST("menus")
    suspend fun createMenu(@Body menu: MenuModel): Response<Unit>

    @PATCH("menus")
    suspend fun updateMenu(
        @Query("id") idFilter: String,
        @Body menu: MenuModel
    ): Response<Unit>

    @DELETE("menus")
    suspend fun deleteMenu(
        @Query("id") idFilter: String
    ): Response<Unit>

    // ================= 3. TABEL ORDERS (CRUD RIWAYAT PESANAN) =================
    @GET("orders")
    suspend fun getOrders(
        @Query("status") statusFilter: String? = null,
        @Query("order") order: String = "id.desc"
    ): Response<List<OrderModel>>

    @POST("orders")
    suspend fun createOrder(@Body order: OrderModel): Response<Unit>

    @PATCH("orders")
    suspend fun updateOrder(
        @Query("id") idFilter: String,
        @Body order: OrderModel
    ): Response<Unit>

    @DELETE("orders")
    suspend fun deleteOrder(
        @Query("id") idFilter: String
    ): Response<Unit>
}
