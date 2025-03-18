package declaration.stock;

/**
 * 股票数据字段 <br>
 * Stock data field <br>
 * <br>
 * 列举了所有可能的股票数据字段 <br>
 * List all possible stock data fields <br>
 * <br>
 */
public enum Field {
    STOCK_CODE("Stkcd"),
    MARKET_TYPE("Markettype"),

    TRADE_DATE("Trddt"),
    TRADE_STATUS("Trdsta"),
    OPEN_PRICE("Opnprc"),
    HIGH_PRICE("Hiprc"),
    LOW_PRICE("Loprc"),
    CLOSE_PRICE("Clsprc"),
    DAILY_SHARES_TRADED("Dnshrtrd"),
    DAILY_VALUE_TRADED("Dnvaltrd"),
    MARKET_VALUE("Dsmvosd"),
    TOTAL_MARKET_VALUE("Dsmvtll"),
    STOCK_RETURN_WITH_REINVEST("Dretwd"),
    STOCK_RETURN("Dretnd"),
    DIVIDEND_CLOSE_PRICE_WITH_REINVEST("Adjprcwd"),
    DIVIDEND_CLOSE_PRICE("Adjprcnd"),
    CAPITAL_CHANGE_DATE("Capchgdt"),
    SHARED_POST_TRADED("Ahshrtrd_D"),
    VALUE_POST_TRADED("Ahvaltrd_D"),
    PRE_CLOSE_PRICE("PreClosePrice"),
    CHANGE_RATIO("ChangeRatio"),

    STOCK_NAME("Stknme_en"),
    LIST_DATE("Listdt"),
    COUNTRY_CODE("Cuntrycd"),
    COMPANY_NAME("Conme"),
    COMPANY_EN_NAME("Conme_en"),
    INDUSTRY_CODE_A("Indcd"),
    INDUSTRY_NAME_A("Indnme_en"),
    INDUSTRY_CODE_B("Nindcd"),
    INDUSTRY_NAME_B("Nindnme_en"),
    INDUSTRY_CODE_C("Nnindcd"),
    INDUSTRY_NAME_C("Nnindnme_en"),
    ESTABLISH_DATE("Estbdt"),
    PROVINCE("PROVINCE_EN"),
    PROVINCE_CODE("PROVINCECODE"),
    CITY("CITY_EN"),
    CITY_CODE("CITYCODE"),
    OWNERSHIP_TYPE("OWNERSHIPTYPE_EN"),
    OWNERSHIP_CODE("OWNERSHIPTYPECODE"),
    FIRST_RECORD_DATE("Favaldt"),
    CURRENCY_TRADED("Curtrd"),
    IPO_PREMIUM("Ipoprm"),
    IPO_PRICE("Ipoprc"),
    IPO_CURRENCY("Ipocur"),
    IPO_SHARES("Nshripo"),
    PAR_VALUE_CURRENCY("Parvcur"),
    IPO_DATE("Ipodt"),
    PAR_VALUE("Parval"),
    ZONE_CODE("Sctcd"),
    COMPANY_STATUS("Statco"),
    CROSS_CODE("Crcd"),
    STATE_DATE("Statdt"),
    COMMNT("Commnt"),
    ;

    public final String fieldName;

    Field(String f) {
        fieldName = f;
    }

}
