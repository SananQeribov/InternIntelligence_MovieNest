package com.legalist.movienest.ui
import com.legalist.data.model.Category
import com.legalist.movienest.R
import com.legalist.movienest.adapter.SearchAdapter
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.FragmentCategoryBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
class SearchFragment : BaseFragment<FragmentCategoryBinding>() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<Category>()
    private lateinit var adapter: SearchAdapter

    override fun buildUi() {
        recyclerView = binding.recyclerView
        searchView = binding.searchView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        addDataToList()

        adapter = SearchAdapter(mList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

    }




    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryBinding {
        return FragmentCategoryBinding.inflate(inflater, container, false)
    }

    private fun addDataToList() {
        mList.add(
            Category("SpiderMan", R.drawable.spiderman,"https://www.youtube.com/watch?v=JfVOs4VSpmA" )
        )
        mList.add(
            Category("Batman", R.drawable.batman, "https://www.youtube.com/watch?v=ug8bB0mmyQE?si=CrcKwHZiq1YcZdP5")
        )
        mList.add(
            Category("Inception", R.drawable.inception, "https://www.youtube.com/watch?v=8hP9D6kZseM?si=Iu6oFytnb8Z3H7QS")
        )
        mList.add(
            Category("The Matrix", R.drawable.matrix, "https://www.youtube.com/watch?v=d-wBjHZRSJA?si=9WOn_IDo-jnMN3ns")
        )
        mList.add(
            Category("John Wick", R.drawable.johnwick, "https://www.youtube.com/watch?v=QKryqvikLJI?si=-z0renxXJt7ycY-D")
        )
        mList.add(
            Category("Madagascar", R.drawable.md, "https://www.youtube.com/watch?v=kAG-PccLiVY?si=hMVL8c0BtsOYWAS5")
        )
        mList.add(
            Category("Jurassic Park", R.drawable.js, "https://www.youtube.com/watch?v=ot0cwH6r0Lg?si=ThUiVj_JPvz_sZCX")
        )
        mList.add(
            Category("Red One", R.drawable.ro, "https://www.youtube.com/watch?v=7l3hfD74X-4?si=aome_9yHjVnshLTP")
        )
    }
    private fun filterList(query: String?) {
        if (!query.isNullOrEmpty()) {
            val filteredList = mList.filter {
                it.title.uppercase(Locale.ROOT).contains(query.uppercase(Locale.ROOT))
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList as ArrayList<Category>)
            }
        } else {
            // Reset the list if the query is empty
            adapter.setFilteredList(mList)
        }
    }

}
