package com.simplation.androiddemos.function_summary.picture.listener;

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/24
 * @描述:
 * @更新:
 */
public interface DragListener {
    /**
     * 是否将 item拖动到删除处，根据状态改变颜色
     *
     * @param isDelete
     */
    void deleteState(boolean isDelete);

    /**
     * 是否于拖拽状态
     *
     * @param isStart
     */
    void dragState(boolean isStart);
}
