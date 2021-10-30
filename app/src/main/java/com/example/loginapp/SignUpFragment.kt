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
import com.example.loginapp.databinding.FragmentSignUpBinding
import com.example.loginapp.objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up,container,false)
        mAuth = FirebaseAuth.getInstance()

        binding.signinlogin.setOnClickListener {
            it.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
        }

        binding.signupButton.setOnClickListener {
            signUp()
        }

        return binding.root
    }

    private fun signUp() {
        val nm = binding.signUpName.text.toString().trim { it<=' ' }
        val em = binding.signUpEmail.text.toString().trim { it<=' ' }
        val pd = binding.signUpPassword.text.toString().trim { it<=' ' }
        val cpd = binding.confirmSignupPassword.text.toString().trim { it<=' ' }


        if(TextUtils.isEmpty(nm)){
            Toast.makeText(requireContext(),"Enter your name", Toast.LENGTH_LONG).show()
        }
        else if(TextUtils.isEmpty(em)){
            Toast.makeText(requireContext(),"Enter your email address", Toast.LENGTH_LONG).show()
        }
        else if(TextUtils.isEmpty(pd)){
            Toast.makeText(requireContext(),"Enter valid password", Toast.LENGTH_LONG).show()
        }
        else if(TextUtils.isEmpty(cpd)){
            Toast.makeText(requireContext(),"Enter valid password", Toast.LENGTH_LONG).show()
        }
        else if(pd.equals(cpd)){
            mAuth.createUserWithEmailAndPassword(em,pd)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        addUserToDatabase(nm,em,mAuth.currentUser!!.uid)
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
                    }
                    else{
                        Toast.makeText(requireContext(),"There's is some technical error. Request you to try again!",Toast.LENGTH_LONG).show()
                    }
            }
        }
        else{
            Toast.makeText(requireContext(),"Both password doesn't match", Toast.LENGTH_LONG).show()
        }
    }

    private fun addUserToDatabase(nm: String, em: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).setValue(User(nm,em,uid))
    }
}