package com.simplation.androiddemos.function_summary.charts

import android.annotation.TargetApi
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.simplation.androiddemos.R
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.ComboLineColumnChartView
import java.util.ArrayList

/**
 * LineChartView:折线统计图
 * BubbleChartView:气泡图视图
 * PieChartView:饼图
 * PreviewColumnChartView:预览柱形图视图
 * PreviewLineChartView:预览折线图视图
 * ColumnChartView:柱形图视图
 * ComboLineColumnChartView:组合线柱形图视图
 * AbstractChartView:抽象图表视图
 */
class ChartsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container,
                PlaceholderFragment()).commit()
        }
    }

    /**
     * A fragment containing a combo line/column chart view.
     */
    class PlaceholderFragment : Fragment() {
        private var chart: ComboLineColumnChartView? = null
        private var data: ComboLineColumnChartData? = null
        private var numberOfLines = 1
        private val maxNumberOfLines = 4
        private val numberOfPoints = 12
        var randomNumbersTab = Array(maxNumberOfLines) {
            FloatArray(numberOfPoints)
        }
        private var hasAxes = true
        private var hasAxesNames = true
        private var hasPoints = true
        private var hasLines = true
        private var isCubic = false
        private var hasLabels = false
        @TargetApi(Build.VERSION_CODES.P)
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View {
            setHasOptionsMenu(true)
            val rootView: View =
                inflater.inflate(R.layout.fragment_combo_line_column_chart, container, false)
            chart = rootView.findViewById(R.id.chart)
            // chart.setOnValueTouchListener(new ValueTouchListener());
            generateValues()
            generateData()
            return rootView
        }

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.combo_line_column_chart, menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == R.id.action_reset) {
                reset()
                generateData()
                return true
            }
            if (id == R.id.action_add_line) {
                addLineToData()
                return true
            }
            if (id == R.id.action_toggle_lines) {
                toggleLines()
                return true
            }
            if (id == R.id.action_toggle_points) {
                togglePoints()
                return true
            }
            if (id == R.id.action_toggle_cubic) {
                toggleCubic()
                return true
            }
            if (id == R.id.action_toggle_labels) {
                toggleLabels()
                return true
            }
            if (id == R.id.action_toggle_axes) {
                toggleAxes()
                return true
            }
            if (id == R.id.action_toggle_axes_names) {
                toggleAxesNames()
                return true
            }
            if (id == R.id.action_animate) {
                prepareDataAnimation()
                chart!!.startDataAnimation()
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        private fun generateValues() {
            for (i in 0 until maxNumberOfLines) {
                for (j in 0 until numberOfPoints) {
                    randomNumbersTab[i][j] = Math.random().toFloat() * 50f + 5
                }
            }
        }

        private fun reset() {
            numberOfLines = 1
            hasAxes = true
            hasAxesNames = true
            hasLines = true
            hasPoints = true
            hasLabels = false
            isCubic = false
        }

        private fun generateData() {
            /**
             * 创建图表所需的数据
             * 参数一：列数据
             * 参数二：行数据
             */
            data = ComboLineColumnChartData(generateColumnData(), generateLineData())
            if (hasAxes) {
                val axisX = Axis()
                val axisY = Axis().setHasLines(true)
                // 是否需要设置 X、Y 的名称
                if (hasAxesNames) {
                    axisX.name = "月份"
                    axisY.name = "销售额"
                }
                data!!.axisXBottom = axisX
                data!!.axisYLeft = axisY
            } else {
                data!!.axisXBottom = null
                data!!.axisYLeft = null
            }

            // 给图标设置数据
            chart!!.comboLineColumnChartData = data
        }

        /**
         * 生成行数据
         */
        private fun generateLineData(): LineChartData {
            val lines: MutableList<Line> = ArrayList()
            for (i in 0 until numberOfLines) {
                val values: MutableList<PointValue> = ArrayList()
                for (j in 0 until numberOfPoints) {
                    values.add(PointValue(j.toFloat(), randomNumbersTab[i][j]))
                }
                val line = Line(values)
                line.color = ChartUtils.COLORS[i]
                line.isCubic = isCubic
                line.setHasLabels(hasLabels)
                line.setHasLines(hasLines)
                line.setHasPoints(hasPoints)
                lines.add(line)
            }
            return LineChartData(lines)
        }

        /**
         * 生成列数据
         */
        private fun generateColumnData(): ColumnChartData {
            val numSubcolumns = 1

            // 列的个数
            val numColumns = 24
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            val columns: MutableList<Column> = ArrayList()
            var values: MutableList<SubcolumnValue?>
            for (i in 0 until numColumns) {
                values = ArrayList()
                for (j in 0 until numSubcolumns) {
                    values.add(SubcolumnValue(Math.random().toFloat() * 50 + 5,
                        ChartUtils.DEFAULT_DARKEN_COLOR))
                }
                columns.add(Column(values))
            }
            return ColumnChartData(columns)
        }

        private fun addLineToData() {
            if (data!!.lineChartData.lines.size >= maxNumberOfLines) {
                Toast.makeText(getActivity(), "Samples app uses max 4 lines!", Toast.LENGTH_SHORT)
                    .show()
                return
            } else {
                ++numberOfLines
            }
            generateData()
        }

        private fun toggleLines() {
            hasLines = !hasLines
            generateData()
        }

        private fun togglePoints() {
            hasPoints = !hasPoints
            generateData()
        }

        private fun toggleCubic() {
            isCubic = !isCubic
            generateData()
        }

        private fun toggleLabels() {
            hasLabels = !hasLabels
            generateData()
        }

        private fun toggleAxes() {
            hasAxes = !hasAxes
            generateData()
        }

        private fun toggleAxesNames() {
            hasAxesNames = !hasAxesNames
            generateData()
        }

        private fun prepareDataAnimation() {

            // Line animations
            for (line in data!!.lineChartData.lines) {
                for (value in line.values) {
                    // Here I modify target only for Y values but it is OK to modify X targets as well.
                    value.setTarget(value.x, Math.random().toFloat() * 50 + 5)
                }
            }

            // Columns animations
            for (column in data!!.columnChartData.columns) {
                for (value in column.values) {
                    value.setTarget(Math.random().toFloat() * 50 + 5)
                }
            }
        }

        // 显示触控点的数值
        private inner class ValueTouchListener :
            ComboLineColumnChartOnValueSelectListener {
            override fun onValueDeselected() {
                // TODO Auto-generated method stub
            }

            override fun onColumnValueSelected(
                columnIndex: Int,
                subcolumnIndex: Int,
                value: SubcolumnValue,
            ) {
                Toast.makeText(getActivity(), "Selected column: $value", Toast.LENGTH_SHORT).show()
            }

            override fun onPointValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue) {
                Toast.makeText(getActivity(), "Selected line point: $value", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}