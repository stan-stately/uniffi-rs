{{- self.add_import("android.os.Build") }}
{{- self.add_import("androidx.annotation.RequiresApi") }}

private fun UniffiCleaner.Companion.create(): UniffiCleaner =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        AndroidSystemCleaner()
    } else {
        UniffiJnaCleaner()
    }

// The SystemCleaner, available from API Level 33.
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private class AndroidSystemCleaner : UniffiCleaner {
    val cleaner = android.system.SystemCleaner.cleaner()

    override fun register(value: Any, cleanUpTask: Runnable): UniffiCleaner.Cleanable =
        AndroidSystemCleanable(cleaner.register(value, cleanUpTask))
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private class AndroidSystemCleanable(
    private val cleanable: java.lang.ref.Cleaner.Cleanable,
) : UniffiCleaner.Cleanable {
    override fun clean() = cleanable.clean()
}
