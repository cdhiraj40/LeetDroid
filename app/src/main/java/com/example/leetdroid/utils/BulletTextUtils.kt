package com.example.leetdroid.utils

import android.content.Context
import android.text.Spanned

import android.text.style.BulletSpan

import android.text.SpannableString

import android.text.Spannable

import android.text.SpannableStringBuilder


object BulletTextUtils {
    /**
     * Returns a CharSequence containing a bulleted and properly indented list.
     *
     * @param leadingMargin In pixels, the space between the left edge of the bullet and the left edge of the text.
     * @param context
     * @param stringArrayResId A resource id pointing to a string array. Each string will be a separate line/bullet-point.
     * @return
     */
    fun makeBulletListFromStringArrayResource(
        leadingMargin: Int,
        context: Context,
        stringArrayResId: Int
    ): CharSequence {
        return makeBulletList(
            leadingMargin,
            context.resources.getStringArray(stringArrayResId).toString()
        )
    }

    /**
     * Returns a CharSequence containing a bulleted and properly indented list.
     *
     * @param leadingMargin In pixels, the space between the left edge of the bullet and the left edge of the text.
     * @param context
     * @param linesResIds An array of string resource ids. Each string will be a separate line/bullet-point.
     * @return
     */
    fun makeBulletListFromStringResources(
        leadingMargin: Int,
        context: Context,
        vararg linesResIds: Int
    ): CharSequence {
        val len = linesResIds.size
        val cslines = arrayOfNulls<CharSequence>(len)
        for (i in 0 until len) {
            cslines[i] = context.getString(linesResIds[i])
        }
        return makeBulletList(leadingMargin, cslines.toString())
    }

    /**
     * Returns a CharSequence containing a bulleted and properly indented list.
     *
     * @param leadingMargin In pixels, the space between the left edge of the bullet and the left edge of the text.
     * @param lines An array of CharSequences. Each CharSequences will be a separate line/bullet-point.
     * @return
     */
    private fun makeBulletList(leadingMargin: Int, vararg lines: CharSequence): CharSequence {
        val sb = SpannableStringBuilder()
        for (i in lines.indices) {
            val line: CharSequence = lines[i].toString() + if (i < lines.size - 1) "\n" else ""
            val spannable: Spannable = SpannableString(line)
            spannable.setSpan(
                BulletSpan(leadingMargin),
                0,
                spannable.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            sb.append(spannable)
        }
        return sb
    }
    fun List<String>.toBulletedList(): CharSequence {
        return SpannableString(this.joinToString("\n")).apply {
            this@toBulletedList.foldIndexed(0) { index, acc, span ->
                val end = acc + span.length + if (index != this@toBulletedList.size - 1) 1 else 0
                this.setSpan(BulletSpan(16), acc, end, 0)
                end
            }
        }
    }
}