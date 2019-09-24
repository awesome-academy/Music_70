package vn.sunasterisk.music_70.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_silde.view.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.LoadImage
import vn.sunasterisk.music_70.util.StringUtils

class SliderTrackAdapter() : PagerAdapter() {
    private var listTrack = emptyList<Track>()
    private lateinit var onItemClicked: (track: Track) -> Unit
    fun setOnItemClicked(onItemClicked: (track: Track) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    override fun isViewFromObject(view: View, item: Any): Boolean = view == item

    override fun getCount(): Int = listTrack.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val itemView = inflater.inflate(R.layout.item_silde, container, false)

        itemView.setOnClickListener {
            if (::onItemClicked.isInitialized) onItemClicked.invoke(listTrack[position])
        }
        getView(itemView, listTrack[position])
        container.addView(itemView)
        return itemView
    }

    private fun getView(itemView: View?, track: Track) {
        itemView?.apply {
            textNameTrack.isSelected = true
            textNameTrack.text = track.title
            track.artist?.let {
                textDescription.text = it
            }
            track.artworkUrl?.let { LoadImage.loadImage(imageTrack, it) }
        }
    }

    fun getListData() = listTrack

    fun updateTrack(data: MutableList<Track>) {
        listTrack = data
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }
}
