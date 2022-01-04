package dev.ogabek.mockapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import dev.ogabek.mockapi.databinding.ActivityMainBinding
import org.json.JSONArray




class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        connectAPI()

    }

    private fun connectAPI() {

        // Connect Uzbek API
        var isConnectedUZB: Boolean = false
        var jsonArrayUZB = JSONArray()
        val API_UZBEK = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/UzbekDB"
        val queueUZB = Volley.newRequestQueue(this)
        val jsonObjectRequestUZB = JsonArrayRequest (
            Request.Method.GET, API_UZBEK, jsonArrayUZB, {
                isConnectedUZB = true
                jsonArrayUZB = it
                Toast.makeText(this, "UZB", Toast.LENGTH_LONG).show()
            }, {
                isConnectedUZB = false
                Toast.makeText(this, "Xatolik sodir bo'ldi", Toast.LENGTH_LONG).show()
            }
        )
        queueUZB.add(jsonObjectRequestUZB)

        // Connect English API
        var isConnectedENGLISH: Boolean = false
        var jsonArrayENGLISH = JSONArray()
        val API_ENGLISH = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/EnglishDB"
        val queueENGLISH = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonArrayRequest (
            Request.Method.GET, API_ENGLISH, jsonArrayENGLISH, {
                isConnectedENGLISH = true
                jsonArrayENGLISH = it
                Toast.makeText(this, "UZB", Toast.LENGTH_LONG).show()
            }, {
                isConnectedENGLISH = false
                Toast.makeText(this, "Something went Wrong", Toast.LENGTH_LONG).show()
            }
        )
        queueENGLISH.add(jsonObjectRequest)

    }

    private fun findAnswer(question: String, jsonArray: JSONArray, language: String): String {
        val question = question.lowercase()
        for (i in 0 until jsonArray.length()) {
            var isHave = true
            val answerObj = jsonArray.getJSONObject(i).getJSONArray("keys")
            for (j in 0 until answerObj.length()) {
                if (!question.contains(answerObj[j].toString())) {
                    isHave = false
                    break
                }
            }
            if (isHave) {
                return jsonArray.getJSONObject(i).get("answer").toString()
            }
        }

        return when (language) {
            "English" -> "Sorry I don't understand the question.\nYou can get more information via contacting  +998 97 777 47 47"
            "Uzbek" -> "Uzr savolingizga tushunmadim.\n+998 97 777 47 47  telefon raqamiga bog'lanib ba'tafsil ma'lumot olishingiz mumkin"
            else -> {"Error in Language / Tilda xatolik sodir bo'ldi"}
        }
    }

}