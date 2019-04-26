
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeZone.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TimeZone">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="(GMT-11.00) Midway Island / Samoa"/>
 *     &lt;enumeration value="(GMT-10.00) Hawaii"/>
 *     &lt;enumeration value="(GMT-09.00) Alaska"/>
 *     &lt;enumeration value="(GMT-08.00) Pacific Time (US &amp; Canada)"/>
 *     &lt;enumeration value="(GMT-08.00) Tijuana / Baja California"/>
 *     &lt;enumeration value="(GMT-07.00) Arizona"/>
 *     &lt;enumeration value="(GMT-07.00) Chihuahua / La Paz / Mazatlan - New"/>
 *     &lt;enumeration value="(GMT-07.00) Chihuahua / La Paz / Mazatlan - Old"/>
 *     &lt;enumeration value="(GMT-07.00) Mountain Time (US &amp; Canada)"/>
 *     &lt;enumeration value="(GMT-06.00) Central America"/>
 *     &lt;enumeration value="(GMT-06.00) Central Time (US &amp; Canada)"/>
 *     &lt;enumeration value="(GMT-06.00) Guadalajara / Mexico City / Monterrey - New"/>
 *     &lt;enumeration value="(GMT-06.00) Guadalajara / Mexico City / Monterrey - Old"/>
 *     &lt;enumeration value="(GMT-06.00) Saskatchewan"/>
 *     &lt;enumeration value="(GMT-05.00) Bogota / Lima / Quito / Rio Branco"/>
 *     &lt;enumeration value="(GMT-05.00) Eastern Time (US &amp; Canada)"/>
 *     &lt;enumeration value="(GMT-05.00) Indiana (East)"/>
 *     &lt;enumeration value="(GMT-04.00) Atlantic Time (Canada)"/>
 *     &lt;enumeration value="(GMT-04.00) Caracas / La Paz"/>
 *     &lt;enumeration value="(GMT-04.00) Manaus"/>
 *     &lt;enumeration value="(GMT-04.00) Santiago"/>
 *     &lt;enumeration value="(GMT-03.30) Newfoundland"/>
 *     &lt;enumeration value="(GMT-03.00) Brasilia"/>
 *     &lt;enumeration value="(GMT-03.00) Buenos Aires / Georgetown"/>
 *     &lt;enumeration value="(GMT-03.00) Greenland"/>
 *     &lt;enumeration value="(GMT-03.00) Montevideo"/>
 *     &lt;enumeration value="(GMT-02.00) Mid-Atlantic"/>
 *     &lt;enumeration value="(GMT-01.00) Azores"/>
 *     &lt;enumeration value="(GMT-01.00) Cape Verde Is."/>
 *     &lt;enumeration value="(GMT) Casablanca / Monrovia / Reykjavik"/>
 *     &lt;enumeration value="(GMT) Greenwich Mean Time - Dublin / Edinburgh / Lisbon / London"/>
 *     &lt;enumeration value="(GMT) Greenwich Mean Time"/>
 *     &lt;enumeration value="(GMT+01.00) Amsterdam / Berlin / Bern / Rome / Stockholm / Vienna"/>
 *     &lt;enumeration value="(GMT+01.00) Belgrade / Bratislava / Budapest / Ljubljana / Prague"/>
 *     &lt;enumeration value="(GMT+01.00) Brussels / Copenhagen / Madrid / Paris"/>
 *     &lt;enumeration value="(GMT+01.00) Sarajevo / Skopje / Warsaw / Zagreb"/>
 *     &lt;enumeration value="(GMT+01.00) West Central Africa"/>
 *     &lt;enumeration value="(GMT+02.00) Amman"/>
 *     &lt;enumeration value="(GMT+02.00) Athens / Bucharest / Istanbul"/>
 *     &lt;enumeration value="(GMT+02.00) Beirut"/>
 *     &lt;enumeration value="(GMT+02.00) Cairo"/>
 *     &lt;enumeration value="(GMT+02.00) Harare / Pretoria"/>
 *     &lt;enumeration value="(GMT+02.00) Helsinki / Kyiv / Riga / Sofia / Tallinn / Vilnius"/>
 *     &lt;enumeration value="(GMT+02.00) Jerusalem"/>
 *     &lt;enumeration value="(GMT+02.00) Minsk"/>
 *     &lt;enumeration value="(GMT+02.00) Windhoek"/>
 *     &lt;enumeration value="(GMT+03.00) Baghdad"/>
 *     &lt;enumeration value="(GMT+03.00) Kuwait / Riyadh"/>
 *     &lt;enumeration value="(GMT+03.00) Moscow / St. Petersburg / Volgograd"/>
 *     &lt;enumeration value="(GMT+03.00) Nairobi"/>
 *     &lt;enumeration value="(GMT+03.00) Tbilisi"/>
 *     &lt;enumeration value="(GMT+03.30) Tehran"/>
 *     &lt;enumeration value="(GMT+04.00) Abu Dhabi / Muscat"/>
 *     &lt;enumeration value="(GMT+04.00) Baku"/>
 *     &lt;enumeration value="(GMT+04.00) Yerevan"/>
 *     &lt;enumeration value="(GMT+04.30) Kabul"/>
 *     &lt;enumeration value="(GMT+05.00) Ekaterinburg"/>
 *     &lt;enumeration value="(GMT+05.00) Islamabad / Karachi / Tashkent"/>
 *     &lt;enumeration value="(GMT+05.30) Chennai / Kolkata / Mumbai / New Delhi"/>
 *     &lt;enumeration value="(GMT+05.30) Sri Jayawardenepura"/>
 *     &lt;enumeration value="(GMT+05.45) Kathmandu"/>
 *     &lt;enumeration value="(GMT+06.00) Almaty / Novosibirsk"/>
 *     &lt;enumeration value="(GMT+06.00) Astana / Dhaka"/>
 *     &lt;enumeration value="(GMT+06.30) Yangon (Rangoon)"/>
 *     &lt;enumeration value="(GMT+07.00) Bangkok / Hanoi / Jakarta"/>
 *     &lt;enumeration value="(GMT+07.00) Krasnoyarsk"/>
 *     &lt;enumeration value="(GMT+08.00) Beijing / Chongqing / Hong Kong / Urumqi"/>
 *     &lt;enumeration value="(GMT+08.00) Irkutsk / Ulaan Bataar"/>
 *     &lt;enumeration value="(GMT+08.00) Kuala Lumpur / Singapore"/>
 *     &lt;enumeration value="(GMT+08.00) Perth"/>
 *     &lt;enumeration value="(GMT+08.00) Taipei"/>
 *     &lt;enumeration value="(GMT+09.00) Osaka / Sapporo / Tokyo"/>
 *     &lt;enumeration value="(GMT+09.00) Seoul"/>
 *     &lt;enumeration value="(GMT+09.00) Yakutsk"/>
 *     &lt;enumeration value="(GMT+09.30) Adelaide"/>
 *     &lt;enumeration value="(GMT+09.30) Darwin"/>
 *     &lt;enumeration value="(GMT+10.00) Brisbane"/>
 *     &lt;enumeration value="(GMT+10.00) Canberra / Melbourne / Sydney"/>
 *     &lt;enumeration value="(GMT+10.00) Guam / Port Moresby"/>
 *     &lt;enumeration value="(GMT+10.00) Hobart"/>
 *     &lt;enumeration value="(GMT+10.00) Vladivostok"/>
 *     &lt;enumeration value="(GMT+11.00) Magadan / Solomon Is. / New Caledonia"/>
 *     &lt;enumeration value="(GMT+12.00) Auckland / Wellington"/>
 *     &lt;enumeration value="(GMT+12.00) Fiji / Kamchatka / Marshall Is."/>
 *     &lt;enumeration value="(GMT+13.00) Nuku'alofa"/>
 *     &lt;enumeration value="GMT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TimeZone")
