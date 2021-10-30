package com.example.loginapp

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.loginapp.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {

    private lateinit var binding : FragmentLogInBinding
    private lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_log_in,container,false)
        mAuth = FirebaseAuth.getInstance()

        binding.loginsignUp.setOnClickListener {
            it.findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }
        binding.loginButton.setOnClickListener {
            login()
        }
        return binding.root
    }

    private fun login() {

        val em = binding.logInEmail.text.toString().trim { it<=' ' }
        val pd = binding.loginPassword.text.toString().trim { it<=' ' }

        if(TextUtils.isEmpty(em)){
            Toast.makeText(requireContext(),"Enter your email address",Toast.LENGTH_LONG).show()
        }
        else if(TextUtils.isEmpty(pd)){
            Toast.makeText(requireContext(),"Enter valid password",Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.signInWithEmailAndPassword(em,pd)
                .addOnCompleteListener{

                    if(it.isSuccessful){
                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToListFragment())
                    }
                    else{
                        Toast.makeText(requireContext(),"There's is some technical error. Request you to try again!",Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser != null){
            findNavController().navigate(R.id.action_logInFragment_to_listFragment)
        }
    }
}