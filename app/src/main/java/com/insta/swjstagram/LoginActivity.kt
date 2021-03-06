package com.insta.swjstagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.insta.swjstagram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSingInClient: GoogleSignInClient? = null

    private val requestActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        // There are no request codes
        val data: Intent? = activityResult.data
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (result!!.isSuccess) {
            val account = result.signInAccount
            // Second Step
            firebaseAuthGoogle(account)
        }
    }

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
        binding.googleSignInButton.setOnClickListener {
            // First step
            googleLogin()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("27163797556-h3h0pf182ho7qqhda4c9ddl7lp2v68of.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSingInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun googleLogin() {
        val signInIntent = googleSingInClient?.signInIntent

        requestActivity.launch(signInIntent)
    }

    private fun firebaseAuthGoogle(account: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Creating a user account
                    moveMainPage(task.result?.user)
                } else {
                    // Show the Error Message
                    Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }


    private fun signInAndSignUp() {
        try {
            auth?.createUserWithEmailAndPassword(
                binding.emailEdittext.text.toString(),
                binding.passwordEdittext.text.toString()
            )?.addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        // Creating a user account
                        moveMainPage(task.result?.user)
                    }
                    task.exception?.message.isNullOrEmpty() -> {
                        // Show the Error Message
                        Toast.makeText(
                            applicationContext,
                            task.exception?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        // Login if  you have account
                        signInEmail()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signInEmail() {
        auth?.signInWithEmailAndPassword(
            binding.emailEdittext.text.toString(),
            binding.passwordEdittext.text.toString()
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Creating a user account
                moveMainPage(task.result?.user)
            } else {
                // Show the Error Message
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}