package com.grebnevstudio.musicplayer.ui

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.grebnevstudio.musicplayer.R
import com.grebnevstudio.musicplayer.extensions.getPermissionString
import com.grebnevstudio.musicplayer.extensions.hasPermission
import com.grebnevstudio.musicplayer.interfaces.PermissionHandler
import com.grebnevstudio.musicplayer.ui.main.MainViewPagerFragment

class AppActivity : AppCompatActivity(), PermissionHandler {
    private val fragmentContainerId = R.id.fragment_container
    private var actionOnPermission: ((granted: Boolean) -> Unit) = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(baseConfig.appTheme)
        setContentView(R.layout.container_app_activity)
        setupFirstFragmentIfNeeded(MainViewPagerFragment())
    }

    override fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        if (hasPermission(permissionId)) {
            callback(true)
        } else {
            actionOnPermission = callback
            ActivityCompat.requestPermissions(
                this,
                arrayOf(getPermissionString(permissionId)),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            actionOnPermission.invoke(grantResults[0] == PERMISSION_GRANTED)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0)
            super.onBackPressed()
        else
            supportFragmentManager.popBackStack()
    }

    private fun setupFirstFragmentIfNeeded(screenToLaunch: Fragment) {
        if (supportFragmentManager.findFragmentById(fragmentContainerId) == null)
            startScreen(screenToLaunch, addToBackStack = false)
    }

    fun startScreen(newFragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction().run {
            replace(fragmentContainerId, newFragment)
            if (addToBackStack) addToBackStack(null)
            commit()
        }
    }

    fun applyNewTheme(): Boolean {
        recreate()
        return true
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }
}
