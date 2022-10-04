package com.camgapps.constancia_laboral.ui.plantillas

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.DialogFragmentGuardarConstanciaBinding
import java.lang.ClassCastException

class GuardarConstanciaDialogFragment(private val absolutePath: String, private val namePdf: String): DialogFragment(){

    private var _binding: DialogFragmentGuardarConstanciaBinding? = null
    private val binding get() = _binding

    private var mListener: GuardarConstanciaDialogInterface? = null

    interface GuardarConstanciaDialogInterface{
        fun gurdarConstanciaResponsDialog(dialog: DialogFragment, namePdf: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            _binding = DialogFragmentGuardarConstanciaBinding.bind(inflater.inflate(R.layout.dialog_fragment_guardar_constancia, null))

            val builder = AlertDialog.Builder(it)
            builder.setView(binding!!.root)

            binding!!.textViewCarpetaPath.text = absolutePath
            binding!!.editTextNombreCarta.setText(namePdf)

            binding!!.textViewAceptarSave.setOnClickListener {
                if(binding!!.editTextNombreCarta.text.trim().isEmpty()){
                    binding!!.editTextNombreCarta.error = getString(R.string.error_nombre)
                }else{
                    val nuevoName = binding!!.editTextNombreCarta.text.toString()
                    mListener!!.gurdarConstanciaResponsDialog(this, nuevoName)
                }
            }

            binding!!.textViewCancelarSave.setOnClickListener {
                dialog!!.dismiss()
            }

            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mListener = parentFragment as GuardarConstanciaDialogInterface
        }catch (e: ClassCastException){
            throw ClassCastException((context.toString() +
                    " must implement GuardarConstanciaDialogListener"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}