package android.example.webviewapp.fragments

import android.content.Context
import android.example.webviewapp.Constants.KEY_NAME
import android.example.webviewapp.Constants.SHARED_PREFS_NAME
import android.example.webviewapp.R
import android.example.webviewapp.databinding.FragmentStartBinding
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class StartFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreferences = context?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences!!.getBoolean(KEY_NAME, false)){
            if (isNetworkAvailable()){
                navigateToWebView()
            }
            else if (!isNetworkAvailable()){
                navigateToNoInternet()
            }
        }
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webViewStart.loadUrl("file:///android_asset/PrivatePolicy.html")

        if(isNetworkAvailable()){
            binding.agreeButton.setOnClickListener {
                savingThePolicy()
                navigateToWebView()
            }
        }
        else if (!isNetworkAvailable()){
            binding.agreeButton.setOnClickListener{
                savingThePolicy()
                navigateToNoInternet()
            }
        }
        binding.disagreeButton.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid())
        }

    }
    private fun navigateToWebView(){
        findNavController().navigate(R.id.action_startFragment_to_webViewFragment)
    }
    private fun navigateToNoInternet(){
        findNavController().navigate(R.id.action_startFragment_to_webViewFragment)
    }

    private fun savingThePolicy(){
        val sharedPreferences = context?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(KEY_NAME, true)?.apply()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}