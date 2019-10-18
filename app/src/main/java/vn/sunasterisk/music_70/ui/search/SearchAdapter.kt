package vn.sunasterisk.music_70.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_song.view.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.LoadImage

class SearchAdapter(val onItemClicked: (List<Track>, Int) -> Unit) :
    ListAdapter<Track, SearchAdapter.ViewHolder>(TrackDiffCallBack()) {

    private var listSong = emptyList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(itemView)
    }

    override fun submitList(list: MutableList<Track>?) {
        super.submitList(list)
        list?.let { listSong = list }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.apply {
                setOnClickListener {
                    textSongName.isSelected = true
                    onItemClicked.invoke(listSong, adapterPosition)
                }
            }
        }

        fun bindView(track: Track) {
            itemView.apply {
                textSongName.text = track.title
                textArtist.text = track.artist
                track.artworkUrl?.let { LoadImage.loadImageCircleCrop(imageSong, it) }
            }
        }
    }

    class TrackDiffCallBack : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Track, newItem: Track) = oldItem == newItem
    }
}
