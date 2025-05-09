package io.madeformaid.user.grpc.client

import affiliation.Shop.GetShopNamesRequest
import affiliation.Shop.GetShopNamesResponse
import affiliation.ShopServiceGrpc
import org.springframework.stereotype.Service

@Service
class ShopNameFetcher(
    private val shopServiceStub: ShopServiceGrpc.ShopServiceBlockingStub
) {
    fun fetchShopNames(shopIds: List<String>): Map<String, String> {
        val request = GetShopNamesRequest.newBuilder()
            .addAllShopIds(shopIds)
            .build()

        val response: GetShopNamesResponse = shopServiceStub.getShopNames(request)

        return response.shopNamesList.associate { it.shopId to it.shopName }
    }
}
