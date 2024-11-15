package kz.bloom.ui.auth.country_chooser

public fun interface AssetsProvider {
    public fun parseAssetsFileContents(fileName: String): String
}