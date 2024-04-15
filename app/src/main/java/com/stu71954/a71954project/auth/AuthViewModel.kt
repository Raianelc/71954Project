package com.stu71954.a71954project.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.github.javafaker.Faker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AuthViewModel(
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val db = FirebaseFirestore.getInstance()

    fun signup(email: String, password: String, navController: NavController) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                sharedPreferences.edit().putString("userID", userId).apply()
                val user = hashMapOf(
                    "email" to email,
                    "firstName" to Faker().name().firstName(),
                    "lastName" to Faker().name().lastName(),
                    "address" to Faker().address().fullAddress(),
                    "cart" to hashMapOf(
                        "items" to emptyList<Map<String, Any>>(), // Initialize items as a list of maps
                        "total" to 0
                    ),
                    "orders" to emptyList<String>(),
                    "phoneNumber" to Faker().phoneNumber().phoneNumber(),
                    "profilePictureUrl" to "https://thispersondoesnotexist.com/"
                )
                userId?.let {
                    db.collection("users").document(it)
                        .set(user)
                        .addOnSuccessListener {
                            navController.navigate("productlistscreen")
                        }
                        .addOnFailureListener { e ->
                            _errorMessage.value = e.message
                        }
                }
            } else {
                _errorMessage.value = task.exception?.message
            }
        }
    }

    fun login(email: String, password: String, navController: NavController) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                sharedPreferences.edit().putString("userID", userId).apply()
                userId?.let {
                    db.collection("users").document(it)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                navController.navigate("productlistscreen")
                            } else {
                                _errorMessage.value = "User not found"
                            }
                        }
                        .addOnFailureListener { e ->
                            _errorMessage.value = e.message
                        }
                }
            } else {
                _errorMessage.value = task.exception?.message
            }
        }
    }

    fun logout(navController: NavController) {
        auth.signOut()
        sharedPreferences.edit().remove("userID").apply()
        navController.navigate("signup")
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

}