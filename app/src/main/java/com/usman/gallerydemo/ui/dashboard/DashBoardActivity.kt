package com.usman.gallerydemo.ui.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import com.usman.gallerydemo.BR
import com.usman.gallerydemo.R
import com.usman.gallerydemo.databinding.ActivityDashboardBinding
import com.usman.gallerydemo.ui.base.BaseActivity
import com.usman.gallerydemo.ui.gallery.GalleryActivity
import com.usman.gallerydemo.utils.CommonTags
import com.usman.gallerydemo.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashBoardActivity :
    BaseActivity<DashBoardViewModel, ActivityDashboardBinding>(R.layout.activity_dashboard),
    DashBoardNavigator {

    override val viewModel: DashBoardViewModel by viewModels()

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
    }

    override fun openGallery(mode: Int) {
        launchActivity(
            GalleryActivity::class.java,
            bundle = Bundle().apply { putInt(CommonTags.galleryMode, mode) })
    }

}