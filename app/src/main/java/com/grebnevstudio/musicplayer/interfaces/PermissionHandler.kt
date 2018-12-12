package com.grebnevstudio.musicplayer.interfaces

interface PermissionHandler {
    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit)
}