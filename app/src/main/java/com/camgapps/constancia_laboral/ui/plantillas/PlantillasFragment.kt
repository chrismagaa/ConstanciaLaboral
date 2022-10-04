package com.camgapps.constancia_laboral.ui.plantillas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.camgapps.constancia_laboral.AdMobManager
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentConstanciasBinding
import com.camgapps.constancia_laboral.databinding.FragmentPlantillasBinding

class PlantillasFragment : Fragment() {

    private lateinit var adapter: PlantillasAdapter

    private var _binding: FragmentPlantillasBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlantillasBinding.inflate(inflater, container, false)
        val view = binding.root


        var plantillas = ArrayList<Plantilla>()
        val plantilla1 = Plantilla("Plantilla 1", R.drawable.uno)
        val plantilla2 = Plantilla("Plantilla 2", R.drawable.dos)
        val plantilla3 = Plantilla("Plantilla 3", R.drawable.tres)
        val plantilla4 = Plantilla("Plantilla 4", R.drawable.cuatro)
        val plantilla5 = Plantilla("Plantilla 5", R.drawable.cinco)
        plantillas = arrayListOf<Plantilla>(plantilla1, plantilla2, plantilla3, plantilla4, plantilla5)

        adapter = PlantillasAdapter(findNavController(), plantillas)
        binding.list.adapter = adapter
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)

        return view
    }


    override fun onStart() {
        super.onStart()
        AdMobManager.loadAd(requireActivity())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}