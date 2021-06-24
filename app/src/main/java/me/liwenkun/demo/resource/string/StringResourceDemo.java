package me.liwenkun.demo.resource.string;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.liwenkun.demo.demoframework.DemoBaseFragment;
import me.liwenkun.demo.R;
import me.liwenkun.demo.libannotation.Demo;
import thereisnospon.codeview.CodeView;

import static me.liwenkun.demo.resource.string.SpanUtil.bold;
import static me.liwenkun.demo.resource.string.SpanUtil.color;
import static me.liwenkun.demo.resource.string.SpanUtil.italic;

@Demo(category = "/安卓/资源", title = "字符资源")
public class StringResourceDemo extends DemoBaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_string_resource_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTest = view.findViewById(R.id.text);
        tvTest.setText(Html.fromHtml(getString(R.string.rich_text1)));

        TextView tvTest2 = view.findViewById(R.id.text2);
        tvTest2.setText(getText(R.string.rich_text2));

        CodeView richTextSourceCode = view.findViewById(R.id.rich_text_resource_code);
        richTextSourceCode.showCode("<string name=\"rich_text1\" translatable=\"false\">&lt;b&gt;这是&lt;\\b&gt;&lt;i&gt;一段&lt;/i&gt;&lt;font color=\"red\"&gt;富文本&lt;/font&gt;</string>\n" +
                "<string name=\"rich_text2\" translatable=\"false\"><b>这是</b><font color=\"yellow\">另一段</font><u>富文本</u></string>");

        CodeView spanSourceCode = view.findViewById(R.id.spanned_text_source_code);
        spanSourceCode.showCode(SpanUtil.source);

                        TextView tvSpannedText = view.findViewById(R.id.tv_spanned_text);
        CharSequence text = bold(italic("hello"),
                color(Color.RED, "world"));
        tvSpannedText.setText(text);
        TypedArray array = getContext().obtainStyledAttributes(null, R.styleable.PropertySet, 0, 0);
//        array.getString(R.)

    }
}
