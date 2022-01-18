package com.mo.cupid.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private val viewModel = LogInViewModel()
    private lateinit var binding: FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_log_in,
            container,
            false
        )

        return binding.apply {
            viewModel = this@LogInFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.logInSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_logInFragment_to_menuFragment)
        }

        viewModel.logInError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registerFragment)
        }
    }
}