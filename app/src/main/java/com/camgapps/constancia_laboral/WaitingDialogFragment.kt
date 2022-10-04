package com.camgapps.constancia_laboral

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.camgapps.constancia_laboral.databinding.DialogWaitingBinding

class WaitingDialogFragment: DialogFragment() {

    private var _binding: DialogWaitingBinding? = null
    val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogWaitingBinding.inflate(LayoutInflater.from(context))

        var dialog = AlertDialog.Builder(requireActivity())
                .setView(binding.root).create()

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}