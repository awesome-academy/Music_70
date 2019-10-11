package vn.sunasterisk.music_70.constant

object Constant {
    const val BASE_HEADER_URL= "http://api.soundcloud.com/tracks"
    const val BASE_GENRE_URL = "https://api-v2.soundcloud.com/charts?kind=%s&genre=%s&client_id=%s&limit=%d&offset=%d"
    const val BASE_SEARCH_URL = "?q=%s&client_id=%s&limit=%d&offset=%d"
    const val BASE_DOWNLOAD_URL = "/%d/download?client_id=%s"
    const val BASE_STREAM_URL = "/%d/stream?client_id=%s"
    const val BASE_TRENDING_URL = "https://api-v2.soundcloud.com/charts?kind=%s&genre=%s&client_id=%s"
    const val PLAYLIST = "https://api.soundcloud.com/playlists?client_id=%s"
    const val GENRES_COUNTRY = "soundcloud:genres:country"
    const val GENRES_ALL_MUSIC = "soundcloud:genres:all-music"
    const val GENRES_ALL_AUDIO = "soundcloud:genres:all-audio"
    const val GENRES_ROCK = "soundcloud:genres:alternativerock"
    const val GENRES_AMBIENT = "soundcloud:genres:ambient"
    const val GENRES_CLASSICAL = "soundcloud:genres:classical"
    const val IMAGE_LARGE = "large"
    const val IMAGE_FULL = "t500x500"
    const val IMAGE_SMALL = "small"
    const val KIND_TOP = "top"
    const val KIND_TRENDING= "trending"
    const val TRACKS_DIRECTORY = "tracks"
}
