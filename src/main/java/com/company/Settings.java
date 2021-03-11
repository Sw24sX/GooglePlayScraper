package com.company;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    public final String BaseUrl = "https://play.google.com/store/apps";
    public final String SearchUrl = "https://play.google.com/store/search";
    public final String SuggestionUrl = "https://market.android.com/suggest/SuggRequest";

    public Map<Integer, String> PageTokens;

    public Settings() {
        PageTokens = new HashMap<Integer, String>();
        PageTokens.put(0, "");
        PageTokens.put(1, "GAEiAggU:S:ANO1ljLtUJw");
        PageTokens.put(2, "GAEiAggo:S:ANO1ljIeRQQ");
        PageTokens.put(3, "GAEiAgg8:S:ANO1ljIM1CI");
        PageTokens.put(4, "GAEiAghQ:S:ANO1ljLxWBY");
        PageTokens.put(5, "GAEiAghk:S:ANO1ljJkC4I");
        PageTokens.put(6, "GAEiAgh4:S:ANO1ljJfGC4");
        PageTokens.put(7, "GAEiAwiMAQ==:S:ANO1ljL7Yco");
        PageTokens.put(8, "GAEiAwigAQ==:S:ANO1ljLMTko");
        PageTokens.put(9, "GAEiAwi0AQ==:S:ANO1ljJ2maA");
        PageTokens.put(10, "GAEiAwjIAQ==:S:ANO1ljIG2D4");
        PageTokens.put(11, "GAEiAwjcAQ==:S:ANO1ljJ9Wk0");
        PageTokens.put(12, "GAEiAwjwAQ==:S:ANO1ljLFcVI");
    }
}
