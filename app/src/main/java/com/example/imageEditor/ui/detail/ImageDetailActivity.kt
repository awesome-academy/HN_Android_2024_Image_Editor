package com.example.imageEditor.ui.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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

class ImageDetailActivity : BaseActivity<ActivityDetailImageBinding>(), DetailContract.View {
    private val mImageDetailPresenter by lazy { ImageDetailPresenter(DetailRepository.getInstance()) }
    private val mUrl by lazy { intent.getStringExtra(URL) }
    private val mImageListener by lazy { ImageListener(binding.img) }
    private var mScaleGestureDetector: GestureDetector? = null

    private var isDrawing = false
        set(value) {
            field = value
            binding.rgColor.visibility = if (value) View.VISIBLE else View.GONE
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
        }

    private var mLastTouchX = 0f
    private var mLastTouchY: Float = 0f

    override fun getViewBinding(): ActivityDetailImageBinding {
        return ActivityDetailImageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mImageDetailPresenter.setView(this)
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
        mScaleGestureDetector =
            GestureDetector(
                this,
                mImageListener,
            )
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
            binding.constrainConfirm.visibility = View.VISIBLE
            binding.rgColor.visibility = View.VISIBLE
            binding.constrainOption.visibility = View.INVISIBLE
            isDrawing = !isDrawing
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
        binding.imgDone.setOnClickListener {
            isDrawing = !isDrawing
            binding.constrainOption.visibility = View.VISIBLE
            binding.constrainConfirm.visibility = View.INVISIBLE
        }
        binding.imgCancel.setOnClickListener {
            isDrawing = !isDrawing
            binding.constrainConfirm.visibility = View.INVISIBLE
            binding.constrainOption.visibility = View.VISIBLE
            mUrl?.let {
                mChangeImg = false
                binding.img.displayImage(it) { bitmap ->
                    mMutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                }
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
}
