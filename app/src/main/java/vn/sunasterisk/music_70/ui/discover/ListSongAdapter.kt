package vn.sunasterisk.music_70.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_song.view.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.LoadImage

class ListSongAdapter(
    val onItemClicked: (List<Track>, Int) -> Unit,
    val optionClick: (Track) -> Unit
) : RecyclerView.Adapter<ListSongAdapter.ViewHolder>() {
    private var listSong = emptyList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false))

    fun updateList(data: List<Track>) {
        listSong = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = listSong.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindView(listSong[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.apply {
                textSongName.setOnClickListener {
                    textSongName.isSelected = true
                    onItemClicked.invoke(listSong, adapterPosition)
                }
                imageOption.setOnClickListener {
                    optionClick.invoke(listSong[adapterPosition])
                }
            }
        }

        fun bindView(track: Track) {
            itemView.apply {
                textSongName.text = track.title
                textArtist.text = track.artist
                track.artworkUrl?.let { LoadImage.loadImage(imageSong, it) }
            }
        }
    }
}
