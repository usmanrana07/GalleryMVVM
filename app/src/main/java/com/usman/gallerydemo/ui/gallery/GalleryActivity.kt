package com.usman.gallerydemo.ui.gallery

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.SimpleItemAnimator
import com.usman.gallerydemo.BR
import com.usman.gallerydemo.R
import com.usman.gallerydemo.databinding.ActivityGalleryBinding
import com.usman.gallerydemo.ui.base.BaseActivity
import com.usman.gallerydemo.ui.gallery.adapter.folders.GalleryFoldersAdapter
import com.usman.gallerydemo.ui.gallery.adapter.media.GalleryAdapter
import com.usman.gallerydemo.ui.gallery.helper.GalleryHelper
import com.usman.gallerydemo.utils.AppLogger
import com.usman.gallerydemo.utils.ProgressDialog
import com.usman.gallerydemo.utils.showPermissionSettingsConfirmationDialog
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.*
import javax.inject.Inject


@AndroidEntryPoint
@RuntimePermissions
class GalleryActivity :
    BaseActivity<GalleryViewModel, ActivityGalleryBinding>(R.layout.activity_gallery),
    GalleryNavigator {

    override val viewModel: GalleryViewModel by viewModels()

    @Inject
    lateinit var galleryFolderAdapter: GalleryFoldersAdapter

    @Inject
    lateinit var galleryAdapter: GalleryAdapter

    override val galleryHelper: GalleryHelper by lazy {
        GalleryHelper(context)
    }
    private var progressDialog: Dialog? = null


    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)

        if (hasStoragePermission()) {
            requestGalleryData()
        } else {
            viewModel.hasGalleryPermission.value = false
        }
    }

    @NeedsPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun requestGalleryData() {

        viewModel.initLists()
        viewModel.loadGalleryMedia(GalleryHelper.GALLERY_IMAGE_AND_VIDEOS)
        setGalleryFoldersAdapter()
        setGalleryMediaAdapter()
        addGalleryFoldersLiveDataObservers()
        addGalleryLiveDataObservers()
        addLoadingStateObserver()
    }

    private fun addLoadingStateObserver() {
        viewModel.isLoading.observe(this, { loading ->
            progressDialog?.dismiss()
            if (loading) {
                progressDialog = ProgressDialog.progressDialog(context, true)
            }

        })

    }

    private fun setGalleryFoldersAdapter() {
        binding.rvFolders.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = galleryFolderAdapter
            isNestedScrollingEnabled = false
        }
        galleryFolderAdapter.setFolderSelectionListenerLambda(::onFolderSelected)
    }

    private fun setGalleryMediaAdapter() {
        binding.rvGallery.apply {
            if (itemAnimator is SimpleItemAnimator) (itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                false // to avoid blink on notifyItemChanged call
            adapter = galleryAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun addGalleryFoldersLiveDataObservers() {
        viewModel.galleryFoldersLiveDataList?.observe(this, { list ->
            AppLogger.d("usm_gallery_folders", "list= ${list.size}")
            galleryFolderAdapter.apply {
                if (getListSize() > 0)
                    clearItems()
                addItems(list)
            }
        })
    }

    private fun addGalleryLiveDataObservers() {
        viewModel.galleryMediaLiveDataList?.observe(this, { list ->
            AppLogger.d("usm_gallery_items", "list= ${list?.size}")
            galleryAdapter.apply {
                if (getListSize() > 0)
                    clearItems()
                list?.let { addItems(it) }
            }
        })
    }


    private fun onFolderSelected(bucketId: Int, bucketName: String) {
        viewModel.setSelectedFolder(bucketId, bucketName)
        switchGalleryMode()
    }


    override fun loadGallery() {
        viewModel.hasGalleryPermission.value = true
        requestGalleryDataWithPermissionCheck()
    }


    override fun switchGalleryMode() {
        binding.viewSwitcher.showNext()
    }


    override fun hasStoragePermission(): Boolean {
        return PermissionUtils.hasSelfPermissions(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    @OnPermissionDenied(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun onStoragePermissionDenied() {
        viewModel.hasGalleryPermission.value = false
    }


    @OnNeverAskAgain(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun onStoragePermissionNeverAskAgain() {
        viewModel.hasGalleryPermission.value = false
        showPermissionSettingsConfirmationDialog()
    }

    @OnShowRationale(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun showRationaleForStorage(request: PermissionRequest) {
        request.proceed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }

}