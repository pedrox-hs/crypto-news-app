package com.pedrenrique.cryptonews.mock

import android.content.Context
import android.net.Uri
import com.pedrenrique.cryptonews.ext.readAsset
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockDispatcher(private val context: Context) : Dispatcher() {

    companion object {
        const val IMAGE_ASSET = "bitcoin.jpg"

        const val IMAGE_ENDPOINT = "/image.jpg"
    }

    private val responseFilesByPath: Map<String, String> = mapOf(
        IMAGE_ENDPOINT to IMAGE_ASSET
    )

    private val queue = arrayListOf<String>()

    fun enqueue(asset: String) {
        queue.add(asset)
    }

    override fun dispatch(request: RecordedRequest?): MockResponse {
        val errorResponse = MockResponse().setResponseCode(404)

        val pathWithoutQueryParams = Uri.parse(request?.path).path ?: return errorResponse
        val responseFile = responseFilesByPath[pathWithoutQueryParams]
            ?: queue.takeIf { it.isNotEmpty() }?.removeAt(0)

        return if (responseFile != null) {
            val responseBody = context.readAsset(responseFile)
            MockResponse().setResponseCode(200).setBody(responseBody)
        } else {
            errorResponse
        }
    }
}