package com.camgapps.constancia_laboral.ui.plantillas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.camgapps.constancia_laboral.AdMobManager
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentConstanciasBinding
import com.camgapps.constancia_laboral.databinding.FragmentPlantillasBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson

class PlantillasFragment : Fragment() {

    private lateinit var sp: SharedPreferences
    private lateinit var plantillas: Array<Plantilla?>
    private lateinit var adapter: PlantillasAdapter

    private val TAG = "PlantillasFragment"


    private var _binding: FragmentPlantillasBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlantillasBinding.inflate(inflater, container, false)
        val view = binding.root

        sp = activity?.getSharedPreferences("MyApp", Context.MODE_PRIVATE)!!
        plantillas = getPlantillasFromPreferences()

        adapter = PlantillasAdapter{plantilla, position ->
            val bundle = bundleOf(
                "position" to position,
                "img" to plantilla.img
            )

            if(plantilla.isActive) {
                findNavController().navigate(R.id.action_nav_plantillas_to_viewPlantillaFragment, bundle)
            }else{
                showDialogDesbloquear(){
                    //show reward ad
                    AdMobManager.loadRecompensate(requireActivity(),{//onShow
                        changeListPreferences(position)
                        findNavController().navigate(R.id.action_nav_plantillas_to_viewPlantillaFragment, bundle)
                    },{//onCancel

                    })
                }
            }
        }

        adapter.setData(plantillas.toList() as List<Plantilla>)
        binding.list.adapter = adapter
        binding.list.layoutManager = GridLayoutManager(requireContext(), 2)

        return view
    }


    private fun changeListPreferences(position: Int) {
        plantillas[position]?.isActive = true

        setPlantillasToPreferences(plantillas)
        adapter.setData(plantillas.toList() as List<Plantilla>)
    }



    private fun showDialogDesbloquear(onPositive: () -> Unit = {}) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Obstruido")
            .setMessage("Para desbloquear esta plantilla debes mirar un anuncio")
            .setPositiveButton("Ver anuncio") { dialog, which ->
                onPositive()
            }.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }.create().show()


    }


    private fun getPlantillas(): Array<Plantilla?>{
        val plantilla1 = Plantilla("Plantilla 1", R.drawable.uno, true)
        val plantilla2 = Plantilla("Plantilla 2", R.drawable.dos, false)
        val plantilla3 = Plantilla("Plantilla 3", R.drawable.tres, false)
        val plantilla4 = Plantilla("Plantilla 4", R.drawable.cuatro, false)
        val plantilla5 = Plantilla("Plantilla 5", R.drawable.cinco, false)

        return  arrayOf<Plantilla?>(plantilla1, plantilla2, plantilla3, plantilla4, plantilla5)
    }


    override fun onStart() {
        super.onStart()
        AdMobManager.loadAd(requireActivity())
    }

    private fun getPlantillasFromPreferences(): Array<Plantilla?> {
        val jsonData: String? = sp.getString("plantillas", "")

        Log.d(TAG, "getPlantillasFromPreferences: $jsonData")

        if(jsonData == ""){
            setPlantillasToPreferences(getPlantillas())
            return getPlantillas()
        }else{
            val listPlantilla = Gson().fromJson(jsonData, Array<Plantilla?>::class.java)
            return listPlantilla
        }

    }

    private fun setPlantillasToPreferences(plantillas: Array<Plantilla?>){
        Log.d(TAG, "setPLantillas: $plantillas")

        with(sp.edit()){
            putString("plantillas", Gson().toJson(plantillas))
            apply()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}