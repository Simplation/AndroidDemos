package com.simplation.androiddemos.function_summary.pdf_generate.pdf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.simplation.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * PDF文件浏览中心
 */
public class PdfCenterDialog extends DialogFragment implements OnClickListener {

    // 关闭识别结果框的按钮
    private Button btn_close;

    // 显示 pdf 文件集合控件
    private ListView lv;

    // pdf 文件数组
    private String[] strings;

    // pdf 文件集合
    private List<VideoModel> list;

    public PdfCenterDialog() {
        super();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        strings = PdfUtils.getString();
        list = new ArrayList<>();
        if (strings != null)
            for (String string : strings) {
                VideoModel model = new VideoModel();
                model.setName(string.substring(string.lastIndexOf('/') + 1));
                model.setPath(string);
                list.add(model);
            }
    }

    @SuppressWarnings("deprecation")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View views = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_pdf_center, null);
        lv = views.findViewById(R.id.lv_pdf_center);
        lv.setAdapter(new MyPdfAdapter(getActivity(), list));
        lv.setOnItemClickListener((parent, view, position, id) -> {
            String path = PdfUtils.getString()[position];
            Intent intent = new Intent(getActivity(), PdfActivity.class);
            intent.putExtra("path", path);
            startActivity(intent);
        });
        btn_close = views.findViewById(R.id.btn_close_pdf_center);
        btn_close.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog show = builder.show();

        // 设置动画
        show.getWindow().getAttributes().windowAnimations = R.style.dialog_ocr_result;

        // 自定义布局
        show.getWindow().setContentView(views);
        show.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                getActivity().getWindowManager().getDefaultDisplay().getHeight() * 2 / 3);

        // 宽高
        show.getWindow().setGravity(Gravity.BOTTOM);

        // 位置 展示位置
        show.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        return show;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close_pdf_center) {
            // 关闭
            this.dismiss();
        }
    }

    /**
     * PDF 文件适配器
     *
     * @author can
     */
    static class MyPdfAdapter extends BaseAdapter {

        private Context ct;
        private List<VideoModel> list;

        MyPdfAdapter(Context c, List<VideoModel> mList) {
            super();
            this.ct = c;
            this.list = mList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VH vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(ct).inflate(R.layout.item_pdf_list, null);
                vh = new VH();
                vh.tv_name = convertView.findViewById(R.id.tv_name_pdf_list);
                vh.tv_time = convertView.findViewById(R.id.tv_time_pdf_list);
                vh.iv = convertView.findViewById(R.id.iv_pdf_list);
                vh.tv_length = convertView.findViewById(R.id.tv_length_pdf_list);
                convertView.setTag(vh);
            } else {
                vh = (VH) convertView.getTag();
            }
            vh.tv_name.setText(list.get(position).getName());
            String path = list.get(position).getPath();
            vh.tv_time.setText(BitmapUtil.getFileCreatedTime(path));
            vh.tv_length.setText(BitmapUtil.getVideoLength(path));
            return convertView;
        }

        class VH {
            ImageView iv;
            TextView tv_name, tv_time, tv_length;
        }

    }

}
