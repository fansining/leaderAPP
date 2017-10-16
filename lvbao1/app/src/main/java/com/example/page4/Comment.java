package com.example.page4;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by cmm on 15/10/31.
 */
public class Comment {
    private Spanned comment;

    public Comment(String comment) {
        this.comment = Html.fromHtml("<font color='#6699ff'>我: </font>" + comment);
    }

    public Spanned getComment() {
        return comment;
    }
}
