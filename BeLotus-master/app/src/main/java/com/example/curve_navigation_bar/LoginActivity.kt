package com.example.curve_navigation_bar

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.curve_navigation_bar.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()

        val uid = firebaseAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val database = Firebase.database
        val myRef = database.getReference("User")

        //change of view between login and registration
        binding.inscription.setOnClickListener {
            binding.inscription.background =
                ResourcesCompat.getDrawable(resources, R.drawable.switch_trcks, null)
            binding.inscription.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
            binding.login.background = null
            binding.singUpLayout.visibility = View.VISIBLE
            binding.logInLayout.visibility = View.GONE
            binding.login.setTextColor(ResourcesCompat.getColor(resources, R.color.blue, null))
        }
        binding.login.setOnClickListener {
            binding.login.background =
                ResourcesCompat.getDrawable(resources, R.drawable.switch_trcks, null)
            binding.login.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
            binding.inscription.background = null
            binding.singUpLayout.visibility = View.GONE
            binding.logInLayout.visibility = View.VISIBLE
            binding.inscription.setTextColor(ResourcesCompat.getColor(resources, R.color.blue, null))
        }

        //connexion LogIn
        binding.singIn.setOnClickListener {
            binding.singIn.background = null
            binding.singIn.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.blue,
                    null
                )
            )
            binding.singIn.background =
                ResourcesCompat.getDrawable(resources, R.drawable.switch_trcks, null)
            binding.singUpLayout.visibility = View.GONE
            binding.logInLayout.visibility = View.VISIBLE
            binding.singIn.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))


            //Firebase connexion
            binding.singIn.setOnClickListener {
                val email = binding.eMail.text.toString()
                val pass = binding.passwords.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(this, "Echec", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                } else {
                    Toast.makeText(this, "Certains champs sont vides !!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Firebase registration
        binding.signUp.setOnClickListener {
            val email = binding.eMails.text.toString()
            val pass = binding.passwordET.text.toString()
            val confirmPass = binding.confirmPassword.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val address = binding.postalAdresse.text.toString()


            if (email.isNotEmpty() && isEmailValid(email) && pass.isNotEmpty() && (pass?.count()
                    ?: 0) >= 6 && confirmPass.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && address.isNotEmpty()
            ) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val user = User(firstName, lastName, address, email)
                            if (uid != null) {
                                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        myRef.setValue("user")
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "failed to upload profile",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Les mots de passe ne coincident pas", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Aucun compte ne corresponds aux informations",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}
//  }

//binding.singIn.setOnClickListener {
//  val intent = Intent(this, MainActivity::class.java)
//startActivity(intent)
//}


//}
//}