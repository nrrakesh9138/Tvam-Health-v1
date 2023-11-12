package utils

import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.NonNull
import com.tvam.health.R
//import kotlinx.android.synthetic.main.atyati_progressbar_layout.view.*


class CustomProgressBar {

     var dialog: Dialog? = null

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context?, title: CharSequence?): Dialog {
        val inflator = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflator.inflate(R.layout.atyati_progressbar_layout, null)
//        setColorFilter(
//           // view.circular_progress_bar.indeterminateDrawable,
//            ResourcesCompat.getColor(context.resources, R.color.color_orange, null)
//        )
        dialog = Dialog(context, R.style.CustomProgressBarTheme)

        if (dialog?.window != null) {
            dialog?.window!!.setBackgroundDrawableResource(R.color.bg_color)
        }

        dialog?.setContentView(view)
        dialog?.show()
        dialog?.setCancelable(false)
        return dialog!!
    }

    fun setColorFilter(@NonNull drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

}
