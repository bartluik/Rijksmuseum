package it.luik.rijksmuseum.ui.art.collection

import it.luik.rijksmuseum.art.domain.collection.ArtSummary


internal fun List<ArtSummary>.toOverviewItems(): List<CollectionItem> = groupBy { it.author }
    .flatMap { (author, artItems) ->
        listOf(CollectionItem.HeaderCollectionItem(author)) +
                artItems.map {
                    CollectionItem.ArtCollectionItem(
                        id = it.id,
                        title = it.title,
                        imageUrl = it.imageUrl
                    )
                }
    }
