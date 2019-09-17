package vn.sunasterisk.music_70.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), BaseView {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = container?.inflate(getContentViewId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData(savedInstanceState)
        initComponents()
        registerListeners()
    }

    override fun onDestroyView() {
        unregisterListeners()
        super.onDestroyView()
    }

    abstract fun registerListeners()

    abstract fun unregisterListeners()

    fun ViewGroup.inflate(@LayoutRes layoutResource: Int) =
        LayoutInflater.from(context)?.inflate(layoutResource, this, false)
}
