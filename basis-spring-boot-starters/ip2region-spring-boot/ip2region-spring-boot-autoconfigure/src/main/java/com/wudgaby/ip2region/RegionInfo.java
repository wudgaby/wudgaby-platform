package com.wudgaby.ip2region;

import com.google.common.base.Joiner;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.nutz.plugins.ip2region.DataBlock;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 15:00
 * @desc :
 */
@Data
public class RegionInfo {
    public static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 国家
     */
    private String country;
    /**
     * 区域
     */
    private String area;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 运营商
     */
    private String isp;

    /**
     * db文件中的ptr
     */
    private int dataPtr;

    public RegionInfo() {
    }

    /**
     * Translate this string "中国|华东|江苏省|南京市|电信" to location fields.
     * @param region location region address info array
     */
    public RegionInfo(String[] region) {
        if (region.length < 5) {
            region = Arrays.copyOf(region, 5);
        }
        this.country = filterZero(region[0]);
        this.area = filterZero(region[1]);
        this.province = filterZero(region[2]);
        this.city = filterZero(region[3]);
        this.isp = filterZero(region[4]);
    }

    /**
     * Basic constructor method
     * @param country   Country name
     * @param province  province name
     * @param city      city name
     * @param area      area name
     * @param ISP       ISP name
     */
    public RegionInfo(String country, String province, String city, String area, String isp) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.area = area;
        this.isp = isp;
    }

    public static RegionInfo of(@Nullable DataBlock dataBlock) {
        if (dataBlock == null) {
            return null;
        }
        RegionInfo regionInfo = new RegionInfo(SPLIT_PATTERN.split(dataBlock.getRegion()));
        int cityId = dataBlock.getCityId();
        regionInfo.setCityId(cityId == 0 ? null : cityId);
        regionInfo.setDataPtr(dataBlock.getDataPtr());
        return regionInfo;
    }

    /**
     * 拼接完整的地址
     *
     * @return address
     */
    public String getAddress() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(area);
        regionSet.add(province);
        regionSet.add(city);
        return Joiner.on(StringUtils.EMPTY).skipNulls().join(regionSet);
    }
    /**
     * 拼接完整的地址
     *
     * @return address
     */
    public String getAddressAndIsp() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(area);
        regionSet.add(province);
        regionSet.add(city);
        regionSet.add(isp);
        return Joiner.on(StringUtils.EMPTY).skipNulls().join(regionSet);
    }

    /**
     * 生成未匹配对象
     * @return
     */
    public static RegionInfo createNotMatchRegionInfo() {
        String NOT_MATCH = "未分配或者内网IP|0|0|0|0";
        return new RegionInfo(RegionInfo.SPLIT_PATTERN.split(NOT_MATCH));
    }

    private static String filterZero(@Nullable String info) {
        // null 或 0 返回 null
        if (info == null || "0".equals(info)) {
            return null;
        }
        return info;
    }
}
