package com.camgapps.constancia_laboral.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.camgapps.constancia_laboral.AdMobManager
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentConstanciasBinding
import com.camgapps.constancia_laboral.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.cardViewConstancias.setOnClickListener {
            AdMobManager.showAd(requireActivity()){
                findNavController().navigate(R.id.nav_constancias)
            }
        }

        binding.cardViewPlantillas.setOnClickListener {
            AdMobManager.showAd(requireActivity()) {
                findNavController().navigate(R.id.nav_plantillas)
            }
        }

        binding.cardViewLibre.setOnClickListener {
            AdMobManager.showAd(requireActivity()) {
                findNavController().navigate(R.id.cartaLibreFragment)
            }
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_compartir -> {
                compartirApp()
                return true
            }
            R.id.action_valorar -> {
                valorarApp()
            }
            else -> {

            }

        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun compartirApp() {
        var urlApp = "https://play.google.com/store/apps/details?id=com.camgapps.constancia_laboral"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, urlApp)
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    fun valorarApp() {
        var url = "https://play.google.com/store/apps/details?id=com.camgapps.constancia_laboral"
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}