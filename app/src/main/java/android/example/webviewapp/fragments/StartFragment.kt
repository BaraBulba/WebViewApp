package android.example.webviewapp.fragments

import android.content.Context
import android.example.webviewapp.Constants.KEY_NAME
import android.example.webviewapp.R
import android.example.webviewapp.databinding.FragmentStartBinding
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class StartFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val isAgreed: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webViewStart.loadUrl("file:///android_asset/PrivatePolicy.html")

        if(isNetworkAvailable()){
            binding.agreeButton.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_webViewFragment)
            }
        }
        else if (!isNetworkAvailable()){
            binding.agreeButton.setOnClickListener{
                findNavController().navigate(R.id.action_startFragment_to_noInternetFragment)
            }
        }
        binding.disagreeButton.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid())
        }

    }

    private fun savingThePolicy(state: Boolean): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (isAgreed)
        sharedPreferences!!.edit().putBoolean(KEY_NAME, state).commit()
        return true
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}