package kz.bloom.ui.ui_components

import android.content.Context

public fun Context.parseAssetsFileContents(fileName: String): String =
    assets.open(fileName)
        .bufferedReader()
        .use { reader -> reader.readText() }