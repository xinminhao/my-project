package components.s_AA0200;

import java.util.HashMap;
import java.util.Map;

public class KanaConverterUtil {

    // 全角 → 半角映射
    private static final Map<String, String> ZENKAKU_TO_HANKAKU = new HashMap<>();
    // 半角 → 全角映射
    private static final Map<String, String> HANKAKU_TO_ZENKAKU = new HashMap<>();

    static {
        // 基础五十音 + 浊音 + 半浊音（部分，常用项，可扩展）
    	String[][] kanaPairs = {
    		    // 基本片假名五十音
    		    {"ア", "ｱ"}, {"イ", "ｲ"}, {"ウ", "ｳ"}, {"エ", "ｴ"}, {"オ", "ｵ"},
    		    {"カ", "ｶ"}, {"キ", "ｷ"}, {"ク", "ｸ"}, {"ケ", "ｹ"}, {"コ", "ｺ"},
    		    {"サ", "ｻ"}, {"シ", "ｼ"}, {"ス", "ｽ"}, {"セ", "ｾ"}, {"ソ", "ｿ"},
    		    {"タ", "ﾀ"}, {"チ", "ﾁ"}, {"ツ", "ﾂ"}, {"テ", "ﾃ"}, {"ト", "ﾄ"},
    		    {"ナ", "ﾅ"}, {"ニ", "ﾆ"}, {"ヌ", "ﾇ"}, {"ネ", "ﾈ"}, {"ノ", "ﾉ"},
    		    {"ハ", "ﾊ"}, {"ヒ", "ﾋ"}, {"フ", "ﾌ"}, {"ヘ", "ﾍ"}, {"ホ", "ﾎ"},
    		    {"マ", "ﾏ"}, {"ミ", "ﾐ"}, {"ム", "ﾑ"}, {"メ", "ﾒ"}, {"モ", "ﾓ"},
    		    {"ヤ", "ﾔ"}, {"ユ", "ﾕ"}, {"ヨ", "ﾖ"},
    		    {"ラ", "ﾗ"}, {"リ", "ﾘ"}, {"ル", "ﾙ"}, {"レ", "ﾚ"}, {"ロ", "ﾛ"},
    		    {"ワ", "ﾜ"}, {"ヲ", "ｦ"}, {"ン", "ﾝ"},

    		    // 浊音（゛）
    		    {"ガ", "ｶﾞ"}, {"ギ", "ｷﾞ"}, {"グ", "ｸﾞ"}, {"ゲ", "ｹﾞ"}, {"ゴ", "ｺﾞ"},
    		    {"ザ", "ｻﾞ"}, {"ジ", "ｼﾞ"}, {"ズ", "ｽﾞ"}, {"ゼ", "ｾﾞ"}, {"ゾ", "ｿﾞ"},
    		    {"ダ", "ﾀﾞ"}, {"ヂ", "ﾁﾞ"}, {"ヅ", "ﾂﾞ"}, {"デ", "ﾃﾞ"}, {"ド", "ﾄﾞ"},
    		    {"バ", "ﾊﾞ"}, {"ビ", "ﾋﾞ"}, {"ブ", "ﾌﾞ"}, {"ベ", "ﾍﾞ"}, {"ボ", "ﾎﾞ"},

    		    // 半浊音（゜）
    		    {"パ", "ﾊﾟ"}, {"ピ", "ﾋﾟ"}, {"プ", "ﾌﾟ"}, {"ペ", "ﾍﾟ"}, {"ポ", "ﾎﾟ"},

    		    // 小型片假名（拗音用）
    		    {"ァ", "ｧ"}, {"ィ", "ｨ"}, {"ゥ", "ｩ"}, {"ェ", "ｪ"}, {"ォ", "ｫ"},
    		    {"ャ", "ｬ"}, {"ュ", "ｭ"}, {"ョ", "ｮ"}, {"ッ", "ｯ"},

    		    // 长音符号
    		    {"ー", "ｰ"},

    		    // 标点符号
    		    {"。", "｡"}, {"、", "､"}, {"「", "｢"}, {"」", "｣"}, {"・", "･"},

    		    // 额外常用全角标点（建议根据实际需求添加）
    		    {"！", "!"}, {"＂", "\""}, {"＃", "#"}, {"＄", "$"}, {"％", "%"},
    		    {"＆", "&"}, {"＇", "'"}, {"（", "("}, {"）", ")"}, {"＊", "*"},
    		    {"＋", "+"}, {"，", ","}, {"－", "-"}, {"．", "."}, {"／", "/"},
    		    {"：", ":"}, {"；", ";"}, {"＜", "<"}, {"＝", "="}, {"＞", ">"},
    		    {"？", "?"}, {"＠", "@"}, {"［", "["}, {"＼", "\\"}, {"］", "]"},
    		    {"＾", "^"}, {"＿", "_"}, {"｀", "`"}, {"｛", "{"}, {"｜", "|"},
    		    {"｝", "}"}, {"～", "~"},

    		    // 全角数字对应半角
    		    {"０", "0"}, {"１", "1"}, {"２", "2"}, {"３", "3"}, {"４", "4"},
    		    {"５", "5"}, {"６", "6"}, {"７", "7"}, {"８", "8"}, {"９", "9"},

    		    // 全角英文字母对应半角（大写）
    		    {"Ａ", "A"}, {"Ｂ", "B"}, {"Ｃ", "C"}, {"Ｄ", "D"}, {"Ｅ", "E"},
    		    {"Ｆ", "F"}, {"Ｇ", "G"}, {"Ｈ", "H"}, {"Ｉ", "I"}, {"Ｊ", "J"},
    		    {"Ｋ", "K"}, {"Ｌ", "L"}, {"Ｍ", "M"}, {"Ｎ", "N"}, {"Ｏ", "O"},
    		    {"Ｐ", "P"}, {"Ｑ", "Q"}, {"Ｒ", "R"}, {"Ｓ", "S"}, {"Ｔ", "T"},
    		    {"Ｕ", "U"}, {"Ｖ", "V"}, {"Ｗ", "W"}, {"Ｘ", "X"}, {"Ｙ", "Y"},
    		    {"Ｚ", "Z"},

    		    // 全角英文字母对应半角（小写）
    		    {"ａ", "a"}, {"ｂ", "b"}, {"ｃ", "c"}, {"ｄ", "d"}, {"ｅ", "e"},
    		    {"ｆ", "f"}, {"ｇ", "g"}, {"ｈ", "h"}, {"ｉ", "i"}, {"ｊ", "j"},
    		    {"ｋ", "k"}, {"ｌ", "l"}, {"ｍ", "m"}, {"ｎ", "n"}, {"ｏ", "o"},
    		    {"ｐ", "p"}, {"ｑ", "q"}, {"ｒ", "r"}, {"ｓ", "s"}, {"ｔ", "t"},
    		    {"ｕ", "u"}, {"ｖ", "v"}, {"ｗ", "w"}, {"ｘ", "x"}, {"ｙ", "y"},
    		    {"ｚ", "z"}
    		};


        for (String[] pair : kanaPairs) {
            ZENKAKU_TO_HANKAKU.put(pair[0], pair[1]);
            HANKAKU_TO_ZENKAKU.put(pair[1], pair[0]);
        }
    }

