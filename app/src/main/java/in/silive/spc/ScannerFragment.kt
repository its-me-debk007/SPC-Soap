package `in`.silive.spc

import `in`.silive.spc.databinding.FragmentScannerBinding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScannerFragment : Fragment(R.layout.fragment_scanner), ZBarScannerView.ResultHandler {
    private lateinit var binding: FragmentScannerBinding
    private lateinit var scannerView: ZBarScannerView
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

        binding.btn.setOnClickListener {
            it.isEnabled = false
            scannerView.resumeCameraPreview(this)
        }
    }

    override fun handleResult(rawResult: Result?) {
        val data = rawResult?.contents.toString()
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()

        binding.btn.isEnabled = true
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

        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }
}