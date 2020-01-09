package com.example.newassigment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        buttonRegisteronLogin.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
            finish()
        }

        buttonLoginOnLogin.setOnClickListener{
            doLogin()
        }
    }

    private fun doLogin() {
        if(editTextEmailOnLogin.text.toString().isEmpty()){
            editTextEmail.error="Please Enter your Email."
            editTextEmail.requestFocus()
            return
            }
        if(!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text.toString()).matches()){
            editTextEmail.error="Please Enter Valid Email."
            editTextEmail.requestFocus()
            return
        }
        if(editTextPassword.text.toString().isEmpty()){
            editTextPassword.error="Please Enter your Password"
            editTextPassword.requestFocus()
            return
        }



        auth.signInWithEmailAndPassword(editTextEmailOnLogin.text.toString(), editTextLoginPasswrod.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    
    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null){
            if(currentUser.isEmailVerified){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(baseContext, "Please Verify Your Email Address .",
                    Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()
        }
    }

}
