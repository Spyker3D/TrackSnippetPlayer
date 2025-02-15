package com.spyker3d.tracksnippetplayer.utils

import android.content.Context
import android.widget.Toast


fun makeToast(context: Context, errorId: Int) {
    Toast.makeText(context, context.getString(errorId), Toast.LENGTH_SHORT).show()
}