package com.rija.pmob7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rija.pmob7.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)

        // Setup RecyclerView
        binding.rvBooks.layoutManager = LinearLayoutManager(this)

        fetchBooks()
    }

    private fun fetchBooks() {
        binding.progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://potterapi-fedeperin.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(
                call: Call<List<Book>>,
                response: Response<List<Book>>
            ) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()

                    adapter = BookAdapter(books) { book ->
                        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra("book", book)
                        }

                        // Slide animation
                        val options = ActivityOptionsCompat.makeCustomAnimation(
                            this@MainActivity,
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )

                        startActivity(intent, options.toBundle())
                    }

                    binding.rvBooks.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}