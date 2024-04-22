package com.example.imageEditor.ui.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.imageEditor.ImageListener
import com.example.imageEditor.R
import com.example.imageEditor.base.BaseActivity
import com.example.imageEditor.databinding.ActivityDetailImageBinding
import com.example.imageEditor.repository.DetailRepository
import com.example.imageEditor.utils.URL
import com.example.imageEditor.utils.displayImage
import com.example.imageEditor.utils.displayImageWithBitmap

class ImageDetailActivity :
    BaseActivity<ActivityDetailImageBinding>(),
    DetailContract.View,
    CropSuccessCallback {
    private val mImageDetailPresenter by lazy { ImageDetailPresenter(DetailRepository.getInstance()) }
    private val mUrl by lazy { intent.getStringExtra(URL) }
    private val mImageListener by lazy { ImageListener(binding.img) }
    private var mScaleGestureDetector: GestureDetector? = null

    private var isDrawing = false
        set(value) {
            field = value
            binding.rgColor.visibility = if (value) View.VISIBLE else View.GONE
        }
    private var isCropping = false
        set(value) {
            field = value
            if (value) {
                binding.cropView.visibility = View.VISIBLE
                binding.constrainOption.visibility = View.INVISIBLE
                binding.constrainConfirm.visibility = View.VISIBLE
                binding.cropView.setCropSuccessCallback(this)
            } else {
                binding.cropView.visibility = View.GONE
                binding.cropView.removeCropCallback()
            }
        }

    private var mChangeImg = false
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

    override fun getViewBinding(): ActivityDetailImageBinding {
        return ActivityDetailImageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mImageDetailPresenter.setView(this)
        mScaleGestureDetector =
            GestureDetector(
                this,
                mImageListener,
            )
        mUrl?.let {
            binding.img.displayImage(
                it,
            ) { bitmap ->
                mMutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.imgCrop.setOnClickListener {
            isCropping = true
        }
        mScaleGestureDetector?.setOnDoubleTapListener(mImageListener)
        binding.imgDownLoad.setOnClickListener {
            if (mChangeImg) {
                mImageDetailPresenter.saveImage(bitmap = binding.img.drawable.toBitmap())
            } else {
                mUrl?.let {
                    mImageDetailPresenter.downloadImage(it)
                }
            }
        }

        drawListener()

        binding.imgDone.setOnClickListener {
            if (isCropping) {
                croppedBitmap?.let { it1 ->
                    binding.img.displayImageWithBitmap(it1) { bitmap ->
                        mMutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    }
                }
                isCropping = false
            }
            isDrawing = false
            binding.constrainConfirm.visibility = View.INVISIBLE
            binding.constrainOption.visibility = View.VISIBLE
        }
        binding.imgCancel.setOnClickListener {
            isDrawing = false
            isCropping = false
            setupDefaultView()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun drawListener() {
        binding.img.setOnTouchListener { view, event ->
            if (!isDrawing) {
                mScaleGestureDetector?.onTouchEvent(event)
            } else {
                val touchX = event.x
                val touchY = event.y
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mLastTouchX = touchX
                        mLastTouchY = touchY
                        mPath.reset()
                        mPath.moveTo(touchX, touchY)
                        binding.rgColor.visibility = View.GONE
                    }

                    MotionEvent.ACTION_MOVE -> {
                        mPath.quadTo(
                            mLastTouchX,
                            mLastTouchY,
                            (touchX + mLastTouchX) / 2,
                            (touchY + mLastTouchY) / 2,
                        )
                        mLastTouchX = touchX
                        mLastTouchY = touchY
                        mCanvas?.drawPath(mPath, mPaint)
                        binding.img.setImageBitmap(mMutableBitmap)
                        mChangeImg = true
                    }

                    MotionEvent.ACTION_UP -> {
                        binding.rgColor.visibility = View.VISIBLE
                    }
                }
            }
            view.performClick()
            true
        }
        binding.imgDraw.setOnClickListener {
            isDrawing = true
            binding.constrainConfirm.visibility = View.VISIBLE
            binding.constrainOption.visibility = View.INVISIBLE
        }
        binding.rgColor.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbBlack -> mPaint.color = Color.BLACK
                R.id.rbRed -> mPaint.color = Color.RED
                R.id.rbBlue -> mPaint.color = Color.BLUE
                R.id.rbYellow -> mPaint.color = Color.YELLOW
                R.id.rbOrange -> mPaint.color = getColor(R.color.orange)
                R.id.rbPink -> mPaint.color = getColor(R.color.pink)
                R.id.rbWhite -> mPaint.color = Color.WHITE
            }
        }
    }

    private fun setupDefaultView() {
        mChangeImg = false
        binding.constrainOption.visibility = View.VISIBLE
        binding.constrainConfirm.visibility = View.INVISIBLE
        mUrl?.let {
            binding.img.displayImage(it) { bitmap ->
                mMutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            }
        }
    }

    override fun onDestroy() {
        mScaleGestureDetector = null
        super.onDestroy()
    }

    override fun onSaveSuccess() {
        Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_LONG).show()
    }

    override fun onSaveError(throwable: Throwable) {
        Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun onDownloading() {
        Toast.makeText(this, getString(R.string.downloading), Toast.LENGTH_SHORT).show()
    }

    override fun onCropSuccess(
        startPointF: PointF,
        endPointF: PointF,
    ) {
        mMutableBitmap?.let {
            // Tính toán hình chữ nhật cắt (crop rectangle) từ startPointF và endPointF
            val left = minOf(startPointF.x, endPointF.x)
            val top = minOf(startPointF.y, endPointF.y)
            val right = maxOf(startPointF.x, endPointF.x)
            val bottom = maxOf(startPointF.y, endPointF.y)
            val cropRect = RectF(left, top, right, bottom)

            // Tạo một bitmap mới từ phần đã cắt (crop) của bitmap gốc
            croppedBitmap =
                Bitmap.createBitmap(
                    it,
                    cropRect.left.toInt(),
                    cropRect.top.toInt(),
                    cropRect.width().toInt(),
                    cropRect.height().toInt(),
                )
        }
    }
}
