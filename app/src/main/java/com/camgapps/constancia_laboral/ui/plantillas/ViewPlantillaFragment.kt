package com.camgapps.constancia_laboral.ui.plantillas

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentViewPlantillaBinding


class ViewPlantillaFragment : Fragment() {


    var position: Int? = 0
    private var _binding: FragmentViewPlantillaBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentViewPlantillaBinding.inflate(inflater, container, false)
        val view = binding.root

        setHasOptionsMenu(true)

        position =  arguments?.getInt("position")
        val img =  arguments?.getInt("img")

        binding.buttonEditar.setOnClickListener {
            editarPlantilla()
        }


        binding.imgView.setImageResource(img!!)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_view_plantilla, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_edit_plantilla -> {
              editarPlantilla()
            }
            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun editarPlantilla(){
        val bundle = bundleOf("position" to position)
        findNavController().navigate(R.id.action_viewPlantillaFragment_to_editPlantillaFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}