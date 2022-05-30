package eu.hanna.recyclerviewwithretrofit


import eu.hanna.recyclerviewwithretrofit.Constants.END_POINT_DELETE
import eu.hanna.recyclerviewwithretrofit.Constants.END_POINT_GET
import eu.hanna.recyclerviewwithretrofit.Constants.END_POINT_POST
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface PostApi {

    @GET(END_POINT_GET)
    suspend fun getAllPosts(): Response<List<PostItem>>

    @POST(END_POINT_POST)
    suspend fun addPost(
        @Body postItem: PostItem
    ): Response<PostItem>

    @DELETE(END_POINT_DELETE)
    suspend fun deletePost(): Response<ResponseBody>
}