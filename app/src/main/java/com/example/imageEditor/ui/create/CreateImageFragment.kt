package com.example.imageEditor.ui.create

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.example.imageEditor.R
import com.example.imageEditor.base.BaseFragment
import com.example.imageEditor.custom.BitmapFilterAdapter
import com.example.imageEditor.custom.CropSuccessCallback
import com.example.imageEditor.custom.ImageListener
import com.example.imageEditor.custom.OnFilterPicked
import com.example.imageEditor.databinding.FragmentCreateBinding
import com.example.imageEditor.repository.CreateImageRepository
import com.example.imageEditor.utils.imageProxyToBitmap

class CreateImageFragment :
    BaseFragment<FragmentCreateBinding>(),
    CreateImageContract.View,
    CropSuccessCallback,
    OnFilterPicked {
    private var mImageCapture: ImageCapture? = null
    private val mPresenter by lazy { CreateImagePresenter(CreateImageRepository.getInstance()) }

    private val mImageListener by lazy { binding?.imgCapture?.let { ImageListener(it) } }
    private var mScaleGestureDetector: GestureDetector? = null

    private var isDrawing = false
        set(value) {
            field = value
            binding?.rgColor?.visibility = if (value) View.VISIBLE else View.GONE
            if (isFiltering) {
                binding?.recycleViewFilterOption?.visibility =
                    if (value) View.GONE else View.VISIBLE
            }
        }
    private var isCropping = false
        set(value) {
            field = value
            if (value) {
                binding?.cropView?.visibility = View.VISIBLE
                binding?.constrainOption?.visibility = View.INVISIBLE
                binding?.constrainConfirm?.visibility = View.VISIBLE
                binding?.cropView?.setCropSuccessCallback(this)
            } else {
                binding?.cropView?.visibility = View.GONE
                binding?.cropView?.removeCropCallback()
            }
        }
    private val mPath = Path()

    private var mCanvas: Canvas? = null
    private var mMutableBitmap: Bitmap? = null
        set(value) {
            field = value
            mCanvas = value?.let { Canvas(it) }
        }

    private var mPaint =
        Paint().apply {
            color = Color.BLACK
            strokeWidth = 5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

    private var mLastTouchX = 0f
    private var mLastTouchY: Float = 0f

    private var croppedBitmap: Bitmap? = null
    private var bitmapCapture: Bitmap? = null
    private var mFlashMode = false
        set(value) {
            field = value
            if (value) {
                mImageCapture?.flashMode = FLASH_MODE_ON
            } else {
                mImageCapture?.flashMode = FLASH_MODE_OFF
            }
        }

    private var isFrontCamera = false
    private var isFiltering = false
        set(value) {
            field = value
            binding?.recycleViewFilterOption?.visibility = if (value) View.VISIBLE else View.GONE
        }
    private lateinit var filterAdapter: BitmapFilterAdapter

    override fun getViewBinding(inflater: LayoutInflater): FragmentCreateBinding {
        return FragmentCreateBinding.inflate(inflater)
    }

    override fun initView() {
        mPresenter.setView(this)
    }

    override fun initData() {
    }

    override fun initListener() {
        mScaleGestureDetector?.setOnDoubleTapListener(mImageListener)
        drawListener()
        binding?.apply {
            btCapture.setOnClickListener {
                takePhoto()
            }
            btCapture.setOnClickListener {
                takePhoto()
            }
            imgBack.setOnClickListener {
                resetView()
            }
            imgDownLoad.setOnClickListener {
                binding?.imgCapture?.drawToBitmap(Bitmap.Config.ARGB_8888)?.let {
                    mPresenter.saveImage(it)
                }
            }
            imgCrop.setOnClickListener {
                isCropping = true
            }
            imgCancel.setOnClickListener {
                isDrawing = false
                isCropping = false
                constrainOption.visibility = View.VISIBLE
                constrainConfirm.visibility = View.INVISIBLE
                bitmapCapture?.let {
                    imgCapture.setImageBitmap(it)
                    mMutableBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
                }
            }
            imgDone.setOnClickListener {
                if (isCropping) {
                    croppedBitmap?.let { it1 ->
                        imgCapture.setImageBitmap(it1)
                        mMutableBitmap = it1.copy(Bitmap.Config.ARGB_8888, true)
                    }
                    isCropping = false
                }
                isDrawing = false
                constrainConfirm.visibility = View.INVISIBLE
                constrainOption.visibility = View.VISIBLE
            }
            imgLight.setOnClickListener {
                mFlashMode = !mFlashMode
            }
            imgChangeCamera.setOnClickListener {
                isFrontCamera = !isFrontCamera
                startCamera(isFrontCamera)
            }
            imgFilter.setOnClickListener {
                isFiltering = !isFiltering
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun drawListener() {
        binding?.apply {
            imgCapture.setOnTouchListener { view, event ->
                if (!isDrawing) {
                    mScaleGestureDetector?.onTouchEvent(event)
                } else {
                    val drawable = imgCapture.drawable
                    val imageMatrix = imgCapture.imageMatrix

                    // Tính toán tọa độ tương đối trong Drawable
                    val imageBounds = drawable.bounds
                    val intrinsicWidth = drawable.intrinsicWidth
                    val intrinsicHeight = drawable.intrinsicHeight

                    // Lấy ra ma trận hiển thị của Drawable
                    val displayMatrix = Matrix()
                    imageMatrix.invert(displayMatrix)

                    val touchPoint = floatArrayOf(event.x, event.y)
                    displayMatrix.mapPoints(touchPoint)

                    val imageX =
                        (touchPoint[0] - imageBounds.left) * (intrinsicWidth / imageBounds.width())
                    val imageY =
                        (touchPoint[1] - imageBounds.top) * (intrinsicHeight / imageBounds.height())
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            mLastTouchX = imageX
                            mLastTouchY = imageY
                            mPath.reset()
                            mPath.moveTo(imageX, imageY)
                            rgColor.visibility = View.GONE
                        }

                        MotionEvent.ACTION_MOVE -> {
                            mPath.quadTo(
                                mLastTouchX,
                                mLastTouchY,
                                (imageX + mLastTouchX) / 2,
                                (imageY + mLastTouchY) / 2,
                            )
                            mLastTouchX = imageX
                            mLastTouchY = imageY
                            mCanvas?.drawPath(mPath, mPaint)
                            imgCapture.setImageBitmap(mMutableBitmap)
                        }

                        MotionEvent.ACTION_UP -> {
                            rgColor.visibility = View.VISIBLE
                        }
                    }
                }
                view.performClick()
                true
            }
            imgDraw.setOnClickListener {
                isDrawing = true
                constrainConfirm.visibility = View.VISIBLE
                constrainOption.visibility = View.INVISIBLE
            }
            rgColor.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.rbBlack -> mPaint.color = Color.BLACK
                    R.id.rbRed -> mPaint.color = Color.RED
                    R.id.rbBlue -> mPaint.color = Color.BLUE
                    R.id.rbYellow -> mPaint.color = Color.YELLOW
                    R.id.rbOrange -> mPaint.color = requireContext().getColor(R.color.orange)
                    R.id.rbPink -> mPaint.color = requireContext().getColor(R.color.pink)
                    R.id.rbWhite -> mPaint.color = Color.WHITE
                }
            }
        }
    }

    private fun startCamera(isFrontCamera: Boolean = false) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview =
                Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)
                    }

            mImageCapture =
                ImageCapture.Builder()
                    .build()
            // Select back camera as a default
            val cameraSelector =
                if (!isFrontCamera) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    mImageCapture,
                )
            } catch (exc: Exception) {
                Log.e(">>>>>", exc.message.toString())
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        mImageCapture?.run {
            takePicture(
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        try {
                            val bitmap = imageProxyToBitmap(image)
                            bitmap?.let {
                                setViewAfterCapture(it)
                            }
                        } catch (ex: Exception) {
                            Log.e(">>>>>>", "Error displaying captured image: ${ex.message}")
                        } finally {
                            // Đóng ImageProxy sau khi sử dụng xong
                            image.close()
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                    }
                },
            )
        }
    }

    private fun resetView() {
        binding?.imgCapture?.alpha = 0F
        binding?.viewFinder?.visibility = View.VISIBLE
        binding?.constrainOption?.visibility = View.GONE
        binding?.lnOption?.visibility = View.VISIBLE
        binding?.recycleViewFilterOption?.visibility = View.GONE
    }

    private fun setViewAfterCapture(bitmap: Bitmap) {
        binding?.imgCapture?.alpha = 1F
        binding?.viewFinder?.visibility = View.GONE
        binding?.constrainOption?.visibility = View.VISIBLE
        binding?.lnOption?.visibility = View.GONE
        mMutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmapCapture = bitmap
        binding?.imgCapture?.setImageBitmap(mMutableBitmap)
        filterAdapter = BitmapFilterAdapter(bitmap, requireActivity(), this)
        binding?.recycleViewFilterOption?.adapter = filterAdapter
    }

    override fun onSaveSuccess() {
        Toast.makeText(requireContext(), getString(R.string.save_success), Toast.LENGTH_LONG).show()
        resetView()
    }

    override fun onSaveError(throwable: Throwable) {
        Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
        resetView()
    }

    override fun onDownloading() {
        Toast.makeText(requireContext(), getString(R.string.downloading), Toast.LENGTH_SHORT).show()
    }

    override fun onCropSuccess(
        startPointF: PointF,
        endPointF: PointF,
    ) {
        binding?.imgCapture?.let {
            val drawable = it.drawable
            val imageMatrix = binding?.imgCapture?.imageMatrix

            // Tính toán tọa độ tương đối trong Drawable
            val imageBounds = drawable.bounds
            val intrinsicWidth = drawable.intrinsicWidth
            val intrinsicHeight = drawable.intrinsicHeight

            // Lấy ra ma trận hiển thị của Drawable
            val displayMatrix = Matrix()
            imageMatrix?.invert(displayMatrix)

            val startPoint = floatArrayOf(startPointF.x, startPointF.y)
            displayMatrix.mapPoints(startPoint)

            val endPoint = floatArrayOf(endPointF.x, endPointF.y)
            displayMatrix.mapPoints(endPoint)

            val startX =
                (startPoint[0] - imageBounds.left) * (intrinsicWidth / imageBounds.width())
            val startY =
                (startPoint[1] - imageBounds.top) * (intrinsicHeight / imageBounds.height())

            val endX =
                (endPoint[0] - imageBounds.left) * (intrinsicWidth / imageBounds.width())
            val endY =
                (endPoint[1] - imageBounds.top) * (intrinsicHeight / imageBounds.height())
            mMutableBitmap?.let { bitmap ->
                // Tính toán hình chữ nhật cắt (crop rectangle) từ startPointF và endPointF
                val left = minOf(startX, endX)
                val top = minOf(startY, endY)
                val right = maxOf(startX, endX)
                val bottom = maxOf(startY, endY)
                val cropRect = RectF(left, top, right, bottom)

                // Tạo một bitmap mới từ phần đã cắt (crop) của bitmap gốc
                croppedBitmap =
                    Bitmap.createBitmap(
                        bitmap,
                        cropRect.left.toInt(),
                        cropRect.top.toInt(),
                        cropRect.width().toInt(),
                        cropRect.height().toInt(),
                    )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
        mScaleGestureDetector =
            mImageListener?.let {
                GestureDetector(
                    requireContext(),
                    it,
                )
            }
    }

    override fun onPause() {
        super.onPause()
        ProcessCameraProvider.getInstance(requireContext()).get().unbindAll()
    }

    override fun onStop() {
        mScaleGestureDetector = null
        super.onStop()
    }

    override fun filterPicked(colorFilter: ColorFilter) {
        binding?.imgCapture?.colorFilter = colorFilter
    }
}
