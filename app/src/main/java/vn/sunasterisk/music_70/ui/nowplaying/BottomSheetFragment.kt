package vn.sunasterisk.music_70.ui.nowplaying

<<<<<<< HEAD
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
=======
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
>>>>>>> Task#17532 Ui for Option Bottom Sheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_option.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackAttributes
import vn.sunasterisk.music_70.util.LoadImage

<<<<<<< HEAD
=======

>>>>>>> Task#17532 Ui for Option Bottom Sheet
class BottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var track: Track

    override fun onClick(v: View) {
        when (v.id) {
            R.id.textDownload -> dismiss()

            R.id.textAddToFavourite -> dismiss()

            R.id.textAddToPlaylist -> dismiss()

            R.id.textShare -> {
                val myShareIntent = Intent(Intent.ACTION_SEND)
                myShareIntent.type = SHARE_TYPE
                val value = String.format(
                    SHARE_VALUE,
                    track.title,
                    track.username,
                    track.streamUrl
                )
                myShareIntent.putExtra(Intent.EXTRA_TEXT, value)
                startActivity(
                    Intent.createChooser(
                        myShareIntent,
                        track.title
                    )
                )
            }

            R.id.textInformation -> dismiss()
        }
    }

    fun bindData(){
        textSongName.text=track.title
        textArtist.text= track.artist
        track.artworkUrl?.let { LoadImage.loadImage(imageSong, it) }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData(savedInstanceState)
        initializeComponents()
        registerListeners()
    }

    private fun registerListeners() {
        textDownload.setOnClickListener(this)
        textInformation.setOnClickListener(this)
        textAddToFavourite.setOnClickListener(this)
        textAddToPlaylist.setOnClickListener(this)
        textShare.setOnClickListener(this)
    }

    private fun initializeComponents() {
    }

    private fun initializeData(savedInstanceState: Bundle?) {
        arguments?.let {
            track = it.getParcelable(TrackAttributes.TRACK) as Track
        }
        bindData()
    }

    companion object {

        fun newInstance(track: Track): BottomSheetFragment {
            const val SHARE_VALUE = "Track: %s\nArtits: %s\nLink: %s"
            const val SHARE_TYPE = "text/plain"
            val args = Bundle().apply {
                putParcelable(TrackAttributes.TRACK, track)
            }
            return BottomSheetFragment().apply {
                arguments = args
            }
        }
    }
}

