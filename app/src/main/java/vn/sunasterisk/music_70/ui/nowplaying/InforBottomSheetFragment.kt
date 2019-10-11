package vn.sunasterisk.music_70.ui.nowplaying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_infor.*
import kotlinx.android.synthetic.main.bottom_sheet_option.textArtist
import kotlinx.android.synthetic.main.bottom_sheet_option.textSongName
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackAttributes
import vn.sunasterisk.music_70.util.LoadImage
import vn.sunasterisk.music_70.util.StringUtils

class InforBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var track: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_infor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData(savedInstanceState)
        initializeComponents()
        registerListeners()
    }

    private fun initializeComponents() {
    }

    private fun initializeData(savedInstanceState: Bundle?) {
        arguments?.let {
            track = it.getParcelable(TrackAttributes.TRACK) as Track
        }
        bindData()
    }

    fun bindData() {
        textSongName.text = track.title
        textArtist.text = track.artist
        track.artworkUrl?.let { LoadImage.loadImage(imageSong, it) }
        textLike.text = StringUtils.passCount(track.likesCount)
    }

    private fun registerListeners() {
    }

    companion object {
        fun newInstance(track: Track): InforBottomSheetFragment {
            val args = Bundle().apply {
                putParcelable(TrackAttributes.TRACK, track)
            }
            return InforBottomSheetFragment().apply {
                arguments = args
            }
        }
    }
}
