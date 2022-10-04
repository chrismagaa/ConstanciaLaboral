package com.camgapps.constancia_laboral.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.DialogFragmentValorarBinding
import java.lang.ClassCastException

class ValorarDialogFragment: DialogFragment() {

    private var _binding: DialogFragmentValorarBinding? = null
    private val binding get() = _binding

    private var mListener: ValorarDialogInterface? = null

    interface ValorarDialogInterface{
        fun valorar(dialog: DialogFragment)
        fun mandarRetro(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            _binding = DialogFragmentValorarBinding.bind(inflater.inflate(R.layout.dialog_fragment_valorar, null))
            val builder = AlertDialog.Builder(it)
            builder.setView(binding!!.root)

            initEvents()

            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initEvents() {
        binding!!.animationView.repeatCount = 1

        binding!!.buttonMandarRetro.setOnClickListener {
            mListener!!.mandarRetro(this)
        }

        binding!!.buttonValorar.setOnClickListener{
            mListener!!.valorar(this)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try{
            mListener = activity as ValorarDialogInterface
        }catch (e: ClassCastException){
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }
}
