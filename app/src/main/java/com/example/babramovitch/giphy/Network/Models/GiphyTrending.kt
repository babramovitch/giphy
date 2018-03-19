package com.example.babramovitch.giphy.Network.Models

/**
* Created by babramovitch on 2018-03-14.
*
*/
data class GiphyTrending(
        val data: MutableList<Data>,
        val pagination: Pagination,
        val meta: Meta
)

data class Meta(
        val status: Int,
        val msg: String,
        val response_id: String
)

data class Pagination(
        val total_count: Int,
        val count: Int,
        val offset: Int
)

data class Data(
        val type: String,
        val id: String,
        val slug: String,
        val url: String,
        val bitly_gif_url: String,
        val bitly_url: String,
        val embed_url: String,
        val username: String,
        val source: String,
        val rating: String,
        val content_url: String,
        val source_tld: String,
        val source_post_url: String,
        val is_indexable: Int,
        val is_sticker: Int,
        val import_datetime: String,
        val trending_datetime: String,
        val images: Images,
        val title: String
)

data class Images(
        val fixed_height: FixedHeight
)

data class FixedHeight(
        val url: String,
        val width: String,
        val height: String,
        val size: String,
        val mp4: String,
        val mp4_size: String,
        val webp: String,
        val webp_size: String
)