@XmlEnum
public enum TimeZone {

    @XmlEnumValue("(GMT-11.00) Midway Island / Samoa")
    GMT_11_00_MIDWAY_ISLAND_SAMOA("(GMT-11.00) Midway Island / Samoa"),
    @XmlEnumValue("(GMT-10.00) Hawaii")
    GMT_10_00_HAWAII("(GMT-10.00) Hawaii"),
    @XmlEnumValue("(GMT-09.00) Alaska")
    GMT_09_00_ALASKA("(GMT-09.00) Alaska"),
    @XmlEnumValue("(GMT-08.00) Pacific Time (US & Canada)")
    GMT_08_00_PACIFIC_TIME_US_CANADA("(GMT-08.00) Pacific Time (US & Canada)"),
    @XmlEnumValue("(GMT-08.00) Tijuana / Baja California")
    GMT_08_00_TIJUANA_BAJA_CALIFORNIA("(GMT-08.00) Tijuana / Baja California"),
    @XmlEnumValue("(GMT-07.00) Arizona")
    GMT_07_00_ARIZONA("(GMT-07.00) Arizona"),
    @XmlEnumValue("(GMT-07.00) Chihuahua / La Paz / Mazatlan - New")
    GMT_07_00_CHIHUAHUA_LA_PAZ_MAZATLAN_NEW("(GMT-07.00) Chihuahua / La Paz / Mazatlan - New"),
    @XmlEnumValue("(GMT-07.00) Chihuahua / La Paz / Mazatlan - Old")
    GMT_07_00_CHIHUAHUA_LA_PAZ_MAZATLAN_OLD("(GMT-07.00) Chihuahua / La Paz / Mazatlan - Old"),
    @XmlEnumValue("(GMT-07.00) Mountain Time (US & Canada)")
    GMT_07_00_MOUNTAIN_TIME_US_CANADA("(GMT-07.00) Mountain Time (US & Canada)"),
    @XmlEnumValue("(GMT-06.00) Central America")
    GMT_06_00_CENTRAL_AMERICA("(GMT-06.00) Central America"),
    @XmlEnumValue("(GMT-06.00) Central Time (US & Canada)")
    GMT_06_00_CENTRAL_TIME_US_CANADA("(GMT-06.00) Central Time (US & Canada)"),
    @XmlEnumValue("(GMT-06.00) Guadalajara / Mexico City / Monterrey - New")
    GMT_06_00_GUADALAJARA_MEXICO_CITY_MONTERREY_NEW("(GMT-06.00) Guadalajara / Mexico City / Monterrey - New"),
    @XmlEnumValue("(GMT-06.00) Guadalajara / Mexico City / Monterrey - Old")
    GMT_06_00_GUADALAJARA_MEXICO_CITY_MONTERREY_OLD("(GMT-06.00) Guadalajara / Mexico City / Monterrey - Old"),
    @XmlEnumValue("(GMT-06.00) Saskatchewan")
    GMT_06_00_SASKATCHEWAN("(GMT-06.00) Saskatchewan"),
    @XmlEnumValue("(GMT-05.00) Bogota / Lima / Quito / Rio Branco")
    GMT_05_00_BOGOTA_LIMA_QUITO_RIO_BRANCO("(GMT-05.00) Bogota / Lima / Quito / Rio Branco"),
    @XmlEnumValue("(GMT-05.00) Eastern Time (US & Canada)")
    GMT_05_00_EASTERN_TIME_US_CANADA("(GMT-05.00) Eastern Time (US & Canada)"),
    @XmlEnumValue("(GMT-05.00) Indiana (East)")
    GMT_05_00_INDIANA_EAST("(GMT-05.00) Indiana (East)"),
    @XmlEnumValue("(GMT-04.00) Atlantic Time (Canada)")
    GMT_04_00_ATLANTIC_TIME_CANADA("(GMT-04.00) Atlantic Time (Canada)"),
    @XmlEnumValue("(GMT-04.00) Caracas / La Paz")
    GMT_04_00_CARACAS_LA_PAZ("(GMT-04.00) Caracas / La Paz"),
    @XmlEnumValue("(GMT-04.00) Manaus")
    GMT_04_00_MANAUS("(GMT-04.00) Manaus"),
    @XmlEnumValue("(GMT-04.00) Santiago")
    GMT_04_00_SANTIAGO("(GMT-04.00) Santiago"),
    @XmlEnumValue("(GMT-03.30) Newfoundland")
    GMT_03_30_NEWFOUNDLAND("(GMT-03.30) Newfoundland"),
    @XmlEnumValue("(GMT-03.00) Brasilia")
    GMT_03_00_BRASILIA("(GMT-03.00) Brasilia"),
    @XmlEnumValue("(GMT-03.00) Buenos Aires / Georgetown")
    GMT_03_00_BUENOS_AIRES_GEORGETOWN("(GMT-03.00) Buenos Aires / Georgetown"),
    @XmlEnumValue("(GMT-03.00) Greenland")
    GMT_03_00_GREENLAND("(GMT-03.00) Greenland"),
    @XmlEnumValue("(GMT-03.00) Montevideo")
    GMT_03_00_MONTEVIDEO("(GMT-03.00) Montevideo"),
    @XmlEnumValue("(GMT-02.00) Mid-Atlantic")
    GMT_02_00_MID_ATLANTIC("(GMT-02.00) Mid-Atlantic"),
    @XmlEnumValue("(GMT-01.00) Azores")
    GMT_01_00_AZORES("(GMT-01.00) Azores"),
    @XmlEnumValue("(GMT-01.00) Cape Verde Is.")
    GMT_01_00_CAPE_VERDE_IS("(GMT-01.00) Cape Verde Is."),
    @XmlEnumValue("(GMT) Casablanca / Monrovia / Reykjavik")
    GMT_CASABLANCA_MONROVIA_REYKJAVIK("(GMT) Casablanca / Monrovia / Reykjavik"),
    @XmlEnumValue("(GMT) Greenwich Mean Time - Dublin / Edinburgh / Lisbon / London")
    GMT_GREENWICH_MEAN_TIME_DUBLIN_EDINBURGH_LISBON_LONDON("(GMT) Greenwich Mean Time - Dublin / Edinburgh / Lisbon / London"),
    @XmlEnumValue("(GMT) Greenwich Mean Time")
    GMT_GREENWICH_MEAN_TIME("(GMT) Greenwich Mean Time"),
    @XmlEnumValue("(GMT+01.00) Amsterdam / Berlin / Bern / Rome / Stockholm / Vienna")
    GMT_01_00_AMSTERDAM_BERLIN_BERN_ROME_STOCKHOLM_VIENNA("(GMT+01.00) Amsterdam / Berlin / Bern / Rome / Stockholm / Vienna"),
    @XmlEnumValue("(GMT+01.00) Belgrade / Bratislava / Budapest / Ljubljana / Prague")
    GMT_01_00_BELGRADE_BRATISLAVA_BUDAPEST_LJUBLJANA_PRAGUE("(GMT+01.00) Belgrade / Bratislava / Budapest / Ljubljana / Prague"),
    @XmlEnumValue("(GMT+01.00) Brussels / Copenhagen / Madrid / Paris")
    GMT_01_00_BRUSSELS_COPENHAGEN_MADRID_PARIS("(GMT+01.00) Brussels / Copenhagen / Madrid / Paris"),
    @XmlEnumValue("(GMT+01.00) Sarajevo / Skopje / Warsaw / Zagreb")
    GMT_01_00_SARAJEVO_SKOPJE_WARSAW_ZAGREB("(GMT+01.00) Sarajevo / Skopje / Warsaw / Zagreb"),
    @XmlEnumValue("(GMT+01.00) West Central Africa")
    GMT_01_00_WEST_CENTRAL_AFRICA("(GMT+01.00) West Central Africa"),
    @XmlEnumValue("(GMT+02.00) Amman")
    GMT_02_00_AMMAN("(GMT+02.00) Amman"),
    @XmlEnumValue("(GMT+02.00) Athens / Bucharest / Istanbul")
    GMT_02_00_ATHENS_BUCHAREST_ISTANBUL("(GMT+02.00) Athens / Bucharest / Istanbul"),
    @XmlEnumValue("(GMT+02.00) Beirut")
    GMT_02_00_BEIRUT("(GMT+02.00) Beirut"),
    @XmlEnumValue("(GMT+02.00) Cairo")
    GMT_02_00_CAIRO("(GMT+02.00) Cairo"),
    @XmlEnumValue("(GMT+02.00) Harare / Pretoria")
    GMT_02_00_HARARE_PRETORIA("(GMT+02.00) Harare / Pretoria"),
    @XmlEnumValue("(GMT+02.00) Helsinki / Kyiv / Riga / Sofia / Tallinn / Vilnius")
    GMT_02_00_HELSINKI_KYIV_RIGA_SOFIA_TALLINN_VILNIUS("(GMT+02.00) Helsinki / Kyiv / Riga / Sofia / Tallinn / Vilnius"),
    @XmlEnumValue("(GMT+02.00) Jerusalem")
    GMT_02_00_JERUSALEM("(GMT+02.00) Jerusalem"),
    @XmlEnumValue("(GMT+02.00) Minsk")
    GMT_02_00_MINSK("(GMT+02.00) Minsk"),
    @XmlEnumValue("(GMT+02.00) Windhoek")
    GMT_02_00_WINDHOEK("(GMT+02.00) Windhoek"),
    @XmlEnumValue("(GMT+03.00) Baghdad")
    GMT_03_00_BAGHDAD("(GMT+03.00) Baghdad"),
    @XmlEnumValue("(GMT+03.00) Kuwait / Riyadh")
    GMT_03_00_KUWAIT_RIYADH("(GMT+03.00) Kuwait / Riyadh"),
    @XmlEnumValue("(GMT+03.00) Moscow / St. Petersburg / Volgograd")
    GMT_03_00_MOSCOW_ST_PETERSBURG_VOLGOGRAD("(GMT+03.00) Moscow / St. Petersburg / Volgograd"),
    @XmlEnumValue("(GMT+03.00) Nairobi")
    GMT_03_00_NAIROBI("(GMT+03.00) Nairobi"),
    @XmlEnumValue("(GMT+03.00) Tbilisi")
    GMT_03_00_TBILISI("(GMT+03.00) Tbilisi"),
    @XmlEnumValue("(GMT+03.30) Tehran")
    GMT_03_30_TEHRAN("(GMT+03.30) Tehran"),
    @XmlEnumValue("(GMT+04.00) Abu Dhabi / Muscat")
    GMT_04_00_ABU_DHABI_MUSCAT("(GMT+04.00) Abu Dhabi / Muscat"),
    @XmlEnumValue("(GMT+04.00) Baku")
    GMT_04_00_BAKU("(GMT+04.00) Baku"),
    @XmlEnumValue("(GMT+04.00) Yerevan")
    GMT_04_00_YEREVAN("(GMT+04.00) Yerevan"),
    @XmlEnumValue("(GMT+04.30) Kabul")
    GMT_04_30_KABUL("(GMT+04.30) Kabul"),
    @XmlEnumValue("(GMT+05.00) Ekaterinburg")
    GMT_05_00_EKATERINBURG("(GMT+05.00) Ekaterinburg"),
    @XmlEnumValue("(GMT+05.00) Islamabad / Karachi / Tashkent")
    GMT_05_00_ISLAMABAD_KARACHI_TASHKENT("(GMT+05.00) Islamabad / Karachi / Tashkent"),
    @XmlEnumValue("(GMT+05.30) Chennai / Kolkata / Mumbai / New Delhi")
    GMT_05_30_CHENNAI_KOLKATA_MUMBAI_NEW_DELHI("(GMT+05.30) Chennai / Kolkata / Mumbai / New Delhi"),
    @XmlEnumValue("(GMT+05.30) Sri Jayawardenepura")
    GMT_05_30_SRI_JAYAWARDENEPURA("(GMT+05.30) Sri Jayawardenepura"),
    @XmlEnumValue("(GMT+05.45) Kathmandu")
    GMT_05_45_KATHMANDU("(GMT+05.45) Kathmandu"),
    @XmlEnumValue("(GMT+06.00) Almaty / Novosibirsk")
    GMT_06_00_ALMATY_NOVOSIBIRSK("(GMT+06.00) Almaty / Novosibirsk"),
    @XmlEnumValue("(GMT+06.00) Astana / Dhaka")
    GMT_06_00_ASTANA_DHAKA("(GMT+06.00) Astana / Dhaka"),
    @XmlEnumValue("(GMT+06.30) Yangon (Rangoon)")
    GMT_06_30_YANGON_RANGOON("(GMT+06.30) Yangon (Rangoon)"),
    @XmlEnumValue("(GMT+07.00) Bangkok / Hanoi / Jakarta")
    GMT_07_00_BANGKOK_HANOI_JAKARTA("(GMT+07.00) Bangkok / Hanoi / Jakarta"),
    @XmlEnumValue("(GMT+07.00) Krasnoyarsk")
    GMT_07_00_KRASNOYARSK("(GMT+07.00) Krasnoyarsk"),
    @XmlEnumValue("(GMT+08.00) Beijing / Chongqing / Hong Kong / Urumqi")
    GMT_08_00_BEIJING_CHONGQING_HONG_KONG_URUMQI("(GMT+08.00) Beijing / Chongqing / Hong Kong / Urumqi"),
    @XmlEnumValue("(GMT+08.00) Irkutsk / Ulaan Bataar")
    GMT_08_00_IRKUTSK_ULAAN_BATAAR("(GMT+08.00) Irkutsk / Ulaan Bataar"),
    @XmlEnumValue("(GMT+08.00) Kuala Lumpur / Singapore")
    GMT_08_00_KUALA_LUMPUR_SINGAPORE("(GMT+08.00) Kuala Lumpur / Singapore"),
    @XmlEnumValue("(GMT+08.00) Perth")
    GMT_08_00_PERTH("(GMT+08.00) Perth"),
    @XmlEnumValue("(GMT+08.00) Taipei")
    GMT_08_00_TAIPEI("(GMT+08.00) Taipei"),
    @XmlEnumValue("(GMT+09.00) Osaka / Sapporo / Tokyo")
    GMT_09_00_OSAKA_SAPPORO_TOKYO("(GMT+09.00) Osaka / Sapporo / Tokyo"),
    @XmlEnumValue("(GMT+09.00) Seoul")
    GMT_09_00_SEOUL("(GMT+09.00) Seoul"),
    @XmlEnumValue("(GMT+09.00) Yakutsk")
    GMT_09_00_YAKUTSK("(GMT+09.00) Yakutsk"),
    @XmlEnumValue("(GMT+09.30) Adelaide")
    GMT_09_30_ADELAIDE("(GMT+09.30) Adelaide"),
    @XmlEnumValue("(GMT+09.30) Darwin")
    GMT_09_30_DARWIN("(GMT+09.30) Darwin"),
    @XmlEnumValue("(GMT+10.00) Brisbane")
    GMT_10_00_BRISBANE("(GMT+10.00) Brisbane"),
    @XmlEnumValue("(GMT+10.00) Canberra / Melbourne / Sydney")
    GMT_10_00_CANBERRA_MELBOURNE_SYDNEY("(GMT+10.00) Canberra / Melbourne / Sydney"),
    @XmlEnumValue("(GMT+10.00) Guam / Port Moresby")
    GMT_10_00_GUAM_PORT_MORESBY("(GMT+10.00) Guam / Port Moresby"),
    @XmlEnumValue("(GMT+10.00) Hobart")
    GMT_10_00_HOBART("(GMT+10.00) Hobart"),
    @XmlEnumValue("(GMT+10.00) Vladivostok")
    GMT_10_00_VLADIVOSTOK("(GMT+10.00) Vladivostok"),
    @XmlEnumValue("(GMT+11.00) Magadan / Solomon Is. / New Caledonia")
    GMT_11_00_MAGADAN_SOLOMON_IS_NEW_CALEDONIA("(GMT+11.00) Magadan / Solomon Is. / New Caledonia"),
    @XmlEnumValue("(GMT+12.00) Auckland / Wellington")
    GMT_12_00_AUCKLAND_WELLINGTON("(GMT+12.00) Auckland / Wellington"),
    @XmlEnumValue("(GMT+12.00) Fiji / Kamchatka / Marshall Is.")
    GMT_12_00_FIJI_KAMCHATKA_MARSHALL_IS("(GMT+12.00) Fiji / Kamchatka / Marshall Is."),
    @XmlEnumValue("(GMT+13.00) Nuku'alofa")
    GMT_13_00_NUKU_ALOFA("(GMT+13.00) Nuku'alofa"),
    GMT("GMT");
    private final String value;

    TimeZone(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimeZone fromValue(String v) {
        for (TimeZone c: TimeZone.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
