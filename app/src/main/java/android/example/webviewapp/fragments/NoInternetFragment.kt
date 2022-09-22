package android.example.webviewapp.fragments

import android.example.webviewapp.R
import android.example.webviewapp.databinding.FragmentNoInternetBinding
import android.example.webviewapp.internet.ConnectivityObserver
import android.example.webviewapp.internet.NetworkConnectivityObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class NoInternetFragment : Fragment(R.layout.fragment_no_internet) {

    private lateinit var connectivityObserver: ConnectivityObserver
    private var _binding: FragmentNoInternetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityObserver = NetworkConnectivityObserver(requireContext())
        connectivityObserver.observe().onEach {
            if (it.toString() == "Available"){
                findNavController().popBackStack(R.id.noInternetFragment, true)
                findNavController().navigate(R.id.webViewFragment)
            }
        }.launchIn(lifecycleScope)
    }
}