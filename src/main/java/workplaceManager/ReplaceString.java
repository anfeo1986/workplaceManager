package workplaceManager;

public class ReplaceString {
    public static String replace(String str) {
        if(str == null || str.isEmpty()) {
            return "";
        }
        return str
                .replaceAll("&", "&amp;")
                .replaceAll("\"","&quot;") // Двойная кавычка
                .replaceAll("<", "&lt;")   // Открывающаяся угловая скобка
                .replaceAll(">", "&gt;");  // Закрывающаяся угловая скобка
    }
}
