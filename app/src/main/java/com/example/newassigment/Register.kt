package com.example.newassigment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        buttonRegisterOnRegister.setOnClickListener {
            signUpUser()
        }

    }
    private fun signUpUser(){

        if(editTextFullName.text.toString().isEmpty()){
            editTextFullName.error="Please Enter Your Full Name."
            editTextFullName.requestFocus()
            return
        }
        if(editTextUsername.text.toString().isEmpty()){
            editTextUsername.error="Please Enter Your Username"
            editTextUsername.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text.toString()).matches()){
            editTextEmail.error="Please Email a Valid Email."
            editTextEmail.requestFocus()
            return
        }


        if(radioGroup.getCheckedRadioButtonId() == -1){
            radioButtonLecture.setError("Select One")
            //radioGroup.requestFocus()
            return
        }else{
            radioButtonLecture.setError(null)
        }


        if(editTextPassword.text.toString().isEmpty()){
            editTextPassword.error="Please Enter your Password"
            editTextPassword.requestFocus()
            return
        }
        if(!editTextConfirmPassword.text.toString().equals(editTextPassword.text.toString())){
            editTextConfirmPassword.error = "Your Password is not Match"
            editTextConfirmPassword.requestFocus()
            return
        }

        var userState : String? =""
        if(radioButtonStudent.isChecked){
            userState="Lecture"
        }else{
            userState="Student"
        }


        auth.createUserWithEmailAndPassword(editTextEmail.text.toString(), editTextPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user=auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                               /* var user =Users(editTextFullName.text.toString(),editTextUsername.text.toString(),userState)
                                database.child("users").child(editTextEmail.text.toString()).setValue(user)
                                */

                                val ref = FirebaseDatabase.getInstance().getReference("users")
                                val uid = ref.push().key
                                val user=Users(editTextEmail.text.toString(),editTextFullName.text.toString(),editTextUsername.text.toString(),userState)

                                if (uid != null) {
                                    ref.child(uid).setValue(user).addOnCompleteListener{
                                        Toast.makeText(baseContext, "Account created, Verify on Email .",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }







                                startActivity(Intent(this,Login::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Sign Up failed. Try again after some time. ",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }


}
