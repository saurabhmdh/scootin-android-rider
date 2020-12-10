package com.scootin.pages

import androidx.paging.PagingSource
import com.scootin.network.api.APIService
import com.scootin.network.response.UnAssignedOrderResponse
import retrofit2.Response
import timber.log.Timber
import java.lang.RuntimeException

class UnassignedOrderDataSource (
    private val apiService: APIService
) : PagingSource<Int, UnAssignedOrderResponse>() {

    var initialOffset: Int = 0
    var count: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnAssignedOrderResponse> {
        return try {

            val offset = params.key ?: initialOffset

            val alreadyLoaded = offset * params.loadSize

            val loadsize = if (count != 0 && alreadyLoaded > count) {
                count - (alreadyLoaded - params.loadSize)
            } else {
                params.loadSize
            }
            Timber.i("offset $offset alrerady loaded $alreadyLoaded loadsize = ${loadsize}" )

            val response: Response<List<UnAssignedOrderResponse>> = apiService.getAllUnAssigned(offset,loadsize)
            val data = response.body()
            if (response.isSuccessful && data != null) {
                count = response.headers().get("x-total-count")?.toInt() ?: 0

                val nextOffset = if (alreadyLoaded >= count) {
                    null
                } else {
                    offset + 1
                }

                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = nextOffset
                )
            } else {
                LoadResult.Error(RuntimeException("${response.code()} ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}