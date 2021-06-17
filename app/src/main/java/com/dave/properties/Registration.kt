package com.dave.properties

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class Registration : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        progressDialog = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        btnSignUp.setOnClickListener {

            progressDialog.setTitle("Account Verification on going.")
            progressDialog.setMessage("Please wait as your account is being verified.")
            progressDialog.setCanceledOnTouchOutside(false)

            val txtEmail = etEmailAddress.text.toString()
            val txtPassword = etPassword.text.toString()

            if (!TextUtils.isEmpty(txtEmail) &&
                !TextUtils.isEmpty(txtPassword) &&
                txtPassword.length > 6) {

                mAuth.fetchSignInMethodsForEmail(txtEmail)
                    .addOnCompleteListener { task ->
                        val isNewUser = Objects.requireNonNull(
                            Objects.requireNonNull(task.result)!!.signInMethods
                        ).isEmpty()
                        if (isNewUser) {
                            createUser(txtEmail, txtPassword)
                        } else {
                            LoginUser(txtEmail, txtPassword)
                        }
                    }

                progressDialog.show()

            } else {
                progressDialog.dismiss()
                if (TextUtils.isEmpty(txtEmail)) etEmailAddress.error =
                    "Email address cannot be empty.."
                if (TextUtils.isEmpty(txtPassword)) etPassword.error = "Password cannot be empty.."
                if (txtPassword.length <= 6) Toast.makeText(
                    this@Registration,
                    "Enter as stronger password.",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }

    private fun LoginUser(txtEmail: String, txtPassword: String) {
        mAuth.signInWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()

                progressDialog.dismiss()
                Toast.makeText(this@Registration, "Login successful..", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.dismiss()
                Log.w("TAG", "signUserWithEmail:failure", task.exception)
                Toast.makeText(
                    this@Registration, "Authentication failed. Try again " + task.exception!!
                        .localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createUser(txtEmail: String, txtPassword: String) {
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val uid = Objects.requireNonNull(mAuth.currentUser)!!.uid

                val newPost = databaseReference.child("users").child(uid)
                newPost.child("uid").setValue(uid)
                newPost.child("email").setValue(txtEmail)

                progressDialog.dismiss()

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()

                Toast.makeText(this@Registration, "Registration successfully..", Toast.LENGTH_SHORT).show()

            } else {
                progressDialog.dismiss()
                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    this@Registration, "Authentication failed. Try again " + task.exception!!
                        .localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}