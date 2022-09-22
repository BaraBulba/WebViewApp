package android.example.webviewapp.internet

interface ConnectivityObserver {

    fun observe(): kotlinx.coroutines.flow.Flow<Status>

    enum class Status {
        Available, Unavailable, Lost, Losing
    }
}