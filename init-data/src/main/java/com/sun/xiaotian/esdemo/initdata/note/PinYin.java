package com.sun.xiaotian.esdemo.initdata.note;


import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 汉子转拼音
 */
public class PinYin {

    /**
     * 把汉字转换为拼音，转换结果  pinyin(prefix_content_suffix)
     *
     * @param prefix  前缀
     * @param content 要转换的内容
     * @param suffix  后缀
     */
    public static String from(String prefix, String content, String suffix) throws PinyinException {
        return from(prefix + "_" + content + "_" + suffix);
    }

    /**
     * 汉字转换为拼音，音调用数字表示
     *
     * @param content
     * @return
     * @throws PinyinException
     */
    private static String from(String content) throws PinyinException {
        return PinyinHelper.convertToPinyinString(content, "", PinyinFormat.WITH_TONE_NUMBER);
    }
}
