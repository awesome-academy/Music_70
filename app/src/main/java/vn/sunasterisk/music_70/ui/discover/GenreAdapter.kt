package vn.sunasterisk.music_70.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_genre.view.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.data.model.Genre
import vn.sunasterisk.music_70.util.LoadImage

class GenreAdapter (val onItemClicked: (genres: Genre) -> Unit): RecyclerView.Adapter<GenreAdapter.ViewHolder>(){

    private var listGenre = emptyList<Genre>()

    fun updateGenre(data: MutableList<Genre>) {
        listGenre = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false))


    override fun getItemCount() = listGenre.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listGenre[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                onItemClicked.invoke(listGenre[adapterPosition])
            }
        }

        fun bindView(item: Genre) {
            itemView.apply {
                textTitle.text = item.nameGenre
                LoadImage.loadImageFromDrawable(imageGenres,item.imageGenre)
            }
        }
    }
}
