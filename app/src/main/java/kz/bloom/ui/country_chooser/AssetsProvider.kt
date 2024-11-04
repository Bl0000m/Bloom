package kz.bloom.ui.country_chooser

public fun interface AssetsProvider {
    public fun parseAssetsFileContents(fileName: String): String
}