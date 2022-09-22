package android.example.webviewapp.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.example.webviewapp.R
import android.example.webviewapp.databinding.FragmentWebViewBinding
import android.example.webviewapp.internet.ConnectivityObserver
import android.example.webviewapp.internet.NetworkConnectivityObserver
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private lateinit var connectivityObserver: ConnectivityObserver
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityObserver = NetworkConnectivityObserver(requireContext())
        connectivityObserver.observe().onEach {
            if (it.toString() == "Unavailable"){
                clearBackStack()
                navigateToNoInternet()
            }
            if (it.toString() == "Losing"){
                clearBackStack()
                navigateToNoInternet()
            }
            if (it.toString() == "Lost"){
                clearBackStack()
                navigateToNoInternet()
            }
        }.launchIn(lifecycleScope)

        binding.webView.webViewClient = MyWebClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(getUrl()!!)

        binding.webView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    val webView = v as WebView
                    when (keyCode) {
                        KeyEvent.KEYCODE_BACK -> if (webView.canGoBack()) {
                            webView.goBack()
                            return true
                        }
                    }
                }
                return false
            }
        })

    }

    private fun clearBackStack(){
        findNavController().popBackStack(R.id.webViewFragment, true)
    }
    private fun navigateToNoInternet(){
        findNavController().navigate(R.id.noInternetFragment)
    }

    inner class MyWebClient: WebViewClient(){
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url!!)
            return true
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            saveUrl(url)
        }
    }

    fun saveUrl(url: String?) {
        val sp: SharedPreferences = requireContext()
            .getSharedPreferences("SP_WEBVIEW_PREFS", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString("SAVED_URL", url)
        editor.apply()
    }

    private fun getUrl(): String? {
        val sp: SharedPreferences = requireContext()
            .getSharedPreferences("SP_WEBVIEW_PREFS", MODE_PRIVATE)
        return sp.getString("SAVED_URL", "https://github.com/")
    }

}


