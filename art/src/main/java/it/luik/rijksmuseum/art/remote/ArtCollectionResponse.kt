package it.luik.rijksmuseum.art.remote

import androidx.annotation.Keep

@Keep
internal class ArtCollectionResponse(
    val count: Int,
    val artObjects: List<ArtObjectResponse>
)

@Keep
internal class ArtObjectResponse(
    val id: String,
    val longTitle: String,
    val principalOrFirstMaker: String,
    val webImage: WebImageResponse
)

@Keep
internal class WebImageResponse(
    val url: String
)
