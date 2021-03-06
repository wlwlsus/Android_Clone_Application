package com.insta.swjstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.insta.swjstagram.databinding.ActivityLoginBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    var googleSingInClient : GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        binding.emailLoginButton.setOnClickListener {
            signInAndSignUp()
        }
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun googleLogin(){
        var signInIntent = googleSingInClient?.signInIntent
s
        val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ){
            activityResult ->
            if(activityResult.resultCode == GOOGLE_LOGIN_CODE){

            }
        }
    }

    private fun signInAndSignUp(){
        try {
            auth?.createUserWithEmailAndPassword(
                binding.emailEdittext.text.toString(),
                binding.passwordEdittext.text.toString())?.addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        // Creating a user account
                        moveMainPage(task.result?.user)
                    }
                    task.exception?.message.isNullOrEmpty() -> {
                        // Show the Error Message
                        Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        // Login if  you have account
                        signInEmail()
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun signInEmail(){
        auth?.signInWithEmailAndPassword(
            binding.emailEdittext.text.toString(),
            binding.passwordEdittext.text.toString())?.addOnCompleteListener { task ->
            if(task.isSuccessful){
                // Creating a user account
                moveMainPage(task.result?.user)
            }
            else{
                // Show the Error Message
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun moveMainPage(user : FirebaseUser?){
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}