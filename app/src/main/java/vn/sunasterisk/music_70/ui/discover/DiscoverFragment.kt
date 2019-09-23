package vn.sunasterisk.music_70.ui.discover

import android.os.Bundle
import android.text.Html
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_discover.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.base.BaseFragment
import vn.sunasterisk.music_70.constant.Constant
import vn.sunasterisk.music_70.data.local.LocalDataSource
import vn.sunasterisk.music_70.data.model.Genre
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.RemoteDataSource
import vn.sunasterisk.music_70.data.remote.TrackRepository
import vn.sunasterisk.music_70.util.StringUtils
import java.util.*

class DiscoverFragment : BaseFragment(), GenreContract.View {

    private lateinit var sliderTrackAdapter: SliderTrackAdapter
    private lateinit var genrePresent: GenrePresenter
    private lateinit var tracksRepository: TrackRepository
    private lateinit var timer: Timer
    private lateinit var mDots: ArrayList<TextView>

    private lateinit var listGenre: List<Genre>

    override val getContentViewId = R.layout.fragment_discover

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
        val apiTop = StringUtils.generateGenreUrl(Constant.KIND_TOP, Constant.GENRES_ALL_MUSIC, 10, 10)
        tracksRepository = TrackRepository.getInstance(
            RemoteDataSource.getInstance(context!!),
            LocalDataSource.getInstance(context!!)
        )
        genrePresent = GenrePresenter(tracksRepository, this)
        genrePresent.getTrack(apiTop)
        sliderTrackAdapter = SliderTrackAdapter()
        viewPagerTop.adapter = sliderTrackAdapter
        timer = Timer()

        listGenre=genrePresent.getListGenre(context!!)
        attachAdapterToRecyclerView(recyclerGenre,GenreAdapter {
            Toast.makeText(context!!,it.nameGenre,Toast.LENGTH_SHORT).show()
        })
    }

    override fun registerListeners() {
        viewPagerTop.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                addDotIndicator(position)
            }
        })
        sliderTrackAdapter.setOnItemClicked {
            Toast.makeText(context,it.title, Toast.LENGTH_LONG).show()
        }
    }

    private fun attachAdapterToRecyclerView(recyclerView: RecyclerView, genresAdapter: GenreAdapter) {
        genresAdapter.updateGenre(context?.let { genrePresent.getListGenre(it) } as MutableList<Genre>)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = genresAdapter
        }

    }
    override fun unregisterListeners() {
    }

    override fun showTrack(tracks: List<Track>) {
        sliderTrackAdapter.updateTrack(tracks as MutableList<Track>)
        timer.schedule(object : TimerTask() {
            override fun run() {

                activity?.runOnUiThread {
                    viewPagerTop?.let {
                        var index = it.currentItem + 1 % sliderTrackAdapter.count
                        if (index == sliderTrackAdapter.count) {
                            index = 0
                        }
                        viewPagerTop.setCurrentItem(index, true)
                    }
                }
            }
        }, TIME_DELAY, TIME_PERIOD)
    }

    private fun addDotIndicator(postion: Int) {
        mDots = ArrayList()
        linearDots.removeAllViews()

        for (i in 0 until sliderTrackAdapter.count) {
            mDots.add(TextView(context))
            mDots[i].apply {
                setText(Html.fromHtml(context.getString(R.string.string_html)))
                textSize = TEXT_SIZE
                setTextColor(resources.getColor(R.color.color_tranparent_white))
            }
            linearDots.addView(mDots[i])
        }

        if (mDots.size > 0) {
            mDots[postion].setTextColor(resources.getColor(R.color.color_white))
        }
    }

    override fun showError(exception: Exception) {
        Toast.makeText(context, getString(R.string.string_error), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TIME_DELAY= 1000L
        const val TIME_PERIOD= 5000L
        const val TEXT_SIZE= 35F
        fun newInstance() = DiscoverFragment()
    }
}
