package com.insta.swjstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.insta.swjstagram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        binding.apply {
            emailLoginButton.setOnClickListener {
                signInAndSignUp()
            }
        }
    }

    private fun signInAndSignUp(){
        binding.apply {
            auth?.createUserWithEmailAndPassword(
                emailEdittext.text.toString(),
                passwordEdittext.text.toString())?.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        // Creating a user account
                        moveMainPage(task.result?.user)
                    }else if(!task.exception?.message.isNullOrEmpty()){
                        // Show the Error Message
                        Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                    else{
                        // Login if  you have account
                        signInEmail()
                    }
            }
        }
    }
    private fun signInEmail(){
        binding.apply {
            auth?.signInWithEmailAndPassword(
            emailEdittext.text.toString(),
                passwordEdittext.text.toString())?.addOnCompleteListener { task ->
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

    }
    private fun moveMainPage(user : FirebaseUser?){
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}