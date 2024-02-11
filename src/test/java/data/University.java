package data;

public enum University {
    SECHENOV("Сеченовский университет — Первый Московский государственный медицинский университет имени И.М. Сеченова",
            "Медицина", "https://www.sechenov.ru/", 1),
    RGUTS("РГУТИС — Российский государственный университет туризма и сервиса",
     "Туризм и гостеприимство", "https://rguts.ru/", 3);
    //MFUA("МФЮА — Московский финансово-юридический университет МФЮА","","https://www.mfua.ru/");

    public final String fullname;
    public final String category;
    public final String url;
    public final int ratingPlace;

    University(String fullname, String category, String url, int ratingPlace) {
        this.fullname = fullname;
        this.category = category;
        this.url = url;
        this.ratingPlace = ratingPlace;
    }
}
