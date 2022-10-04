package com.camgapps.constancia_laboral.ui.retroalimentacion

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.camgapps.constancia_laboral.R
import com.camgapps.constancia_laboral.databinding.FragmentBlankBinding


class FeedbackFragment : Fragment() {

    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        val view = binding.root

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_feedback, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_send -> {
                sendEmail()
            }
            else -> {

            }
        }


        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail() {
        val nombre = binding!!.editTextTuNombre.text.toString()
        val mensaje = binding!!.editTextIdeas.text.toString()

        var email: Array<String> = arrayOf(getString(R.string.camg_mail))

        val i = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, email)
            putExtra(Intent.EXTRA_SUBJECT, nombre)
            putExtra(Intent.EXTRA_TEXT, mensaje)
        }
        if (i.resolveActivity(context?.packageManager!!) != null) {
            startActivity(i)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}