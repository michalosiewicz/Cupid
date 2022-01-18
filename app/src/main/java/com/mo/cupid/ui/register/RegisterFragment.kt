package com.mo.cupid.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private val viewModel = RegisterViewModel()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )

        return binding.apply {
            viewModel = this@RegisterFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerInSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_registerFragment_to_logInFragment)
        }

        viewModel.registerInError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.logIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_logInFragment)
        }
    }

}