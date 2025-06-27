package garcia.carlosdamian.practicaautenticaciongarciacd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.etEmail)
        val password: EditText = findViewById(R.id.etPassword)
        val button: Button = findViewById(R.id.btnLogin)
        val registerButton: Button = findViewById(R.id.btnGoRegister)

        showError(visible = false)

        button.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                login(email.text.toString(), password.text.toString())
            } else {
                showError("Correo y contraseña no deben estar vacíos", true)
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMain(currentUser)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError(visible = false)
                    goToMain(user!!)
                } else {
                    showError("Usuario y/o contraseña equivocados", true)
                }
            }
    }

    private fun goToMain(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", user.email)
        startActivity(intent)

        finish()
    }

    private fun showError(text: String = "", visible: Boolean) {
        val errorTv: TextView = findViewById(R.id.tvError)
        errorTv.text = text
        errorTv.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}