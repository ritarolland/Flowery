package com.example.prac1.data.api

import com.example.prac1.data.api.model.CartItemDataModel
import com.example.prac1.data.api.model.FavouriteDataModel
import com.example.prac1.data.api.model.FlowerDataModel
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.data.api.requests.LoginRequest
import com.example.prac1.data.api.requests.OrderCartItemRequest
import com.example.prac1.data.api.requests.RefreshTokenRequest
import com.example.prac1.data.api.requests.UpdateCartItemRequest
import com.example.prac1.data.api.responses.ImageUploadResponse
import com.example.prac1.data.api.responses.LoginResponse
import com.example.prac1.data.api.responses.RefreshTokenResponse
import com.example.prac1.data.api.responses.UserIdResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface for interacting with the flower API using Retrofit.
 * This interface defines all the API endpoints and their corresponding HTTP methods
 * for performing actions related to flowers, cart items, orders, users, and favorites.
 * It handles requests for retrieving, adding, updating, and deleting data,
 * and also supports file uploads.
 *
 * @author Eduard Pavlov
 */
interface FlowerApi {

    /**
     * Fetches the catalog of flowers.
     *
     * @return A [Response] containing a list of [FlowerDataModel] objects representing the catalog of flowers.
     */
    @GET("/rest/v1/flower_items")
    suspend fun getFlowersCatalog(): Response<List<FlowerDataModel>>

    /**
     * Fetches cart items associated with the user's order.
     *
     * @param token Authorization token to authenticate the user.
     * @return A [Response] containing a list of [CartItemDataModel] objects representing the cart items.
     */
    @GET("/rest/v1/cart_items?order_id=is.null")
    suspend fun getCartItems(
        @Header("Authorization") token: String
    ): Response<List<CartItemDataModel>>

    /**
     * Adds an item to the cart.
     *
     * @param token Authorization token to authenticate the user.
     * @param cartItemDataModel The cart item to be added.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST("/rest/v1/cart_items")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addCartItem(
        @Header("Authorization") token: String,
        @Body cartItemDataModel: CartItemDataModel
    ): Response<Unit>

    /**
     * Signs in the user by providing their credentials.
     *
     * @param loginRequest The login credentials for the user.
     * @return A [Response] containing the login response with user authentication details.
     */
    @POST("/auth/v1/token?grant_type=password")
    @Headers("Content-Type: application/json")
    suspend fun signIn(@Body loginRequest: LoginRequest): Response<LoginResponse>

    /**
     * Signs up a new user.
     *
     * @param loginRequest The sign-up details for the user.
     * @return A [Response] containing the sign-up response with user authentication details.
     */
    @POST("/auth/v1/signup")
    @Headers("Content-Type: application/json")
    suspend fun signUp(@Body loginRequest: LoginRequest): Response<LoginResponse>

    /**
     * Refreshes the user's token for continued authentication.
     *
     * @param refreshTokenRequest The refresh token request details.
     * @return A [Response] containing the refreshed token response.
     */
    @POST("/auth/v1/token?grant_type=refresh_token")
    @Headers("Content-Type: application/json")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>

