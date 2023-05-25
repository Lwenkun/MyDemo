package me.liwenkun.demo.resource.string;

import static me.liwenkun.demo.resource.string.SpanUtil.bold;
import static me.liwenkun.demo.resource.string.SpanUtil.color;
import static me.liwenkun.demo.resource.string.SpanUtil.italic;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.liwenkun.demo.R;
import me.liwenkun.demo.demoframework.DemoBaseFragment;
import me.liwenkun.demo.libannotation.Demo;
import thereisnospon.codeview.CodeView;

@Demo(title = "字符资源")
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

        TextView tvSpannedText2 = view.findViewById(R.id.tv_spanned_text2);
        SpannableString spannableString = new SpannableString("这是一段富文本");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD){}, 0, 3,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ScaleXSpan(2f){}, 2, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC){}, 1, 4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSpannedText2.setText(spannableString);
    }
}
