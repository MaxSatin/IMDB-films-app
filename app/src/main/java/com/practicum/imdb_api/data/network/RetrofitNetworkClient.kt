package com.practicum.imdb_api.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.practicum.imdb_api.data.NetworkClient
import com.practicum.imdb_api.data.dto.movies_search_request.MoviesSearchRequest
import com.practicum.imdb_api.data.dto.Response
import com.practicum.imdb_api.data.dto.cast_request.CastInfoRequest
import com.practicum.imdb_api.data.dto.movies_details_request.MoviesDetailsRequest
import com.practicum.imdb_api.data.dto.persons_search_request.PersonsSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val imdbService: IMDbApiService,
) : NetworkClient {


    override suspend fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }

        if ((dto !is MoviesSearchRequest)
            && (dto !is MoviesDetailsRequest)
            && (dto !is CastInfoRequest)
            && (dto !is PersonsSearchRequest)
        ) {
            return Response().apply { resultCode = 400 }
        }
        return when (dto) {

            is PersonsSearchRequest -> withContext(Dispatchers.IO) {
                try {
                    val response = imdbService.searchPersons(dto.expression)
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }

//            is MoviesSearchRequest -> withContext(Dispatchers.IO) {
//                try {
//                    val response = imdbService.searchMovies(dto.expression)
//                    response.apply { resultCode = 200 }
//                } catch (e: Throwable) {
//                    Response().apply { resultCode = 500 }
//                }
//            }
//            is MoviesDetailsRequest -> imdbService.getMovieDetails(dto.movieId).execute()
//            is CastInfoRequest -> imdbService.getCastList(dto.movieId).execute()
            else -> return Response().apply { resultCode = 400 }
        }

//        val body = response.body()
//        return if (body != null) {
//            body.apply { resultCode = response.code() }
//        } else {
//            Response().apply { resultCode = response.code() }
//        }
    }


//    (dto is MoviesSearchRequest) ->
//    {
//        val response = imdbService.searchMovies(dto.expression).execute()
//        val body = response.body()
//        return if (body != null) {
//            body.apply { resultCode = response.code() }
//        } else {
//            Response().apply { resultCode = response.code() }
//        }
//    }
//
//    (dto is MoviesDetailsRequest) ->
//    {
//        val response = imdbService.getMovieDetails(dto.movieId).execute()
//        val body = response.body()
//        return if (body != null) {
//            body.apply { resultCode = response.code() }
//        } else {
//            Response().apply {
//                resultCode = response.code()
//            }
//        }
//    }
//
//    (dto is CastInfoRequest) ->
//    {
//        val response = imdbService.getCastList(dto.movieId).execute()
//        Log.d("castInfo", "${response}")
//        val body = response.body()
//        return if (body != null) {
//            body.apply { resultCode = response.code() }
//        } else {
//            Response().apply {
//                resultCode = response.code()
//            }
//        }
//    }
//    (dto is PersonsSearchRequest) ->
//    {
//        val response = imdbService.searchPersons(dto.expression).execute()
//        Log.d("personsSearch", "${response}")
//        val body = response.body()
//        return if (body != null) {
//            body.apply { resultCode = response.code() }
//        } else {
//            Response().apply { resultCode = response.code() }
//        }
//    }
//    else -> return Response().apply
//    { resultCode = 400 }
//}
//}


    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true

            }
        }
        return false
    }
}