    /**
     * Fetches the user information.
     *
     * @param token Authorization token to authenticate the user.
     * @return A [Response] containing the user ID information.
     */
    @GET("/auth/v1/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserIdResponse>

    /**
     * Adds a new user to the system.
     *
     * @param token Authorization token to authenticate the user.
     * @param user The user details to be added.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST("/rest/v1/users")
    suspend fun addUser(
        @Header("Authorization") token: String,
        @Body user: UserInfoDataModel
    ): Response<Unit>

    /**
     * Fetches user information based on the provided user ID.
     *
     * @param userId The ID of the user whose information is to be fetched.
     * @return A [Response] containing the list of user information.
     */
    @GET("/rest/v1/users")
    suspend fun getUserInfo(
        @Query("id") userId: String
    ): Response<List<UserInfoDataModel>>

    /**
     * Loads the user's favorite flowers.
     *
     * @param token Authorization token to authenticate the user.
     * @param userId The user ID to fetch the favorite flowers for.
     * @return A [Response] containing a list of favorite flowers.
     */
    @GET("/rest/v1/favourites")
    suspend fun loadFavourites(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String
    ): Response<List<FavouriteDataModel>>

    /**
     * Fetches all orders placed by a specific user.
     *
     * @param token Authorization token to authenticate the user.
     * @param userId The user ID to fetch the orders for.
     * @return A [Response] containing a list of the user's orders.
     */
    @GET("/rest/v1/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String
    ): Response<List<OrderDataModel>>

    /**
     * Fetches a specific order by its ID.
     *
     * @param token Authorization token to authenticate the user.
     * @param orderId The ID of the order to fetch.
     * @return A [Response] containing the order details.
     */
    @GET("/rest/v1/orders")
    suspend fun getOrderById(
        @Header("Authorization") token: String,
        @Query("id") orderId: String
    ): Response<List<OrderDataModel>>

    /**
     * Fetches all cart items associated with a specific order.
     *
     * @param token Authorization token to authenticate the user.
     * @param orderId The order ID to fetch cart items for.
     * @return A [Response] containing a list of cart items.
     */
    @GET("rest/v1/cart_items")
    suspend fun getOrderItems(
        @Header("Authorization") token: String,
        @Query("order_id") orderId: String
    ): Response<List<CartItemDataModel>>

    /**
     * Adds a flower to the user's favorites.
     *
     * @param token Authorization token to authenticate the user.
     * @param favouriteDataModel The favorite flower data to be added.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST("/rest/v1/favourites")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addFavourite(
        @Header("Authorization") token: String,
        @Body favouriteDataModel: FavouriteDataModel
    ): Response<Unit>

    /**
     * Removes a flower from the user's favorites.
     *
     * @param token Authorization token to authenticate the user.
     * @param id The ID of the favorite to be removed.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @DELETE("/rest/v1/favourites")
    suspend fun deleteFavourite(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): Response<Unit>

    /**
     * Fetches a specific flower by its ID.
     *
     * @param token Authorization token to authenticate the user.
     * @param flowerId The ID of the flower to fetch.
     * @return A [Response] containing the details of the flower.
     */
    @GET("/rest/v1/flower_items")
    suspend fun getFlowerById(
        @Header("Authorization") token: String,
        @Query("id") flowerId: String
    ): Response<List<FlowerDataModel>>

    /**
     * Deletes an item from the user's cart.
     *
     * @param token Authorization token to authenticate the user.
     * @param itemId The ID of the item to be deleted from the cart.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @DELETE("/rest/v1/cart_items")
    suspend fun deleteItemFromCart(
        @Header("Authorization") token: String,
        @Query("id") itemId: String
    ): Response<Unit>

    /**
     * Updates the quantity of an item in the cart.
     *
     * @param itemId The ID of the item to be updated.
     * @param token Authorization token to authenticate the user.
     * @param updateCartItemRequest The request body containing the updated quantity.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @PATCH("/rest/v1/cart_items")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun updateCartItemQuantity(
        @Query("id") itemId: String,
        @Header("Authorization") token: String,
        @Body updateCartItemRequest: UpdateCartItemRequest
    ): Response<Unit>

    /**
     * Orders a cart item for the user.
     *
     * @param itemId The ID of the cart item to be ordered.
     * @param token Authorization token to authenticate the user.
     * @param orderCartItemRequest The request body containing the order details.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @PATCH("/rest/v1/cart_items")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun orderCartItem(
        @Query("id") itemId: String,
        @Header("Authorization") token: String,
        @Body orderCartItemRequest: OrderCartItemRequest
    ): Response<Unit>

    /**
     * Adds a new order for the user.
     *
     * @param token Authorization token to authenticate the user.
     * @param orderDataModel The order data to be added.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST("/rest/v1/orders")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addOrder(
        @Header("Authorization") token: String,
        @Body orderDataModel: OrderDataModel
    ) : Response<Unit>

    /**
     * Uploads a file (e.g., an image) to the storage.
     *
     * @param token Authorization token to authenticate the user.
     * @param fileName The name of the file to be uploaded.
     * @param file The file to be uploaded.
     * @return A [Response] containing the file upload response details.
     */
    @Multipart
    @POST("storage/v1/object/userImages/{file_name}")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Path("file_name") fileName: String,
        @Part file: MultipartBody.Part
    ): Response<ImageUploadResponse>

    /**
     * Adds user information.
     *
     * @param token Authorization token to authenticate the user.
     * @param userInfoDataModel The user info data to be added.
     * @return A [Response] indicating the success or failure of the operation.
     */
    @POST("/rest/v1/users")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addUserInfo(
        @Header("Authorization") token: String,
        @Body userInfoDataModel: UserInfoDataModel
    ) : Response<Unit>

}