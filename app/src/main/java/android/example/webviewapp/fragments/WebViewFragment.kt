package android.example.webviewapp.fragments

import android.example.webviewapp.R
import android.example.webviewapp.databinding.FragmentWebViewBinding
import android.example.webviewapp.internet.ConnectivityObserver
import android.example.webviewapp.internet.NetworkConnectivityObserver
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
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

        binding.webView.settings.javaScriptEnabled
        binding.webView.loadUrl("https://github.com/")

        val webViewClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }
        }
        binding.webView.webViewClient = webViewClient

        connectivityObserver = NetworkConnectivityObserver(requireContext())
        connectivityObserver.observe().onEach {
            if (it.toString() == "Unavailable"){
                findNavController().popBackStack(R.id.webViewFragment, true)
                findNavController().navigate(R.id.noInternetFragment)
            }
            if (it.toString() == "Losing"){
                findNavController().popBackStack(R.id.webViewFragment, true)
                findNavController().navigate(R.id.noInternetFragment)
            }
            if (it.toString() == "Lost"){
                findNavController().popBackStack(R.id.webViewFragment, true)
                findNavController().navigate(R.id.noInternetFragment)
            }
        }.launchIn(lifecycleScope)

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



}