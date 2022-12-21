package `in`.silive.spc

import `in`.silive.spc.R
import `in`.silive.spc.databinding.FragmentItemDisplayBinding
import `in`.silive.spc.model.data
import `in`.silive.spc.retrofit.RetrofitClient
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemDisplay : Fragment() {
    private lateinit var binding: FragmentItemDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentItemDisplayBinding.inflate(inflater,container,false)




        return binding.root
    }


}