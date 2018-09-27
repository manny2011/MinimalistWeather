package com.baronzhang.android.weather.new_data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * 空气质量实况
 *
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/2/25
 */
@Entity(tableName = "AirQuality")
public class AirQualityLive {

    public static final String CITY_ID_FIELD_NAME = "cityId";
    public static final String AQI_FIELD_NAME = "aqi";
    public static final String PM25_FIELD_NAME = "pm25";
    public static final String PM10_FIELD_NAME = "pm10";
    public static final String PUBLISH_TIME_FIELD_NAME = "publishTime";
    public static final String ADVICE_FIELD_NAME = "advice";
    public static final String CITY_RANK_FIELD_NAME = "cityRank";
    public static final String QUALITY_FIELD_NAME = "quality";

    public static final String CO_FIELD_NAME = "co";
    public static final String SO2_FIELD_NAME = "so2";
    public static final String NO2_FIELD_NAME = "no2";
    public static final String O3_FIELD_NAME = "o3";
    public static final String PRIMARY_FIELD_NAME = "primary";

    @PrimaryKey
    @ColumnInfo(name = CITY_ID_FIELD_NAME)
    private String cityId;
    @ColumnInfo(name = AQI_FIELD_NAME)
    private int aqi;
    @ColumnInfo(name = PM25_FIELD_NAME)
    private int pm25;
    @ColumnInfo(name = PM10_FIELD_NAME)
    private int pm10;
    @ColumnInfo(name = PUBLISH_TIME_FIELD_NAME)
    private String publishTime;
    @ColumnInfo(name = ADVICE_FIELD_NAME)
    private String advice;//建议
    @ColumnInfo(name = CITY_RANK_FIELD_NAME)
    private String cityRank;//城市排名
    @ColumnInfo(name = QUALITY_FIELD_NAME)
    private String quality;//空气质量
    @ColumnInfo(name = CO_FIELD_NAME)
    private String co;//一氧化碳浓度(mg/m3)
    @ColumnInfo(name = SO2_FIELD_NAME)
    private String so2;//二氧化硫浓度(μg/m3)
    @ColumnInfo(name = NO2_FIELD_NAME)
    private String no2;//二氧化氮浓度(μg/m3)
    @ColumnInfo(name = O3_FIELD_NAME)
    private String o3;//臭氧浓度(μg/m3)
    @ColumnInfo(name = PRIMARY_FIELD_NAME)
    private String primary;//首要污染物


    public AirQualityLive() {
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getCityRank() {
        return cityRank;
    }

    public void setCityRank(String cityRank) {
        this.cityRank = cityRank;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }
}
