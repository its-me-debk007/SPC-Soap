package `in`.silive.spc

import `in`.silive.spc.databinding.FragmentScannerBinding
import `in`.silive.spc.model.data
import `in`.silive.spc.retrofit.RetrofitClient
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScannerFragment : Fragment(R.layout.fragment_scanner), ZBarScannerView.ResultHandler {
    private lateinit var binding: FragmentScannerBinding
    private lateinit var scannerView: ZBarScannerView
    var adapter:FoodAdapter = FoodAdapter()
    companion object{
        var token:String=""
    }
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it) {
            Toast.makeText(context, "Please allow camera permission", Toast.LENGTH_SHORT).show()
            activity?.finishAffinity()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScannerBinding.bind(view)
        initializeCamera()

//        binding.btn.setOnClickListener {
//            it.isEnabled = false
//            scannerView.resumeCameraPreview(this)
//        }
    }

    override fun handleResult(rawResult: Result?) {
        val data = rawResult?.contents.toString()
        token = data
//        binding.btn.isEnabled = true

        val dialodView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_item_display, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(dialodView)
        val alertDialog: AlertDialog =mBuilder.create()
        alertDialog.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        alertDialog.show()
//        alertDialog.setCancelable(false)
        RetrofitClient.init().getData(token = token,true).enqueue(object :
            Callback<data?> {

            override fun onResponse(call: Call<data?>, response: Response<data?>) {
                adapter.addData(response.body()!!.Item)
                Log.d("ASDAS", token.toString())
                dialodView.findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                var total= (response.body()!!.amount)/100
                dialodView.findViewById<ImageView>(R.id.imageView).setOnClickListener {
                    alertDialog.cancel()
                   initializeCamera()
                }
                if(response.body()!!.status!="Completed")
                {
                    dialodView.findViewById<MaterialButton>(R.id.button).isEnabled=false
                    dialodView.findViewById<TextView>(R.id.textView2).visibility=View.VISIBLE
                }

                dialodView.findViewById<TextView>(R.id.TotalCost).text="₹"+total.toString()
                dialodView.findViewById<TextView>(R.id.Order_Id).text=  response.body()!!._id.toString()
                dialodView.findViewById<RecyclerView>(R.id.recyclerView).adapter=adapter
                dialodView.findViewById<RecyclerView>(R.id.recyclerView).layoutManager= LinearLayoutManager(requireContext())
                dialodView.findViewById<MaterialButton>(R.id.button).setOnClickListener{
                    RetrofitClient.init().postData(token).enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                        ) {
                            Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                    alertDialog.cancel()
                }
            }

            override fun onFailure(call: Call<data?>, t: Throwable) {
                Toast.makeText(requireContext(),"Please check your internet Connection .", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializeCamera() {
        scannerView = ZBarScannerView(context)
        scannerView.apply {
            setResultHandler(this@ScannerFragment)
            setBorderColor(ContextCompat.getColor(requireContext(), R.color.white))
            setLaserColor(ContextCompat.getColor(requireContext(), R.color.black))
            setBorderStrokeWidth(13)
            setMaskColor(ContextCompat.getColor(requireContext(), R.color.colorTranslucent))
            setLaserEnabled(true)
            setBorderLineLength(200)
            setupScanner()
            setAutoFocus(true)
            startCamera()
            binding.scannerContainer.addView(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED
        ) {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
        initializeCamera()
//        scannerView.setResultHandler(this)
//        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }
}