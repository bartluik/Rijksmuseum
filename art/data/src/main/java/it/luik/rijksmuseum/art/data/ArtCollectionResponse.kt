package it.luik.rijksmuseum.art.data

import androidx.annotation.Keep

@Keep
internal class ArtCollectionResponse(
    val count: Int,
    val artObjects: List<ArtObjectResponse>
)

@Keep
internal class ArtObjectResponse(
    val id: String,
    val objectNumber: String,
    val title: String,
    val longTitle: String,
    val principalOrFirstMaker: String,
    val webImage: WebImageResponse? = null
)

@Keep
internal class ArtDetailsResponse(
    val artObject: ArtDetailsObjectResponse
)

@Keep
internal class ArtDetailsObjectResponse(
    val id: String,
    val objectNumber: String,
    val title: String,
    val description: String? = null,
    val principalOrFirstMaker: String,
    val webImage: WebImageResponse? = null
)

@Keep
internal class WebImageResponse(
    val url: String
)
