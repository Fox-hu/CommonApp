

import android.view.View
import com.silver.fox.stateslayout.StateLayout
import com.silver.fox.stateslayout.StateParam

/**
 * created by francis.fan on 2019/12/9
 *
 */
class NormalLayout constructor(val view: View) : StateLayout {


    override fun getStateView(): View {
        return view
    }

    override fun bindData(stateParam: StateParam) {

    }
}