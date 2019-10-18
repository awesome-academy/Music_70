package vn.sunasterisk.music_70.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.ui.mymusic.MyMusicFragment
import vn.sunasterisk.music_70.util.AlertDialogUtil.showMessageDialog

object PermissionUtil {
    val storagePermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    fun isGrantResultsGranted(grantResults: IntArray) =
        if (grantResults.isNotEmpty()) {
            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) {
                    false
                }
            }
            true
        } else {
            false
        }

    fun isPermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        permissions.forEach {
            if (!isPermissionGranted(context, it)) return false
        }
        return true
    }

    fun requestStoragePermission(fragment: Fragment) {
        if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            fragment.context?.let {
                showMessageDialog(
                    it, it.getString(R.string.alert_permissions_rationale), false
                ) {
                    fragment.requestPermissions(
                        storagePermissions,
                        MyMusicFragment.PERMISSION_STORAGE
                    )
                }
            }
        } else {
            fragment.requestPermissions(storagePermissions, MyMusicFragment.PERMISSION_STORAGE)
        }
    }

    private fun isPermissionGranted(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
}
