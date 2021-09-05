package com.baiganov.devlife.viemodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.baiganov.devlife.data.Repository
import com.baiganov.devlife.models.Result
import com.baiganov.devlife.models.ResultItem
import com.baiganov.devlife.models.Section
import com.baiganov.devlife.util.Constants.Companion.SECTION_HOT
import com.baiganov.devlife.util.Constants.Companion.SECTION_LATEST
import com.baiganov.devlife.util.Constants.Companion.SECTION_TOP
import com.baiganov.devlife.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _lastPostsResponse: MutableLiveData<NetworkResult<List<ResultItem>>> = MutableLiveData()
    private val _topPostResponse: MutableLiveData<NetworkResult<List<ResultItem>>> = MutableLiveData()
    private val _hotPostResponse: MutableLiveData<NetworkResult<List<ResultItem>>> = MutableLiveData()

    val lastPostsResponse: LiveData<NetworkResult<List<ResultItem>>> = _lastPostsResponse
    val topPostResponse: LiveData<NetworkResult<List<ResultItem>>> =  _topPostResponse
    val hotPostResponse: LiveData<NetworkResult<List<ResultItem>>> = _hotPostResponse

    fun getData(section: String, page: Int) = viewModelScope.launch {
        getPostsSafeCall(section, page)
    }

    fun clearDb() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAll()
    }

    private suspend fun getPostsSafeCall(section: String, page: Int) {
        showLoading(section)
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(section = section, page = page)
                val cacheData: MutableList<ResultItem> = repository.local.getPosts(section) as MutableList<ResultItem>
                insertInLiveData(section, response, cacheData)
            } catch (e: Exception) {
               showError(section)
            }
        } else {
            showNoInternetConnection(section)
        }
    }


    private fun offlineCache(data: List<ResultItem>, section: String) {
        val sections = mutableListOf<Section>()
        data.forEach { post ->
            sections.add(Section(fkId = post.id, section = section))
        }
        insertPosts(data, sections)
    }

    private fun insertPosts(data: List<ResultItem>, sections: List<Section>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(data)
            repository.local.insertSection(sections)
        }
    }

    private fun insertInLiveData(section: String, response: Response<Result>, cacheData: MutableList<ResultItem>) {
        when(section) {
            SECTION_LATEST -> {
                _lastPostsResponse.value = handlePostsResponse(response, cacheData)
                val result = _lastPostsResponse.value?.data
                if (result != null) {
                    offlineCache(result, section)
                }
            }
            SECTION_TOP -> {
                _topPostResponse.value = handlePostsResponse(response, cacheData)
                val result = topPostResponse.value?.data
                if (result != null) {
                    offlineCache(result, section)
                }
            }
            SECTION_HOT -> {
                _hotPostResponse.value = handlePostsResponse(response, cacheData)
                val result = hotPostResponse.value?.data
                if (result != null) {
                    offlineCache(result, section)
                }
            }
        }
    }

    private fun handlePostsResponse(response: Response<Result>, cacheData: MutableList<ResultItem>): NetworkResult<List<ResultItem>> {
        when {
            response.message().toString().contains(CHECKING_CONTAINS) -> {
                return NetworkResult.Error(message = MESSAGE_TIMEOUT)
            }
            response.body()?.result.isNullOrEmpty() -> {
                return NetworkResult.Error(message = MESSAGE_NO_FOUND)
            }
            response.isSuccessful -> {
                val result: List<ResultItem> = response.body()!!.result
                cacheData.addAll(result)
                return NetworkResult.Success(cacheData)
            }
            else -> {
                return NetworkResult.Error(message = response.message())
            }
        }
    }

    private fun showError(section: String) {
        when(section) {
            SECTION_LATEST -> {
                _lastPostsResponse.value = NetworkResult.Error(message = MESSAGE_NO_FOUND)
            }
            SECTION_TOP -> {
                _topPostResponse.value = NetworkResult.Error(message = MESSAGE_NO_FOUND)
            }
            SECTION_HOT -> {
                _hotPostResponse.value = NetworkResult.Error(message = MESSAGE_NO_FOUND)
            }
        }
    }
    private fun showLoading(section: String) {
        when(section) {
            SECTION_LATEST -> {
                _lastPostsResponse.value = NetworkResult.Loading()
            }
            SECTION_TOP -> {
                _topPostResponse.value = NetworkResult.Loading()
            }
            SECTION_HOT -> {
                _hotPostResponse.value = NetworkResult.Loading()
            }
        }
    }

    private fun showNoInternetConnection(section: String) {
        when(section) {
            SECTION_LATEST -> {
                _lastPostsResponse.value = NetworkResult.Error(message = MESSAGE_NOT_NETWORK)
            }
            SECTION_TOP -> {
                _topPostResponse.value = NetworkResult.Error(message = MESSAGE_NOT_NETWORK)
            }
            SECTION_HOT -> {
                _hotPostResponse.value = NetworkResult.Error(message = MESSAGE_NOT_NETWORK)
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    companion object {
        private const val MESSAGE_NOT_NETWORK = "No Network Connection"
        private const val MESSAGE_NO_FOUND = "Posts not found"
        private const val MESSAGE_TIMEOUT = "Timeout"
        private const val CHECKING_CONTAINS = "timeout"
    }
}