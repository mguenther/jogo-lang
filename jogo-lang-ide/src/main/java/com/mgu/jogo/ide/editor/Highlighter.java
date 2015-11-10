package com.mgu.jogo.ide.editor;

import org.apache.commons.lang3.StringUtils;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Highlighter {

    private static final Pattern KEYWORD_PATTERN = Pattern.compile("\\b(to|repeat|end)|([0-9]+)|(:[A-Za-z]+[A-Za-z0-9]*)\\b");

    public static StyleSpans<Collection<String>> computeHighlighting(final String text) {
        final Matcher matcher = KEYWORD_PATTERN.matcher(text);
        final StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        int lastKeywordIndex = 0;
        while(matcher.find()) {
            String token = text.substring(matcher.start(), matcher.end());
            if (StringUtils.isNumeric(token)) {
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordIndex);
                spansBuilder.add(Collections.singleton("numbers"), matcher.end() - matcher.start());
            } else if (token.startsWith(":")) {
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordIndex);
                spansBuilder.add(Collections.singleton("expression"), matcher.end() - matcher.start());
            } else {
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordIndex);
                spansBuilder.add(Collections.singleton("keyword"), matcher.end() - matcher.start());
            }
            lastKeywordIndex = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKeywordIndex);
        return spansBuilder.create();
    }
}