    /**
     * 全角片假名 → 半角片假名
     */
    public static String toHalfWidthKana(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String ch = String.valueOf(input.charAt(i));
            if (i + 1 < input.length()) {
                // 检查是否为组合字符（如 ガ）
                String combined = ch + input.charAt(i + 1);
                if (ZENKAKU_TO_HANKAKU.containsKey(combined)) {
                    sb.append(ZENKAKU_TO_HANKAKU.get(combined));
                    i++; // 跳过下一个字符
                    continue;
                }
            }
            sb.append(ZENKAKU_TO_HANKAKU.getOrDefault(ch, ch));
        }
        return sb.toString();
    }

    /**
     * 半角片假名 → 全角片假名
     */
    public static String toFullWidthKana(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if ((i + 1) < input.length()) {
                char next = input.charAt(i + 1);
                if ((next == 'ﾞ' || next == 'ﾟ')) {
                    String combined = "" + ch + next;
                    if (HANKAKU_TO_ZENKAKU.containsKey(combined)) {
                        sb.append(HANKAKU_TO_ZENKAKU.get(combined));
                        i++; // 跳过下一个字符
                        continue;
                    }
                }
            }
            String key = String.valueOf(ch);
            sb.append(HANKAKU_TO_ZENKAKU.getOrDefault(key, key));
        }
        return sb.toString();
    }

    // 简单测试
    public static void main(String[] args) {
        String full = "ガギグゲゴ パピプペポ アイウエオ カキクケコ ァー＆Ｐ７ｑ＜ ";
        String half = toHalfWidthKana(full);
        System.out.println("全角: " + full);
        System.out.println("→ 半角: " + half);

        String back = toFullWidthKana(half);
        System.out.println("→ 再转回全角: " + back);
    }
}

